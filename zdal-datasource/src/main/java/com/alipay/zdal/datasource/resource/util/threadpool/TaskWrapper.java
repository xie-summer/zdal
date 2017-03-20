/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * A task wrapper for a thread pool.
 *
 * @author ²®ÑÀ
 * @version $Id: TaskWrapper.java, v 0.1 2014-1-6 ÏÂÎç05:45:09 Exp $
 */
public interface TaskWrapper extends Runnable {
    // Constants -----------------------------------------------------

    // Public --------------------------------------------------------

    /**
     * Get the type of wait
     *
     * @return the wait type
     */
    int getTaskWaitType();

    /**
     * The priority of the task
     *
     * @return the task priority
     */
    int getTaskPriority();

    /**
     * The time before the task must be accepted
     *
     * @return the start timeout
     */
    long getTaskStartTimeout();

    /**
     * The time before the task must be completed
     *
     * @return the completion timeout
     */
    long getTaskCompletionTimeout();

    /**
     * Wait according the wait type
     */
    void waitForTask();

    /**
     * Invoked by the threadpool when it wants to stop the task
     */
    void stopTask();

    /**
     * The task has been accepted
     *
     */
    void acceptTask();

    /**
     * The task has been rejected
     *
     * @param e any error associated with the rejection
     */
    void rejectTask(RuntimeException e);

    /**
     * Indicate if the task has exited the Runnable#run method
     * @return true if the task has exited the Runnable#run method
     */
    boolean isComplete();
    // Inner classes -------------------------------------------------
}
