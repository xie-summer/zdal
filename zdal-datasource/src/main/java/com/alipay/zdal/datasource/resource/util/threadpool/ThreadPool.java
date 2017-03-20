/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * A thread pool.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: ThreadPool.java, v 0.1 2014-1-6 ÏÂÎç05:45:20 Exp $
 */
public interface ThreadPool {
    // Constants -----------------------------------------------------

    // Public --------------------------------------------------------

    /**
     * Stop the pool
     *
     * @param immediate whether to shutdown immediately
     */
    public void stop(boolean immediate);

    /** Wait on the queued tasks to complete. This can only be called after
     * after stop.
     * 
     * @throws InterruptedException
     */
    public void waitForTasks() throws InterruptedException;

    /** Wait on the queued tasks to complete upto maxWaitTime milliseconds. This
     * can only be called after after stop.
     * 
     * @param maxWaitTime
     * @throws InterruptedException
     */
    public void waitForTasks(long maxWaitTime) throws InterruptedException;

    /**
     * Run a task wrapper
     *
     * @param wrapper the task wrapper
     */
    public void runTaskWrapper(TaskWrapper wrapper);

    /**
     * Run a task
     *
     * @param task the task
     * @throws IllegalArgumentException for a null task
     */
    public void runTask(Task task);

    /**
     * Run a runnable
     *
     * @param runnable the runnable
     * @throws IllegalArgumentException for a null runnable
     */
    public void run(Runnable runnable);

    /**
     * 
     * @param runnable
     * @param startTimeout
     * @param completeTimeout
     */
    public void run(Runnable runnable, long startTimeout, long completeTimeout);
}
