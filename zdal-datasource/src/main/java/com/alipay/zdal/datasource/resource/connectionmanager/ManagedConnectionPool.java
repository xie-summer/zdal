/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import javax.security.auth.Subject;

import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;
import com.alipay.zdal.datasource.transaction.Transaction;

/**
 * A managed connection pool
 *
 * 
 * @author 伯牙
 * @version $Id: ManagedConnectionPool.java, v 0.1 2014-1-6 下午05:35:44 Exp $
 */
public interface ManagedConnectionPool {
    /**
     * Retrieve the managed connection factory for this pool
     *
     * @return the managed connection factory
     */
    ManagedConnectionFactory getManagedConnectionFactory();

    /**
     * Set the connection listener factory
     *
     * @param clf the connection event listener factory
     */
    void setConnectionListenerFactory(ConnectionListenerFactory clf);

    /**
     * Get a connection
     *
     * @param trackByTransaction for transaction stickiness
     * @param subject the subject for connection
     * @param cri the connection request information
     * @return a connection event listener wrapping the connection
     * @throws ResourceException for any error
     */
    ConnectionListener getConnection(Transaction trackByTransaction, Subject subject,
                                     ConnectionRequestInfo cri) throws ResourceException;

    /**
     * Return a connection
     *
     * @param cl the connection event listener wrapping the connection
     * @param kill whether to destroy the managed connection
     * @throws ResourceException for any error
     */
    void returnConnection(ConnectionListener cl, boolean kill) throws ResourceException;

    /**
     * @return the connection count
     */
    int getConnectionCount();

    /**
     * @return the connections in use count
     */
    int getInUseConnectionCount();

    /**
     * @return the connections created count
     */
    int getConnectionCreatedCount();

    /**
     * @return the connections destroyed count
     */
    int getConnectionDestroyedCount();

    /**
     * shutdown the pool
     */
    void shutdown();

    /**
     * @return the available connections
     */
    long getAvailableConnectionCount();

    /**
     * @return the available connections
     */
    int getMaxConnectionsInUseCount();

    /**
     * flush the pool
     */
    void flush();

}
