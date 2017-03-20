/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionEvent;
import com.alipay.zdal.datasource.resource.spi.ConnectionEventListener;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionMetaData;
import com.alipay.zdal.datasource.resource.spi.ResourceAdapterInternalException;

/**
 * BaseWrapperManagedConnection
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: BaseWrapperManagedConnection.java, v 0.1 2014-1-6 ÏÂÎç05:27:08 Exp $
 */
public abstract class BaseWrapperManagedConnection implements ManagedConnection {
    protected final BaseWrapperManagedConnectionFactory mcf;
    protected final Connection                          con;
    protected final Properties                          props;
    private final int                                   transactionIsolation;
    private final boolean                               readOnly;

    /**  cel = ConnectionEventListener */
    private final Collection                            cels                 = new ArrayList();
    private final Set                                   handles              = new HashSet();
    private PreparedStatementCache                      psCache              = null;

    protected final Object                              stateLock            = new Object();

    protected boolean                                   inManagedTransaction = false;
    protected AtomicBoolean                             inLocalTransaction   = new AtomicBoolean(
                                                                                 false);
    protected boolean                                   jdbcAutoCommit       = true;
    protected boolean                                   underlyingAutoCommit = true;
    protected boolean                                   jdbcReadOnly;
    protected boolean                                   underlyingReadOnly;
    protected int                                       jdbcTransactionIsolation;
    protected boolean                                   destroyed            = false;

