/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * The thread pool was stopped.
 *
 * @author ²®ÑÀ
 * @version $Id: ThreadPoolStoppedException.java, v 0.1 2014-1-6 ÏÂÎç05:45:54 Exp $
 */
public class ThreadPoolStoppedException extends RuntimeException {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Create a new ThreadPoolStoppedException
     */
    public ThreadPoolStoppedException() {
        super();
    }

    /**
     * Create a new ThreadPoolStoppedException
     *
     * @param message the message
     */
    public ThreadPoolStoppedException(String message) {
        super(message);
    }

    // Public --------------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
