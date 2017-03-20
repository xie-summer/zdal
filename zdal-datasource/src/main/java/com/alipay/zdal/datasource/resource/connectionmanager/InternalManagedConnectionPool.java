/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.Constants;
import com.alipay.zdal.datasource.client.util.ZConstants;
import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.adapter.jdbc.local.LocalManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.spi.ValidatingManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.util.UnreachableStatementException;

/**
 * The internal pool implementation
 *
 * @author 伯牙
 * @version $Id: InternalManagedConnectionPool.java, v 0.1 2014-1-6 下午05:34:47 Exp $
 */
public class InternalManagedConnectionPool {
    private static final Logger             logger               = Logger
                                                                     .getLogger(Constants.ZDAL_DATASOURCE_POOL_LOGNAME);

    private static final String             DEFAULT_FORMAT_STYLE = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat                sdf                  = new SimpleDateFormat(
                                                                     DEFAULT_FORMAT_STYLE);
    /** The managed connection factory */
    private final ManagedConnectionFactory  mcf;

    /** The connection listener factory */
    private final ConnectionListenerFactory clf;

    /** The default subject */
    private final Subject                   defaultSubject;

    /** The default connection request information */
    private final ConnectionRequestInfo     defaultCri;

    /** The pooling parameters */
    private final PoolParams                poolParams;

    /** Copy of the maximum size from the pooling parameters.
     * Dynamic changes to this value are not compatible with
     * the semaphore which cannot change be dynamically changed.
     */
    private final int                       maxSize;

    /** The available connection event listeners */
    private final ArrayList                 connectionListeners;

    /** The permits used to control who can checkout a connection */
    private final InternalSemaphore         permits;

    /** The log */
    private final Logger                    log;

    /** Whether trace is enabled */
    private final boolean                   trace;

    /** Stats */
    private final Counter                   connectionCounter    = new Counter();

    /** The checked out connections */
    private final HashSet                   checkedOut           = new HashSet();

    /** Whether the pool has been started */
    private boolean                         started              = false;

    /** Whether the pool has been shutdown */
    private final AtomicBoolean             shutdown             = new AtomicBoolean(false);

    /** the max connections ever checked out **/
    private volatile int                    maxUsedConnections   = 0;

    private String                          poolName;

    /**
     * Create a new internal pool
     *
     * @param mcf the managed connection factory
     * @param subject the subject
     * @param cri the connection request information
     * @param poolParams the pooling parameters
     * @param log the log
     */
    protected InternalManagedConnectionPool(ManagedConnectionFactory mcf,
                                            ConnectionListenerFactory clf, Subject subject,
                                            ConnectionRequestInfo cri, PoolParams poolParams,
                                            Logger log, String poolName) {
        this.mcf = mcf;
        this.clf = clf;
        defaultSubject = subject;
        defaultCri = cri;
        this.poolParams = poolParams;
        this.poolName = poolName;
        this.maxSize = this.poolParams.maxSize;

        this.log = log;
        this.trace = log.isDebugEnabled();
        connectionListeners = new ArrayList(this.maxSize);
        permits = new InternalSemaphore(this.maxSize);

        if (poolParams.prefill) {
            PoolFiller.fillPool(this);
        }

    }

    /**
     * Initialize the pool
     */
    protected void initialize() {
        if (poolParams.idleTimeout != 0)
            IdleRemover.registerPool(this, poolParams.idleTimeout);

        if (poolParams.backgroundValidation) {

            if (log.isDebugEnabled()) {
                log.debug("Registering for background validation at interval "
                          + poolParams.backgroundInterval);
            }

            ConnectionValidator.registerPool(this, poolParams.backgroundInterval);
        }

        //start a background thread for sofa checker, the interval is <blocking-timeout-millis>
    }

    public long getAvailableConnections() {
        return permits.availablePermits();
    }

    public int getMaxConnectionsInUseCount() {
        return maxUsedConnections;
    }

    public int getConnectionInUseCount() {
        return checkedOut.size();
    }

    /**
     * 强制使连接池的信号量与实际的数据库总连接保持一致
     */
    public void compareAndResetPermit() {
        long total = getAvailableConnections() + getConnectionInUseCount();
        int difference = (int) (maxSize - total);
        if (difference > 0) {
            permits.release(difference);
        } else if (difference < 0) {
            permits.reduceSemaphores(-difference);
        }
    }

