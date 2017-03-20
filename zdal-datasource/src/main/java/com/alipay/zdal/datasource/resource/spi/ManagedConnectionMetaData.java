/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * The ManagedConnectionMetaData interface provides information about the
 * underlying resource associated with a ManagedConnetion. The Application
 * Server can use this information to get information at runtime from the
 * underlying resource.
 */
public interface ManagedConnectionMetaData {
    /**
     * Returns product name of the underlying resource.
     * 
     * @return the product name
     * @throws ResourceException for a generic error 
     */
    public String getEISProductName() throws ResourceException;

    /**
     * Returns product version of the underlying resource.
     * 
     * @return the product version
     * @throws ResourceException for a generic error 
     */
    public String getEISProductVersion() throws ResourceException;

    /**
     * Returns the maximum supported number of connections allowed to the
     * underlying resource.
     * 
     * @return the maximum number of connections
     * @throws ResourceException for a generic error 
     */
    public int getMaxConnections() throws ResourceException;

    /**
     * Returns user name associated with the underlying connection.
     * 
     * @return the user name
     * @throws ResourceException for a generic error 
     */
    public String getUserName() throws ResourceException;
}