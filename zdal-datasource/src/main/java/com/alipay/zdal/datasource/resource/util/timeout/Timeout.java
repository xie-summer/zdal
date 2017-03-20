/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.timeout;

/**
 * The public interface of timeouts.
 *   
 * 
 * @author ²®ÑÀ
 * @version $Id: Timeout.java, v 0.1 2014-1-6 ÏÂÎç05:46:05 Exp $
 */
public interface Timeout {
    /**
     * Cancel this timeout.
     *
     * It is guaranteed that on return from this method this timer is
     * no longer active. This means that either it has been cancelled and
     * the timeout will not happen, or (in case of late cancel) the
     * timeout has happened and the timeout callback function has returned.
     *
     * On return from this method this instance should no longer be
     * used. The reason for this is that an implementation may reuse
     * cancelled timeouts, and at return the instance may already be
     * in use for another timeout.
     */
    public boolean cancel();
}