    /**
     * todo distinguish between connection dying while match called
     * and bad match strategy.  In latter case we should put it back in
     * the pool.
     */
    public ConnectionListener getConnection(Subject subject, ConnectionRequestInfo cri)
                                                                                       throws ResourceException {

        subject = (subject == null) ? defaultSubject : subject;
        cri = (cri == null) ? defaultCri : cri;
        long startWait = System.currentTimeMillis();
        try {
            if (permits.tryAcquire(poolParams.blockingTimeout, TimeUnit.MILLISECONDS)) {
                //We have a permit to get a connection. Is there one in the pool already?
                ConnectionListener cl = null;
                do {
                    synchronized (connectionListeners) {
                        if (shutdown.get()) {
                            permits.release();
                            throw new ResourceException("The pool has been shutdown");
                        }

                        if (connectionListeners.size() > 0) {
                            cl = (ConnectionListener) connectionListeners
                                .remove(connectionListeners.size() - 1);
                            checkedOut.add(cl);
                            int size = (maxSize - permits.availablePermits());

                            //Update the maxUsedConnections
                            if (size > maxUsedConnections)
                                maxUsedConnections = size;
                        }
                    }
                    if (cl != null) {
                        //Yes, we retrieved a ManagedConnection from the pool. Does it match?
                        try {
                            Object matchedMC = mcf.matchManagedConnections(Collections.singleton(cl
                                .getManagedConnection()), subject, cri);
                            if (matchedMC != null) {
                                if (log.isDebugEnabled()) {
                                    log.debug("supplying ManagedConnection from pool: " + cl);
                                }

                                cl.grantPermit(true);
                                return cl;
                            }

                            //Match did not succeed but no exception was thrown.
                            //Either we have the matching strategy wrong or the
                            //connection died while being checked.  We need to
                            //distinguish these cases, but for now we always
                            //destroy the connection.
                            log
                                .warn("Destroying connection that could not be successfully matched: "
                                      + cl);
                            synchronized (connectionListeners) {
                                checkedOut.remove(cl);
                            }
                            doDestroy(cl, "getConnection");
                            cl = null;

                        } catch (Throwable t) {
                            log.warn(
                                "Throwable while trying to match ManagedConnection, destroying connection: "
                                        + cl, t);
                            synchronized (connectionListeners) {
                                checkedOut.remove(cl);
                            }
                            doDestroy(cl, "getConnection");
                            cl = null;

                        }
                        //We made it here, something went wrong and we should validate if we should continue attempting to acquire a connection
                        if (poolParams.useFastFail) {
                            if (log.isDebugEnabled()) {
                                log
                                    .debug("Fast failing for connection attempt. No more attempts will be made to acquire connection from pool and a new connection will be created immeadiately");
                            }

                            break;
                        }

                    }
                } while (connectionListeners.size() > 0);//end of do loop

                //OK, we couldnt find a working connection from the pool.  Make a new one.
                try {
                    //No, the pool was empty, so we have to make a new one.
                    cl = createConnectionEventListener(subject, cri);
                    synchronized (connectionListeners) {
                        checkedOut.add(cl);
                        int size = (maxSize - permits.availablePermits());
                        if (size > maxUsedConnections)
                            maxUsedConnections = size;
                    }

                    //lack of synch on "started" probably ok, if 2 reads occur we will just
                    //run fillPool twice, no harm done.
                    if (started == false) {
                        started = true;
                        if (poolParams.minSize > 0)
                            PoolFiller.fillPool(this);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("supplying new ManagedConnection: " + cl);
                    }

                    cl.grantPermit(true);
                    return cl;
                } catch (Throwable t) {
                    log.warn("Throwable while attempting to get a new connection: " + cl, t);
                    //return permit and rethrow
                    synchronized (connectionListeners) {
                        checkedOut.remove(cl);
                    }
                    permits.release();
                    JBossResourceException.rethrowAsResourceException(
                        "Unexpected throwable while trying to create a connection: " + cl, t);
                    throw new UnreachableStatementException();
                }
            } else {
                if (this.maxSize == 0) {// 如果最大连接数为0，则说明db处于不可用状态
                    throw new ResourceException("当前数据库处于不可用状态,poolName = " + poolName,
                        ZConstants.ERROR_CODE_DB_NOT_AVAILABLE);
                } else if (this.maxSize == this.maxUsedConnections) {
                    throw new ResourceException("数据源最大连接数已满，并且在超时时间范围内没有新的连接释放,poolName = "
                                                + poolName
                                                + " blocking timeout="
                                                + poolParams.blockingTimeout
                                                + "(ms),now-time = "
                                                + sdf.format(new Date())
                                                + ",connectionUrl = "
                                                + ((LocalManagedConnectionFactory) mcf)
                                                    .getConnectionURL(),
                        ZConstants.ERROR_CODE_CONNECTION_NOT_AVAILABLE);
                } else {// 属于超时
                    throw new ResourceException(
                        "No ManagedConnections available within configured blocking timeout ( "
                                + poolParams.blockingTimeout + " [ms] ),the poolName = " + poolName
                                + ",connectionUrl = "
                                + ((LocalManagedConnectionFactory) mcf).getConnectionURL(),
                        ZConstants.ERROR_CODE_CONNECTION_TIMEOUT);
                }
            }

        } catch (InterruptedException ie) {
            long end = System.currentTimeMillis() - startWait;
            throw new ResourceException("Interrupted while requesting permit! Waited " + end
                                        + " ms", ie);
        }
    }

