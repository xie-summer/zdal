/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.exception;

/**
 * 
 * 
 * @author liangjie.li
 * @version $Id: DBNotAvailableException.java, v 0.1 May 18, 2012 2:57:22 PM liangjie.li Exp $
 */
public class TimeOutException extends BaseException {

    /**  */
    private static final long serialVersionUID = -6117676801548326638L;

    /**
     * Construct a <tt>TimeOutAvailableException</tt> with the specified detail 
     * message.
     *
     * @param msg  Detail message.
     */
    public TimeOutException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>TimeOutAvailableException</tt> with the specified detail 
     * message and nested <tt>Throwable</tt>.
     *
     * @param msg     Detail message.
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public TimeOutException(final String msg, final Throwable nested) {
        super(msg, nested);
    }

    /**
     * Construct a <tt>TimeOutAvailableException</tt> with the specified
     * nested <tt>Throwable</tt>.
     *
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public TimeOutException(final Throwable nested) {
        this(nested.getMessage(), nested);
    }

}
