/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi.work;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * Thrown when there is an error handling work.
 */
public class WorkException extends ResourceException {
    /** An internal error */
    public static final String INTERNAL                      = "-1";
    /** An undefined error */
    public static final String UNDEFINED                     = "0";
    /** Expiration before work was started */
    public static final String START_TIMED_OUT               = "1";
    /** Not allowed to do concurrent work on a transaction */
    public static final String TX_CONCURRENT_WORK_DISALLOWED = "2";
    /** Could not recreate the transaction context */
    public static final String TX_RECREATE_FAILED            = "3";

    /**
     * Create an exception.
     */
    public WorkException() {
        super();
    }

    /**
     * Create an exception with a reason.
     *
     * @param reason the reason
     */
    public WorkException(String reason) {
        super(reason);
    }

    /**
     * Create an exception with a reason and an errorCode.
     *
     * @param reason the reason
     * @param errorCode the error code
     */
    public WorkException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    /**
     * Create an exception with a reason and an error.
     *
     * @param reason the reason
     * @param throwable the error
     */
    public WorkException(String reason, Throwable throwable) {
        super(reason, throwable);
    }

    /**
     * Create an exception with an error.
     *
     * @param throwable the error
     */
    public WorkException(Throwable throwable) {
        super(throwable);
    }
}
