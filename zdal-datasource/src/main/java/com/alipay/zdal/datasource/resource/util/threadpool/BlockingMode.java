/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

import java.io.ObjectStreamException;
import java.io.Serializable;

/** A type-safe enum for the BasicThreadPool blocking mode.
 * 
 * @author ²®ÑÀ
 * @version $Id: BlockingMode.java, v 0.1 2014-1-6 ÏÂÎç05:44:02 Exp $
 */
public class BlockingMode implements Serializable {
    /** @since 1.0 */
    private static final long        serialVersionUID    = -9102277941374138830L;

    public static final int          RUN_TYPE            = 0;
    public static final int          WAIT_TYPE           = 1;
    public static final int          DISCARD_TYPE        = 2;
    public static final int          DISCARD_OLDEST_TYPE = 3;
    public static final int          ABORT_TYPE          = 4;

    /** Set the policy for blocked execution to be that the current thread
       executes the command if there are no available threads in the pool.
     */
    public static final BlockingMode RUN                 = new BlockingMode("run", RUN_TYPE);
    /** Set the policy for blocked execution to be to wait until a thread
     * is available, unless the pool has been shut down, in which case
     * the action is discarded.
     */
    public static final BlockingMode WAIT                = new BlockingMode("wait", WAIT_TYPE);
    /** Set the policy for blocked execution to be to return without
     * executing the request.
     */
    public static final BlockingMode DISCARD             = new BlockingMode("discard", DISCARD_TYPE);
    /** Set the policy for blocked execution to be to discard the oldest
     * unhandled request
     */
    public static final BlockingMode DISCARD_OLDEST      = new BlockingMode("discardOldest",
                                                             DISCARD_OLDEST_TYPE);
    /** Set the policy for blocked execution to be to throw an AbortWhenBlocked
     * (a subclass of RuntimeException).
     */
    public static final BlockingMode ABORT               = new BlockingMode("abort", ABORT_TYPE);

    /** The string form of the enum */
    private final transient String   name;
    /** The enum manifest constant */
    private final int                type;

    /** A utility method to convert a string name to a BlockingMode
     * @param name
     * @return The associated BlockingMode constant if name is valid, null otherwise
     */
    public static final BlockingMode toBlockingMode(String name) {
        BlockingMode mode = null;
        if (name == null) {
            mode = null;
        } else if (name.equalsIgnoreCase("run")) {
            mode = RUN;
        } else if (name.equalsIgnoreCase("wait")) {
            mode = WAIT;
        } else if (name.equalsIgnoreCase("discard")) {
            mode = DISCARD;
        } else if (name.equalsIgnoreCase("discardOldest")) {
            mode = DISCARD_OLDEST;
        } else if (name.equalsIgnoreCase("abort")) {
            mode = ABORT;
        }
        return mode;
    }

    private BlockingMode(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return name;
    }

    /**
     * Overriden to return the indentity instance of BlockingMode based on the
     * stream type int value. This ensures that BlockingMode enums can be
     * compared using ==.
     * 
     * @return The BlockingMode instance for the XXX_TYPE int.
     * @throws ObjectStreamException
     */
    Object readResolve() throws ObjectStreamException {
        // Replace the marshalled instance type with the local instance
        BlockingMode mode = ABORT;
        switch (type) {
            case RUN_TYPE:
                mode = RUN;
                break;
            case WAIT_TYPE:
                mode = RUN;
                break;
            case DISCARD_TYPE:
                mode = RUN;
                break;
            case DISCARD_OLDEST_TYPE:
                mode = RUN;
                break;
            case ABORT_TYPE:
                mode = RUN;
                break;
        }
        return mode;
    }
}
