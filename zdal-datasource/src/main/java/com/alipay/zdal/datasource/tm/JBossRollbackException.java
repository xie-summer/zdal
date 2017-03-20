/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.alipay.zdal.datasource.resource.util.NestedThrowable;
import com.alipay.zdal.datasource.transaction.RollbackException;

/**
 * JBossRollbackException.java
 *
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: JBossRollbackException.java, v 0.1 2014-1-6 ÏÂÎç05:46:46 Exp $
 */
public class JBossRollbackException extends RollbackException implements NestedThrowable {
    static final long serialVersionUID = 2924502280803535350L;

    Throwable         t;

    public JBossRollbackException() {
        super();
    }

    public JBossRollbackException(final String message) {
        super(message);
    }

    public JBossRollbackException(final Throwable t) {
        super();
        this.t = t;
    }

    public JBossRollbackException(final String message, final Throwable t) {
        super(message);
        this.t = t;
    }

    // Implementation of org.jboss.util.NestedThrowable

    public Throwable getNested() {
        return t;
    }

    public Throwable getCause() {
        return t;
    }

    /**
     * Returns the composite throwable message.
     *
     * @return  The composite throwable message.
     */
    public String getMessage() {
        return NestedThrowable.Util.getMessage(super.getMessage(), t);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print stream.
     *
     * @param stream  Stream to print to.
     */
    public void printStackTrace(final PrintStream stream) {
        if (t == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(stream);
        }
        NestedThrowable.Util.print(t, stream);
    }

    /**
     * Prints the composite message and the embedded stack trace to the
     * specified print writer.
     *
     * @param writer  Writer to print to.
     */
    public void printStackTrace(final PrintWriter writer) {
        if (t == null || NestedThrowable.PARENT_TRACE_ENABLED) {
            super.printStackTrace(writer);
        }
        NestedThrowable.Util.print(t, writer);
    }

    /**
     * Prints the composite message and the embedded stack trace to
     * <tt>System.err</tt>.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

}// JBossRollbackException