    /**
     * 
     * @param cl
     * @param kill
     */
    public void returnConnection(ConnectionListener cl, boolean kill) {
        if (cl.getState() == ConnectionListener.DESTROYED) {

            if (log.isDebugEnabled()) {
                log.debug("ManagedConnection is being returned after it was destroyed" + cl);
            }

            if (cl.hasPermit()) {
                // release semaphore
                cl.grantPermit(false);
                permits.release();
            }

            return;
        }

        if (trace)
            if (log.isDebugEnabled()) {
                log.debug("putting ManagedConnection back into pool kill=" + kill + " cl=" + cl);
            }
        try {
            cl.getManagedConnection().cleanup();
        } catch (ResourceException re) {
            log.warn("ResourceException cleaning up ManagedConnection: " + cl, re);
            kill = true;
        }

        // We need to destroy this one
        if (cl.getState() == ConnectionListener.DESTROY)
            kill = true;

        synchronized (connectionListeners) {
            checkedOut.remove(cl);

            // This is really an error
            if (kill == false && connectionListeners.size() >= poolParams.maxSize) {
                log.warn("Destroying returned connection, maximum pool size exceeded " + cl);
                kill = true;
            }

            // If we are destroying, check the connection is not in the pool
            if (kill) {
                // Adrian Brock: A resource adapter can asynchronously notify us that
                // a connection error occurred.
                // This could happen while the connection is not checked out.
                // e.g. JMS can do this via an ExceptionListener on the connection.
                // I have twice had to reinstate this line of code, PLEASE DO NOT REMOTE IT!
                connectionListeners.remove(cl);
            }
            // return to the pool
            else {
                cl.used();
                connectionListeners.add(cl);
            }

            if (cl.hasPermit()) {
                // release semaphore
                cl.grantPermit(false);
                permits.release();
            }
        }

        if (kill) {
            if (trace)
                if (log.isDebugEnabled()) {
                    log.debug("Destroying returned connection " + cl);
                }
            doDestroy(cl, "returnConnection");
        }

    }

    /**
     * 
     */
    public void flush() {
        ArrayList destroy = null;
        synchronized (connectionListeners) {
            if (trace)
                if (log.isDebugEnabled()) {
                    log.debug("Flushing pool checkedOut=" + checkedOut + " inPool="
                              + connectionListeners);
                }

            // Mark checked out connections as requiring destruction
            for (Iterator i = checkedOut.iterator(); i.hasNext();) {
                ConnectionListener cl = (ConnectionListener) i.next();
                if (trace)
                    if (log.isDebugEnabled()) {
                        log.debug("Flush marking checked out connection for destruction " + cl);
                    }
                cl.setState(ConnectionListener.DESTROY);
            }
            // Destroy connections in the pool
            while (connectionListeners.size() > 0) {
                ConnectionListener cl = (ConnectionListener) connectionListeners.remove(0);
                if (destroy == null)
                    destroy = new ArrayList();
                destroy.add(cl);
            }
        }

        // We need to destroy some connections
        if (destroy != null) {
            for (int i = 0; i < destroy.size(); ++i) {
                ConnectionListener cl = (ConnectionListener) destroy.get(i);
                if (trace)
                    if (log.isDebugEnabled()) {
                        log.debug("Destroying flushed connection " + cl);
                    }
                doDestroy(cl, "flushConnectionPool");
            }

            // We destroyed something, check the minimum.
            if (shutdown.get() == false && poolParams.minSize > 0)
                PoolFiller.fillPool(this);
        }

    }

