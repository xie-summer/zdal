/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * A task for a thread pool.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: Task.java, v 0.1 2014-1-6 ÏÂÎç05:44:50 Exp $
 */
public interface Task {
    // Constants -----------------------------------------------------

    /** Don't wait for task */
    static final int WAIT_NONE         = 0;

    /** Synchronized start, wait for task to start */
    static final int WAIT_FOR_START    = 1;

    /** Synchronized task, wait for task to complete */
    static final int WAIT_FOR_COMPLETE = 2;

    // Public --------------------------------------------------------

    /**
     * Get the type of wait
     *
     * @return the wait type
     */
    int getWaitType();

    /**
     * The priority of the task
     *
     * @return the task priority
     */
    int getPriority();

    /**
     * The time before the task must be accepted
     *
     * @return the start timeout
     */
    long getStartTimeout();

    /**
     * The time before the task must be completed
     *
     * @return the completion timeout
     */
    long getCompletionTimeout();

    /**
     * Execute the task
     */
    void execute();

    /**
     * Invoked by the threadpool when it wants to stop the task
     */
    void stop();

    /**
     * The task has been accepted
     *
     * @param time the time taken to accept the task
     */
    void accepted(long time);

    /**
     * The task has been rejected
     *
     * @param time the time taken to reject the task
     * @param throwable any error associated with the rejection
     */
    void rejected(long time, Throwable t);

    /**
     * The task has been started
     *
     * @param time the time taken to start the task
     */
    void started(long time);

    /**
     * The task has been completed
     *
     * @param time the time taken to reject the task
     * @param throwable any error associated with the completion
     */
    void completed(long time, Throwable t);

    // Inner classes -------------------------------------------------
}
