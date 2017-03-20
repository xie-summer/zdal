/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import java.io.Serializable;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * The ConnectionManager interface provides the hook which allows a resource
 * adapter to pass a connection to the Application Server. The Application
 * Server implements this interface in order to control QoS services to the
 * resource adapter for connection pools.
 */

public interface ConnectionManager extends Serializable {
    /**
     * Gets called by the resource adapter's connection factory. The resource adapter
     * uses this method to pass its managed connection factory to the connection manager.
     * 
     * @param mcf the managed connection factory
     * @param cxRequestInfo the connection request info
     * @return the connection handle
     * @throws ResourceException for an generic error
     */
    public Object allocateConnection(ManagedConnectionFactory mcf,
                                     ConnectionRequestInfo cxRequestInfo) throws ResourceException;
}