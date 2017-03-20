/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

import java.util.EventListener;

/**
 * An interface used to handle <tt>Throwable</tt> events.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: ThrowableListener.java, v 0.1 2014-1-6 ÏÂÎç05:40:34 Exp $
 */
public interface ThrowableListener extends EventListener {
    /**
     * Process a throwable.
     *
     * @param type    The type off the throwable.
     * @param t       Throwable
     */
    void onThrowable(int type, Throwable t);
}
