/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm.integrity;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.tm.TransactionImpl;
import com.alipay.zdal.datasource.transaction.Transaction;

/**
 * A NOOP implementation of transaction integrity.<p>
 * 
 * Implementations should extend this for future compatibility.
 * 
 */
public class AbstractTransactionIntegrity implements TransactionIntegrity {
    /** The log */
    protected Logger log = Logger.getLogger(getClass());

    public void checkTransactionIntegrity(TransactionImpl transaction) {
        // Do nothing
    }

    /**
     * Mark the transaction for rollback
     * 
     * @param transaction the transacton
     */
    protected void markRollback(Transaction transaction) {
        try {
            transaction.setRollbackOnly();
        } catch (Exception e) {
            log.warn("Unable to mark the transaction for rollback " + transaction, e);
        }
    }
}
