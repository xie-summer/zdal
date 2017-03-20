/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wrapper class for cached PreparedStatements. Keeps a refcount. When this
 * refcount reaches 0, it will close ps.
 *
 * 
 * @author sicong.shou
 * @version $Id: CachedPreparedStatement.java, v 0.1 2012-11-23 上午11:25:02 sicong.shou Exp $
 */
public class CachedPreparedStatement implements PreparedStatement {

    private final PreparedStatement ps;
    private final AtomicBoolean     cached = new AtomicBoolean(true);
    private final AtomicInteger     inUse  = new AtomicInteger(1);

    private final int               defaultMaxFieldSize;
    private final int               defaultMaxRows;
    private final int               defaultQueryTimeout;
    private final int               defaultFetchDirection;
    private final int               defaultFetchSize;
    private int                     currentMaxFieldSize;
    private int                     currentMaxRows;
    private int                     currentQueryTimeout;
    private int                     currentFetchDirection;
    private int                     currentFetchSize;

    /**
     * @param ps
     * @throws SQLException
     */
    public CachedPreparedStatement(PreparedStatement ps) throws SQLException {
        this.ps = ps;

        // Remember the defaults
        defaultMaxFieldSize = ps.getMaxFieldSize();
        defaultMaxRows = ps.getMaxRows();
        defaultQueryTimeout = ps.getQueryTimeout();
        defaultFetchDirection = ps.getFetchDirection();
        defaultFetchSize = ps.getFetchSize();
        currentMaxFieldSize = defaultMaxFieldSize;
        currentMaxRows = defaultMaxRows;
        currentQueryTimeout = defaultQueryTimeout;
        currentFetchDirection = defaultFetchDirection;
        currentFetchSize = defaultFetchSize;
    }

    /**
     * 
     * @return
     */
    public PreparedStatement getUnderlyingPreparedStatement() {
        return ps;
    }

    /** 
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return ps.executeQuery();
    }

    /** 
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return ps.executeUpdate();
    }

    /** 
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        ps.setNull(parameterIndex, sqlType);
    }

    /** 
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        ps.setBoolean(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        ps.setByte(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        ps.setShort(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        ps.setInt(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        ps.setLong(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        ps.setFloat(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        ps.setDouble(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        ps.setBigDecimal(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        ps.setString(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int parameterIndex, byte x[]) throws SQLException {
        ps.setBytes(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
        ps.setDate(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, java.sql.Time x) throws SQLException {
        ps.setTime(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws SQLException {
        ps.setTimestamp(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length)
                                                                                     throws SQLException {
        ps.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length)
                                                                                       throws SQLException {
        ps.setUnicodeStream(parameterIndex, x, length);
    }

    /** 
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length)
                                                                                      throws SQLException {
        ps.setBinaryStream(parameterIndex, x, length);
    }

    /** 
     * @see java.sql.PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        ps.clearParameters();
    }

    /** 
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
                                                                                     throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType, scale);
    }

    /** 
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType);
    }

    /** 
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        ps.setObject(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return ps.execute();
    }

    /** 
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        ps.addBatch();
    }

    /** 
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length)
                                                                                         throws SQLException {
        ps.setCharacterStream(parameterIndex, reader, length);
    }

    /** 
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(int i, Ref x) throws SQLException {
        ps.setRef(i, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int i, Blob x) throws SQLException {
        ps.setBlob(i, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int i, Clob x) throws SQLException {
        ps.setClob(i, x);
    }

    /** 
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int i, Array x) throws SQLException {
        ps.setArray(i, x);
    }

    /** 
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return ps.getMetaData();
    }

    /** 
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {
        ps.setDate(parameterIndex, x, cal);
    }

    /** 
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(int parameterIndex, java.sql.Time x, Calendar cal) throws SQLException {
        ps.setTime(parameterIndex, x, cal);
    }

    /** 
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x, Calendar cal)
                                                                                    throws SQLException {
        ps.setTimestamp(parameterIndex, x, cal);
    }

    /** 
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        ps.setNull(paramIndex, sqlType, typeName);
    }

    /** 
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
        ps.setURL(parameterIndex, x);
    }

    /** 
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return ps.getParameterMetaData();
    }

    /** 
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return ps.executeQuery(sql);
    }

    /** 
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String sql) throws SQLException {
        return ps.executeUpdate(sql);
    }

    /**
     * 
     * @return
     */
    public boolean isInUse() {
        return inUse.get() > 0;
    }

    /**
     * 
     */
    public void inUse() {
        inUse.incrementAndGet();
    }

