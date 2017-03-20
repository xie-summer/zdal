/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * The task was stopped.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: TaskStoppedException.java, v 0.1 2014-1-6 ÏÂÎç05:45:00 Exp $
 */
public class TaskStoppedException extends RuntimeException {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Create a new TaskStoppedException
     */
    public TaskStoppedException() {
        super();
    }

    /**
     * Create a new TaskStoppedException
     *
     * @param message the message
     */
    public TaskStoppedException(String message) {
        super(message);
    }

    // Public --------------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
