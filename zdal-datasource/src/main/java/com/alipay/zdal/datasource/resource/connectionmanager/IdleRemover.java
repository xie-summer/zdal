/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * IdleRemover
 *
 * @author ²®ÑÀ
 * @version $Id: IdleRemover.java, v 0.1 2014-1-6 ÏÂÎç05:34:35 Exp $
 */
public class IdleRemover {
    private final Logger             log      = Logger.getLogger(getClass());

    private final Collection         pools    = new ArrayList();

    private long                     interval = Long.MAX_VALUE;

    private long                     next     = Long.MAX_VALUE;              //important initialization!

    private static final IdleRemover remover  = new IdleRemover();

    /**
     * 
     * @param mcp
     * @param interval
     */
    public static void registerPool(InternalManagedConnectionPool mcp, long interval) {
        remover.internalRegisterPool(mcp, interval);
    }

    /**
     * 
     * @param mcp
     */
    public static void unregisterPool(InternalManagedConnectionPool mcp) {
        remover.internalUnregisterPool(mcp);
    }

    /**
     * For testing
     */
    public static void waitForBackgroundThread() {
        synchronized (remover.pools) {
            return;
        }
    }

    private IdleRemover() {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                Runnable runnable = new IdleRemoverRunnable();
                Thread removerThread = new Thread(runnable,
                    "zdal-datasource-Connection-IdleRemover");
                removerThread.setDaemon(true);
                removerThread.start();
                return null;
            }
        });
    }

    private void internalRegisterPool(InternalManagedConnectionPool mcp, long interval) {
        if (log.isDebugEnabled()) {
            log.debug("internalRegisterPool: registering pool with interval " + interval
                      + " old interval: " + this.interval);
        }

        synchronized (pools) {
            pools.add(mcp);
            if (interval > 1 && interval / 2 < this.interval) {
                this.interval = interval / 2;
                long maybeNext = System.currentTimeMillis() + this.interval;
                if (next > maybeNext && maybeNext > 0) {
                    next = maybeNext;

                    if (log.isDebugEnabled()) {
                        log.debug("internalRegisterPool: about to notify thread: old next: " + next
                                  + ", new next: " + maybeNext);
                    }

                    pools.notify();
                }
            }
        }
    }

    private void internalUnregisterPool(InternalManagedConnectionPool mcp) {
        synchronized (pools) {
            pools.remove(mcp);
            if (pools.size() == 0) {

                if (log.isDebugEnabled()) {
                    log.debug("internalUnregisterPool: setting interval to Long.MAX_VALUE");
                }

                interval = Long.MAX_VALUE;
            }
        }
    }

    /**
     * Change the context classloader to be where the idle remover was loaded from.<p>
     * 
     * This avoids holding a reference to the caller's classloader which may be undeployed.
     */
    private void setupContextClassLoader() {
        // Could be null if loaded from system classloader
        final ClassLoader cl = IdleRemover.class.getClassLoader();
        if (cl == null)
            return;

        SecurityManager sm = System.getSecurityManager();
        if (sm == null)
            Thread.currentThread().setContextClassLoader(cl);

        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                Thread.currentThread().setContextClassLoader(cl);
                return null;
            }
        });
    }

    /**
     * Idle Remover background thread
     */
    private class IdleRemoverRunnable implements Runnable {
        public void run() {
            setupContextClassLoader();

            synchronized (pools) {
                while (true) {
                    try {
                        pools.wait(interval);

                        if (log.isDebugEnabled()) {
                            log.debug("run: IdleRemover notifying pools, interval: " + interval);
                        }

                        for (Iterator i = pools.iterator(); i.hasNext();)
                            ((InternalManagedConnectionPool) i.next()).removeTimedOut();
                        next = System.currentTimeMillis() + interval;
                        if (next < 0)
                            next = Long.MAX_VALUE;

                    } catch (InterruptedException ie) {
                        if (log.isDebugEnabled()) {
                            log.debug("run: IdleRemover has been interrupted, returning");
                        }
                        return;
                    } catch (RuntimeException e) {
                        log.warn("run: IdleRemover ignored unexpected runtime exception", e);
                    } catch (Error e) {
                        log.warn("run: IdleRemover ignored unexpected error", e);
                    }
                }
            }
        }
    }
}
