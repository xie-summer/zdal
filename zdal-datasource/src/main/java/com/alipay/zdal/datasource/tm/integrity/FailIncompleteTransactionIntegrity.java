/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm.integrity;

import java.util.Set;

import com.alipay.zdal.datasource.tm.TransactionImpl;

/**
 * A transaction integrity that rolls back the transaction
 * if there are other threads associated with it.
 * 
 * @version $Revision: 57208 $
 */
public class FailIncompleteTransactionIntegrity extends AbstractTransactionIntegrity {
    @SuppressWarnings("unchecked")
    public void checkTransactionIntegrity(TransactionImpl transaction) {
        // Assert the only thread is ourselves
        Set threads = transaction.getAssociatedThreads();
        String rollbackError = null;
        synchronized (threads) {
            if (threads.size() > 1)
                rollbackError = "Too many threads " + threads + " associated with transaction "
                                + transaction;
            else if (threads.size() != 0) {
                Thread other = (Thread) threads.iterator().next();
                Thread current = Thread.currentThread();
                if (current.equals(other) == false)
                    rollbackError = "Attempt to commit transaction " + transaction + " on thread "
                                    + current
                                    + " with other threads still associated with the transaction "
                                    + other;
            }
        }
        if (rollbackError != null) {
            log.error(rollbackError, new IllegalStateException("STACKTRACE"));
            markRollback(transaction);
        }
    }
}
