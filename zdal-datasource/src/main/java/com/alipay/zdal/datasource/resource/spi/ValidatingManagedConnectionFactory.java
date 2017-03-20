/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import java.util.Set;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * A mixin interface for connection factories that can validate their managed connections
 */
public interface ValidatingManagedConnectionFactory {
    /**
     * Returns the invalid connections in a set
     *
     * @param connectionSet the set of connections to validate
     * @return the set of invalid connections
     * @throws ResourceException for a generic error
     */
    Set getInvalidConnections(Set connectionSet) throws ResourceException;
}