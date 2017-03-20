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
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A wrapper for a result set
 *
 * @author ����
 * @version $Id: WrappedResultSet.java, v 0.1 2014-1-6 ����05:31:08 Exp $
 */
public class WrappedResultSet implements ResultSet {

    /** The wrapped statement */
    private final WrappedStatement statement;

    /** The real result set */
    private final ResultSet        resultSet;

    /** Whether we are closed */
    private boolean                closed = false;

    /** The state lock */
    private final Object           lock   = new Object();

    /**
     * Create a new wrapped result set
     *
     * @param statement the wrapped statement
     * @param resultSet the real result set
     */
    public WrappedResultSet(WrappedStatement statement, ResultSet resultSet) {
        if (statement == null)
            throw new IllegalArgumentException("Null statement!");
        if (resultSet == null)
            throw new IllegalArgumentException("Null result set!");
        this.statement = statement;
        this.resultSet = resultSet;
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        else if (o == this)
            return true;
        else if (o instanceof WrappedResultSet)
            return (resultSet.equals(((WrappedResultSet) o).resultSet));
        else if (o instanceof ResultSet)
            return resultSet.equals(o);
        return false;
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return resultSet.hashCode();
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return resultSet.toString();
    }

    public ResultSet getUnderlyingResultSet() throws SQLException {
        checkState();
        return resultSet;
    }

