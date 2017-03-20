/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

import org.apache.log4j.Logger;

/**
 * Makes a runnable a task.
 *
 * @author ²®ÑÀ
 * @version $Id: RunnableTaskWrapper.java, v 0.1 2014-1-6 ÏÂÎç05:44:27 Exp $
 */
public class RunnableTaskWrapper implements TaskWrapper {
    // Constants -----------------------------------------------------

    /** The log */
    private static final Logger log = Logger.getLogger(RunnableTaskWrapper.class);

    // Attributes ----------------------------------------------------

    /** The runnable */
    private final Runnable      runnable;
    private boolean             started;
    private Thread              runThread;
    /** The start timeout */
    private final long          startTimeout;
    /** The completion timeout */
    private final long          completionTimeout;

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Create a new RunnableTaskWrapper
     *
     * @param runnable the runnable
     * @throws IllegalArgumentException for a null runnable
     */
    public RunnableTaskWrapper(Runnable runnable) {
        this(runnable, 0, 0);
    }

    public RunnableTaskWrapper(Runnable runnable, long startTimeout, long completeTimeout) {
        if (runnable == null)
            throw new IllegalArgumentException("Null runnable");
        this.runnable = runnable;
        this.startTimeout = startTimeout;
        this.completionTimeout = completeTimeout;
    }

    // Public --------------------------------------------------------

    // TaskWrapper implementation ---------------------------------------

    public int getTaskWaitType() {
        return Task.WAIT_NONE;
    }

    public int getTaskPriority() {
        return Thread.NORM_PRIORITY;
    }

    public long getTaskStartTimeout() {
        return startTimeout;
    }

    public long getTaskCompletionTimeout() {
        return completionTimeout;
    }

    public void acceptTask() {
        // Nothing to do
    }

    public void rejectTask(RuntimeException t) {
        throw t;
    }

    public void stopTask() {
        // Interrupt the run thread if its not null
        if (runThread != null && runThread.isInterrupted() == false) {
            runThread.interrupt();
            if (log.isDebugEnabled()) {
                log.debug("stopTask, interrupted thread=" + runThread);
            }

        } else if (runThread != null) {
            /* If the thread has not been returned after being interrupted, then
            use the deprecated stop method to try to force the thread abort.
            */
            runThread.stop();
            if (log.isDebugEnabled()) {
                log.debug("stopTask, stopped thread=" + runThread);
            }

        }
    }

    public void waitForTask() {
        // Nothing to do
    }

    public boolean isComplete() {
        return started == true && runThread == null;
    }

    // Runnable implementation ---------------------------------------

    public void run() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Begin run, wrapper=" + this);
            }

            runThread = Thread.currentThread();
            started = true;
            runnable.run();
            runThread = null;
            if (log.isDebugEnabled()) {
                log.debug("End run, wrapper=" + this);
            }

        } catch (Throwable t) {
            log.warn("Unhandled throwable for runnable: " + runnable, t);
        }
    }

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
