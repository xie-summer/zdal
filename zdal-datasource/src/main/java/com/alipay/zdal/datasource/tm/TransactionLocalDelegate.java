/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import com.alipay.zdal.datasource.transaction.Transaction;

/**
 * The interface to implementated for a transaction local implementation
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionLocalDelegate.java, v 0.1 2014-1-6 ÏÂÎç05:48:18 Exp $
 */
public interface TransactionLocalDelegate {
    /**
     * get the transaction local value.
     */
    Object getValue(TransactionLocal local, Transaction tx);

    /**
     * put the value in the transaction local
     */
    void storeValue(TransactionLocal local, Transaction tx, Object value);

    /**
     * does Transaction contain object?
     */
    boolean containsValue(TransactionLocal local, Transaction tx);

    /**
     * Lock the transaction local in the context of this transaction
     * 
     * @throws IllegalStateException if the transaction is not active
     * @throws InterruptedException if the thread is interrupted
     */
    void lock(TransactionLocal local, Transaction tx) throws InterruptedException;

    /**
     * Unlock the transaction local in the context of this transaction
     */
    void unlock(TransactionLocal local, Transaction tx);
}
