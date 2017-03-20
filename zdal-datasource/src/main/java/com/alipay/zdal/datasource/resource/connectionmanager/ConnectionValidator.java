/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

/**
 * A ConnectionValidator that performs background validation of managed connections for an
 * InternalManagedConnectionPool.
 * 
 * 
 * @author ²®ÑÀ
 * @version $Id: ConnectionValidator.java, v 0.1 2014-1-6 ÏÂÎç05:34:20 Exp $
 */
public class ConnectionValidator {

    /** The log */
    private static final Logger                                       log       = Logger
                                                                                    .getLogger(ConnectionValidator.class);

    /** The pools */
    private final CopyOnWriteArrayList<InternalManagedConnectionPool> pools     = new CopyOnWriteArrayList<InternalManagedConnectionPool>();

    /** The interval */
    private long                                                      interval  = Long.MAX_VALUE;

    /** The next */
    private long                                                      next      = Long.MAX_VALUE;                                           //important initialization!

    /** The validator */
    private static final ConnectionValidator                          validator = new ConnectionValidator();

    private ConnectionValidator() {

        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                Runnable runnable = new ConnectionValidatorRunnable();
                Thread removerThread = new Thread(runnable, "zdal-datasource-Connection-Validator");
                removerThread.setDaemon(true);
                removerThread.start();
                return null;
            }
        });

    }

    public static void registerPool(InternalManagedConnectionPool mcp, long interval) {
        validator.internalRegisterPool(mcp, interval);
    }

    public static void unRegisterPool(InternalManagedConnectionPool mcp) {
        validator.internalUnregisterPool(mcp);

    }

    private void internalRegisterPool(InternalManagedConnectionPool mcp, long interval) {

        synchronized (pools) {
            pools.addIfAbsent(mcp);

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

    private void setupContextClassLoader() {
        // Could be null if loaded from system classloader
        final ClassLoader cl = ConnectionValidator.class.getClassLoader();
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
     * 
     */
    public static void waitForBackgroundThread() {
        synchronized (validator.pools) {
            return;
        }
    }

    private class ConnectionValidatorRunnable implements Runnable {

        public void run() {
            setupContextClassLoader();

            synchronized (pools) {

                while (true) {

                    try {

                        pools.wait(interval);

                        if (log.isDebugEnabled()) {
                            log.debug("run: ConnectionValidator notifying pools, interval: "
                                      + interval);
                        }

                        for (Iterator iter = pools.iterator(); iter.hasNext();) {
                            InternalManagedConnectionPool mcp = (InternalManagedConnectionPool) iter
                                .next();
                            mcp.validateConnections();
                        }

                        next = System.currentTimeMillis() + interval;

                        if (next < 0) {
                            next = Long.MAX_VALUE;

                        }

                    } catch (InterruptedException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("run: ConnectionValidator has been interrupted, returning");
                        }

                        return;
                    } catch (RuntimeException e) {
                        log
                            .warn("run: ConnectionValidator ignored unexpected runtime exception",
                                e);
                    } catch (Exception e) {
                        log.warn("run: ConnectionValidator ignored unexpected error", e);

                    }

                }

            }

        }

    }
}
