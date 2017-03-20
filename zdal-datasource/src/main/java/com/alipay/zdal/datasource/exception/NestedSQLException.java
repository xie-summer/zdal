/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.exception;

import java.sql.SQLException;

/**
 * A common superclass for <tt>SQLException</tt> classes that can contain
 * a nested <tt>Throwable</tt> detail object.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: NestedSQLException.java, v 0.1 2014-1-6 ÏÂÎç05:25:42 Exp $
 */
public class NestedSQLException extends SQLException {
    /**  */
    private static final long serialVersionUID = -441747636494736964L;

    /**
     * Construct a <tt>NestedSQLException</tt> with the specified detail 
     * message.
     *
     * @param msg  Detail message.
     */
    public NestedSQLException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>NestedSQLException</tt> with the specified detail 
     * message and nested <tt>Throwable</tt>.
     *
     * @param msg     Detail message.
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public NestedSQLException(final String msg, final Throwable nested) {
        super(msg, nested);
    }

    /**
     * Construct a <tt>NestedSQLException</tt> with the specified
     * nested <tt>Throwable</tt>.
     *
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public NestedSQLException(final Throwable nested) {
        this(nested.getMessage(), nested);
    }

    /**
     * Construct a <tt>NestedSQLException</tt>.
     *
     * @param msg     Detail message.
     * @param state   SQL state message.
     */
    public NestedSQLException(final String msg, final String state) {
        super(msg, state);
    }

    /**
     * Construct a <tt>NestedSQLException</tt>.
     *
     * @param msg     Detail message.
     * @param state   SQL state message.
     * @param code    SQL vendor code.
     */
    public NestedSQLException(final String msg, final String state, final int code) {
        super(msg, state, code);
    }

}
