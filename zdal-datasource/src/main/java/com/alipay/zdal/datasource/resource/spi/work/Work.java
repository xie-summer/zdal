/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi.work;

/**
 * Work submitted to a work manager
 */
public interface Work extends Runnable {
    /**
     * Invoked by the work manager as a hint to stop processing
     */
    void release();
}