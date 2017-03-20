/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.alipay.zdal.datasource.resource.util.NestedThrowable;

/**
 * 
 * 
 * @author liangjie.li
 * @version $Id: AbstractException.java, v 0.1 May 18, 2012 3:06:17 PM liangjie.li Exp $
 */
public class BaseException extends SQLException implements NestedThrowable {

    /**  */
    private static final long serialVersionUID = -2725065717652326046L;
    /** The nested throwable */
    protected final Throwable nested;

    /**
     * Construct a <tt>DBNotAvailableException</tt> with the specified detail 
     * message.
     *
     * @param msg  Detail message.
     */
    public BaseException(final String msg) {
        super(msg);
        this.nested = null;
    }

    /**
     * Construct a <tt>DBNotAvailableException</tt> with the specified detail 
     * message and nested <tt>Throwable</tt>.
     *
     * @param msg     Detail message.
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public BaseException(final String msg, final Throwable nested) {
        super(msg);
        this.nested = nested;
        NestedThrowable.Util.checkNested(this, nested);
    }

    /**
     * Construct a <tt>DBNotAvailableException</tt> with the specified
     * nested <tt>Throwable</tt>.
     *
     * @param nested  Nested <tt>Throwable</tt>.
     */
    public BaseException(final Throwable nested) {
        this(nested.getMessage(), nested);
    }

    /**
     * Construct a <tt>NestedSQLException</tt>.
     *
     * @param msg     Detail message.
     * @param state   SQL state message.
     */
    public BaseException(final String msg, final String state) {
        super(msg, state);
        this.nested = null;
    }

    /**
     * Construct a <tt>NestedSQLException</tt>.
     *
     * @param msg     Detail message.
     * @param state   SQL state message.
     * @param code    SQL vendor code.
     */
    public BaseException(final String msg, final String state, final int code) {
        super(msg, state, code);
        this.nested = null;
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getNested() {
        return nested;
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * <p>For JDK 1.4 compatibility.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getCause() {
        return nested;
    }

    /**
     * Returns the composite throwable message.
     *
     * @return  The composite throwable message.
     */
    public String getMessage() {
        return NestedThrowable.Util.getMessage(super.getMessage(), nested);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print stream.
     *
     * @param stream  Stream to print to.
     */
    public void printStackTrace(final PrintStream stream) {
        if (nested == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(stream);
        }
        NestedThrowable.Util.print(nested, stream);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print writer.
     *
     * @param writer  Writer to print to.
     */
    public void printStackTrace(final PrintWriter writer) {
        if (nested == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(writer);
        }
        NestedThrowable.Util.print(nested, writer);
    }

    /**
     * Prints the composite message and the embedded stack trace to 
     * <tt>System.err</tt>.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }
}
