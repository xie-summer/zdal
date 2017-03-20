/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.alipay.zdal.datasource.ZDataSource;

/**
 * A wrapper for a prepared statement.
 *
 * @author ²®ÑÀ
 * @version $Id: WrappedPreparedStatement.java, v 0.1 2014-1-6 ÏÂÎç05:30:56 Exp $
 */
public class WrappedPreparedStatement extends WrappedStatement implements PreparedStatement {

    private final PreparedStatement ps;
    private final String            sql;

    /**
     * @param lc
     * @param ps
     * @param sql
     * @param zdatasource
     */
    public WrappedPreparedStatement(final WrappedConnection lc, final PreparedStatement ps,
                                    final String sql, ZDataSource zdatasource) {
        super(lc, ps, zdatasource);
        this.ps = ps;
        this.sql = sql;
    }

    /**
     * @param lc
     * @param ps
     * @param sql
     * @param dataSourceName
     * @param zdatasource
     */
    public WrappedPreparedStatement(final WrappedConnection lc, final PreparedStatement ps,
                                    final String sql, String dataSourceName, ZDataSource zdatasource) {
        super(lc, ps, zdatasource);
        this.ps = ps;
        this.sql = sql;
        this.dataSourceName = dataSourceName;
    }

    @Override
    public Statement getUnderlyingStatement() throws SQLException {
        checkState();
        if (ps instanceof CachedPreparedStatement) {
            return ((CachedPreparedStatement) ps).getUnderlyingPreparedStatement();
        } else {
            return ps;
        }
    }

    public void setBoolean(int parameterIndex, boolean value) throws SQLException {
        checkState();
        try {
            ps.setBoolean(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setByte(int parameterIndex, byte value) throws SQLException {
        checkState();
        try {
            ps.setByte(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setShort(int parameterIndex, short value) throws SQLException {
        checkState();
        try {
            ps.setShort(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setInt(int parameterIndex, int value) throws SQLException {
        checkState();
        try {
            ps.setInt(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setLong(int parameterIndex, long value) throws SQLException {
        checkState();
        try {
            ps.setLong(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setFloat(int parameterIndex, float value) throws SQLException {
        checkState();
        try {
            ps.setFloat(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDouble(int parameterIndex, double value) throws SQLException {
        checkState();
        try {
            ps.setDouble(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setURL(int parameterIndex, URL value) throws SQLException {
        checkState();
        try {
            ps.setURL(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTime(int parameterIndex, Time value) throws SQLException {
        checkState();
        try {
            ps.setTime(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTime(int parameterIndex, Time value, Calendar calendar) throws SQLException {
        checkState();
        try {
            ps.setTime(parameterIndex, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        checkTransaction();
        try {
            checkConfiguredQueryTimeout();
            return ps.execute();
        } catch (Throwable t) {
            throw checkException(t);
        } finally {
        }
    }

    /** 
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        checkTransaction();
        try {
            checkConfiguredQueryTimeout();
            ResultSet resultSet = ps.executeQuery();
            return registerResultSet(resultSet);
        } catch (Throwable t) {
            throw checkException(t);
        } finally {
        }
    }

    /** 
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {

        checkTransaction();
        try {
            checkConfiguredQueryTimeout();
            return ps.executeUpdate();
        } catch (Throwable t) {
            throw checkException(t);
        } finally {
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        checkState();
        try {
            return ps.getMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void addBatch() throws SQLException {
        checkState();
        try {
            ps.addBatch();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        checkState();
        try {
            ps.setNull(parameterIndex, sqlType);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkState();
        try {
            ps.setNull(parameterIndex, sqlType, typeName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
        checkState();
        try {
            ps.setBigDecimal(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setString(int parameterIndex, String value) throws SQLException {
        checkState();
        try {
            ps.setString(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBytes(int parameterIndex, byte[] value) throws SQLException {
        checkState();
        try {
            ps.setBytes(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDate(int parameterIndex, Date value) throws SQLException {
        checkState();
        try {
            ps.setDate(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDate(int parameterIndex, Date value, Calendar calendar) throws SQLException {
        checkState();
        try {
            ps.setDate(parameterIndex, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
        checkState();
        try {
            ps.setTimestamp(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp value, Calendar calendar)
                                                                                    throws SQLException {
        checkState();
        try {
            ps.setTimestamp(parameterIndex, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setAsciiStream(int parameterIndex, InputStream stream, int length)
                                                                                  throws SQLException {
        checkState();
        try {
            ps.setAsciiStream(parameterIndex, stream, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream stream, int length)
                                                                                    throws SQLException {
        checkState();
        try {
            ps.setUnicodeStream(parameterIndex, stream, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream stream, int length)
                                                                                   throws SQLException {
        checkState();
        try {
            ps.setBinaryStream(parameterIndex, stream, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void clearParameters() throws SQLException {
        checkState();
        try {
            ps.clearParameters();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(int parameterIndex, Object value, int sqlType, int scale)
                                                                                   throws SQLException {
        checkState();
        try {
            ps.setObject(parameterIndex, value, sqlType, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(int parameterIndex, Object value, int sqlType) throws SQLException {
        checkState();
        try {
            ps.setObject(parameterIndex, value, sqlType);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(int parameterIndex, Object value) throws SQLException {
        checkState();
        try {
            ps.setObject(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length)
                                                                                 throws SQLException {
        checkState();
        try {
            ps.setCharacterStream(parameterIndex, reader, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setRef(int parameterIndex, Ref value) throws SQLException {
        checkState();
        try {
            ps.setRef(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBlob(int parameterIndex, Blob value) throws SQLException {
        checkState();
        try {
            ps.setBlob(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setClob(int parameterIndex, Clob value) throws SQLException {
        checkState();
        try {
            ps.setClob(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setArray(int parameterIndex, Array value) throws SQLException {
        checkState();
        try {
            ps.setArray(parameterIndex, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        checkState();
        try {
            return ps.getParameterMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length)
                                                                                 throws SQLException {
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length)
                                                                                  throws SQLException {
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length)
                                                                                  throws SQLException {
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
    }

}
