/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.Service;
import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.adapter.jdbc.local.LocalTxDataSource;
import com.alipay.zdal.datasource.resource.connectionmanager.InternalManagedConnectionPool.PoolParams;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;
import com.alipay.zdal.datasource.tm.TransactionLocal;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * The JBossManagedConnectionPool mbean configures and supplies pooling of
 * JBossConnectionEventListeners to the BaseConnectionManager2 mbean.
 * <p>
 *
 * It may be replaced by any mbean with a readable ManagedConnectionPool
 * attribute of type ManagedConnectionPool. Normal pooling parameters are
 * supplied, and the criteria to distinguish ManagedConnections is set in the
 * Criteria attribute.
 *
 * @author 伯牙
 * @version $Id: JBossManagedConnectionPool.java, v 0.1 2014-1-6 下午05:35:12 Exp $
 */
public class JBossManagedConnectionPool implements JBossManagedConnectionPoolMBean, Service {

    /** The log */
    private static final Logger      log        = Logger
                                                    .getLogger(JBossManagedConnectionPool.class);

    /** The pooling criteria */
    private String                   criteria   = "ByNothing";

    /** The pooling strategy */
    private ManagedConnectionPool    poolingStrategy;

    /** The pooling parameters */
    private final PoolParams         poolParams = new InternalManagedConnectionPool.PoolParams();

    /** Whether to use separate pools for transactional and non-transaction use */
    private boolean                  noTxSeparatePools;

    private ManagedConnectionFactory mcf;

    private String                   state;

    private LocalTxDataSource        dataSource;

    private String                   name;

    public ManagedConnectionPool getPoolingStrategy() {
        return poolingStrategy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManagedConnectionFactory(ManagedConnectionFactory managedConnectionFactory) {
        this.mcf = managedConnectionFactory;
    }

    public void setDataSource(LocalTxDataSource dataSource) {
        this.dataSource = dataSource;
    }

    //    @DOperation
    public void flush() {
        if (getPoolingStrategy() == null)
            throw new IllegalStateException("The connection pool is not started");

        getPoolingStrategy().flush();

        if (getPoolingStrategy() instanceof PreFillPoolSupport) {
            final PreFillPoolSupport pfs = (PreFillPoolSupport) getPoolingStrategy();

            if (pfs.shouldPreFill())
                pfs.prefill(noTxSeparatePools);
        }
    }

    //    //@DAttribute
    public long getAvailableConnectionCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy()
            .getAvailableConnectionCount();
    }

    public void setAvailableConnectionCount(long availableConnectionCount) {

    }

    //@DAttribute
    public boolean getBackGroundValidation() {
        return poolParams.backgroundValidation;
    }

    //@DAttribute
    public long getBackGroundValidationMinutes() {
        return poolParams.backgroundInterval / (1000 * 60);
    }

    //@DAttribute
    public int getBlockingTimeoutMillis() {
        return poolParams.blockingTimeout;
    }

