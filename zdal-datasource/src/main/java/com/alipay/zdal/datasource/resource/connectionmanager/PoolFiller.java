/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * PoolFiller
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: PoolFiller.java, v 0.1 2014-1-6 ÏÂÎç05:35:56 Exp $
 */
public class PoolFiller implements Runnable {
    private final LinkedList<InternalManagedConnectionPool> pools       = new LinkedList<InternalManagedConnectionPool>();
    private static final Logger                             logger      = Logger
                                                                            .getLogger(PoolFiller.class);
    private final Thread                                    fillerThread;

    private static final PoolFiller                         filler      = new PoolFiller();
    private static ClassLoader                              classLoader = null;

    public static void fillPool(InternalManagedConnectionPool mcp) {
        filler.internalFillPool(mcp);
    }

    public PoolFiller() {
        fillerThread = new Thread(this, "JCA PoolFiller");
        fillerThread.setDaemon(true);
        fillerThread.start();
    }

    public void run() {
        if (classLoader == null) {
            PoolFiller.classLoader = getClass().getClassLoader();
        }
        Thread.currentThread().setContextClassLoader(PoolFiller.classLoader);
        //keep going unless interrupted
        while (true) {
            try {
                InternalManagedConnectionPool mcp = null;
                //keep iterating through pools till empty, exception escapes.
                while (true) {

                    synchronized (pools) {
                        mcp = pools.removeFirst();
                    }
                    if (mcp == null) {
                        break;
                    }

                    mcp.fillToMin();
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e.getMessage());
                }
            }

            try {
                synchronized (pools) {
                    while (pools.isEmpty()) {
                        pools.wait();

                    }
                }
            } catch (InterruptedException ie) {
                return;
            }
        }
    }

    private void internalFillPool(InternalManagedConnectionPool mcp) {
        synchronized (pools) {
            pools.addLast(mcp);
            pools.notify();
        }
    }

    public static void setClassLoader(ClassLoader classLoader) {
        PoolFiller.classLoader = classLoader;
    }

}
