/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A static singleton that handles processing throwables that otherwise would
 * be ignored or dumped to System.err.
 *
 * @author ²®ÑÀ
 * @version $Id: ThrowableHandler.java, v 0.1 2014-1-6 ÏÂÎç05:40:25 Exp $
 */
public final class ThrowableHandler {
    private static Logger log = Logger.getLogger(ThrowableHandler.class);

    /**
     * Container for throwable types.
     */
    public static interface Type {
        /** Unknown throwable. */
        int UNKNOWN = 0;

        /** Error throwable. */
        int ERROR   = 1;

        /** Warning throwable. */
        int WARNING = 2;
    }

    /////////////////////////////////////////////////////////////////////////
    //                            Listener Methods                         //
    /////////////////////////////////////////////////////////////////////////

    /** The list of listeners */
    protected static List listeners = Collections.synchronizedList(new ArrayList());

    /**
     * Add a ThrowableListener to the listener list.  Listener is added only
     * if if it is not already in the list.
     *
     * @param listener   ThrowableListener to add to the list.
     */
    public static void addThrowableListener(ThrowableListener listener) {
        // only add the listener if it isn't already in the list
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a ThrowableListener from the listener list.
     *
     * @param listener   ThrowableListener to remove from the list.
     */
    public static void removeThrowableListener(ThrowableListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire onThrowable to all registered listeners.
     * 
     * @param type    The type off the throwable.
     * @param t       Throwable
     */
    protected static void fireOnThrowable(int type, Throwable t) {
        Object[] list = listeners.toArray();

        for (int i = 0; i < list.length; i++) {
            ((ThrowableListener) list[i]).onThrowable(type, t);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //                          Throwable Processing                       //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Add a throwable that is to be handled.
     *
     * @param type    The type off the throwable.
     * @param t       Throwable to be handled.
     */
    public static void add(int type, Throwable t) {
        // don't add null throwables
        if (t == null)
            return;

        try {
            fireOnThrowable(type, t);
        } catch (Throwable bad) {
            // don't let these propagate, that could introduce unwanted side-effects
            log.error(bad);
        }
    }

    /**
     * Add a throwable that is to be handled with unknown type.
     *
     * @param t    Throwable to be handled.
     */
    public static void add(Throwable t) {
        add(Type.UNKNOWN, t);
    }

    /**
     * Add a throwable that is to be handled with error type.
     *
     * @param t    Throwable to be handled.
     */
    public static void addError(Throwable t) {
        add(Type.ERROR, t);
    }

    /**
     * Add a throwable that is to be handled with warning type.
     *
     * @param t    Throwable to be handled.
     */
    public static void addWarning(Throwable t) {
        add(Type.ERROR, t);
    }
}
