/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.alipay.zdal.datasource.resource.util.NestedRuntimeException;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.Synchronization;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * An implementation of the transaction local implementation
 * using Transaction synchronizations.
 *
 * There is one of these per transaction local
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionLocalDelegateImpl.java, v 0.1 2014-1-6 ÏÂÎç05:48:27 Exp $
 */
public class TransactionLocalDelegateImpl implements TransactionLocalDelegate {
    // Attributes ----------------------------------------------------

    /** The transaction manager */
    protected TransactionManager                                                     manager;

    // Static --------------------------------------------------------

    /** The synchronizations for each transaction */
    protected static ConcurrentHashMap<Transaction, TransactionLocalSynchronization> synchronizationsByTransaction = new ConcurrentHashMap<Transaction, TransactionLocalSynchronization>();

    /**
     * Retrieve a synchronization for the transaction
     *
     * @param tx the transaction
     * @param create whether to create a synchronization if one doesn't exist
     */
    protected static TransactionLocalSynchronization getSynchronization(Transaction tx,
                                                                        boolean create) {
        synchronized (tx) {
            TransactionLocalSynchronization result = synchronizationsByTransaction.get(tx);
            if (result == null && create == true) {
                result = new TransactionLocalSynchronization(tx);
                try {
                    tx.registerSynchronization(result);
                } catch (RollbackException e) {
                    throw new IllegalStateException(
                        "Transaction already rolled back or marked for rollback");
                } catch (SystemException e) {
                    throw new NestedRuntimeException(e);
                }
                synchronizationsByTransaction.put(tx, result);
            }
            return result;
        }
    }

    /**
     * Remove a synchronization
     *
     * @param tx the transaction to remove
     */
    protected static void removeSynchronization(Transaction tx) {
        synchronizationsByTransaction.remove(tx);
    }

    // Constructor ---------------------------------------------------

    /**
     * Construct a new delegate for the given transaction manager
     *
     * @param manager the transaction manager
     */
    public TransactionLocalDelegateImpl(TransactionManager manager) {
        this.manager = manager;
    }

    public void lock(TransactionLocal local, Transaction tx) throws InterruptedException {
        TransactionLocalSynchronization sync = getSynchronization(tx, true);
        sync.lock(local);
    }

    public void unlock(TransactionLocal local, Transaction tx) {
        TransactionLocalSynchronization sync = getSynchronization(tx, false);
        if (sync != null)
            sync.unlock(local);
        else
            throw new IllegalStateException("No synchronization found tx=" + tx + " local=" + local);
    }

    public Object getValue(TransactionLocal local, Transaction tx) {
        TransactionLocalSynchronization sync = getSynchronization(tx, false);
        if (sync == null)
            return null;
        return sync.getValue(local);
    }

    public void storeValue(TransactionLocal local, Transaction tx, Object value) {
        TransactionLocalSynchronization sync = getSynchronization(tx, true);
        sync.setValue(local, value);
    }

    public boolean containsValue(TransactionLocal local, Transaction tx) {
        TransactionLocalSynchronization sync = getSynchronization(tx, false);
        if (sync == null)
            return false;
        return sync.containsValue(local);
    }

    // InnerClasses ---------------------------------------------------

    protected static class TransactionLocalSynchronization implements Synchronization {
        protected Transaction   tx;

        private Map             valuesByLocal = Collections.synchronizedMap(new HashMap());

        protected ReentrantLock reentrantLock = new ReentrantLock();

        public TransactionLocalSynchronization(Transaction tx) {
            this.tx = tx;
        }

        public void beforeCompletion() {
        }

        public void afterCompletion(int status) {
            removeSynchronization(tx);
            valuesByLocal.clear(); // Help the GC
        }

        public void lock(Object local) throws InterruptedException {
            boolean locked = reentrantLock.tryLock(60000, TimeUnit.MILLISECONDS);
            if (locked == false)
                throw new IllegalStateException("Failed to acquire lock within 60 seconds.");
        }

        public void unlock(Object local) {
            reentrantLock.unlock();
        }

        public Object getValue(Object local) {
            return valuesByLocal.get(local);
        }

        public void setValue(Object local, Object value) {
            valuesByLocal.put(local, value);
        }

        public boolean containsValue(Object local) {
            return valuesByLocal.containsKey(local);
        }
    }
}
