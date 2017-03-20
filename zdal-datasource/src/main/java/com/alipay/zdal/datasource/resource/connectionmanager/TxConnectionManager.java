/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.ObjectName;
import javax.security.auth.Subject;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionEvent;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.LocalTransaction;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.util.NestedRuntimeException;
import com.alipay.zdal.datasource.tm.LastResource;
import com.alipay.zdal.datasource.tm.TransactionTimeoutConfiguration;
import com.alipay.zdal.datasource.tm.TxUtils;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.Status;
import com.alipay.zdal.datasource.transaction.Synchronization;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * The TxConnectionManager is a JBoss ConnectionManager
 * implementation for jca adapters implementing LocalTransaction and XAResource support.
 *
 * It implements a ConnectionEventListener that implements XAResource to
 * manage transactions through the Transaction Manager. To assure that all
 * work in a local transaction occurs over the same ManagedConnection, it
 * includes a xid to ManagedConnection map.  When a Connection is requested
 * or a transaction started with a connection handle in use, it checks to
 * see if a ManagedConnection already exists enrolled in the global
 * transaction and uses it if found. Otherwise a free ManagedConnection
 * has its LocalTransaction started and is used.  From the
 * BaseConnectionManager2, it includes functionality to obtain managed
 * connections from
 * a ManagedConnectionPool mbean, find the Subject from a SubjectSecurityDomain,
 * and interact with the CachedConnectionManager for connections held over
 * transaction and method boundaries.  Important mbean references are to a
 * ManagedConnectionPool supplier (typically a JBossManagedConnectionPool), and a
 * RARDeployment representing the ManagedConnectionFactory.
 *
 * This connection manager has to perform the following operations:
 *
 * 1. When an application component requests a new ConnectionHandle,
 *    it must find a ManagedConnection, and make sure a
 *    ConnectionEventListener is registered. It must inform the
 *    CachedConnectionManager that a connection handle has been given
 *    out. It needs to count the number of handles for each
 *    ManagedConnection.  If there is a current transaction, it must
 *    enlist the ManagedConnection's LocalTransaction in the transaction
 *    using the ConnectionEventListeners XAResource XAResource implementation.
 * Entry point: ConnectionManager.allocateConnection.
 * written.
 *
 * 2. When a ConnectionClosed event is received from the
 *    ConnectionEventListener, it must reduce the handle count.  If
 *    the handle count is zero, the XAResource should be delisted from
 *    the Transaction, if any. The CachedConnectionManager must be
 *    notified that the connection is closed.
 * Entry point: ConnectionEventListener.ConnectionClosed.
 * written
 *
 *3. When a transaction begun notification is received from the
 * UserTransaction (via the CachedConnectionManager, all
 * managedConnections associated with the current object must be
 * enlisted in the transaction.
 *  Entry point: (from
 * CachedConnectionManager)
 * ConnectionCacheListener.transactionStarted(Transaction,
 * Collection). The collection is of ConnectionRecord objects.
 * written.
 *
 * 5. When an "entering object" notification is received from the
 * CachedConnectionInterceptor, all the connections for the current
 * object must be associated with a ManagedConnection.  if there is a
 * Transaction, the XAResource must be enlisted with it.
 *  Entry point: ConnectionCacheListener.reconnect(Collection conns) The Collection
 * is of ConnectionRecord objects.
 * written.
 *
 * 6. When a "leaving object" notification is received from the
 * CachedConnectionInterceptor, all the managedConnections for the
 * current object must have their XAResources delisted from the
 * current Transaction, if any, and cleanup called on each
 * ManagedConnection.
 * Entry point: ConnectionCacheListener.disconnect(Collection conns).
 * written.
 *
 * @author ²®ÑÀ
 * @version $Id: TxConnectionManager.java, v 0.1 2014-1-6 ÏÂÎç05:36:35 Exp $
 */
public class TxConnectionManager extends BaseConnectionManager2 {
    private static final Logger    log                 = Logger
                                                           .getLogger(TxConnectionManager.class);

    private static final Throwable FAILED_TO_ENLIST    = new Throwable(
                                                           "Unabled to enlist resource, see the previous warnings.");

    private ObjectName             transactionManagerService;

    private TransactionManager     tm;

    private boolean                trackConnectionByTx = false;

    private boolean                localTransactions;

    private int                    xaResourceTimeout   = 0;

    private Boolean                isSameRMOverrideValue;

    protected static void rethrowAsSystemException(String context, Transaction tx, Throwable t)
                                                                                               throws SystemException {
        if (t instanceof SystemException)
            throw (SystemException) t;
        if (t instanceof RuntimeException)
            throw (RuntimeException) t;
        if (t instanceof Error)
            throw (Error) t;
        if (t instanceof RollbackException)
            throw new IllegalStateException(context + " tx=" + tx + " marked for rollback.");
        throw new NestedRuntimeException(context + " tx=" + tx + " got unexpected error ", t);
    }

    /**
     * Default managed TxConnectionManager constructor for mbean instances.
     */
    public TxConnectionManager() {
    }

    /**
     * Creates a new <code>TxConnectionManager</code> instance.
     * for TESTING ONLY!!! not a managed constructor!!
     *
     * @param ccm a <code>CachedConnectionManager</code> value
     * @param poolingStrategy a <code>ManagedConnectionPool</code> value
     * @param tm a <code>TransactionManager</code> value
     */
    public TxConnectionManager(final CachedConnectionManager ccm,
                               final ManagedConnectionPool poolingStrategy,
                               final TransactionManager tm) {
        super(ccm, poolingStrategy);
        this.tm = tm;
    }

    public ObjectName getTransactionManagerService() {
        return transactionManagerService;
    }

    public void setTransactionManagerService(ObjectName transactionManagerService) {
        this.transactionManagerService = transactionManagerService;
    }

    @Override
    public TransactionManager getTransactionManagerInstance() {
        return tm;
    }

    public void setTransactionManagerInstance(TransactionManager tm) {
        this.tm = tm;
    }

    public boolean isTrackConnectionByTx() {
        return trackConnectionByTx;
    }

    public void setTrackConnectionByTx(boolean trackConnectionByTx) {
        this.trackConnectionByTx = trackConnectionByTx;
    }

    public boolean isLocalTransactions() {
        return localTransactions;
    }

    public void setLocalTransactions(boolean localTransactions) {
        this.localTransactions = localTransactions;
    }

    public int getXAResourceTransactionTimeout() {
        return xaResourceTimeout;
    }

    public void setXAResourceTransactionTimeout(int timeout) {
        this.xaResourceTimeout = timeout;
    }

    public Boolean getIsSameRMOverrideValue() {
        return isSameRMOverrideValue;
    }

    public void setIsSameRMOverrideValue(Boolean isSameRMOverride) {
        this.isSameRMOverrideValue = isSameRMOverride;
    }

    @Override
    public long getTimeLeftBeforeTransactionTimeout(boolean errorRollback) throws RollbackException {
        if (tm == null)
            throw new IllegalStateException("No transaction manager");
        if (tm instanceof TransactionTimeoutConfiguration)
            return ((TransactionTimeoutConfiguration) tm)
                .getTimeLeftBeforeTransactionTimeout(errorRollback);
        return -1;
    }

    @Override
    public ConnectionListener getManagedConnection(Subject subject, ConnectionRequestInfo cri)
                                                                                              throws ResourceException {
        Transaction trackByTransaction = null;
        try {
            Transaction tx = tm.getTransaction();
            if (tx != null && TxUtils.isActive(tx) == false) {
                throw new ResourceException("Transaction is not active: tx=" + tx);
            }
            if (trackConnectionByTx) {
                trackByTransaction = tx;
            }
        } catch (Throwable t) {
            JBossResourceException.rethrowAsResourceException("Error checking for a transaction.",
                t);
        }

        return super.getManagedConnection(trackByTransaction, subject, cri);
    }

    @Override
    public void transactionStarted(Collection crs) throws SystemException {
        Set cls = new HashSet();
        for (Iterator i = crs.iterator(); i.hasNext();) {
            ConnectionRecord cr = (ConnectionRecord) i.next();
            ConnectionListener cl = cr.cl;
            if (!cls.contains(cl)) {
                cls.add(cl);
                cl.enlist();
            }
        }
    }

    @Override
    protected void managedConnectionReconnected(ConnectionListener cl) throws ResourceException {
        try {
            cl.enlist();
        } catch (Throwable t) {

            throw new JBossResourceException(
                "Could not enlist in transaction on entering meta-aware object!", t);
        }
    }

    @Override
    protected void managedConnectionDisconnected(ConnectionListener cl) throws ResourceException {
        Throwable throwable = null;
        try {
            cl.delist();
        } catch (Throwable t) {
            throwable = t;
        }

        //if there are no more handles and tx is complete, we can return to pool.
        boolean isFree = cl.isManagedConnectionFree();

        if (isFree) {
            returnManagedConnection(cl, false);
        }

        // Rethrow the error
        if (throwable != null)
            JBossResourceException.rethrowAsResourceException(
                "Could not delist resource, probably a transaction rollback? ", throwable);
    }

    public ConnectionListener createConnectionListener(ManagedConnection mc, Object context)
                                                                                            throws ResourceException {
        XAResource xaResource = null;

        if (localTransactions) {
            xaResource = new LocalXAResource(log);
            //            if (log.isDebugEnabled() && xaResourceTimeout != 0) {
            //                log.debug("XAResource transaction timeout cannot be set for local transactions: "
            //                          + getName());
            //            }
        } else {
            throw new UnsupportedOperationException("Support local transactions only");
            //         xaResource = JcaXAResourceWrapperFactory.getResourceWrapper(mc.getXAResource(), isSameRMOverrideValue);
            //
            //         if (xaResourceTimeout != 0)
            //         {
            //            try
            //            {
            //               if (xaResource.setTransactionTimeout(xaResourceTimeout) == false)
            //                  log.debug("XAResource does not support transaction timeout configuration: " + getName());
            //            }
            //            catch (XAException e)
            //            {
            //               throw new JBossResourceException("Unable to set XAResource transaction timeout: " + getName(), e);
            //            }
            //         }
        }

        ConnectionListener cli = new TxConnectionEventListener(mc, poolingStrategy, context, log,
            xaResource);
        mc.addConnectionEventListener(cli);
        return cli;
    }

    @Override
    public boolean isTransactional() {
        return TxUtils.isCompleted(tm) == false;
    }

    // implementation of javax.resource.spi.ConnectionEventListener interface
    //there is one of these for each ManagedConnection instance.  It lives as long as the ManagedConnection.
    protected class TxConnectionEventListener extends
                                             BaseConnectionManager2.BaseConnectionEventListener {
        /** Use our own logger to prevent MNFE caused by compiler bug with nested classes. */
        protected Logger                     log;

        protected TransactionSynchronization transactionSynchronization;

        private final XAResource             xaResource;

        /** Whether there is a local transaction */
        private final AtomicBoolean          localTransaction = new AtomicBoolean(false);

        public TxConnectionEventListener(final ManagedConnection mc,
                                         final ManagedConnectionPool mcp, final Object context,
                                         Logger log, final XAResource xaResource)
                                                                                 throws ResourceException {
            super(mc, mcp, context, log);
            this.log = log;
            this.xaResource = xaResource;
            if (xaResource instanceof LocalXAResource)
                ((LocalXAResource) xaResource).setConnectionListener(this);
        }

        @Override
        public void enlist() throws SystemException {
            // This method is a bit convulted, but it has to be such because
            // there is a race condition in the transaction manager where it
            // unlocks during the enlist of the XAResource. It does this
            // to avoid distributed deadlocks and to ensure the transaction
            // timeout can fail a badly behaving resource during the enlist.
            //
            // When two threads in the same transaction are trying to enlist
            // connections they could be from the same resource manager
            // or even the same connection when tracking the connection by transaction.
            //
            // For the same connection, we only want to do the real enlist once.
            // For two connections from the same resource manager we don't
            // want the join before the initial start request.
            //
            // The solution is to build up a list of unenlisted resources
            // in the TransactionSynchronizer and then choose one of the
            // threads that is contending in the transaction to enlist them
            // in order. The actual order doesn't really matter as it is the
            // transaction manager that calculates the enlist flags and determines
            // whether the XAResource was already enlisted.
            //
            // Once there are no unenlisted resources the threads are released
            // to return the result of the enlistments.
            //
            // In practice, a thread just takes a snapshot to try to avoid one
            // thread having to do all the work. If it did not do them all
            // the next waiting thread will do the next snapshot until there
            // there is either no snapshot or no waiting threads.
            //
            // A downside to this design is a thread could have its resource enlisted by
            // an earlier thread while it enlists some later thread's resource.
            // Since they are all a part of the same transaction, this is probably
            // not a real issue.

            // No transaction associated with the thread
            int status = tm.getStatus();
            if (status == Status.STATUS_NO_TRANSACTION) {
                if (transactionSynchronization != null
                    && transactionSynchronization.currentTx != null) {
                    String error = "Attempt to use connection outside a transaction when already a tx!";

                    throw new IllegalStateException(error);
                }
                return;
            }

            // Inactive transaction
            Transaction threadTx = tm.getTransaction();
            if (threadTx == null || status != Status.STATUS_ACTIVE) {
                String error = "Transaction " + threadTx + " is not active "
                               + TxUtils.getStatusAsString(status);

                throw new IllegalStateException(error);
            }

            // Our synchronization
            TransactionSynchronization ourSynchronization = null;

            // Serializes enlistment when two different threads are enlisting
            // different connections in the same transaction concurrently
            TransactionSynchronizer synchronizer = null;

            TransactionSynchronizer.lock(threadTx);
            try {
                // Interleaving should have an unenlisted transaction
                // TODO: We should be able to do some sharing shouldn't we?
                if (isTrackByTx() == false && transactionSynchronization != null) {
                    String error = "Can't enlist - already a tx!";

                    throw new IllegalStateException(error);
                }

                // Check for different transaction
                if (transactionSynchronization != null
                    && transactionSynchronization.currentTx.equals(threadTx) == false) {
                    String error = "Trying to change transaction " + threadTx + " in enlist!";

                    throw new IllegalStateException(error);
                }

                // Get the synchronizer
                try {

                    synchronizer = TransactionSynchronizer.getRegisteredSynchronizer(threadTx);
                } catch (Throwable t) {
                    setTrackByTx(false);
                    rethrowAsSystemException("Cannot register synchronization", threadTx, t);
                }

                // First time through, create a transaction synchronization
                if (transactionSynchronization == null) {
                    TransactionSynchronization synchronization = new TransactionSynchronization(
                        threadTx, isTrackByTx());
                    synchronizer.addUnenlisted(synchronization);
                    transactionSynchronization = synchronization;
                }
                ourSynchronization = transactionSynchronization;
            } finally {
                TransactionSynchronizer.unlock(threadTx);
            }

            // Perform the enlistment(s)
            ArrayList unenlisted = synchronizer.getUnenlisted();
            if (unenlisted != null) {
                try {
                    for (int i = 0; i < unenlisted.size(); ++i) {
                        TransactionSynchronization sync = (TransactionSynchronization) unenlisted
                            .get(i);
                        if (sync.enlist())
                            synchronizer.addEnlisted(sync);
                    }
                } finally {
                    synchronizer.enlisted();
                }
            }

            ourSynchronization.checkEnlisted();
        }

        @Override
        public void delist() throws ResourceException {

            try {
                if (isTrackByTx() == false && transactionSynchronization != null) {
                    Transaction tx = transactionSynchronization.currentTx;
                    TransactionSynchronization synchronization = transactionSynchronization;
                    transactionSynchronization = null;
                    if (TxUtils.isUncommitted(tx)) {
                        TransactionSynchronizer synchronizer = TransactionSynchronizer
                            .getRegisteredSynchronizer(tx);
                        if (synchronization.enlisted)
                            synchronizer.removeEnlisted(synchronization);
                        if (tx.delistResource(getXAResource(), XAResource.TMSUSPEND) == false)
                            throw new ResourceException("Failure to delist resource: " + this);
                    }
                }
            } catch (Throwable t) {
                JBossResourceException.rethrowAsResourceException("Error in delist!", t);
            }
        }

        //local will return this, xa will return one from mc.
        protected XAResource getXAResource() {
            return xaResource;
        }

        public void connectionClosed(ConnectionEvent ce) {

            if (this.getManagedConnection() != (ManagedConnection) ce.getSource())
                throw new IllegalArgumentException(
                    "ConnectionClosed event received from wrong ManagedConnection! Expected: "
                            + this.getManagedConnection() + ", actual: " + ce.getSource());
            try {
                getCcm().unregisterConnection(TxConnectionManager.this, ce.getConnectionHandle());
            } catch (Throwable t) {
                log.warn(t);
            }

            try {
                unregisterAssociation(this, ce.getConnectionHandle());
                boolean isFree = isManagedConnectionFree();

                //no more handles
                if (isFree) {
                    delist();
                    returnManagedConnection(this, false);
                }
            } catch (Throwable t) {
                log.error("Error while closing connection handle!", t);
                returnManagedConnection(this, true);
            }
        }

        public void localTransactionStarted(ConnectionEvent ce) {
            localTransaction.set(true);
        }

        public void localTransactionCommitted(ConnectionEvent ce) {
            localTransaction.set(false);
        }

        public void localTransactionRolledback(ConnectionEvent ce) {
            localTransaction.set(false);
        }

        @Override
        public void tidyup() throws ResourceException {
            // We have a hanging transaction
            if (localTransaction.get()) {
                LocalTransaction local = null;
                ManagedConnection mc = getManagedConnection();
                try {
                    local = mc.getLocalTransaction();
                } catch (Throwable t) {
                    JBossResourceException.rethrowAsResourceException(
                        "Unfinished local transaction - error getting local transaction from "
                                + this, t);
                }
                if (local == null)
                    throw new ResourceException(
                        "Unfinished local transaction but managed connection does not provide a local transaction. "
                                + this);
                else {
                    local.rollback();

                    //                    if (log.isDebugEnabled()) {
                    //                        log.debug("Unfinished local transaction was rolled back." + this);
                    //                    }
                }
            }
        }

        @Override
        public void connectionErrorOccurred(ConnectionEvent ce) {
            transactionSynchronization = null;
            super.connectionErrorOccurred(ce);
        }

        //Important method!!
        @Override
        public boolean isManagedConnectionFree() {
            if (isTrackByTx() && transactionSynchronization != null)
                return false;
            return super.isManagedConnectionFree();
        }

        private class TransactionSynchronization implements Synchronization {
            /** Transaction */
            private final Transaction currentTx;

            /** This is the status when we were registered */
            private final boolean     wasTrackByTx;

            /** Whether we are enlisted */
            private boolean           enlisted = false;

            /** Any error during enlistment */
            private Throwable         enlistError;

            /**
             * Create a new TransactionSynchronization.
             *
             * @param trackByTx whether this is track by connection
             */
            public TransactionSynchronization(Transaction tx, boolean trackByTx) {
                this.currentTx = tx;
                wasTrackByTx = trackByTx;
            }

            /**
             * Get the result of the enlistment
             *
             * @throws SystemExeption for any error
             */
            public void checkEnlisted() throws SystemException {
                if (enlistError != null) {
                    String error = "Error enlisting resource in transaction=" + currentTx;

                    // Wrap the error to give a reasonable stacktrace since the resource
                    // could have been enlisted by a different thread
                    if (enlistError == FAILED_TO_ENLIST)
                        throw new SystemException(FAILED_TO_ENLIST + " tx=" + currentTx);
                    else {
                        SystemException e = new SystemException(error);
                        e.initCause(enlistError);
                        throw e;
                    }
                }
                if (enlisted == false) {

                    throw new IllegalStateException("Resource was not enlisted.");
                }
            }

            /**
             * Enlist the resource
             *
             * @return true when enlisted, false otherwise
             */
            public boolean enlist() {

                try {
                    XAResource resource = getXAResource();
                    if (false == currentTx.enlistResource(resource))
                        enlistError = FAILED_TO_ENLIST;
                } catch (Throwable t) {
                    enlistError = t;
                }

                synchronized (this) {
                    if (enlistError != null) {

                        setTrackByTx(false);
                        transactionSynchronization = null;
                        return false;
                    }

                    enlisted = true;
                    return true;
                }
            }

            public void beforeCompletion() {
            }

            public void afterCompletion(int status) {
                // The connection got destroyed during the transaction
                if (getState() == DESTROYED)
                    return;

                // Are we still in the original transaction?
                if (this.equals(transactionSynchronization) == false) {
                    // If we are interleaving transactions we have nothing to do
                    if (wasTrackByTx == false)
                        return;
                    else {
                        // There is something wrong with the pooling
                        String message = "afterCompletion called with wrong tx! Expected: " + this
                                         + ", actual: " + transactionSynchronization;
                        IllegalStateException e = new IllegalStateException(message);
                        log.error("There is something wrong with the pooling?", e);
                    }
                }
                // "Delist"
                transactionSynchronization = null;
                // This is where we close when doing track by transaction
                if (wasTrackByTx) {
                    setTrackByTx(false);
                    if (isManagedConnectionFree())
                        returnManagedConnection(TxConnectionEventListener.this, false);
                }
            }

            @Override
            public String toString() {
                StringBuffer buffer = new StringBuffer();
                buffer.append("TxSync").append(System.identityHashCode(this));
                buffer.append("{tx=").append(currentTx);
                buffer.append(" wasTrackByTx=").append(wasTrackByTx);
                buffer.append(" enlisted=").append(enlisted);
                buffer.append("}");
                return buffer.toString();
            }
        }

        // For debugging
        @Override
        protected void toString(StringBuffer buffer) {
            buffer.append(" xaResource=").append(xaResource);
            buffer.append(" txSync=").append(transactionSynchronization);
        }
    }

    private class LocalXAResource implements XAResource, LastResource {
        protected Logger           log;

        private ConnectionListener cl;

        /**
         * <code>warned</code> is set after one warning about a local participant
         * in a multi-branch jta transaction is logged.
         *
         */
        private boolean            warned = false;

        private Xid                currentXid;

        public LocalXAResource(final Logger log) {
            this.log = log;
        }

        void setConnectionListener(ConnectionListener cl) {
            this.cl = cl;
        }

        // implementation of javax.transaction.xa.XAResource interface

        public void start(Xid xid, int flags) throws XAException {
            if (log.isDebugEnabled()) {
                log.debug("start, xid: " + xid + ", flags: " + flags);
            }

            if (currentXid != null && flags == XAResource.TMNOFLAGS)
                throw new JBossLocalXAException(
                    "Trying to start a new tx when old is not complete! old: " + currentXid
                            + ", new " + xid + ", flags " + flags, XAException.XAER_PROTO);
            if (currentXid == null && flags != XAResource.TMNOFLAGS)
                throw new JBossLocalXAException("Trying to start a new tx with wrong flags!  new "
                                                + xid + ", flags " + flags, XAException.XAER_PROTO);
            if (currentXid == null) {
                try {
                    cl.getManagedConnection().getLocalTransaction().begin();
                } catch (ResourceException re) {
                    throw new JBossLocalXAException("Error trying to start local tx: ",
                        XAException.XAER_RMERR, re);
                } catch (Throwable t) {
                    throw new JBossLocalXAException("Throwable trying to start local transaction!",
                        XAException.XAER_RMERR, t);
                }

                currentXid = xid;
            }
        }

        public void end(Xid xid, int flags) throws XAException {
            if (log.isDebugEnabled()) {
                log.debug("end on xid: " + xid + " called with flags " + flags);
            }
        }

        public void commit(Xid xid, boolean onePhase) throws XAException {
            if (xid.equals(currentXid) == false)
                throw new JBossLocalXAException("wrong xid in commit: expected: " + currentXid
                                                + ", got: " + xid, XAException.XAER_PROTO);
            currentXid = null;
            try {
                cl.getManagedConnection().getLocalTransaction().commit();
            } catch (ResourceException re) {
                returnManagedConnection(cl, true);
                if (log.isDebugEnabled()) {
                    log.debug("commit problem: ", re);
                }

                throw new JBossLocalXAException("could not commit local tx",
                    XAException.XA_RBROLLBACK, re);
            }
        }

        public void forget(Xid xid) throws XAException {
            throw new JBossLocalXAException("forget not supported in local tx",
                XAException.XAER_RMERR);
        }

        public int getTransactionTimeout() throws XAException {
            // TODO: implement this javax.transaction.xa.XAResource method
            return 0;
        }

        public boolean isSameRM(XAResource xaResource) throws XAException {
            return xaResource == this;
        }

        public int prepare(Xid xid) throws XAException {
            if (!warned)
                log
                    .warn("Prepare called on a local tx. Use of local transactions on a jta transaction with more than one branch may result in inconsistent data in some cases of failure.");
            warned = true;
            return XAResource.XA_OK;
        }

        public Xid[] recover(int flag) throws XAException {
            throw new JBossLocalXAException("no recover with local-tx only resource managers",
                XAException.XAER_RMERR);
        }

        public void rollback(Xid xid) throws XAException {
            if (xid.equals(currentXid) == false)
                throw new JBossLocalXAException("wrong xid in rollback: expected: " + currentXid
                                                + ", got: " + xid, XAException.XAER_PROTO);
            currentXid = null;
            try {
                cl.getManagedConnection().getLocalTransaction().rollback();
            } catch (ResourceException re) {
                returnManagedConnection(cl, true);
                if (log.isDebugEnabled()) {
                    log.debug("rollback problem: ", re);
                }
                throw new JBossLocalXAException("could not rollback local tx",
                    XAException.XAER_RMERR, re);
            }
        }

        public boolean setTransactionTimeout(int seconds) throws XAException {
            // TODO: implement this javax.transaction.xa.XAResource method
            return false;
        }
    }

}
