/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import com.alipay.zdal.datasource.transaction.Transaction;

/**
 *  Implementations of this interface are used for importing a transaction
 *  propagation context into the transaction manager.
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionPropagationContextImporter.java, v 0.1 2014-1-6 ÏÂÎç05:48:47 Exp $
 */
public interface TransactionPropagationContextImporter {
    /**
     *  Import the transaction propagation context into the transaction
     *  manager, and return the resulting transaction.
     *  If this transaction propagation context has already been imported
     *  into the transaction manager, this method simply returns the
     *  <code>Transaction</code> representing the transaction propagation
     *  context in the local VM.
     *  Returns <code>null</code> if the transaction propagation context is
     *  <code>null</code>, or if it represents a <code>null</code> transaction.
     */
    public Transaction importTransactionPropagationContext(Object tpc);
}
