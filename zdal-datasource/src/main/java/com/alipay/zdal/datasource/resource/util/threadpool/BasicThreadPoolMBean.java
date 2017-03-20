/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * Management interface for the thread pool.
 *
 * @author ²®ÑÀ
 * @version $Id: BasicThreadPoolMBean.java, v 0.1 2014-1-6 ÏÂÎç05:43:49 Exp $
 */
public interface BasicThreadPoolMBean extends ThreadPoolMBean {
    // Constants -----------------------------------------------------

    // Public --------------------------------------------------------

    /**
     * Get the current queue size
    *
     * @return the queue size
     */
    int getQueueSize();

    /**
     * Get the maximum queue size
     *
     * @return the maximum queue size
     */
    int getMaximumQueueSize();

    /**
     * Set the maximum queue size
     *
     * @param size the new maximum queue size
     */
    void setMaximumQueueSize(int size);

    /**
     * @return the blocking mode
     */
    BlockingMode getBlockingMode();

    /** Set the behavior of the pool when a task is added and the queue is full.
     * The mode string indicates one of the following modes:
     * abort - a RuntimeException is thrown
     * run - the calling thread executes the task
     * wait - the calling thread blocks until the queue has room
     * discard - the task is silently discarded without being run
     * discardOldest - check to see if a task is about to complete and enque
     *    the new task if possible, else run the task in the calling thread
     * 
     * @param mode one of run, wait, discard, discardOldest or abort without
     *    regard to case.
     */
    void setBlockingMode(BlockingMode mode);

    /**
     * Retrieve the thread group name
     *
     * @return the thread group name
     */
    String getThreadGroupName();

    /**
     * Set the thread group name
     *
     * @param threadGroupName - the thread group name
     */
    void setThreadGroupName(String threadGroupName);

    /**
     * Get the keep alive time
     *
     * @return the keep alive time
     */
    long getKeepAliveTime();

    /**
     * Set the keep alive time
     *
     * @param time the keep alive time
     */
    void setKeepAliveTime(long time);

    // Inner classes -------------------------------------------------
}
