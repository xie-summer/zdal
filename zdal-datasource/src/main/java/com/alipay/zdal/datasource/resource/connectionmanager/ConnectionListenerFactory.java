/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * A factory for connection event listeners
 *
 * @author ²®ÑÀ
 * @version $Id: ConnectionListenerFactory.java, v 0.1 2014-1-6 ÏÂÎç05:33:48 Exp $
 */
public interface ConnectionListenerFactory {
    /**
     * Create a managed connection listener for the managed connection
     * 
     * @param mc the managed connection
     * @param context a context object used by the pool
     * @return a new connection event listener
     * @throws ResourceException for any error
     */
    ConnectionListener createConnectionListener(ManagedConnection mc, Object context)
                                                                                     throws ResourceException;

    /**
     * Determine whether is a transaction
     *
     * @return whether there is a transaction
     */
    boolean isTransactional();

    /**
     * Get the transaction manager
     * 
     * @return the transaction manager
     */
    TransactionManager getTransactionManagerInstance();
}