    /**
     * 
     */
    public void removeTimedOut() {
        ArrayList destroy = null;
        long timeout = System.currentTimeMillis() - poolParams.idleTimeout;
        while (true) {
            synchronized (connectionListeners) {
                // Nothing left to destroy
                if (connectionListeners.size() == 0)
                    break;

                // Check the first in the list
                ConnectionListener cl = (ConnectionListener) connectionListeners.get(0);
                if (cl.isTimedOut(timeout)) {
                    // We need to destroy this one
                    connectionListeners.remove(0);
                    if (destroy == null)
                        destroy = new ArrayList();
                    destroy.add(cl);
                } else {
                    //They were inserted chronologically, so if this one isn't timed out, following ones won't be either.
                    break;
                }
            }
        }

        // We found some connections to destroy
        if (destroy != null) {
            for (int i = 0; i < destroy.size(); ++i) {
                ConnectionListener cl = (ConnectionListener) destroy.get(i);
                if (trace) {
                    if (log.isDebugEnabled()) {
                        log.debug("Destroying timedout connection " + cl);
                    }
                }
                doDestroy(cl, "removeTimeout");
            }

            // We destroyed something, check the minimum.
            if (shutdown.get() == false && poolParams.minSize > 0)
                PoolFiller.fillPool(this);
        }
    }

    /**
     * For testing
     */
    public void shutdownWithoutClear() {
        IdleRemover.unregisterPool(this);
        IdleRemover.waitForBackgroundThread();
        ConnectionValidator.unRegisterPool(this);
        ConnectionValidator.waitForBackgroundThread();
        fillToMin();
        shutdown.set(true);
    }

    /**
     * 
     */
    public void shutdown() {
        shutdown.set(true);
        IdleRemover.unregisterPool(this);
        ConnectionValidator.unRegisterPool(this);
        flush();
    }

