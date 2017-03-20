/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.exception.NestedSQLException;

/**
 * A wrapper for a connection.
 *
 * @author ����
 * @version $Id: WrappedConnection.java, v 0.1 2014-1-6 ����05:30:28 Exp $
 */
public class WrappedConnection implements Connection {

    private static final Logger          log         = Logger.getLogger(WrappedConnection.class);

    private BaseWrapperManagedConnection mc;
    private WrapperDataSource            dataSource;
    private HashMap                      statements;
    private boolean                      closed      = false;
    private int                          trackStatements;
    /**  ���ÿ����Ĕ���Դ*/
    private ZDataSource                  zdatasource = null;

    /**
     * @param mc
     */
    public WrappedConnection(final BaseWrapperManagedConnection mc) {
        this.mc = mc;

        if (mc != null) {
            trackStatements = mc.getTrackStatements();
        }
    }

    void setManagedConnection(final BaseWrapperManagedConnection mc) {
        this.mc = mc;
        if (mc != null) {
            trackStatements = mc.getTrackStatements();
        }
    }

    public WrapperDataSource getDataSource() {
        return dataSource;
    }

    protected void setDataSource(WrapperDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        checkStatus();
        try {
            mc.setJdbcReadOnly(readOnly);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean isReadOnly() throws SQLException {
        checkStatus();
        try {
            return mc.isJdbcReadOnly();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        closed = true;
        if (mc != null) {
            if (trackStatements != BaseWrapperManagedConnectionFactory.TRACK_STATEMENTS_FALSE_INT) {
                synchronized (this) {
                    if (statements != null) {
                        for (Iterator i = statements.entrySet().iterator(); i.hasNext();) {
                            Map.Entry entry = (Map.Entry) i.next();
                            WrappedStatement ws = (WrappedStatement) entry.getKey();
                            if (trackStatements == BaseWrapperManagedConnectionFactory.TRACK_STATEMENTS_TRUE_INT) {
                                Throwable stackTrace = (Throwable) entry.getValue();
                                log
                                    .warn(
                                        "Closing a statement you left open, please do your own housekeeping",
                                        stackTrace);
                            }
                            try {
                                ws.internalClose();
                            } catch (Throwable t) {
                                log.warn("Exception trying to close statement:", t);
                            }
                        }
                    }
                }
            }
            mc.closeHandle(this);
        }
        mc = null;
        dataSource = null;
    }

    public boolean isClosed() throws SQLException {
        return closed;
    }

    /** 
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        checkTransaction();
        try {
            return new WrappedStatement(this, mc.getConnection().createStatement(), dataSource
                .getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
                                                                                 throws SQLException {
        checkTransaction();
        try {
            return new WrappedStatement(this, mc.getConnection().createStatement(resultSetType,
                resultSetConcurrency), dataSource.getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency,
                                     int resultSetHoldability) throws SQLException {

        checkTransaction();
        try {
            return new WrappedStatement(this, mc.getConnection().createStatement(resultSetType,
                resultSetConcurrency, resultSetHoldability), dataSource.getDataSourceName(),
                zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.prepareStatement(sql,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY), sql, dataSource
                .getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.prepareStatement(sql, resultSetType,
                resultSetConcurrency), sql, dataSource.getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
                                                                                                 throws SQLException {
        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.getConnection().prepareStatement(sql,
                resultSetType, resultSetConcurrency, resultSetHoldability), sql, dataSource
                .getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                                                                                throws SQLException {
        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.getConnection().prepareStatement(sql,
                autoGeneratedKeys), sql, dataSource.getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.getConnection().prepareStatement(sql,
                columnIndexes), sql, dataSource.getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {

        checkTransaction();
        try {
            return new WrappedPreparedStatement(this, mc.getConnection().prepareStatement(sql,
                columnNames), sql, dataSource.getDataSourceName(), zdatasource);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        checkTransaction();
        try {
            //  doSqlValve(sql);
            return new WrappedCallableStatement(this, mc.prepareCall(sql,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                                                                                                 throws SQLException {
        checkTransaction();
        try {
            //   doSqlValve(sql);
            return new WrappedCallableStatement(this, mc.prepareCall(sql, resultSetType,
                resultSetConcurrency));
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {

        checkTransaction();
        try {
            // doSqlValve(sql);
            return new WrappedCallableStatement(this, mc.getConnection().prepareCall(sql,
                resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(String sql) throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().nativeSQL(sql);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setAutoCommit(boolean autocommit) throws SQLException {
        checkStatus();
        try {
            mc.setJdbcAutoCommit(autocommit);
        } catch (Throwable t) {
            throw checkException(t);
        }

    }

    public boolean getAutoCommit() throws SQLException {
        checkStatus();
        try {
            return mc.isJdbcAutoCommit();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void commit() throws SQLException {
        checkTransaction();
        try {
            mc.jdbcCommit();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void rollback() throws SQLException {
        checkTransaction();
        // check Exception for jdbcRollback, add by an.sun at 2011-06-07 
        try {
            mc.jdbcRollback();
        } catch (Throwable t) {
            throw checkException(new SQLException(t.getMessage(), t.getMessage(),
                ExceptionSorter.ROLLBACK_ERRORCODE, t));
        } finally {
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        checkTransaction();
        // check Exception for jdbcRollback with savepoint, add by an.sun at 2011-06-07 
        try {
            mc.jdbcRollback(savepoint);
        } catch (Throwable t) {
            throw checkException(new SQLException(t.getMessage(), t.getMessage(),
                ExceptionSorter.ROLLBACK_ERRORCODE, t));
        } finally {
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().getMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setCatalog(String catalog) throws SQLException {
        checkTransaction();
        try {
            mc.getConnection().setCatalog(catalog);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public String getCatalog() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().getCatalog();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTransactionIsolation(int isolationLevel) throws SQLException {
        checkStatus();
        try {
            mc.setJdbcTransactionIsolation(isolationLevel);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getTransactionIsolation() throws SQLException {
        checkStatus();
        try {
            return mc.getJdbcTransactionIsolation();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().getWarnings();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void clearWarnings() throws SQLException {
        checkTransaction();
        try {
            mc.getConnection().clearWarnings();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Map getTypeMap() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().getTypeMap();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTypeMap(Map<String, Class<?>> typeMap) throws SQLException {
        checkTransaction();
        try {
            mc.getConnection().setTypeMap(typeMap);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setHoldability(int holdability) throws SQLException {
        checkTransaction();
        try {
            mc.getConnection().setHoldability(holdability);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getHoldability() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().getHoldability();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().setSavepoint();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection().setSavepoint(name);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkTransaction();
        try {
            mc.getConnection().releaseSavepoint(savepoint);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Connection getUnderlyingConnection() throws SQLException {
        checkTransaction();
        try {
            return mc.getConnection();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    void checkTransaction() throws SQLException {
        checkStatus();
        try {
            mc.checkTransaction();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * The checkStatus method checks that the handle has not been closed and
     * that it is associated with a managed connection.
     *
     * @exception SQLException if an error occurs
     */
    protected void checkStatus() throws SQLException {
        if (closed) {
            throw new SQLException("Connection handle has been closed and is unusable");
        }
        if (mc == null) {
            throw new SQLException(
                "Connection handle is not currently associated with a ManagedConnection");
        }
    }

    /**
     * The base checkException method rethrows the supplied exception, informing
     * the ManagedConnection of the error. Subclasses may override this to
     * filter exceptions based on their severity.
     *
     * @param e a <code>SQLException</code> value
     * @exception Exception if an error occurs
     */
    protected SQLException checkException(Throwable t) throws SQLException {
        if (mc != null) {
            mc.connectionError(t);
        }

        if (t instanceof SQLException) {
            throw (SQLException) t;
        } else {
            throw new NestedSQLException("Error", t);
        }

    }

    int getTrackStatements() {
        return trackStatements;
    }

    void registerStatement(WrappedStatement ws) {
        if (trackStatements == BaseWrapperManagedConnectionFactory.TRACK_STATEMENTS_FALSE_INT) {
            return;
        }

        synchronized (this) {
            if (statements == null) {
                statements = new HashMap();
            }

            if (trackStatements == BaseWrapperManagedConnectionFactory.TRACK_STATEMENTS_TRUE_INT) {
                statements.put(ws, new Throwable("STACKTRACE"));
            } else {
                statements.put(ws, null);
            }
        }
    }

    /**
     * 
     * @param ws
     */
    void unregisterStatement(WrappedStatement ws) {
        if (trackStatements == BaseWrapperManagedConnectionFactory.TRACK_STATEMENTS_FALSE_INT) {
            return;
        }
        synchronized (this) {
            if (statements != null) {
                statements.remove(ws);
            }
        }
    }

    /**
     * 
     * @param ws
     * @throws SQLException
     */
    void checkConfiguredQueryTimeout(WrappedStatement ws) throws SQLException {
        if (mc == null || dataSource == null) {
            return;
        }

        int timeout = 0;

        // Use the transaction timeout
        if (mc.isTransactionQueryTimeout()) {
            timeout = dataSource.getTimeLeftBeforeTransactionTimeout();
        }

        // Look for a configured value
        if (timeout <= 0) {
            timeout = mc.getQueryTimeout();
        }

        if (timeout > 0) {
            ws.setQueryTimeout(timeout);
        }
    }

    Logger getLogger() {
        return log;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        try {
            return iface.isInstance(mc.getConnection());
        } catch (Throwable t) {
            throw checkException(t);
        }

    }

    public <T> T unwrap(Class<T> iface) throws SQLException {

        try {
            return (T) (iface.isInstance(mc.getConnection()) ? mc.getConnection() : null);
        } catch (Throwable t) {
            throw checkException(t);
        }

    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    public Blob createBlob() throws SQLException {
        return null;
    }

    public Clob createClob() throws SQLException {
        return null;
    }

    public NClob createNClob() throws SQLException {
        return null;
    }

    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    public Properties getClientInfo() throws SQLException {
        return null;
    }

    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
    }

    public ZDataSource getZdatasource() {
        return zdatasource;
    }

    public void setZdatasource(ZDataSource zdatasource) {
        this.zdatasource = zdatasource;
    }

	@Override
	public void abort(Executor arg0) throws SQLException {
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return 0;
	}

	@Override
	public String getSchema() throws SQLException {
		return null;
	}

	@Override
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		
	}

	@Override
	public void setSchema(String arg0) throws SQLException {
		
	}

}
