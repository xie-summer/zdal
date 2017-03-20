/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import com.alipay.zdal.datasource.transaction.Transaction;

/**
 *  Implementations of this interface are used for getting
 *  a transaction propagation context at the client-side.
 *  We need a specific implementation of this interface for
 *  each kind of DTM we are going to interoperate with. (So
 *  we may have 20 new classes if we are going to interoperate
 *  with 20 different kinds of distributed transaction
 *  managers.)
 *  The reason for having the methods in this interface return
 *  Object is that we do not really know what kind of transaction
 *  propagation context is returned.
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionPropagationContextFactory.java, v 0.1 2014-1-6 ÏÂÎç05:48:38 Exp $
 */
public interface TransactionPropagationContextFactory {
    /**
     *  Return a transaction propagation context for the transaction
     *  currently associated with the invoking thread, or <code>null</code>
     *  if the invoking thread is not associated with a transaction.
     */
    public Object getTransactionPropagationContext();

    /**
     *  Return a transaction propagation context for the transaction
     *  given as an argument, or <code>null</code>
     *  if the argument is <code>null</code> or of a type unknown to
     *  this factory.
     */
    public Object getTransactionPropagationContext(Transaction tx);

}