    /** 
     * @see java.sql.ResultSet#absolute(int)
     */
    public boolean absolute(int row) throws SQLException {
        checkState();
        try {
            return resultSet.absolute(row);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#afterLast()
     */
    public void afterLast() throws SQLException {
        checkState();
        try {
            resultSet.afterLast();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#beforeFirst()
     */
    public void beforeFirst() throws SQLException {
        checkState();
        try {
            resultSet.beforeFirst();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#cancelRowUpdates()
     */
    public void cancelRowUpdates() throws SQLException {
        checkState();
        try {
            resultSet.cancelRowUpdates();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        checkState();
        try {
            resultSet.clearWarnings();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#close()
     */
    public void close() throws SQLException {
        synchronized (lock) {
            if (closed)
                return;
            closed = true;

        }
        statement.unregisterResultSet(this);
        internalClose();
    }

    /** 
     * @see java.sql.ResultSet#deleteRow()
     */
    public void deleteRow() throws SQLException {
        checkState();
        try {
            resultSet.deleteRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#findColumn(java.lang.String)
     */
    public int findColumn(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.findColumn(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /** 
     * @see java.sql.ResultSet#first()
     */
    public boolean first() throws SQLException {
        checkState();
        try {
            return resultSet.first();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Array getArray(int i) throws SQLException {
        checkState();
        try {
            return resultSet.getArray(i);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Array getArray(String colName) throws SQLException {
        checkState();
        try {
            return resultSet.getArray(colName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getAsciiStream(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getAsciiStream(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getBigDecimal(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        checkState();
        try {
            return resultSet.getBigDecimal(columnIndex, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getBigDecimal(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        checkState();
        try {
            return resultSet.getBigDecimal(columnName, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getBinaryStream(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getBinaryStream(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Blob getBlob(int i) throws SQLException {
        checkState();
        try {
            return resultSet.getBlob(i);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Blob getBlob(String colName) throws SQLException {
        checkState();
        try {
            return resultSet.getBlob(colName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getBoolean(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean getBoolean(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getBoolean(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte getByte(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getByte(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte getByte(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getByte(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getBytes(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte[] getBytes(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getBytes(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getCharacterStream(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getCharacterStream(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Clob getClob(int i) throws SQLException {
        checkState();
        try {
            return resultSet.getClob(i);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Clob getClob(String colName) throws SQLException {
        checkState();
        try {
            return resultSet.getClob(colName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getConcurrency() throws SQLException {
        checkState();
        try {
            return resultSet.getConcurrency();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public String getCursorName() throws SQLException {
        checkState();
        try {
            return resultSet.getCursorName();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getDate(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getDate(columnIndex, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getDate(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getDate(columnName, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public double getDouble(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getDouble(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public double getDouble(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getDouble(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getFetchDirection() throws SQLException {
        checkState();
        try {
            return resultSet.getFetchDirection();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getFetchSize() throws SQLException {
        checkState();
        try {
            return resultSet.getFetchSize();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public float getFloat(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getFloat(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public float getFloat(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getFloat(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getInt(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getInt(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getInt(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getInt(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public long getLong(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getLong(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public long getLong(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getLong(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        checkState();
        try {
            return resultSet.getMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getObject(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        checkState();
        try {
            return resultSet.getObject(i, map);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getObject(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        checkState();
        try {
            return resultSet.getObject(colName, map);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Ref getRef(int i) throws SQLException {
        checkState();
        try {
            return resultSet.getRef(i);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Ref getRef(String colName) throws SQLException {
        checkState();
        try {
            return resultSet.getRef(colName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getRow() throws SQLException {
        checkState();
        try {
            return resultSet.getRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public short getShort(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getShort(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public short getShort(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getShort(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Statement getStatement() throws SQLException {
        checkState();
        return statement;
    }

    public String getString(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getString(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public String getString(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getString(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getTime(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getTime(columnIndex, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getTime(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getTime(columnName, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getTimestamp(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getTimestamp(columnIndex, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getTimestamp(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        checkState();
        try {
            return resultSet.getTimestamp(columnName, cal);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getType() throws SQLException {
        checkState();
        try {
            return resultSet.getType();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getUnicodeStream(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        try {
            return resultSet.getUnicodeStream(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public URL getURL(int columnIndex) throws SQLException {
        checkState();
        try {
            return resultSet.getURL(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public URL getURL(String columnName) throws SQLException {
        checkState();
        try {
            return resultSet.getURL(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        checkState();
        try {
            return resultSet.getWarnings();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void insertRow() throws SQLException {
        checkState();
        try {
            resultSet.insertRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean isAfterLast() throws SQLException {
        checkState();
        try {
            return resultSet.isAfterLast();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean isBeforeFirst() throws SQLException {
        checkState();
        try {
            return resultSet.isBeforeFirst();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean isFirst() throws SQLException {
        checkState();
        try {
            return resultSet.isFirst();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean isLast() throws SQLException {
        checkState();
        try {
            return resultSet.isLast();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean last() throws SQLException {
        checkState();
        try {
            return resultSet.last();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void moveToCurrentRow() throws SQLException {
        checkState();
        try {
            resultSet.moveToCurrentRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void moveToInsertRow() throws SQLException {
        checkState();
        try {
            resultSet.moveToInsertRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean next() throws SQLException {
        checkState();
        try {
            return resultSet.next();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean previous() throws SQLException {
        checkState();
        try {
            return resultSet.previous();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void refreshRow() throws SQLException {
        checkState();
        try {
            resultSet.refreshRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean relative(int rows) throws SQLException {
        checkState();
        try {
            return resultSet.relative(rows);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean rowDeleted() throws SQLException {
        checkState();
        try {
            return resultSet.rowDeleted();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean rowInserted() throws SQLException {
        checkState();
        try {
            return resultSet.rowInserted();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean rowUpdated() throws SQLException {
        checkState();
        try {
            return resultSet.rowUpdated();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setFetchDirection(int direction) throws SQLException {
        checkState();
        try {
            resultSet.setFetchDirection(direction);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setFetchSize(int rows) throws SQLException {
        checkState();
        try {
            resultSet.setFetchSize(rows);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        checkState();
        try {
            resultSet.updateArray(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        checkState();
        try {
            resultSet.updateArray(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        checkState();
        try {
            resultSet.updateAsciiStream(columnIndex, x, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        checkState();
        try {
            resultSet.updateAsciiStream(columnName, x, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        checkState();
        try {
            resultSet.updateBigDecimal(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        checkState();
        try {
            resultSet.updateBigDecimal(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        checkState();
        try {
            resultSet.updateBinaryStream(columnIndex, x, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBinaryStream(String columnName, InputStream x, int length)
                                                                                throws SQLException {
        checkState();
        try {
            resultSet.updateBinaryStream(columnName, x, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        checkState();
        try {
            resultSet.updateBlob(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        checkState();
        try {
            resultSet.updateBlob(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        checkState();
        try {
            resultSet.updateBoolean(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
        checkState();
        try {
            resultSet.updateBoolean(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        checkState();
        try {
            resultSet.updateByte(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        checkState();
        try {
            resultSet.updateByte(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        checkState();
        try {
            resultSet.updateBytes(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException {
        checkState();
        try {
            resultSet.updateBytes(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        checkState();
        try {
            resultSet.updateCharacterStream(columnIndex, x, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateCharacterStream(String columnName, Reader reader, int length)
                                                                                   throws SQLException {
        checkState();
        try {
            resultSet.updateCharacterStream(columnName, reader, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        checkState();
        try {
            resultSet.updateClob(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        checkState();
        try {
            resultSet.updateClob(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        checkState();
        try {
            resultSet.updateDate(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        checkState();
        try {
            resultSet.updateDate(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        checkState();
        try {
            resultSet.updateDouble(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateDouble(String columnName, double x) throws SQLException {
        checkState();
        try {
            resultSet.updateDouble(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        checkState();
        try {
            resultSet.updateFloat(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        checkState();
        try {
            resultSet.updateFloat(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        checkState();
        try {
            resultSet.updateInt(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateInt(String columnName, int x) throws SQLException {
        checkState();
        try {
            resultSet.updateInt(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        checkState();
        try {
            resultSet.updateLong(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateLong(String columnName, long x) throws SQLException {
        checkState();
        try {
            resultSet.updateLong(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateNull(int columnIndex) throws SQLException {
        checkState();
        try {
            resultSet.updateNull(columnIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateNull(String columnName) throws SQLException {
        checkState();
        try {
            resultSet.updateNull(columnName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        checkState();
        try {
            resultSet.updateObject(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        checkState();
        try {
            resultSet.updateObject(columnIndex, x, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateObject(String columnName, Object x) throws SQLException {
        checkState();
        try {
            resultSet.updateObject(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        checkState();
        try {
            resultSet.updateObject(columnName, x, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        checkState();
        try {
            resultSet.updateRef(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        checkState();
        try {
            resultSet.updateRef(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateRow() throws SQLException {
        checkState();
        try {
            resultSet.updateRow();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        checkState();
        try {
            resultSet.updateShort(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateShort(String columnName, short x) throws SQLException {
        checkState();
        try {
            resultSet.updateShort(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        checkState();
        try {
            resultSet.updateString(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateString(String columnName, String x) throws SQLException {
        checkState();
        try {
            resultSet.updateString(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        checkState();
        try {
            resultSet.updateTime(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        checkState();
        try {
            resultSet.updateTime(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        checkState();
        try {
            resultSet.updateTimestamp(columnIndex, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        checkState();
        try {
            resultSet.updateTimestamp(columnName, x);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean wasNull() throws SQLException {
        checkState();
        try {
            return resultSet.wasNull();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    SQLException checkException(Throwable t) throws SQLException {
        throw statement.checkException(t);
    }

    void internalClose() throws SQLException {
        synchronized (lock) {
            closed = true;
        }
        resultSet.close();
    }

    void checkState() throws SQLException {
        synchronized (lock) {
            if (closed)
                throw new SQLException("The result set is closed.");
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(resultSet);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) (iface.isInstance(resultSet) ? resultSet : null);
    }

    public int getHoldability() throws SQLException {
        return 0;
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return null;
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return null;
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return null;
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return null;
    }

    public String getNString(int columnIndex) throws SQLException {
        return null;
    }

    public String getNString(String columnLabel) throws SQLException {
        return null;
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        return null;
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        return null;
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return null;
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return null;
    }

    public boolean isClosed() throws SQLException {
        return false;
    }

    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    }

    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length)
                                                                                 throws SQLException {
    }

    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    }

    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length)
                                                                                  throws SQLException {
    }

    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    }

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length)
                                                                                 throws SQLException {
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length)
                                                                                    throws SQLException {
    }

    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    }

    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length)
                                                                                     throws SQLException {
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
    }

    public void updateClob(String columnLabel, Reader reader) throws SQLException {
    }

    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    }

    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length)
                                                                                      throws SQLException {
    }

    public void updateNClob(int columnIndex, NClob clob) throws SQLException {
    }

    public void updateNClob(String columnLabel, NClob clob) throws SQLException {
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    }

    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    }

    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    }

    public void updateNString(int columnIndex, String string) throws SQLException {
    }

    public void updateNString(String columnLabel, String string) throws SQLException {
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    }

	@Override
	public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
		return null;
	}

	@Override
	public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
		return null;
	}

}
