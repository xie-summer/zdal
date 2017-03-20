/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.util.NestedRuntimeException;
import com.alipay.zdal.datasource.tm.TransactionLocal;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.Synchronization;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * Organizes transaction synchronization done by JCA.<p>
 * 
 * This class exists to make sure all Tx synchronizations
 * are invoked before the cached connection manager closes any
 * closed connections.
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionSynchronizer.java, v 0.1 2014-1-6 ÏÂÎç05:36:20 Exp $
 */
public class TransactionSynchronizer implements Synchronization {
    /** The logger */
    private static final Logger       log = Logger.getLogger(TransactionSynchronizer.class);

    /** The transaction synchronizations */
    protected static TransactionLocal txSynchs;

    /** The transaction */
    protected Transaction             tx;

    /** The enlisting thread */
    protected Thread                  enlistingThread;

    /** Unenlisted */
    protected ArrayList               unenlisted;

    /** Enlisted */
    protected ArrayList               enlisted;

    /** The cached connection manager synchronization */
    protected Synchronization         ccmSynch;

    /** Initialization */
    public static void setTransactionManager(TransactionManager tm) {
        txSynchs = new TransactionLocal(tm);
    }

    /**
     * Create a new transaction synchronizer
     * 
     * @param tx the transaction to synchronize with
     */
    private TransactionSynchronizer(Transaction tx) {
        this.tx = tx;
    }

    /**
     * Add a new Tx synchronization that has not been enlisted
     * 
     * @param synch the synchronization
     */
    synchronized void addUnenlisted(Synchronization synch) {
        if (unenlisted == null)
            unenlisted = new ArrayList();
        unenlisted.add(synch);
    }

    /**
     * Get the unenlisted synchronizations
     * and say we are enlisting if some are returned.
     * 
     * @return the unenlisted synchronizations
     */
    synchronized ArrayList getUnenlisted() {
        Thread currentThread = Thread.currentThread();
        while (enlistingThread != null && enlistingThread != currentThread) {
            boolean interrupted = false;
            try {
                wait();
            } catch (InterruptedException e) {
                interrupted = true;
            }
            if (interrupted)
                currentThread.interrupt();
        }
        ArrayList result = unenlisted;
        unenlisted = null;
        if (result != null)
            enlistingThread = currentThread;
        return result;
    }

    /**
     * The synchronization is now enlisted
     * 
     * @param synch the synchronization
     */
    synchronized void addEnlisted(Synchronization synch) {
        if (enlisted == null)
            enlisted = new ArrayList();
        enlisted.add(synch);
    }

    /**
     * Remove an enlisted synchronization
     * 
     * @param synch the synchronization
     * @return true when the synchronization was enlisted
     */
    synchronized boolean removeEnlisted(Synchronization synch) {
        return enlisted.remove(synch);
    }

    /**
     * This thread has finished enlisting
     */
    synchronized void enlisted() {
        Thread currentThread = Thread.currentThread();
        if (enlistingThread == null || enlistingThread != currentThread) {
            log.warn("Thread " + currentThread + " not the enlisting thread " + enlistingThread,
                new Exception("STACKTRACE"));
            return;
        }
        enlistingThread = null;
        notifyAll();
    }

    /**
     * Get a registered transaction synchronizer.
     *
     * @param tx the transaction
     * @return the registered transaction synchronizer for this transaction
     * @throws RolledbackException if the transaction is already rolled back
     * @throws SystemException for an error in the tranaction manager
     */
    static TransactionSynchronizer getRegisteredSynchronizer(Transaction tx)
                                                                            throws RollbackException,
                                                                            SystemException {
        TransactionSynchronizer result = (TransactionSynchronizer) txSynchs.get(tx);
        if (result == null) {
            result = new TransactionSynchronizer(tx);
            tx.registerSynchronization(result);
            txSynchs.set(tx, result);
        }
        return result;
    }

    /**
     * Check whether we have a CCM synchronization
     * 
     * @param tx the transaction
     */
    static Synchronization getCCMSynchronization(Transaction tx) {
        TransactionSynchronizer ts = (TransactionSynchronizer) txSynchs.get(tx);
        if (ts != null)
            return ts.ccmSynch;
        else
            return null;
    }

    /**
     * Register a new CCM synchronization
     * 
     * @param tx the transaction
     * @param synch the synchronization
     * @throws RolledbackException if the transaction is already rolled back
     * @throws SystemException for an error in the tranaction manager
     */
    static void registerCCMSynchronization(Transaction tx, Synchronization synch)
                                                                                 throws RollbackException,
                                                                                 SystemException {
        TransactionSynchronizer ts = getRegisteredSynchronizer(tx);
        ts.ccmSynch = synch;
    }

    /**
     * Lock for the given transaction
     * 
     * @param tx the transaction
     */
    static void lock(Transaction tx) {
        try {
            txSynchs.lock(tx);
        } catch (InterruptedException e) {
            throw new NestedRuntimeException("Unable to get synchronization", e);
        }
    }

    /**
     * Unlock for the given transaction
     * 
     * @param tx the transaction
     */
    static void unlock(Transaction tx) {
        txSynchs.unlock(tx);
    }

    public void beforeCompletion() {
        if (enlisted != null) {
            int i = 0;
            while (i < enlisted.size()) {
                Synchronization synch = (Synchronization) enlisted.get(i);
                invokeBefore(synch);
                ++i;
            }
        }

        if (ccmSynch != null)
            invokeBefore(ccmSynch);
    }

    public void afterCompletion(int status) {
        if (enlisted != null) {
            int i = 0;
            while (i < enlisted.size()) {
                Synchronization synch = (Synchronization) enlisted.get(i);
                invokeAfter(synch, status);
                ++i;
            }
        }

        if (ccmSynch != null)
            invokeAfter(ccmSynch, status);
    }

    /**
     * Invoke a beforeCompletion
     * 
     * @param synch the synchronization
     */
    protected void invokeBefore(Synchronization synch) {
        try {
            synch.beforeCompletion();
        } catch (Throwable t) {
            log.warn("Transaction " + tx + " error in before completion " + synch, t);
        }
    }

    /**
     * Invoke an afterCompletion
     * 
     * @param synch the synchronization
     * @param status the status of the transaction
     */
    protected void invokeAfter(Synchronization synch, int status) {
        try {
            synch.afterCompletion(status);
        } catch (Throwable t) {
            log.warn("Transaction " + tx + " error in after completion " + synch, t);
        }
    }
}
