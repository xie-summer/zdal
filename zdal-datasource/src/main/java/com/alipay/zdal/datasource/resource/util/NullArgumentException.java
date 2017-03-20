/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

/**
 * Thrown to indicate that a method argument was <tt>null</tt> and 
 * should <b>not</b> have been.
 *
 * @author ²®ÑÀ
 * @version $Id: NullArgumentException.java, v 0.1 2014-1-6 ÏÂÎç05:40:03 Exp $
 */
public class NullArgumentException extends IllegalArgumentException {
    /** The name of the argument that was <tt>null</tt>. */
    protected final String name;

    /** The index of the argument or null if no index. */
    protected final Object index;

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name    Argument name.
     */
    public NullArgumentException(final String name) {
        super(makeMessage(name));

        this.name = name;
        this.index = null;
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name    Argument name.
     * @param index   Argument index.
     */
    public NullArgumentException(final String name, final long index) {
        super(makeMessage(name, new Long(index)));

        this.name = name;
        this.index = new Long(index);
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name    Argument name.
     * @param index   Argument index.
     */
    public NullArgumentException(final String name, final Object index) {
        super(makeMessage(name, index));

        this.name = name;
        this.index = index;
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     */
    public NullArgumentException() {
        this.name = null;
        this.index = null;
    }

    /**
     * Get the argument name that was <tt>null</tt>.
     *
     * @return  The argument name that was <tt>null</tt>.
     */
    public final String getArgumentName() {
        return name;
    }

    /**
     * Get the argument index.
     *
     * @return  The argument index.
     */
    public final Object getArgumentIndex() {
        return index;
    }

    /**
     * Make a execption message for the argument name.
     */
    private static String makeMessage(final String name) {
        return "'" + name + "' is null";
    }

    /**
     * Make a execption message for the argument name and index
     */
    private static String makeMessage(final String name, final Object index) {
        return "'" + name + "[" + index + "]' is null";
    }
}
