/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

/**
 * The ConnectionRequestInfo allows a resource adapter to pass its own
 * information along with a request for a connection. In order to make use of
 * this functionality, a resource adapter needs to extend this interface and
 * add it's information.
 */
public interface ConnectionRequestInfo {
    /**
     * Tests object for equality
     * 
     * @param other the object to test
     * @return true when equal, false otherwise
     */
    public boolean equals(Object other);

    /**
     * Generates a hashCode for this object
     * 
     * @return the hash code
     */
    public int hashCode();
}