    /**
     * @param mcf
     * @param con
     * @param props
     * @param transactionIsolation
     * @param psCacheSize
     * @throws SQLException
     */
    public BaseWrapperManagedConnection(final BaseWrapperManagedConnectionFactory mcf,
                                        final Connection con, final Properties props,
                                        final int transactionIsolation, final int psCacheSize)
                                                                                              throws SQLException {
        this.mcf = mcf;
        this.con = con;
        this.props = props;
        if (psCacheSize > 0)
            psCache = new PreparedStatementCache(psCacheSize);

        if (transactionIsolation == -1)
            this.transactionIsolation = con.getTransactionIsolation();
        else {
            this.transactionIsolation = transactionIsolation;
            con.setTransactionIsolation(transactionIsolation);
        }

        readOnly = con.isReadOnly();

        if (mcf.getNewConnectionSQL() != null) {
            Statement s = con.createStatement();
            try {
                s.execute(mcf.getNewConnectionSQL());
            } finally {
                s.close();
            }
        }

        underlyingReadOnly = readOnly;
        jdbcReadOnly = readOnly;
        jdbcTransactionIsolation = this.transactionIsolation;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#addConnectionEventListener(com.alipay.zdal.datasource.resource.spi.ConnectionEventListener)
     */
    public void addConnectionEventListener(ConnectionEventListener cel) {
        synchronized (cels) {
            cels.add(cel);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#removeConnectionEventListener(com.alipay.zdal.datasource.resource.spi.ConnectionEventListener)
     */
    public void removeConnectionEventListener(ConnectionEventListener cel) {
        synchronized (cels) {
            cels.remove(cel);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#associateConnection(java.lang.Object)
     */
    public void associateConnection(Object handle) throws ResourceException {
        if (!(handle instanceof WrappedConnection))
            throw new JBossResourceException("Wrong kind of connection handle to associate"
                                             + handle);
        ((WrappedConnection) handle).setManagedConnection(this);
        synchronized (handles) {
            handles.add(handle);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#getLogWriter()
     */
    public PrintWriter getLogWriter() throws ResourceException {
        // TODO: implement this javax.resource.spi.ManagedConnection method
        return null;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#getMetaData()
     */
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        // TODO: implement this javax.resource.spi.ManagedConnection method
        return null;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter param1) throws ResourceException {
        // TODO: implement this javax.resource.spi.ManagedConnection method
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#cleanup()
     */
    public void cleanup() throws ResourceException {
        synchronized (handles) {
            for (Iterator i = handles.iterator(); i.hasNext();) {
                WrappedConnection lc = (WrappedConnection) i.next();
                lc.setManagedConnection(null);
            }
            handles.clear();
        }
        //reset all the properties we know about to defaults.
        synchronized (stateLock) {
            jdbcAutoCommit = true;
            jdbcReadOnly = readOnly;
            if (jdbcTransactionIsolation != transactionIsolation) {
                try {
                    con.setTransactionIsolation(jdbcTransactionIsolation);
                    jdbcTransactionIsolation = transactionIsolation;
                } catch (SQLException e) {
                    mcf.log.warn("Error resetting transaction isolation ", e);
                }
            }
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject, com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo)
     */
    public Object getConnection(Subject subject, ConnectionRequestInfo cri)
                                                                           throws ResourceException {
        checkIdentity(subject, cri);
        WrappedConnection lc = new WrappedConnection(this);
        synchronized (handles) {
            handles.add(lc);
        }
        return lc;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnection#destroy()
     */
    public void destroy() throws ResourceException {
        synchronized (stateLock) {
            destroyed = true;
        }

        cleanup();
        try {
            con.close();
        } catch (SQLException ignored) {
            getLog().error("Ignored error during close: ", ignored);
        }
    }

    /**
     * 
     * @return
     */
    public boolean checkValid() {
        SQLException e = mcf.isValidConnection(con);

        if (e == null)
            // It's ok
            return true;
        else {
            getLog().warn(
                "Destroying connection that is not valid, due to the following exception: " + con,
                e);
            broadcastConnectionError(e);
            return false;
        }
    }

    void closeHandle(WrappedConnection handle) {
        synchronized (stateLock) {
            if (destroyed)
                return;
        }

        synchronized (handles) {
            handles.remove(handle);
        }
        ConnectionEvent ce = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        ce.setConnectionHandle(handle);
        Collection copy = null;
        synchronized (cels) {
            copy = new ArrayList(cels);
        }
        for (Iterator i = copy.iterator(); i.hasNext();) {
            ConnectionEventListener cel = (ConnectionEventListener) i.next();
            cel.connectionClosed(ce);
        }
    }

    void connectionError(Throwable t) {
        if (t instanceof SQLException == false || mcf.isExceptionFatal((SQLException) t))
            broadcastConnectionError(t);
    }

    protected void broadcastConnectionError(Throwable e) {
        synchronized (stateLock) {
            if (destroyed) {
                Logger log = getLog();
                if (log.isDebugEnabled())
                    log.debug("Not broadcasting error, already destroyed " + this, e);
                return;
            }
        }

        Exception ex = null;
        if (e instanceof Exception)
            ex = (Exception) e;
        else
            ex = new ResourceAdapterInternalException("Unexpected error", e);
        ConnectionEvent ce = new ConnectionEvent(this, ConnectionEvent.CONNECTION_ERROR_OCCURRED,
            ex);
        Collection copy = null;
        synchronized (cels) {
            copy = new ArrayList(cels);
        }
        for (Iterator i = copy.iterator(); i.hasNext();) {
            ConnectionEventListener cel = (ConnectionEventListener) i.next();
            try {
                cel.connectionErrorOccurred(ce);
            } catch (Throwable t) {
                getLog().warn("Error notifying of connection error for listener: " + cel, t);
            }
        }
    }

    Connection getConnection() throws SQLException {
        if (con == null)
            throw new SQLException("Connection has been destroyed!!!");
        return con;
    }

    PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                                                                                               throws SQLException {
        if (psCache != null) {
            PreparedStatementCache.Key key = new PreparedStatementCache.Key(sql,
                PreparedStatementCache.Key.PREPARED_STATEMENT, resultSetType, resultSetConcurrency);
            CachedPreparedStatement cachedps = (CachedPreparedStatement) psCache.get(key);
            if (cachedps != null) {
                if (canUse(cachedps))
                    cachedps.inUse();
                else
                    return doPrepareStatement(sql, resultSetType, resultSetConcurrency);
            } else {
                PreparedStatement ps = doPrepareStatement(sql, resultSetType, resultSetConcurrency);
                cachedps = new CachedPreparedStatement(ps);
                psCache.insert(key, cachedps);
            }
            return cachedps;
        } else
            return doPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    PreparedStatement doPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                                                                                                 throws SQLException {
        return con.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                                                                                          throws SQLException {
        if (psCache != null) {
            PreparedStatementCache.Key key = new PreparedStatementCache.Key(sql,
                PreparedStatementCache.Key.CALLABLE_STATEMENT, resultSetType, resultSetConcurrency);
            CachedCallableStatement cachedps = (CachedCallableStatement) psCache.get(key);
            if (cachedps != null) {
                if (canUse(cachedps))
                    cachedps.inUse();
                else
                    return doPrepareCall(sql, resultSetType, resultSetConcurrency);
            } else {
                CallableStatement cs = doPrepareCall(sql, resultSetType, resultSetConcurrency);
                cachedps = new CachedCallableStatement(cs);
                psCache.insert(key, cachedps);
            }
            return cachedps;
        } else
            return doPrepareCall(sql, resultSetType, resultSetConcurrency);
    }

    CallableStatement doPrepareCall(String sql, int resultSetType, int resultSetConcurrency)
                                                                                            throws SQLException {
        return con.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    boolean canUse(CachedPreparedStatement cachedps) {
        // Nobody is using it so we are ok
        if (cachedps.isInUse() == false)
            return true;

        // Cannot reuse prepared statements in auto commit mode
        // if will close the previous usage of the PS
        if (underlyingAutoCommit == true)
            return false;

        // We have been told not to share
        return mcf.sharePS;
    }

    protected Logger getLog() {
        return mcf.log;
    }

    private void checkIdentity(Subject subject, ConnectionRequestInfo cri) throws ResourceException {
        Properties newProps = mcf.getConnectionProperties(subject, cri);
        if (!props.equals(newProps)) {
            throw new JBossResourceException("Wrong credentials passed to getConnection!");
        } // end of if ()
    }

    /**
     * The <code>checkTransaction</code> method makes sure the adapter follows the JCA
     * autocommit contract, namely all statements executed outside a container managed transaction
     * or a component managed transaction should be autocommitted. To avoid continually calling
     * setAutocommit(enable) before and after container managed transactions, we keep track of the state
     * and check it before each transactional method call.
     */
    void checkTransaction() throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                return;

            // Check autocommit
            if (jdbcAutoCommit != underlyingAutoCommit) {
                con.setAutoCommit(jdbcAutoCommit);
                underlyingAutoCommit = jdbcAutoCommit;
            }
        }

        if (jdbcAutoCommit == false && inLocalTransaction.getAndSet(true) == false) {
            ArrayList copy;
            synchronized (cels) {
                copy = new ArrayList(cels);
            }
            ConnectionEvent ce = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_STARTED);
            for (int i = 0; i < copy.size(); ++i) {
                ConnectionEventListener cel = (ConnectionEventListener) copy.get(i);
                try {
                    cel.localTransactionStarted(ce);
                } catch (Throwable t) {
                    getLog().error("Error notifying of connection committed for listener: " + cel,
                        t);
                }
            }
        }

        checkState();
    }

    protected void checkState() throws SQLException {
        synchronized (stateLock) {
            // Check readonly
            if (jdbcReadOnly != underlyingReadOnly) {
                con.setReadOnly(jdbcReadOnly);
                underlyingReadOnly = jdbcReadOnly;
            }
        }
    }

    boolean isJdbcAutoCommit() {
        return inManagedTransaction ? false : jdbcAutoCommit;
    }

    void setJdbcAutoCommit(final boolean jdbcAutoCommit) throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                throw new SQLException("You cannot set autocommit during a managed transaction!");
            this.jdbcAutoCommit = jdbcAutoCommit;
        }

        if (jdbcAutoCommit && inLocalTransaction.getAndSet(false)) {
            ArrayList copy;
            synchronized (cels) {
                copy = new ArrayList(cels);
            }
            ConnectionEvent ce = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
            for (int i = 0; i < copy.size(); ++i) {
                ConnectionEventListener cel = (ConnectionEventListener) copy.get(i);
                try {
                    cel.localTransactionCommitted(ce);
                } catch (Throwable t) {
                    getLog().error("Error notifying of connection committed for listener: " + cel,
                        t);
                }
            }
        }
    }

    boolean isJdbcReadOnly() {
        return jdbcReadOnly;
    }

    void setJdbcReadOnly(final boolean readOnly) throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                throw new SQLException("You cannot set read only during a managed transaction!");
            this.jdbcReadOnly = readOnly;
        }
    }

    int getJdbcTransactionIsolation() {
        return jdbcTransactionIsolation;
    }

    void setJdbcTransactionIsolation(final int isolationLevel) throws SQLException {
        synchronized (stateLock) {
            this.jdbcTransactionIsolation = isolationLevel;
            con.setTransactionIsolation(jdbcTransactionIsolation);
        }
    }

    void jdbcCommit() throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                throw new SQLException("You cannot commit during a managed transaction!");
            if (jdbcAutoCommit)
                throw new SQLException("You cannot commit with autocommit set!");
        }
        con.commit();

        if (inLocalTransaction.getAndSet(false)) {
            ArrayList copy;
            synchronized (cels) {
                copy = new ArrayList(cels);
            }
            ConnectionEvent ce = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
            for (int i = 0; i < copy.size(); ++i) {
                ConnectionEventListener cel = (ConnectionEventListener) copy.get(i);
                try {
                    cel.localTransactionCommitted(ce);
                } catch (Throwable t) {
                    getLog().error("Error notifying of connection committed for listener: " + cel,
                        t);
                }
            }
        }
    }

    void jdbcRollback() throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                throw new SQLException("You cannot rollback during a managed transaction!");
            if (jdbcAutoCommit)
                throw new SQLException("You cannot rollback with autocommit set!");
        }
        con.rollback();

        if (inLocalTransaction.getAndSet(false)) {
            ArrayList copy;
            synchronized (cels) {
                copy = new ArrayList(cels);
            }
            ConnectionEvent ce = new ConnectionEvent(this,
                ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK);
            for (int i = 0; i < copy.size(); ++i) {
                ConnectionEventListener cel = (ConnectionEventListener) copy.get(i);
                try {
                    cel.localTransactionRolledback(ce);
                } catch (Throwable t) {
                    getLog()
                        .error("Error notifying of connection rollback for listener: " + cel, t);
                }
            }
        }
    }

    void jdbcRollback(Savepoint savepoint) throws SQLException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                throw new SQLException("You cannot rollback during a managed transaction!");
            if (jdbcAutoCommit)
                throw new SQLException("You cannot rollback with autocommit set!");
        }
        con.rollback(savepoint);
    }

    int getTrackStatements() {
        return mcf.trackStatements;
    }

    boolean isTransactionQueryTimeout() {
        return mcf.isTransactionQueryTimeout;
    }

    int getQueryTimeout() {
        return mcf.getQueryTimeout();
    }

    protected void checkException(SQLException e) throws ResourceException {
        connectionError(e);
        throw new JBossResourceException("SQLException", e);
    }
}