    //@DAttribute
    public int getConnectionCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy().getConnectionCount();
    }

    public void setConnectionCount(int connectionCount) {
    }

    //@DAttribute
    public int getConnectionCreatedCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy()
            .getConnectionCreatedCount();
    }

    public void setConnectionCreatedCount(int connectionCreatedCount) {
    }

    //@DAttribute
    public int getConnectionDestroyedCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy()
            .getConnectionDestroyedCount();
    }

    public void setConnectionDestroyedCount(int connectionDestroyedCount) {

    }

    //@DAttribute
    public String getCriteria() {
        return criteria;
    }

    //@DAttribute
    public long getIdleTimeoutMinutes() {
        return poolParams.idleTimeout / (1000 * 60);
    }

    //@DAttribute
    public long getInUseConnectionCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy().getInUseConnectionCount();
    }

    public void setInUseConnectionCount(long inUseConnectionCount) {

    }

    //@DAttribute
    public long getMaxConnectionsInUseCount() {
        return (getPoolingStrategy() == null) ? 0 : getPoolingStrategy()
            .getMaxConnectionsInUseCount();
    }

    public void setMaxConnectionsInUseCount(int maxConnectionsInUseCount) {

    }

    //@DAttribute
    public int getMaxSize() {
        return poolParams.maxSize;
    }

    //@DAttribute
    public int getMinSize() {
        return poolParams.minSize;
    }

    //@DAttribute
    public boolean getNoTxSeparatePools() {
        return noTxSeparatePools;
    }

    //@DAttribute
    public boolean getPreFill() {
        return poolParams.prefill;
    }

    //@DAttribute
    public String getState() {
        return state;
    }

    public void setState(String state) {

    }

    //@DAttribute
    public boolean getUseFastFail() {
        return poolParams.useFastFail;
    }

    public void setBackGroundValidation(boolean backgroundValidation) {
        poolParams.backgroundValidation = backgroundValidation;
    }

    public void setBackGroundValidationMinutes(long backgroundValidationInterval) {
        poolParams.backgroundInterval = backgroundValidationInterval * 1000 * 60;
    }

    public void setBlockingTimeoutMillis(int newBlockingTimeout) {
        poolParams.blockingTimeout = newBlockingTimeout;
    }

    public void setCriteria(String newCriteria) {
        this.criteria = newCriteria;
    }

    public void setIdleTimeoutMinutes(long newIdleTimeoutMinutes) {
        poolParams.idleTimeout = newIdleTimeoutMinutes * 1000 * 60;
    }

    public void setMaxSize(int newMaxSize) {
        poolParams.maxSize = newMaxSize;
    }

    public void setMinSize(int newMinSize) {
        poolParams.minSize = newMinSize;
    }

    public void setNoTxSeparatePools(boolean value) {
        this.noTxSeparatePools = value;
    }

    public void setPreFill(boolean prefill) {
        poolParams.prefill = prefill;
    }

    public void setUseFastFail(boolean useFastFail) {
        poolParams.useFastFail = useFastFail;
    }

    //    @DOperation
    public void start() throws Exception {
        state = STARTING;
        dataSource.start();
        state = STARTED;
    }

    //    @DOperation
    public void stop() throws Exception {
        state = STOPPING;
        dataSource.stop();
        state = STOPPED;
    }

    public void startService() throws Exception {
        if ("ByContainerAndApplication".equals(criteria))
            poolingStrategy = new PoolBySubjectAndCri(mcf, name, poolParams, noTxSeparatePools, log);
        else if ("ByContainer".equals(criteria))
            poolingStrategy = new PoolBySubject(mcf, name, poolParams, noTxSeparatePools, log);
        else if ("ByApplication".equals(criteria))
            poolingStrategy = new PoolByCri(mcf, name, poolParams, noTxSeparatePools, log);
        else
            // "ByNothing".equals(criteria)
            poolingStrategy = new OnePool(mcf, name, poolParams, noTxSeparatePools, log);
    }

    public void stopService() throws Exception {
        if (poolingStrategy != null)
            poolingStrategy.shutdown();

        poolingStrategy = null;
    }

    public static class SubPoolContext {
        /** The subpool */
        private final InternalManagedConnectionPool subPool;

        /** The track by transaction transaction local */
        private TransactionLocal                    trackByTx;

        /**
         * Create a new SubPoolContext.
         *
         * @param tm the transaction manager
         * @param mcf the managed connection factory
         * @param clf the connection listener factory
         * @param subject the subject
         * @param cri the connection request info
         * @param poolParams the pool parameters
         * @param log the log
         */
        public SubPoolContext(TransactionManager tm, ManagedConnectionFactory mcf,
                              ConnectionListenerFactory clf, Subject subject,
                              ConnectionRequestInfo cri, PoolParams poolParams, Logger log,
                              String poolName) {
            subPool = new InternalManagedConnectionPool(mcf, clf, subject, cri, poolParams, log,
                poolName);
            if (tm != null)
                trackByTx = new TransactionLocal(tm);
        }

        /**
         * Get the sub pool
         *
         * @return the sub pool
         */
        public InternalManagedConnectionPool getSubPool() {
            return subPool;
        }

        /**
         * Get the track by transaction
         *
         * @return the transaction local
         */
        public TransactionLocal getTrackByTx() {
            return trackByTx;
        }

        /**
         * Initialize the subpool context
         */
        public void initialize() {
            subPool.initialize();
        }
    }

    /**
     * The base pool implementation
     */
    public abstract static class BasePool implements ManagedConnectionPool, PreFillPoolSupport {
        /** The subpools */
        private final ConcurrentMap<Object, SubPoolContext> subPools     = new ConcurrentHashMap<Object, SubPoolContext>();

        /** The managed connection factory */
        private final ManagedConnectionFactory              mcf;

        /** The connection listener factory */
        private ConnectionListenerFactory                   clf;

        /** The pool parameters */
        protected final PoolParams                          poolParams;

        /**
         * Whether to use separate pools for transactional and non-transaction
         * use
         */
        protected boolean                                   noTxSeparatePools;

        /** The logger */
        private final Logger                                log;

        /** Is trace enabled */
        private boolean                                     traceEnabled = true;

        /** The poolName */
        private final String                                poolName;

        public BasePool(final ManagedConnectionFactory mcf,
                        final InternalManagedConnectionPool.PoolParams poolParams,
                        final boolean noTxSeparatePools, final Logger log) {

            this(mcf, null, poolParams, noTxSeparatePools, log);

        }

        /**
         * Create a new base pool
         *
         * @param mcf the managed connection factory
         * @param poolParams the pooling parameters
         * @param log the log
         */
        public BasePool(final ManagedConnectionFactory mcf, final String poolName,
                        final InternalManagedConnectionPool.PoolParams poolParams,
                        final boolean noTxSeparatePools, final Logger log) {
            this.mcf = mcf;
            this.poolParams = poolParams;
            this.noTxSeparatePools = noTxSeparatePools;
            this.log = log;
            this.traceEnabled = log.isDebugEnabled();
            this.poolName = poolName;
        }

        /**
         * Retrieve the key for this request
         *
         * @param subject the subject
         * @param cri the connection request information
         * @return the key
         * @throws ResourceException for any error
         */
        protected abstract Object getKey(Subject subject, ConnectionRequestInfo cri,
                                         boolean separateNoTx) throws ResourceException;

        public ManagedConnectionFactory getManagedConnectionFactory() {
            return mcf;
        }

        public void setConnectionListenerFactory(ConnectionListenerFactory clf) {
            this.clf = clf;
        }

        public ConnectionListener getConnection(Transaction trackByTransaction, Subject subject,
                                                ConnectionRequestInfo cri) throws ResourceException {
            // Determine the pool key for this request
            boolean separateNoTx = false;
            if (noTxSeparatePools)
                separateNoTx = clf.isTransactional();
            Object key = getKey(subject, cri, separateNoTx);
            SubPoolContext subPool = getSubPool(key, subject, cri, poolName);

            InternalManagedConnectionPool mcp = subPool.getSubPool();

            // Are we doing track by connection?
            TransactionLocal trackByTx = subPool.getTrackByTx();

            // Simple case
            if (trackByTransaction == null || trackByTx == null) {
                ConnectionListener cl = mcp.getConnection(subject, cri);
                if (traceEnabled)
                    dump("Got connection from pool " + cl);
                return cl;
            }

            // Track by transaction
            try {
                trackByTx.lock(trackByTransaction);
            } catch (Throwable t) {
                JBossResourceException.rethrowAsResourceException(
                    "Unable to get connection from the pool for tx=" + trackByTransaction, t);
            }
            try {
                // Already got one
                ConnectionListener cl = (ConnectionListener) trackByTx.get(trackByTransaction);
                if (cl != null) {
                    if (traceEnabled)
                        dump("Previous connection tracked by transaction " + cl + " tx="
                             + trackByTransaction);
                    return cl;
                }
            } finally {
                trackByTx.unlock(trackByTransaction);
            }

            // Need a new one for this transaction
            // This must be done outside the tx local lock, otherwise
            // the tx timeout won't work and get connection can do a lot of
            // other work
            // with many opportunities for deadlocks.
            // Instead we do a double check after we got the transaction to see
            // whether another thread beat us to the punch.
            ConnectionListener cl = mcp.getConnection(subject, cri);
            if (traceEnabled)
                dump("Got connection from pool tracked by transaction " + cl + " tx="
                     + trackByTransaction);

            // Relock and check/set status
            try {
                trackByTx.lock(trackByTransaction);
            } catch (Throwable t) {
                mcp.returnConnection(cl, false);
                if (traceEnabled)
                    dump("Had to return connection tracked by transaction " + cl + " tx="
                         + trackByTransaction + " error=" + t.getMessage());
                JBossResourceException.rethrowAsResourceException(
                    "Unable to get connection from the pool for tx=" + trackByTransaction, t);
            }
            try {
                // Check we weren't racing with another transaction
                ConnectionListener other = (ConnectionListener) trackByTx.get(trackByTransaction);
                if (other != null) {
                    mcp.returnConnection(cl, false);
                    if (traceEnabled)
                        dump("Another thread already got a connection tracked by transaction "
                             + other + " tx=" + trackByTransaction);
                    return other;
                }

                // This is the connection for this transaction
                cl.setTrackByTx(true);
                // FIXME 应该是trackByTx.set(trackByTransaction, cl)吧？
                trackByTx.set(cl);
                if (traceEnabled)
                    dump("Using connection from pool tracked by transaction " + cl + " tx="
                         + trackByTransaction);
                return cl;
            } finally {
                trackByTx.unlock(trackByTransaction);
            }
        }

        public boolean shouldPreFill() {
            return getPreFill();
        }

        public void prefill() {

            prefill(null, null, false);

        }

        public void prefill(boolean noTxSeperatePool) {

            prefill(null, null, noTxSeperatePool);

        }

        public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxSeperatePool) {
            if (getPreFill()) {

                if (log.isDebugEnabled()) {
                    log.debug("Attempting to prefill pool for pool with name" + poolName);
                }

                try {

                    getSubPool(getKey(subject, cri, noTxSeparatePools), subject, cri, poolName);

                } catch (Throwable t) {
                    // No real need to throw here being that pool remains in the
                    // same state as before.
                    log.error("Unable to prefill pool with name" + getPoolName(), t);
                }
            }

        }

        public void returnConnection(ConnectionListener cl, boolean kill) throws ResourceException {
            cl.setTrackByTx(false);
            InternalManagedConnectionPool mcp = (InternalManagedConnectionPool) cl.getContext();
            mcp.returnConnection(cl, kill);
            if (traceEnabled)
                dump("Returning connection to pool " + cl);
        }

        /**
         * Return the inuse count
         *
         * @return the count
         */
        public int getInUseConnectionCount() {
            int count = 0;
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getConnectionInUseCount();
                }
            }
            return count;
        }

        public int getConnectionCount() {
            int count = 0;
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getConnectionCount();
                }
            }
            return count;
        }

        public String getPoolName() {
            return poolName;
        }

        public boolean getPreFill() {
            return this.poolParams.prefill;

        }

        public int getConnectionCreatedCount() {
            int count = 0;
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getConnectionCreatedCount();
                }
            }
            return count;
        }

        public int getConnectionDestroyedCount() {
            int count = 0;
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getConnectionDestroyedCount();
                }
            }
            return count;
        }

        public long getAvailableConnectionCount() {
            long count = 0;
            synchronized (subPools) {
                if (subPools.size() == 0)
                    return poolParams.maxSize;
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getAvailableConnections();
                }
            }
            return count;
        }

        public int getMaxConnectionsInUseCount() {
            int count = 0;
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    count += subPool.getSubPool().getMaxConnectionsInUseCount();
                }
            }
            return count;
        }

        public void shutdown() {
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    subPool.getSubPool().shutdown();
                }
                subPools.clear();
            }
        }

        public void flush() {

            for (Iterator i = subPools.values().iterator(); i.hasNext();) {

                SubPoolContext subPool = (SubPoolContext) i.next();
                // FIXME 应该是subPool.getSubPool().flush()吧？
                subPool.getSubPool().shutdown();
            }
            subPools.clear();

        }

        /**
         * For testing
         */
        protected void shutdownWithoutClear() {
            synchronized (subPools) {
                for (Iterator i = subPools.values().iterator(); i.hasNext();) {
                    SubPoolContext subPool = (SubPoolContext) i.next();
                    subPool.getSubPool().shutdown();
                }
            }
        }

        /**
         * Get any transaction manager associated with the pool
         *
         * @return the transaction manager
         */
        protected TransactionManager getTransactionManager() {
            if (clf != null)
                return clf.getTransactionManagerInstance();
            else
                return null;
        }

        /**
         * Determine the correct pool for this request, creates a new one when
         * necessary
         *
         * @param key the key to the pool
         * @param subject the subject of the pool
         * @param cri the connection request info
         * @return the subpool context
         * @throws ResourceException for any error
         */
        protected SubPoolContext getSubPool(Object key, Subject subject, ConnectionRequestInfo cri,
                                            String poolName) throws ResourceException {
            SubPoolContext subPool = subPools.get(key);
            if (subPool == null) {
                TransactionManager tm = getTransactionManager();
                subPool = new SubPoolContext(tm, mcf, clf, subject, cri, poolParams, log, poolName);
                synchronized (subPools) {
                    if (subPools.containsKey(key))
                        subPool = subPools.get(key);
                    else {
                        subPool.initialize();
                        subPools.put(key, subPool);
                    }
                }
            }
            return subPool;
        }

        protected SubPoolContext getSubPool(Object key) {

            return null;
        }

        /**
         * Dump the stats to the trace log
         *
         * @param info some context
         */
        private void dump(String info) {
            if (log.isDebugEnabled()) {
                StringBuffer toLog = new StringBuffer(100);
                toLog.append(info).append(" [InUse/Available/Max]: [");
                toLog.append(this.getInUseConnectionCount()).append("/");
                toLog.append(this.getAvailableConnectionCount()).append("/");
                toLog.append(this.poolParams.maxSize);
                toLog.append("]");

                log.debug(toLog);
            }
        }
    }

    /**
     * Pooling by subject and connection request information
     */
    public static class PoolBySubjectAndCri extends BasePool {

        public PoolBySubjectAndCri(final ManagedConnectionFactory mcf,
                                   final InternalManagedConnectionPool.PoolParams poolParams,
                                   final boolean noTxSeparatePools, final Logger log) {
            this(mcf, null, poolParams, noTxSeparatePools, log);

        }

        public PoolBySubjectAndCri(final ManagedConnectionFactory mcf, final String poolName,
                                   final InternalManagedConnectionPool.PoolParams poolParams,
                                   final boolean noTxSeparatePools, final Logger log) {
            super(mcf, poolName, poolParams, noTxSeparatePools, log);
        }

        @Override
        protected Object getKey(final Subject subject, final ConnectionRequestInfo cri,
                                final boolean separateNoTx) throws ResourceException {
            return new SubjectCriKey(subject, cri, separateNoTx);
        }

        @Override
        public void prefill() {
            prefill(null, null, false);
        }

        @Override
        public void prefill(boolean noTxSeperatePool) {
            prefill(null, null, noTxSeperatePool);
        }

        public void prefill(Subject subject, ConnectionRequestInfo cri) {
            prefill(subject, cri, false);

        }

        @Override
        public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxSeperatePool) {
            if (getPreFill()) {
                log.warn("Prefill pool option was selected for pool with name " + getPoolName()
                         + " that does not support this feature.");
            }
        }

    }

    /**
     * Pool by subject and criteria
     */
    private static class SubjectCriKey {
        /** Identifies no subject */
        private static final Subject NOSUBJECT = new Subject();

        /** Identifies no connection request information */
        private static final Object  NOCRI     = new Object();

        /** The subject */
        private final Subject        subject;

        /** The connection request information */
        private final Object         cri;

        /** The cached hashCode */
        private int                  hashCode  = Integer.MAX_VALUE;

        /** Separate no tx */
        private final boolean        separateNoTx;

        SubjectCriKey(Subject subject, ConnectionRequestInfo cri, boolean separateNoTx) {
            this.subject = (subject == null) ? NOSUBJECT : subject;
            this.cri = (cri == null) ? NOCRI : cri;
            this.separateNoTx = separateNoTx;
        }

        @Override
        public int hashCode() {
            if (hashCode == Integer.MAX_VALUE)
                hashCode = SubjectActions.hashCode(subject) ^ cri.hashCode();
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || (obj instanceof SubjectCriKey) == false)
                return false;
            SubjectCriKey other = (SubjectCriKey) obj;
            return SubjectActions.equals(subject, other.subject) && cri.equals(other.cri)
                   && separateNoTx == other.separateNoTx;
        }
    }

    /**
     * Pool by subject
     */
    public static class PoolBySubject extends BasePool {
        public PoolBySubject(final ManagedConnectionFactory mcf,
                             final InternalManagedConnectionPool.PoolParams poolParams,
                             final boolean noTxSeparatePools, final Logger log) {
            this(mcf, null, poolParams, noTxSeparatePools, log);
        }

        public PoolBySubject(final ManagedConnectionFactory mcf, final String poolName,
                             final InternalManagedConnectionPool.PoolParams poolParams,
                             final boolean noTxSeparatePools, final Logger log) {
            super(mcf, poolName, poolParams, noTxSeparatePools, log);
        }

        @Override
        protected Object getKey(final Subject subject, final ConnectionRequestInfo cri,
                                boolean separateNoTx) {
            return new SubjectKey(subject, separateNoTx);
        }

        @Override
        public void prefill() {
            prefill(null, null, false);
        }

        @Override
        public void prefill(boolean noTxSeperatePool) {
            prefill(null, null, noTxSeperatePool);
        }

        public void prefill(Subject subject, ConnectionRequestInfo cri) {
            prefill(subject, cri, false);

        }

        @Override
        public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxSeperatePool) {
            if (getPreFill()) {
                log.warn("Prefill pool option was selected for pool with name " + getPoolName()
                         + " that does not support this feature.");
            }
        }
    }

    /**
     * Pool by subject
     */
    private static class SubjectKey {
        /** Identifies no subject */
        private static final Subject NOSUBJECT = new Subject();

        /** The subject */
        private final Subject        subject;

        /** Separate no tx */
        private final boolean        separateNoTx;

        /** The cached hashCode */
        private int                  hashCode  = Integer.MAX_VALUE;

        SubjectKey(Subject subject, boolean separateNoTx) {
            this.subject = (subject == null) ? NOSUBJECT : subject;
            this.separateNoTx = separateNoTx;
        }

        @Override
        public int hashCode() {
            if (hashCode == Integer.MAX_VALUE)
                hashCode = SubjectActions.hashCode(subject);
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || (obj instanceof SubjectKey) == false)
                return false;
            SubjectKey other = (SubjectKey) obj;
            return SubjectActions.equals(subject, other.subject)
                   && separateNoTx == other.separateNoTx;
        }

    }

    /**
     * Pool by connection request information
     */
    public static class PoolByCri extends BasePool {

        public PoolByCri(final ManagedConnectionFactory mcf,
                         final InternalManagedConnectionPool.PoolParams poolParams,
                         final boolean noTxSeparatePools, final Logger log) {
            this(mcf, null, poolParams, noTxSeparatePools, log);
        }

        public PoolByCri(final ManagedConnectionFactory mcf, final String poolName,
                         final InternalManagedConnectionPool.PoolParams poolParams,
                         final boolean noTxSeparatePools, final Logger log) {
            super(mcf, poolName, poolParams, noTxSeparatePools, log);
        }

        @Override
        protected Object getKey(final Subject subject, final ConnectionRequestInfo cri,
                                boolean separateNoTx) {
            return new CriKey(cri, separateNoTx);
        }

        @Override
        public void prefill() {
            prefill(null, null, false);
        }

        @Override
        public void prefill(boolean noTxSeperatePool) {
            prefill(null, null, noTxSeperatePool);
        }

        public void prefill(Subject subject, ConnectionRequestInfo cri) {
            prefill(subject, cri, false);

        }

        @Override
        public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxSeperatePool) {
            if (getPreFill()) {
                log.warn("Prefill pool option was selected for pool with name " + getPoolName()
                         + " that does not support this feature.");
            }
        }
    }

    /**
     * Pool by subject and criteria
     */
    private static class CriKey {
        /** Identifies no connection request information */
        private static final Object NOCRI    = new Object();

        /** The connection request information */
        private final Object        cri;

        /** Separate no tx */
        private final boolean       separateNoTx;

        /** The cached hashCode */
        private int                 hashCode = Integer.MAX_VALUE;

        CriKey(ConnectionRequestInfo cri, boolean separateNoTx) {
            this.cri = (cri == null) ? NOCRI : cri;
            this.separateNoTx = separateNoTx;
        }

        @Override
        public int hashCode() {
            if (hashCode == Integer.MAX_VALUE)
                hashCode = cri.hashCode();
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || (obj instanceof CriKey) == false)
                return false;
            CriKey other = (CriKey) obj;
            return cri.equals(other.cri) && separateNoTx == other.separateNoTx;
        }
    }

    /**
     * One pool
     */
    public static class OnePool extends BasePool {

        public OnePool(final ManagedConnectionFactory mcf,
                       final InternalManagedConnectionPool.PoolParams poolParams,
                       final boolean noTxSeparatePools, final Logger log) {
            this(mcf, null, poolParams, noTxSeparatePools, log);
        }

        public OnePool(final ManagedConnectionFactory mcf, final String poolName,
                       final InternalManagedConnectionPool.PoolParams poolParams,
                       final boolean noTxSeparatePools, final Logger log) {
            super(mcf, poolName, poolParams, noTxSeparatePools, log);
        }

        @Override
        protected Object getKey(final Subject subject, final ConnectionRequestInfo cri,
                                boolean separateNoTx) {
            if (separateNoTx)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }

    }

    private static class SubjectActions implements PrivilegedAction {
        Subject subject;

        Subject other;

        SubjectActions(Subject subject, Subject other) {
            this.subject = subject;
            this.other = other;
        }

        public Object run() {
            Object value = null;
            if (other == null)
                value = new Integer(subject.hashCode());
            else
                value = new Boolean(subject.equals(other));
            return value;
        }

        static int hashCode(Subject subject) {
            SubjectActions action = new SubjectActions(subject, null);
            Integer hash = (Integer) AccessController.doPrivileged(action);
            return hash.intValue();
        }

        static boolean equals(Subject subject, Subject other) {
            SubjectActions action = new SubjectActions(subject, other);
            Boolean equals = (Boolean) AccessController.doPrivileged(action);
            return equals.booleanValue();
        }
    }

}
