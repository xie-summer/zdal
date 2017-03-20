/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

/**
 * Thrown to indicate that a Throwable was caught but was not expected.
 * This is typical when catching Throwables to handle and rethrow Exceptions
 * and Errors.
 *
 * @author 伯牙
 * @version $Id: UnexpectedThrowable.java, v 0.1 2014-1-6 下午05:40:45 Exp $
 */
public class UnexpectedThrowable extends NestedError {
    /**
     * Construct a <tt>UnexpectedThrowable</tt> with the specified 
     * detail message.
     *
     * @param msg  Detail message.
     */
    public UnexpectedThrowable(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>UnexpectedThrowable</tt> with the specified
     * detail message and nested <tt>Throwable</tt>.
     *
     * @param msg     Detail message.
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public UnexpectedThrowable(final String msg, final Throwable nested) {
        super(msg, nested);
    }

    /**
     * Construct a <tt>UnexpectedThrowable</tt> with the specified
     * nested <tt>Throwable</tt>.
     *
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public UnexpectedThrowable(final Throwable nested) {
        super(nested);
    }

    /**
     * Construct a <tt>UnexpectedThrowable</tt> with no detail.
     */
    public UnexpectedThrowable() {
        super();
    }
}
