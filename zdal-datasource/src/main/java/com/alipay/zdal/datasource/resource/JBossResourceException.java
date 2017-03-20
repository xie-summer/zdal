/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.UndeclaredThrowableException;

import com.alipay.zdal.datasource.resource.util.NestedThrowable;

/**
 * Thrown to indicate a problem with a resource related operation.
 *
 * <p>
 * Properly displays linked exception (ie. nested exception)
 * when printing the stack trace.
 *
 * @author 伯牙
 * @version $Id: JBossResourceException.java, v 0.1 2014-1-6 下午05:26:37 Exp $
 */
public class JBossResourceException extends ResourceException implements NestedThrowable {
    /** The servial version uid*/
    private static final long serialVersionUID = 6614203184612359692L;

    /**
     * Rethrow as a resource exception if it is not already
     * 
     * @param message the message
     * @param t the original exception
     * @throws ResourceException the resource exception
     */
    public static void rethrowAsResourceException(String message, Throwable t)
                                                                              throws ResourceException {
        if (t instanceof ResourceException)
            throw (ResourceException) t;
        else
            throw new JBossResourceException(message, t);
    }

    /**
     * Construct a <tt>JBossResourceException</tt> with the specified detail
     * message.
     *
     * @param msg  Detail message.
     */
    public JBossResourceException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>JBossResourceException</tt> with the specified detail
     * message and error code.
     *
     * @param msg   Detail message.
     * @param code  Error code.
     */
    public JBossResourceException(final String msg, final String code) {
        super(msg, code);
    }

    /**
     * Construct a <tt>JBossResourceException</tt> with the specified detail
     * message, error code and linked <tt>Exception</tt>.
     *
     * @param msg     Detail message.
     * @param code    Error code.
     * @param linked  Linked <tt>Exception</tt>.
     */
    public JBossResourceException(final String msg, final String code, final Throwable linked) {
        super(msg, code);
        setLinkedException(process(linked));
    }

    /**
     * Construct a <tt>JBossResourceException</tt> with the specified detail
     * message and linked <tt>Exception</tt>.
     *
     * @param msg     Detail message.
     * @param linked  Linked <tt>Exception</tt>.
     */
    public JBossResourceException(final String msg, final Throwable linked) {
        super(msg);
        setLinkedException(process(linked));
    }

    /**
     * Construct a <tt>JBossResourceException</tt> with the specified
     * linked <tt>Exception</tt>.
     *
     * @param linked  Linked <tt>Exception</tt>.
     */
    public JBossResourceException(final Throwable linked) {
        this(linked.getMessage(), linked);
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getNested() {
        return getLinkedException();
    }

    /**
     * Return the nested <tt>Throwable</tt>.
     *
     * <p>For JDK 1.4 compatibility.
     *
     * @return  Nested <tt>Throwable</tt>.
     */
    public Throwable getCause() {
        return getLinkedException();
    }

    /**
     * Returns the composite throwable message.
     *
     * @return  The composite throwable message.
     */
    public String getMessage() {
        return NestedThrowable.Util.getMessage(super.getMessage(), getLinkedException());
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print stream.
     *
     * @param stream  Stream to print to.
     */
    public void printStackTrace(final PrintStream stream) {
        Exception linked = getLinkedException();
        if (linked == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(stream);
        }
        NestedThrowable.Util.print(linked, stream);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print writer.
     *
     * @param writer  Writer to print to.
     */
    public void printStackTrace(final PrintWriter writer) {
        Exception linked = getLinkedException();
        if (linked == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(writer);
        }
        NestedThrowable.Util.print(linked, writer);
    }

    /**
     * Prints the composite message and the embedded stack trace to
     * <tt>System.err</tt>.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    private Exception process(Throwable t) {
        if (t instanceof Exception) {
            return (Exception) t;
        } // end of if ()
        return new UndeclaredThrowableException(t);
    }
}
