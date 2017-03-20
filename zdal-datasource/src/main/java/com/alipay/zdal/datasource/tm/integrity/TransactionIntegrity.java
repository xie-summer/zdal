/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm.integrity;

import com.alipay.zdal.datasource.tm.TransactionImpl;

/**
 * A policy that checks a transaction before allowing a commit
 *
 * @version $Revision: 57208 $
 */
public interface TransactionIntegrity {
    /**
     * Checks whether a transaction can be committed.<p>
     * 
     * The policy is allowed to wait, e.g. if there
     * are other threads still associated with the transaction.<p>
     * 
     * This method is invoked before any transaction synchronizations'
     * beforeCompletions.<p>
     *
     * This policy should not invoke any methods that change the
     * state of the transaction other than <code>setRollbackOnly()</code>
     * to force a rollback or registering a transaction synchronization.
     * 
     * @param transaction the transaction
     * @throws SecurityException if a commit is not allowed from this context
     */
    void checkTransactionIntegrity(TransactionImpl transaction);
}
