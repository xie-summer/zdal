/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.timeout;

/**
 * The interface of objects that can receive timeouts.
 *   
 * @author ²®ÑÀ
 * @version $Id: TimeoutTarget.java, v 0.1 2014-1-6 ÏÂÎç05:46:26 Exp $
 */
public interface TimeoutTarget {
    /**
     *  The timeout callback function is invoked when the timeout expires.
     */
    public void timedOut(Timeout timeout);
}
