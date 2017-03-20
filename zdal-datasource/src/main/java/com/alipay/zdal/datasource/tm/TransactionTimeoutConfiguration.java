/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.SystemException;

/**
 * The interface to implementated by a transaction manager
 * that supports retrieving the current threads transaction timeout
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionTimeoutConfiguration.java, v 0.1 2014-1-6 ÏÂÎç05:48:57 Exp $
 */
public interface TransactionTimeoutConfiguration {
    /**
     * Get the transaction timeout.
     * 
     * @return the timeout in seconds associated with this thread
     * @throws SystemException for any error
     */
    int getTransactionTimeout() throws SystemException;

    /**
     * Get the time left before transaction timeout
     * 
     * @param errorRollback throw an error if the transaction is marked for rollback
     * @return the remaining in the current transaction or -1
     * if there is no transaction
     * @throws RollbackException if the transaction is marked for rollback and
     * errorRollback is true
     */
    long getTimeLeftBeforeTransactionTimeout(boolean errorRollback) throws RollbackException;
}
