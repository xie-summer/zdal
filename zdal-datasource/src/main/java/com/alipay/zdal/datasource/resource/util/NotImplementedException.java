/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

/**
 * Thrown to indicate that a method has not been implemented yet.
 * 
 * <p>This exception is used to help stub out implementations.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: NotImplementedException.java, v 0.1 2014-1-6 ÏÂÎç05:39:53 Exp $
 */
public class NotImplementedException extends RuntimeException {
    /**
     * Construct a <tt>NotImplementedException</tt> with a detail message.
     *
     * @param msg  Detail message.
     */
    public NotImplementedException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>NotImplementedException</tt> with no detail.
     */
    public NotImplementedException() {
        super();
    }
}
