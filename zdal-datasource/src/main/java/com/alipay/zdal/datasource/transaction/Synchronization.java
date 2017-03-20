/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.transaction;

/**
 *  This is the callback interface that has to be implemented by objects
 *  interested in receiving notification before and after a transaction
 *  commits or rolls back.
 *
 *  An interested party can give an instance implementing this interface
 *  as an argument to the
 *  {@link Transaction#registerSynchronization(Synchronization) Transaction.registerSynchronization}
 *  method to receive callbacks before and after a transaction commits or
 *  rolls back.
 *
 *  @version $Revision: 37390 $
 */
public interface Synchronization {
    /**
     *  This method is invoked before the start of the commit
     *  process. The method invocation is done in the context of the
     *  transaction that is about to be committed.
     */
    public void beforeCompletion();

    /**
     *  This method is invoked after the transaction has committed or
     *  rolled back.
     *
     *  @param status The status of the completed transaction.
     */
    public void afterCompletion(int status);
}
