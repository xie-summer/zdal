/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.alipay.zdal.datasource.validation;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

/**
 *
 */
public class ZConnectionValidator {

    /** The log */
    private static final Logger                                    log       = Logger
                                                                                 .getLogger(ZConnectionValidator.class);

    /** The pools */
    private final CopyOnWriteArrayList<UnreleaseConnectionChecker> pools     = new CopyOnWriteArrayList<UnreleaseConnectionChecker>();

    /** The interval */
    private long                                                   interval  = Long.MAX_VALUE;

    /** The next */
    private long                                                   next      = Long.MAX_VALUE;                                        //important initialization!

    /** The validator */
    private static final ZConnectionValidator                      validator = new ZConnectionValidator();

    private ZConnectionValidator() {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                Runnable runnable = new ConnectionValidatorRunnable();
                Thread removerThread = new Thread(runnable, "SofaUnreleaseConnectionValidator");
                removerThread.setDaemon(true);
                removerThread.start();
                return null;
            }
        });

    }

    public static void registerChecker(UnreleaseConnectionChecker ucc, long interval) {
        validator.internalRegisterChecker(ucc, interval);
    }

    public static void unRegisterChecker(UnreleaseConnectionChecker ucc) {
        validator.internalUnregisterChecker(ucc);

    }

    private void internalRegisterChecker(UnreleaseConnectionChecker ucc, long interval) {
        synchronized (pools) {
            pools.addIfAbsent(ucc);

            if (interval > 1 && interval / 2 < this.interval) {
                this.interval = interval / 2;
                long maybeNext = System.currentTimeMillis() + this.interval;
                if (next > maybeNext && maybeNext > 0) {
                    next = maybeNext;
                    if (log.isDebugEnabled())
                        log.debug("internalRegisterChecker: about to notify thread: old next: "
                                  + next + ", new next: " + maybeNext);
                    pools.notify();
                }
            }
        }

    }

    private void internalUnregisterChecker(UnreleaseConnectionChecker mcp) {
        synchronized (pools) {
            pools.remove(mcp);
            if (pools.size() == 0) {
                if (log.isDebugEnabled())
                    log.debug("internalUnregisterChecker: setting interval to Long.MAX_VALUE");
                interval = Long.MAX_VALUE;
            }
        }
    }

    private void setupContextClassLoader() {
        // Could be null if loaded from system classloader
        final ClassLoader cl = ZConnectionValidator.class.getClassLoader();
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
                            log.debug("run: ZConnectionValidator notifying pools, interval: "
                                      + interval);
                        }

                        for (UnreleaseConnectionChecker ucc : pools) {
                            ucc.connectionCheck();
                        }

                        next = System.currentTimeMillis() + interval;

                        if (next < 0) {
                            next = Long.MAX_VALUE;
                        }

                    } catch (InterruptedException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("run: ZConnectionValidator has been interrupted, returning");
                        }

                        return;
                    } catch (RuntimeException e) {
                        log.warn("run: ZConnectionValidator ignored unexpected runtime exception",
                            e);
                    } catch (Exception e) {
                        log.warn("run: ZConnectionValidator ignored unexpected error", e);

                    }

                }

            }

        }

    }

}
