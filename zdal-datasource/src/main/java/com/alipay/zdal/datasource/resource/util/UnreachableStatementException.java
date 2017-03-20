/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

/**
 * Thrown to indicate that section of code that should never have been
 * reachable, has just been reached.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: UnreachableStatementException.java, v 0.1 2014-1-6 ÏÂÎç05:40:55 Exp $
 */
public class UnreachableStatementException extends RuntimeException {
    /**
     * Construct a <tt>UnreachableStatementException</tt> with a detail message.
     *
     * @param msg  Detail message.
     */
    public UnreachableStatementException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>UnreachableStatementException</tt> with no detail.
     */
    public UnreachableStatementException() {
        super();
    }
}
