/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.transaction.xa.XAException;

import com.alipay.zdal.datasource.resource.util.NestedThrowable;

/**
 * Thrown to indicate a problem with a xaresource related operation.
 *
 * <p>
 * Properly displays linked exception (ie. nested exception)
 * when printing the stack trace.
 *
 * @author ²®ÑÀ
 * @version $Id: JBossXAException.java, v 0.1 2014-1-6 ÏÂÎç05:47:16 Exp $
 */
public class JBossXAException extends XAException implements NestedThrowable {
    /** The serial version uid*/
    private static final long serialVersionUID = 6614203184612359692L;

    /** The linked exception */
    Throwable                 linked;

    /**
     * Rethrow as an xa exception if it is not already
     * 
     * @param message the message
     * @param t the original exception
     * @throws XAException the xa exception
     */
    public static void rethrowAsXAException(String message, Throwable t) throws XAException {
        if (t instanceof XAException)
            throw (XAException) t;
        else
            throw new JBossXAException(message, t);
    }

    /**
     * Construct a <tt>JBossXAException</tt> with the specified detail
     * message.
     *
     * @param msg  Detail message.
     */
    public JBossXAException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>JBossXAException</tt> with the specified detail
     * message and error code.
     *
     * @param code  Error code.
     */
    public JBossXAException(final int code) {
        super(code);
    }

    /**
     * Construct a <tt>JBossXAException</tt> with the specified detail
     * message and linked <tt>Exception</tt>.
     *
     * @param msg     Detail message.
     * @param linked  Linked <tt>Exception</tt>.
     */
    public JBossXAException(final String msg, final Throwable linked) {
        super(msg);
        this.linked = linked;
    }

    /**
     * Construct a <tt>JBossXAException</tt> with the specified
     * linked <tt>Exception</tt>.
     *
     * @param linked  Linked <tt>Exception</tt>.
     */
    public JBossXAException(final Throwable linked) {
        this(linked.getMessage(), linked);
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getNested() {
        return linked;
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * <p>For JDK 1.4 compatibility.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getCause() {
        return linked;
    }

    /**
     * Returns the composite throwable message.
     *
     * @return  The composite throwable message.
     */
    public String getMessage() {
        return NestedThrowable.Util.getMessage(super.getMessage(), linked);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print stream.
     *
     * @param stream  Stream to print to.
     */
    public void printStackTrace(final PrintStream stream) {
        if (linked == null || NestedThrowable.PARENT_TRACE_ENABLED)
            super.printStackTrace(stream);
        NestedThrowable.Util.print(linked, stream);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print writer.
     *
     * @param writer  Writer to print to.
     */
    public void printStackTrace(final PrintWriter writer) {
        if (linked == null || NestedThrowable.PARENT_TRACE_ENABLED)
            super.printStackTrace(writer);
        NestedThrowable.Util.print(linked, writer);
    }

    /**
     * Prints the composite message and the embedded stack trace to
     * <tt>System.err</tt>.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }
}
