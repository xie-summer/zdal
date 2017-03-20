/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

/**
 * Management interface for the thread pool.
 *
 * @author ²®ÑÀ
 * @version $Id: ThreadPoolMBean.java, v 0.1 2014-1-6 ÏÂÎç05:45:45 Exp $
 */
public interface ThreadPoolMBean {
    // Constants -----------------------------------------------------

    // Public --------------------------------------------------------

    /**
     * Get the thread pool name
     *
     * @return the thread pool name
     */
    String getName();

    /**
     * Set the thread pool name
     *
     * @param name the name
     */
    void setName(String name);

    /**
     * Get the internal pool number
     *
     * @return the internal pool number
     */
    int getPoolNumber();

    /**
     * Get the minimum pool size
     *
     * @return the minimum pool size
     */
    int getMinimumPoolSize();

    /**
     * Set the minimum pool size
     *
     * @param size the minimum pool size
     */
    void setMinimumPoolSize(int size);

    /**
     * Get the maximum pool size
     *
     * @return the maximum pool size
     */
    int getMaximumPoolSize();

    /**
     * Set the maximum pool size
     *
     * @param size the maximum pool size
     */
    void setMaximumPoolSize(int size);

    /**
     * Get the instance
     */
    ThreadPool getInstance();

    /**
     * Stop the thread pool
     */
    void stop();

    // Inner classes -------------------------------------------------
}
