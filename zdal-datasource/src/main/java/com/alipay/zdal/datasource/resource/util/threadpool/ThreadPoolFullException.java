/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * The thread pool is full.
 *
 * @author ²®ÑÀ
 * @version $Id: ThreadPoolFullException.java, v 0.1 2014-1-6 ÏÂÎç05:45:30 Exp $
 */
public class ThreadPoolFullException extends RuntimeException {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Create a new ThreadPoolFullException
     */
    public ThreadPoolFullException() {
        super();
    }

    /**
     * Create a new ThreadPoolFullException
     *
     * @param message the message
     */
    public ThreadPoolFullException(String message) {
        super(message);
    }

    // Public --------------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
