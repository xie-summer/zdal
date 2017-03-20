/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.InvalidTransactionException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.spi.work.Work;
import com.alipay.zdal.datasource.resource.spi.work.WorkCompletedException;
import com.alipay.zdal.datasource.resource.spi.work.WorkException;
import com.alipay.zdal.datasource.resource.util.UnexpectedThrowable;
import com.alipay.zdal.datasource.resource.util.UnreachableStatementException;
import com.alipay.zdal.datasource.tm.integrity.TransactionIntegrity;
import com.alipay.zdal.datasource.transaction.HeuristicMixedException;
import com.alipay.zdal.datasource.transaction.HeuristicRollbackException;
import com.alipay.zdal.datasource.transaction.NotSupportedException;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.Status;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * Our TransactionManager implementation.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: TxManager.java, v 0.1 2014-1-6 ÏÂÎç05:49:07 Exp $
 */
public class TxManager implements TransactionManager, TransactionPropagationContextImporter,
                      TransactionPropagationContextFactory, TransactionLocalDelegate,
                      TransactionTimeoutConfiguration, JBossXATerminator {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    /** True if the TxManager should keep a map from GlobalIds to transactions. */
    private boolean              globalIdsEnabled = false;

    /** Whether to interrupt threads at transaction timeout */
    private boolean              interruptThreads = false;

    /** Instance logger. */
    private final Logger         log              = Logger.getLogger(this.getClass());

    /**
     *  Default timeout in milliseconds.
     *  Must be >= 1000!
     */
    private long                 timeOut          = 5 * 60 * 1000;

    // The following two fields are ints (not longs) because
    // volatile 64Bit types are broken (i.e. access is not atomic) in most VMs, and we
    // don't want to lock just for a statistic. Additionaly,
    // it will take several years on a highly loaded system to
    // exceed the int range. Note that we might loose an
    // increment every now and then, since the ++ operation is
    // not atomic on volatile data types.
    /** A count of the transactions that have been committed */
    private volatile int         commitCount;
    /** A count of the transactions that have been rolled back */
    private volatile int         rollbackCount;

    /** The transaction integrity policy */
    private TransactionIntegrity integrity;

    // Static --------------------------------------------------------

    /**
     *  The singleton instance.
     */
    private static TxManager     singleton        = new TxManager();

    /**
     *  Get a reference to the singleton instance.
     */
    public static TxManager getInstance() {
        return singleton;
    }

    // Constructors --------------------------------------------------

    /**
     *  Private constructor for singleton. Use getInstance() to obtain
     *  a reference to the singleton.
     */
    private TxManager() {
        //make sure TxCapsule can be used
        TransactionImpl.defaultXidFactory();
    }

    // Public --------------------------------------------------------

    /**
     *  Setter for attribute <code>globalIdsEnabled</code>.
     */
    public void setGlobalIdsEnabled(boolean newValue) {
        XidImpl.setTrulyGlobalIdsEnabled(newValue);
        globalIdsEnabled = newValue;
    }

    /**
     *  Getter for attribute <code>globalIdsEnabled</code>.
     */
    public boolean getGlobalIdsEnabled() {
        return globalIdsEnabled;
    }

    /**
     * Enable/disable thread interruption at transaction timeout.
     *
     * @param interruptThreads pass true to interrupt threads, false otherwise
     */
    public void setInterruptThreads(boolean interruptThreads) {
        this.interruptThreads = interruptThreads;
    }

    /**
     * Is thread interruption enabled at transaction timeout
     *
     * @return true for interrupt threads, false otherwise
     */
    public boolean isInterruptThreads() {
        return interruptThreads;
    }

    /**
     * Set the transaction integrity policy
     *
     * @param integrity the transaction integrity policy
     */
    public void setTransactionIntegrity(TransactionIntegrity integrity) {
        this.integrity = integrity;
    }

    /**
     * Get the transaction integrity policy
     *
     * @return the transaction integrity policy
     */
    public TransactionIntegrity getTransactionIntegrity() {
        return integrity;
    }

    /**
     *  Begin a new transaction.
     *  The new transaction will be associated with the calling thread.
     */
    public void begin() throws NotSupportedException, SystemException {

        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            if (current.isDone())
                disassociateThread(ti);
            else
                throw new NotSupportedException(
                    "Transaction already active, cannot nest transactions.");
        }

        long timeout = (ti.timeout == 0) ? timeOut : ti.timeout;
        TransactionImpl tx = new TransactionImpl(timeout);
        associateThread(ti, tx);
        localIdTx.put(tx.getLocalId(), tx);
        if (globalIdsEnabled)
            globalIdTx.put(tx.getGlobalId(), tx);

        if (log.isDebugEnabled())
            log.debug("began tx: " + tx);
    }

    /**
     *  Commit the transaction associated with the currently running thread.
     */
    public void commit() throws RollbackException, HeuristicMixedException,
                        HeuristicRollbackException, SecurityException, IllegalStateException,
                        SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            current.commit();
            disassociateThread(ti);
            if (log.isDebugEnabled())
                log.debug("commited tx: " + current);
        } else
            throw new IllegalStateException("No transaction.");
    }

    /**
     *  Return the status of the transaction associated with the currently
     *  running thread, or <code>Status.STATUS_NO_TRANSACTION</code> if no
     *  active transaction is currently associated.
     */
    public int getStatus() throws SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            if (current.isDone())
                disassociateThread(ti);
            else
                return current.getStatus();
        }
        return Status.STATUS_NO_TRANSACTION;
    }

    /**
     *  Return the transaction currently associated with the invoking thread,
     *  or <code>null</code> if no active transaction is currently associated.
     */
    public Transaction getTransaction() throws SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null && current.isDone()) {
            current = null;
            disassociateThread(ti);
        }

        return current;
    }

    /**
     *  Resume a transaction.
     *
     *  Note: This will not enlist any resources involved in this
     *  transaction. According to JTA1.0.1 specification section 3.2.3,
     *  that is the responsibility of the application server.
     */
    public void resume(Transaction transaction) throws InvalidTransactionException,
                                               IllegalStateException, SystemException {
        if (transaction != null && !(transaction instanceof TransactionImpl))
            throw new RuntimeException("Not a TransactionImpl, but a "
                                       + transaction.getClass().getName());

        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            if (current.isDone())
                current = ti.tx = null;
            else
                throw new IllegalStateException("Already associated with a tx");
        }

        if (current != transaction) {
            associateThread(ti, (TransactionImpl) transaction);
        }

        if (log.isDebugEnabled())
            log.debug("resumed tx: " + ti.tx);
    }

    /**
     *  Suspend the transaction currently associated with the current
     *  thread, and return it.
     *
     *  Note: This will not delist any resources involved in this
     *  transaction. According to JTA1.0.1 specification section 3.2.3,
     *  that is the responsibility of the application server.
     */
    public Transaction suspend() throws SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            current.disassociateCurrentThread();
            ti.tx = null;

            if (log.isDebugEnabled())
                log.debug("suspended tx: " + current);

            if (current.isDone())
                current = null;
        }

        return current;
    }

    /**
     *  Roll back the transaction associated with the currently running thread.
     */
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            if (!current.isDone()) {
                current.rollback();

                if (log.isDebugEnabled())
                    log.debug("rolled back tx: " + current);
                return;
            }
            disassociateThread(ti);
        }
        throw new IllegalStateException("No transaction.");
    }

    /**
     *  Mark the transaction associated with the currently running thread
     *  so that the only possible outcome is a rollback.
     */
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        ThreadInfo ti = getThreadInfo();
        TransactionImpl current = ti.tx;

        if (current != null) {
            if (!current.isDone()) {
                current.setRollbackOnly();

                if (log.isDebugEnabled())
                    log.debug("tx marked for rollback only: " + current);
                return;
            }
            ti.tx = null;
        }
        throw new IllegalStateException("No transaction.");
    }

    public int getTransactionTimeout() {
        return (int) (getThreadInfo().timeout / 1000);
    }

    /**
     *  Set the transaction timeout for new transactions started by the
     *  calling thread.
     */
    public void setTransactionTimeout(int seconds) throws SystemException {
        getThreadInfo().timeout = 1000 * seconds;

        if (log.isDebugEnabled())
            log.debug("tx timeout is now: " + seconds + "s");
    }

    /**
     *  Set the default transaction timeout for new transactions.
     *  This default value is used if <code>setTransactionTimeout()</code>
     *  was never called, or if it was called with a value of <code>0</code>.
     */
    public void setDefaultTransactionTimeout(int seconds) {
        timeOut = 1000L * seconds;

        if (log.isDebugEnabled())
            log.debug("default tx timeout is now: " + seconds + "s");
    }

    /**
     *  Get the default transaction timeout.
     *
     *  @return Default transaction timeout in seconds.
     */
    public int getDefaultTransactionTimeout() {
        return (int) (timeOut / 1000);
    }

    public long getTimeLeftBeforeTransactionTimeout(boolean errorRollback) throws RollbackException {
        try {
            ThreadInfo ti = getThreadInfo();
            TransactionImpl current = ti.tx;
            if (current != null && current.isDone()) {
                disassociateThread(ti);
                return -1;
            }
            return current.getTimeLeftBeforeTimeout(errorRollback);
        } catch (RollbackException e) {
            throw e;
        } catch (Exception ignored) {
            return -1;
        }
    }

    /**
     *  The following 2 methods are here to provide association and
     *  disassociation of the thread.
     */
    public Transaction disassociateThread() {
        return disassociateThread(getThreadInfo());
    }

    private Transaction disassociateThread(ThreadInfo ti) {
        TransactionImpl current = ti.tx;
        ti.tx = null;
        current.disassociateCurrentThread();
        return current;
    }

    public void associateThread(Transaction transaction) {
        if (transaction != null && !(transaction instanceof TransactionImpl))
            throw new RuntimeException("Not a TransactionImpl, but a "
                                       + transaction.getClass().getName());

        // Associate with the thread
        TransactionImpl transactionImpl = (TransactionImpl) transaction;
        ThreadInfo ti = getThreadInfo();
        ti.tx = transactionImpl;
        transactionImpl.associateCurrentThread();
    }

    private void associateThread(ThreadInfo ti, TransactionImpl transaction) {
        // Associate with the thread
        ti.tx = transaction;
        transaction.associateCurrentThread();
    }

    /**
     * Return the number of active transactions
     */
    public int getTransactionCount() {
        return localIdTx.size();
    }

    /** A count of the transactions that have been committed */
    public long getCommitCount() {
        return commitCount;
    }

    /** A count of the transactions that have been rolled back */
    public long getRollbackCount() {
        return rollbackCount;
    }

    // Implements TransactionPropagationContextImporter ---------------

    /**
     *  Import a transaction propagation context into this TM.
     *  The TPC is loosely typed, as we may (at a later time) want to
     *  import TPCs that come from other transaction domains without
     *  offloading the conversion to the client.
     *
     *  @param tpc The transaction propagation context that we want to
     *             import into this TM. Currently this is an instance
     *             of LocalId. At some later time this may be an instance
     *             of a transaction propagation context from another
     *             transaction domain like
     *             org.omg.CosTransactions.PropagationContext.
     *
     *  @return A transaction representing this transaction propagation
     *          context, or null if this TPC cannot be imported.
     */
    public Transaction importTransactionPropagationContext(Object tpc) {
        if (tpc instanceof LocalId) {
            LocalId id = (LocalId) tpc;
            return (Transaction) localIdTx.get(id);
        } else if (globalIdsEnabled && tpc instanceof GlobalId) {
            GlobalId id = (GlobalId) tpc;
            Transaction tx = (Transaction) globalIdTx.get(id);
            if (log.isDebugEnabled() && tx != null) {
                log.debug("Successfully imported transaction context " + tpc);
            } else if (log.isDebugEnabled() && tx == null) {
                log.debug("Could not import transaction context " + tpc);
            }
            return tx;
        }

        log.warn("Cannot import transaction propagation context: " + tpc);
        return null;
    }

    // Implements TransactionPropagationContextFactory ---------------

    /**
     *  Return a TPC for the current transaction.
     */
    public Object getTransactionPropagationContext() {
        return getTransactionPropagationContext(getThreadInfo().tx);
    }

    /**
     *  Return a TPC for the argument transaction.
     */
    public Object getTransactionPropagationContext(Transaction tx) {
        // If no transaction or unknown transaction class, return null.
        if (tx == null)
            return null;
        if (!(tx instanceof TransactionImpl)) {
            log.warn("Cannot export transaction propagation context: " + tx);
            return null;
        }

        return ((TransactionImpl) tx).getLocalId();
    }

    // Implements XATerminator ----------------------------------

    public void registerWork(Work work, Xid xid, long timeout) throws WorkCompletedException {
        if (log.isDebugEnabled())
            log.debug("registering work=" + work + " xid=" + xid + " timeout=" + timeout);
        try {
            TransactionImpl tx = importExternalTransaction(xid, timeout);
            tx.setWork(work);
        } catch (WorkCompletedException e) {
            throw e;
        } catch (Throwable t) {
            WorkCompletedException e = new WorkCompletedException("Error registering work", t);
            e.setErrorCode(WorkException.TX_RECREATE_FAILED);
            throw e;
        }
        if (log.isDebugEnabled())
            log.debug("registered work= " + work + " xid=" + xid + " timeout=" + timeout);
    }

    public void startWork(Work work, Xid xid) throws WorkCompletedException {
        if (log.isDebugEnabled())
            log.debug("starting work=" + work + " xid=" + xid);
        TransactionImpl tx = getExternalTransaction(xid);
        associateThread(tx);
        if (log.isDebugEnabled())
            log.debug("started work= " + work + " xid=" + xid);
    }

    public void endWork(Work work, Xid xid) {
        if (log.isDebugEnabled())
            log.debug("ending work=" + work + " xid=" + xid);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            tx.setWork(null);
            disassociateThread();
        } catch (WorkCompletedException e) {
            log.error("Unexpected error from endWork ", e);
            throw new UnexpectedThrowable(e.toString());
        }
        if (log.isDebugEnabled())
            log.debug("ended work=" + work + " xid=" + xid);
    }

    public void cancelWork(Work work, Xid xid) {
        if (log.isDebugEnabled())
            log.debug("cancling work=" + work + " xid=" + xid);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            tx.setWork(null);
        } catch (WorkCompletedException e) {
            log.error("Unexpected error from cancelWork ", e);
            throw new UnexpectedThrowable(e.toString());
        }
        if (log.isDebugEnabled())
            log.debug("cancled work=" + work + " xid=" + xid);
    }

    public int prepare(Xid xid) throws XAException {
        if (log.isDebugEnabled())
            log.debug("preparing xid=" + xid);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            int result = tx.prepare();
            if (log.isDebugEnabled())
                log.debug("prepared xid=" + xid + " result=" + result);
            return result;
        } catch (Throwable t) {
            JBossXAException.rethrowAsXAException("Error during prepare", t);
            throw new UnreachableStatementException();
        }
    }

    public void rollback(Xid xid) throws XAException {
        if (log.isDebugEnabled())
            log.debug("rolling back xid=" + xid);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            tx.rollback();
        } catch (Throwable t) {
            JBossXAException.rethrowAsXAException("Error during rollback", t);
        }
        if (log.isDebugEnabled())
            log.debug("rolled back xid=" + xid);
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        if (log.isDebugEnabled())
            log.debug("committing xid=" + xid + " onePhase=" + onePhase);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            tx.commit(onePhase);
        } catch (Throwable t) {
            JBossXAException.rethrowAsXAException("Error during commit", t);
        }
        if (log.isDebugEnabled())
            log.debug("committed xid=" + xid);
    }

    public void forget(Xid xid) throws XAException {
        if (log.isDebugEnabled())
            log.debug("forgetting xid=" + xid);
        try {
            TransactionImpl tx = getExternalTransaction(xid);
            tx.rollback();
        } catch (Throwable t) {
            JBossXAException.rethrowAsXAException("Error during forget", t);
        }
        if (log.isDebugEnabled())
            log.debug("forgot xid=" + xid);
    }

    public Xid[] recover(int flag) throws XAException {
        // TODO recover
        return new Xid[0];
    }

    TransactionImpl importExternalTransaction(Xid xid, long timeOut) {
        GlobalId gid = new GlobalId(xid);
        TransactionImpl tx = (TransactionImpl) globalIdTx.get(gid);
        if (tx != null) {
            if (log.isDebugEnabled())
                log.debug("imported existing transaction xid: " + xid + " tx=" + tx);
        } else {
            ThreadInfo ti = getThreadInfo();
            long timeout = (ti.timeout == 0) ? timeOut : ti.timeout;
            tx = new TransactionImpl(gid, timeout);
            localIdTx.put(tx.getLocalId(), tx);
            if (globalIdsEnabled)
                globalIdTx.put(gid, tx);

            if (log.isDebugEnabled())
                log.debug("imported new transaction xid: " + xid + " tx=" + tx + " timeout="
                          + timeout);
        }
        return tx;
    }

    TransactionImpl getExternalTransaction(Xid xid) throws WorkCompletedException {
        GlobalId gid = new GlobalId(xid);
        TransactionImpl tx = (TransactionImpl) globalIdTx.get(gid);
        if (tx == null)
            throw new WorkCompletedException("Xid not found " + xid,
                WorkException.TX_RECREATE_FAILED);
        return tx;
    }

    // Implements TransactionLocalDelegate ----------------------

    public void lock(TransactionLocal local, Transaction tx) throws InterruptedException {
        TransactionImpl tximpl = (TransactionImpl) tx;
        tximpl.lock();
    }

    public void unlock(TransactionLocal local, Transaction tx) {
        TransactionImpl tximpl = (TransactionImpl) tx;
        tximpl.unlock();
    }

    public Object getValue(TransactionLocal local, Transaction tx) {
        TransactionImpl tximpl = (TransactionImpl) tx;
        return tximpl.getTransactionLocalValue(local);
    }

    public void storeValue(TransactionLocal local, Transaction tx, Object value) {
        TransactionImpl tximpl = (TransactionImpl) tx;
        tximpl.putTransactionLocalValue(local, value);
    }

    public boolean containsValue(TransactionLocal local, Transaction tx) {
        TransactionImpl tximpl = (TransactionImpl) tx;
        return tximpl.containsTransactionLocal(local);
    }

    // Package protected ---------------------------------------------

    /**
     *  Release the given TransactionImpl.
     */
    void releaseTransactionImpl(TransactionImpl tx) {
        localIdTx.remove(tx.getLocalId());
        if (globalIdsEnabled)
            globalIdTx.remove(tx.getGlobalId());
    }

    /**
     * Increment the commit count
     */
    void incCommitCount() {
        ++commitCount;
    }

    /**
     * Increment the rollback count
     */
    void incRollbackCount() {
        ++rollbackCount;
    }

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    /**
     *  This keeps track of the thread association with transactions
     *  and timeout values.
     *  In some cases terminated transactions may not be cleared here.
     */
    private final ThreadLocal threadTx   = new ThreadLocal();

    /**
     *  This map contains the active transactions as values.
     *  The keys are the <code>LocalId</code>s of the transactions.
     */
    private final Map         localIdTx  = Collections.synchronizedMap(new HashMap());

    /**
     *  If <code>globalIdsEnabled</code> is true, this map associates
     *  <code>GlobalId</code>s to active transactions.
     */
    private final Map         globalIdTx = Collections.synchronizedMap(new HashMap());

    /**
     *  Return the ThreadInfo for the calling thread, and create if not
     *  found.
     */
    private ThreadInfo getThreadInfo() {
        ThreadInfo ret = (ThreadInfo) threadTx.get();

        if (ret == null) {
            ret = new ThreadInfo();
            ret.timeout = timeOut;
            threadTx.set(ret);
        }

        return ret;
    }

    // Inner classes -------------------------------------------------

    /**
     *  A simple aggregate of a thread-associated timeout value
     *  and a thread-associated transaction.
     */
    static class ThreadInfo {
        long            timeout;
        TransactionImpl tx;
    }
}
