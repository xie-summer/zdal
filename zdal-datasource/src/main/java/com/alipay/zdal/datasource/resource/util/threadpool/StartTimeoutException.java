/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * The start timeout was exceeded.
 *
 * @author 伯牙
 * @version $Id: StartTimeoutException.java, v 0.1 2014-1-6 下午05:44:40 Exp $
 */
public class StartTimeoutException extends RuntimeException {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Create a new StartTimeoutException
     */
    public StartTimeoutException() {
        super();
    }

    /**
     * Create a new StartTimeoutException
     *
     * @param message the message
     */
    public StartTimeoutException(String message) {
        super(message);
    }

    // Public --------------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
