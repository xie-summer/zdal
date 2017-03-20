/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

/**
 * Thrown to indicate that a string was empty (aka. <code>""</code>)
 * where it must <b>not</b> be.
 *
 * @author ²®ÑÀ
 * @version $Id: EmptyStringException.java, v 0.1 2014-1-6 ÏÂÎç05:38:48 Exp $
 */
public class EmptyStringException extends IllegalArgumentException {
    /**
     * Construct a <tt>EmptyStringException</tt>.
     *
     * @param msg  Exception message.
     */
    public EmptyStringException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <tt>EmptyStringException</tt>.
     */
    public EmptyStringException() {
        super();
    }
}
