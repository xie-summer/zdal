/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.util.Collection;
import java.util.Set;

import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.transaction.SystemException;

/**
 * ConnectionCacheListener
 *
 * 
 * @author 伯牙
 * @version $Id: ConnectionCacheListener.java, v 0.1 2014-1-6 下午05:33:22 Exp $
 */
public interface ConnectionCacheListener {
    /**
     * Notification of transaction started
     * 
     * @param conns the connections
     * @throws SystemException for any error
     */
    void transactionStarted(Collection conns) throws SystemException;

    /**
     * Notification to reconnect connections
     * 
     * @param conns the connections
     * @param unsharableResources unshareable resources
     * @throws ResourceException for any error
     */
    void reconnect(Collection conns, Set unsharableResources) throws ResourceException;

    /**
     * Notification to disconnect connections
     * 
     * @param conns the connections
     * @param unsharableResources the unshareable resources
     * @throws ResourceException for any error
     */
    void disconnect(Collection conns, Set unsharableResources) throws ResourceException;
}