    /**
     * 
     * @throws SQLException
     */
    public void agedOut() throws SQLException {
        cached.set(false);
        if (inUse.get() == 0)
            ps.close();
    }

    /** 
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        if (inUse.decrementAndGet() == 0) {
            if (cached.get() == false)
                ps.close();
            else {
                // Reset the defaults
                if (defaultMaxFieldSize != currentMaxFieldSize) {
                    ps.setMaxFieldSize(defaultMaxFieldSize);
                    currentMaxFieldSize = defaultMaxFieldSize;
                }
                if (defaultMaxRows != currentMaxRows) {
                    ps.setMaxRows(defaultMaxRows);
                    currentMaxRows = defaultMaxRows;
                }
                if (defaultQueryTimeout != currentQueryTimeout) {
                    ps.setQueryTimeout(defaultQueryTimeout);
                    currentQueryTimeout = defaultQueryTimeout;
                }
                if (defaultFetchDirection != currentFetchDirection) {
                    ps.setFetchDirection(defaultFetchDirection);
                    currentFetchDirection = defaultFetchDirection;
                }
                if (defaultFetchSize != currentFetchSize) {
                    ps.setFetchSize(defaultFetchSize);
                    currentFetchSize = defaultFetchSize;
                }
            }
        }
    }

    /** 
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        return ps.getMaxFieldSize();
    }

    /** 
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws SQLException {
        ps.setMaxFieldSize(max);
        currentMaxFieldSize = max;
    }

    /** 
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        return ps.getMaxRows();
    }

    /** 
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        ps.setMaxRows(max);
        currentMaxRows = max;
    }

    /** 
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        ps.setEscapeProcessing(enable);
    }

    /** 
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        return ps.getQueryTimeout();
    }

    /** 
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        ps.setQueryTimeout(seconds);
        currentQueryTimeout = seconds;
    }

    /** 
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        ps.cancel();
    }

    /** 
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return ps.getWarnings();
    }

    /** 
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        ps.clearWarnings();
    }

    /** 
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String name) throws SQLException {
        ps.setCursorName(name);
    }

    /** 
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String sql) throws SQLException {
        return ps.execute(sql);
    }

    /** 
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        return ps.getResultSet();
    }

    /** 
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        return ps.getUpdateCount();
    }

    /** 
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        return ps.getMoreResults();
    }

    /** 
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        ps.setFetchDirection(direction);
        currentFetchDirection = direction;
    }

    /** 
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return ps.getFetchDirection();
    }

    /** 
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        ps.setFetchSize(rows);
        currentFetchSize = rows;
    }

    /** 
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return ps.getFetchSize();
    }

    /** 
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        return ps.getResultSetConcurrency();
    }

    /** 
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        return ps.getResultSetType();
    }

    /** 
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String sql) throws SQLException {
        ps.addBatch(sql);
    }

    /** 
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        ps.clearBatch();
    }

    /** 
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        return ps.executeBatch();
    }

    /** 
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return ps.getConnection();
    }

    /** 
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int current) throws SQLException {
        return ps.getMoreResults(current);
    }

    /** 
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return ps.getGeneratedKeys();
    }

    /** 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return ps.executeUpdate(sql, autoGeneratedKeys);
    }

    /** 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(String sql, int columnIndexes[]) throws SQLException {
        return ps.executeUpdate(sql, columnIndexes);
    }

    /** 
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(String sql, String columnNames[]) throws SQLException {
        return ps.executeUpdate(sql, columnNames);
    }

    /** 
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return ps.execute(sql, autoGeneratedKeys);
    }

    /** 
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(String sql, int columnIndexes[]) throws SQLException {
        return ps.execute(sql, columnIndexes);
    }

    /** 
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(String sql, String columnNames[]) throws SQLException {
        return ps.execute(sql, columnNames);
    }

    /** 
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        return ps.getResultSetHoldability();
    }

    /** 
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(ps);
    }

    /** 
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) (iface.isInstance(ps) ? ps : null);
    }

    /** 
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
     */
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
                                                                                 throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, long length)
                                                                                  throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
     */
    public void setNCharacterStream(int parameterIndex, Reader value, long length)
                                                                                  throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    public void setNString(int parameterIndex, String value) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
     */
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
    }

    /** 
     * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
     */
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    }

    /** 
     * @see java.sql.Statement#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return false;
    }

    /** 
     * @see java.sql.Statement#isPoolable()
     */
    public boolean isPoolable() throws SQLException {
        return false;
    }

    /** 
     * @see java.sql.Statement#setPoolable(boolean)
     */
    public void setPoolable(boolean poolable) throws SQLException {
    }

	@Override
	public void closeOnCompletion() throws SQLException {
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}
}
