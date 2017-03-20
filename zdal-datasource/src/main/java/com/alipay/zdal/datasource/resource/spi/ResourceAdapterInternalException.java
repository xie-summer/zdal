/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * A ResourceAdapterInternalException indicates any system level error
 * conditions related to a resource adapter. Examples are invalid
 * configuration, failure to create a connection to an underlying resource,
 * other error condition internal to the resource adapter.
 */
public class ResourceAdapterInternalException extends ResourceException {
    /**
     * Create an exception.
     */
    public ResourceAdapterInternalException() {
        super();
    }

    /**
     * Create an exception with a reason.
     */
    public ResourceAdapterInternalException(String reason) {
        super(reason);
    }

    /**
     * Create an exception with a reason and an errorCode.
     */
    public ResourceAdapterInternalException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    /**
     * Create an exception with a reason and cause.
     * 
     * @param reason the reason
     * @param cause the cause
     */
    public ResourceAdapterInternalException(String reason, Throwable cause) {
        super(reason, cause);
    }

    /**
     * Create an exception with a cause.
     * 
     * @param cause the cause
     */
    public ResourceAdapterInternalException(Throwable cause) {
        super(cause);
    }
}