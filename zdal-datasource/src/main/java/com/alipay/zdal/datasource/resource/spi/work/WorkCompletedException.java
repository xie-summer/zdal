/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi.work;

/**
 * An error thrown when work is completed.
 */
public class WorkCompletedException extends WorkException {
    /**
     * Create an exception.
     */
    public WorkCompletedException() {
        super();
    }

    /**
     * Create an exception with a reason.
     *
     * @param reason the reason
     */
    public WorkCompletedException(String reason) {
        super(reason);
    }

    /**
     * Create an exception with a reason and an errorCode.
     *
     * @param reason the reason
     * @param errorCode the error code
     */
    public WorkCompletedException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    /**
     * Create an exception with a reason and an error.
     *
     * @param reason the reason
     * @param throwable the error
     */
    public WorkCompletedException(String reason, Throwable throwable) {
        super(reason, throwable);
    }

    /**
     * Create an exception with an error.
     *
     * @param throwable the error
     */
    public WorkCompletedException(Throwable throwable) {
        super(throwable);
    }
}