    /**
     * 
     */
    public void fillToMin() {
        while (true) {
            // Get a permit - avoids a race when the pool is nearly full
            // Also avoids unnessary fill checking when all connections are checked out
            try {
                if (permits.tryAcquire(poolParams.blockingTimeout, TimeUnit.MILLISECONDS)) {
                    try {
                        if (shutdown.get())
                            return;

                        // We already have enough connections
                        if (getMinSize() - connectionCounter.getGuaranteedCount() <= 0)
                            return;

                        // Create a connection to fill the pool
                        try {
                            ConnectionListener cl = createConnectionEventListener(defaultSubject,
                                defaultCri);
                            synchronized (connectionListeners) {
                                if (trace) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Filling pool cl=" + cl);
                                    }
                                }
                                connectionListeners.add(cl);
                            }
                        } catch (ResourceException re) {
                            log.warn("Unable to fill pool ", re);
                            return;
                        }
                    } finally {
                        permits.release();
                    }
                }
            } catch (InterruptedException ignored) {
                if (log.isDebugEnabled()) {
                    log.debug("Interrupted while requesting permit in fillToMin");
                }
            }
        }
    }

    public int getConnectionCount() {
        return connectionCounter.getCount();
    }

    public int getConnectionCreatedCount() {
        return connectionCounter.getCreatedCount();
    }

    public int getConnectionDestroyedCount() {
        return connectionCounter.getDestroyedCount();
    }

    /**
     * Create a connection event listener
     *
     * @param subject the subject
     * @param cri the connection request information
     * @return the new listener
     * @throws ResourceException for any error
     */
    private ConnectionListener createConnectionEventListener(Subject subject,
                                                             ConnectionRequestInfo cri)
                                                                                       throws ResourceException {
        ManagedConnection mc = mcf.createManagedConnection(subject, cri);
        connectionCounter.inc();
        try {
            return clf.createConnectionListener(mc, this);
        } catch (ResourceException re) {
            connectionCounter.dec();
            mc.destroy();
            throw re;
        }
    }

    /**
     * Destroy a connection
     *
     * @param cl the connection to destroy
     */
    public void doDestroy(ConnectionListener cl, String methodName) {
        if (cl.getState() == ConnectionListener.DESTROYED) {
            if (log.isDebugEnabled()) {
                log.debug("ManagedConnection is already destroyed " + cl);
            }
            return;
        }

        connectionCounter.dec();
        cl.setState(ConnectionListener.DESTROYED);
        try {
            cl.getManagedConnection().destroy();
            logger.warn("WARN ## destroy a connection of poolName = " + poolName + " of "
                        + methodName);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Exception destroying ManagedConnection " + cl, t);
            }
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void validateConnections() throws Exception {
        if (trace)
            if (log.isDebugEnabled()) {
                log.debug("Attempting to  validate connections for pool " + this);
            }

        if (permits.tryAcquire(poolParams.blockingTimeout, TimeUnit.MILLISECONDS)) {
            boolean destroyed = false;
            try {
                while (true) {
                    ConnectionListener cl = null;
                    synchronized (connectionListeners) {
                        if (connectionListeners.size() == 0) {
                            break;
                        }
                        cl = removeForFrequencyCheck();
                    }
                    if (cl == null) {
                        break;
                    }

                    try {

                        Set candidateSet = Collections.singleton(cl.getManagedConnection());

                        if (mcf instanceof ValidatingManagedConnectionFactory) {
                            ValidatingManagedConnectionFactory vcf = (ValidatingManagedConnectionFactory) mcf;
                            candidateSet = vcf.getInvalidConnections(candidateSet);

                            if (candidateSet != null && candidateSet.size() > 0) {

                                if (cl.getState() != ConnectionListener.DESTROY) {
                                    doDestroy(cl, "valiateConnection");
                                    destroyed = true;
                                }
                            }

                        } else {
                            log
                                .warn("warning: background validation was specified with a non compliant ManagedConnectionFactory interface.");
                        }

                    } finally {
                        if (!destroyed) // FIXME: 只要有一个被destroy，那么之后的所有cl都不会被返还给cls了
                        {
                            synchronized (connectionListeners) {
                                returnForFrequencyCheck(cl);
                            }
                        }

                    }

                }

            } finally {
                permits.release();

                if (destroyed && shutdown.get() == false && poolParams.minSize > 0) {
                    PoolFiller.fillPool(this);
                }

            }

        }

    }

    private ConnectionListener removeForFrequencyCheck() {

        if (log.isDebugEnabled()) {
            log.debug("Checking for connection within frequency");
        }
        ConnectionListener cl = null;

        for (Iterator iter = connectionListeners.iterator(); iter.hasNext();) {

            cl = (ConnectionListener) iter.next();
            long lastCheck = cl.getLastValidatedTime();

            if ((System.currentTimeMillis() - lastCheck) >= poolParams.backgroundInterval) {
                connectionListeners.remove(cl);
                break;

            } else {
                cl = null;
            }

        }

        return cl;
    }

    private void returnForFrequencyCheck(ConnectionListener cl) {

        if (log.isDebugEnabled()) {
            log.debug("Returning for connection within frequency");
        }

        cl.setLastValidatedTime(System.currentTimeMillis());
        connectionListeners.add(cl);

    }

    /**
     * Guard against configurations or
     * dynamic changes that may increase the minimum
     * beyond the maximum
     */
    private int getMinSize() {
        if (poolParams.minSize > maxSize)
            return maxSize;
        return poolParams.minSize;
    }

    /**
     * 
     * @author sicong.shou
     * @version $Id: InternalManagedConnectionPool.java, v 0.1 2012-11-23 上午11:49:54 sicong.shou Exp $
     */
    public static class PoolParams {
        public int     minSize            = 0;

        public int     maxSize            = 10;

        public int     blockingTimeout    = 30000;         //milliseconds

        public long    idleTimeout        = 1000 * 60 * 30; //milliseconds, 30 minutes.

        public boolean backgroundValidation;               //set to false by default

        public long    backgroundInterval = 1000 * 60 * 10; //milliseconds, 10 minutes;

        public boolean prefill;

        //Do we want to immeadiately break when a connection cannot be matched and not evaluate the rest of the pool?
        public boolean useFastFail;
    }

    /**
     * 
     * @author sicong.shou
     * @version $Id: InternalManagedConnectionPool.java, v 0.1 2012-11-23 上午11:49:58 sicong.shou Exp $
     */
    public static class InternalSemaphore extends Semaphore {

        private static final long serialVersionUID = -3263541301167160976L;

        public InternalSemaphore(int permits) {
            super(permits);
        }

        public InternalSemaphore(int permits, boolean fair) {
            super(permits, fair);
        }

        public void reduceSemaphores(int reduction) {
            super.reducePermits(reduction);
        }

    }

    /**
     * Stats
     */
    private static class Counter {
        private int created   = 0;

        private int destroyed = 0;

        synchronized int getGuaranteedCount() {
            return created - destroyed;
        }

        int getCount() {
            return created - destroyed;
        }

        int getCreatedCount() {
            return created;
        }

        int getDestroyedCount() {
            return destroyed;
        }

        synchronized void inc() {
            ++created;
        }

        synchronized void dec() {
            ++destroyed;
        }
    }
}