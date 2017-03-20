/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public abstract class DummyTResultSet implements ResultSet {

    public boolean absolute(int row) throws SQLException {
        throw new UnsupportedOperationException("absolute");
    }

    public void afterLast() throws SQLException {
        throw new UnsupportedOperationException("afterLast");
    }

    public void beforeFirst() throws SQLException {
        throw new UnsupportedOperationException("beforeFirst");
    }

    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException("cancelRowUpdates");
    }

    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException("clearWarnings");
    }

    public void close() throws SQLException {
        throw new UnsupportedOperationException("close");
    }

    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException("deleteRow");
    }

    /**
     * bug fix by shenxun : 原来会发生一个情况就是如果TStatement调用了close()方法
     * 而本身其管理的TResultSet没有closed时候。外部会使用iterator来遍历每一个
     * TResultSet，调用关闭的方法，但因为TResultSet的close方法会回调
     * TStatement里面用于创建iterator的Set<ResultSet>对象，并使用remove方法。
     * 这就会抛出一个concurrentModificationException。
     * 
     * @param removeThis
     * @throws SQLException
     */
    public void closeInternal(boolean removeThis) throws SQLException {
        throw new UnsupportedOperationException("closeInternal");
    }

    public int findColumn(String columnName) throws SQLException {
        throw new UnsupportedOperationException("findColumn");
    }

    public boolean first() throws SQLException {
        throw new UnsupportedOperationException("first");
    }

    public Array getArray(int i) throws SQLException {
        throw new UnsupportedOperationException("getArray(int i)");
    }

    public Array getArray(String colName) throws SQLException {
        throw new UnsupportedOperationException("getArray(String colName)");
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getAsciiStream(int columnIndex)");
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getAsciiStream(String columnName)");
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getBigDecimal(int columnIndex)");
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getBigDecimal(String columnName)");
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new UnsupportedOperationException("getBigDecimal(int columnIndex, int scale)");
    }

    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        throw new UnsupportedOperationException("getBigDecimal(String columnName, int scale)");
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getBinaryStream(int columnIndex)");
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getBinaryStream(String columnName)");
    }

    public Blob getBlob(int i) throws SQLException {
        throw new UnsupportedOperationException("getBlob(int i)");
    }

    public Blob getBlob(String colName) throws SQLException {
        throw new UnsupportedOperationException("getBlob(String colName)");
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getBoolean(int columnIndex)");
    }

    public boolean getBoolean(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getBoolean(String columnName)");
    }

    public byte getByte(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getByte(int columnIndex)");
    }

    public byte getByte(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getByte(String columnName)");
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getBytes(int columnIndex)");
    }

    public byte[] getBytes(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getBytes(String columnName)");
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getCharacterStream(int columnIndex)");
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getCharacterStream(String columnName)");
    }

    public Clob getClob(int i) throws SQLException {
        throw new UnsupportedOperationException("getClob(int i)");
    }

    public Clob getClob(String colName) throws SQLException {
        throw new UnsupportedOperationException("getClob(String colName)");
    }

    public int getConcurrency() throws SQLException {
        throw new UnsupportedOperationException("getConcurrency");
    }

    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException("getCursorName");
    }

    public Date getDate(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getDate(int columnIndex)");
    }

    public Date getDate(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getDate(String columnName)");
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getDate(int columnIndex, Calendar cal)");
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getDate(String columnName, Calendar cal)");
    }

    public double getDouble(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getDouble(int columnIndex)");
    }

    public double getDouble(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getDouble(String columnName)");
    }

    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException("getFetchDirection");
    }

    public int getFetchSize() throws SQLException {
        throw new UnsupportedOperationException("getFetchSize");
    }

    public float getFloat(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getFloat(int columnIndex)");
    }

    public float getFloat(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getFloat(String columnName)");
    }

    public int getInt(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getInt(int columnIndex)");
    }

    public int getInt(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getInt(String columnName)");
    }

    public long getLong(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getLong(int columnIndex)");
    }

    public long getLong(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getLong(String columnName)");
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException("getMetaData");
    }

    public Object getObject(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getObject(int columnIndex)");
    }

    public Object getObject(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getObject(String columnName)");
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException("getObject(int i, Map<String, Class<?>> map)");
    }

    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException(
            "getObject(String colName, Map<String, Class<?>> map)");
    }

    public Ref getRef(int i) throws SQLException {
        throw new UnsupportedOperationException("getRef(int i)");
    }

    public Ref getRef(String colName) throws SQLException {
        throw new UnsupportedOperationException("getRef(String colName)");
    }

    public int getRow() throws SQLException {
        throw new UnsupportedOperationException("getRow");
    }

    public short getShort(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getShort(int columnIndex)");
    }

    public short getShort(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getShort(String columnName)");
    }

    public Statement getStatement() throws SQLException {
        throw new UnsupportedOperationException("getStatement");
    }

    public String getString(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getString(int columnIndex)");
    }

    public String getString(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getString(String columnName)");
    }

    public Time getTime(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getTime(int columnIndex)");
    }

    public Time getTime(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getTime(String columnName)");
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getTime(int columnIndex, Calendar cal)");
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getTime(String columnName, Calendar cal)");
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getTimestamp(int columnIndex)");
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getTimestamp(String columnName)");
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getTimestamp(int columnIndex, Calendar cal)");
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("getTimestamp(String columnName, Calendar cal)");
    }

    public int getType() throws SQLException {
        throw new UnsupportedOperationException("getType");
    }

    public URL getURL(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getURL(int columnIndex)");
    }

    public URL getURL(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getURL(String columnName)");
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getUnicodeStream(int columnIndex)");
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException("getUnicodeStream(String columnName)");
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException("getWarnings");
    }

    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException("insertRow");
    }

    public boolean isAfterLast() throws SQLException {
        throw new UnsupportedOperationException("isAfterLast");
    }

    public boolean isBeforeFirst() throws SQLException {
        throw new UnsupportedOperationException("isBeforeFirst");
    }

    public boolean isFirst() throws SQLException {
        throw new UnsupportedOperationException("isFirst");
    }

    public boolean isLast() throws SQLException {
        throw new UnsupportedOperationException("isLast");
    }

    public boolean last() throws SQLException {
        throw new UnsupportedOperationException("last");
    }

    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException("moveToCurrentRow");
    }

    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException("moveToInsertRow");
    }

    public boolean next() throws SQLException {
        throw new UnsupportedOperationException("next");
    }

    public boolean previous() throws SQLException {
        throw new UnsupportedOperationException("previous");
    }

    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException("refreshRow");
    }

    public boolean relative(int rows) throws SQLException {
        throw new UnsupportedOperationException("relative");
    }

    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException("rowDeleted");
    }

    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException("rowInserted");
    }

    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException("rowUpdated");
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new UnsupportedOperationException("setFetchDirection");
    }

    public void setFetchSize(int rows) throws SQLException {
        throw new UnsupportedOperationException("setFetchSize");
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new UnsupportedOperationException("updateArray(int columnIndex, Array x)");
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        throw new UnsupportedOperationException("updateArray(String columnName, Array x)");
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException(
            "updateAsciiStream(int columnIndex, InputStream x, int length)");
    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException(
            "updateAsciiStream(String columnName, InputStream x, int length)");
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new UnsupportedOperationException("updateBigDecimal(int columnIndex, BigDecimal x)");
    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        throw new UnsupportedOperationException("updateBigDecimal(String columnName, BigDecimal x)");
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException(
            "updateBinaryStream(int columnIndex, InputStream x, int length)");
    }

    public void updateBinaryStream(String columnName, InputStream x, int length)
                                                                                throws SQLException {
        throw new UnsupportedOperationException(
            "updateBinaryStream(String columnName, InputStream x, int length)");
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new UnsupportedOperationException("updateBlob(int columnIndex, Blob x)");
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        throw new UnsupportedOperationException("updateBlob(String columnName, Blob x)");
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new UnsupportedOperationException("updateBoolean(int columnIndex, boolean x)");
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
        throw new UnsupportedOperationException("updateBoolean(String columnName, boolean x)");
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new UnsupportedOperationException("updateByte(int columnIndex, byte x)");
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        throw new UnsupportedOperationException("updateByte(String columnName, byte x)");
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("updateBytes(int columnIndex, byte[] x)");
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("updateBytes(String columnName, byte[] x)");
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new UnsupportedOperationException(
            "updateCharacterStream(int columnIndex, Reader x, int length)");
    }

    public void updateCharacterStream(String columnName, Reader reader, int length)
                                                                                   throws SQLException {
        throw new UnsupportedOperationException(
            "updateCharacterStream(String columnName, Reader reader, int length)");
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new UnsupportedOperationException("updateClob(int columnIndex, Clob x)");
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        throw new UnsupportedOperationException("updateClob(String columnName, Clob x)");
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new UnsupportedOperationException("updateDate(int columnIndex, Date x)");
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        throw new UnsupportedOperationException("updateDate(String columnName, Date x)");
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new UnsupportedOperationException("updateDouble(int columnIndex, double x)");
    }

    public void updateDouble(String columnName, double x) throws SQLException {
        throw new UnsupportedOperationException("updateDouble(String columnName, double x)");
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new UnsupportedOperationException("updateFloat(int columnIndex, float x)");
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        throw new UnsupportedOperationException("updateFloat(String columnName, float x)");
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new UnsupportedOperationException("updateInt(int columnIndex, int x)");
    }

    public void updateInt(String columnName, int x) throws SQLException {
        throw new UnsupportedOperationException("updateInt(String columnName, int x)");
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new UnsupportedOperationException("updateLong(int columnIndex, long x)");
    }

    public void updateLong(String columnName, long x) throws SQLException {
        throw new UnsupportedOperationException("updateLong(String columnName, long x)");
    }

    public void updateNull(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("updateNull(int columnIndex)");
    }

    public void updateNull(String columnName) throws SQLException {
        throw new UnsupportedOperationException("updateNull(String columnName)");
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new UnsupportedOperationException("updateObject(int columnIndex, Object x)");
    }

    public void updateObject(String columnName, Object x) throws SQLException {
        throw new UnsupportedOperationException("updateObject(String columnName, Object x)");
    }

    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new UnsupportedOperationException(
            "updateObject(int columnIndex, Object x, int scale)");
    }

    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        throw new UnsupportedOperationException(
            "updateObject(String columnName, Object x, int scale)");
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new UnsupportedOperationException("updateRef(int columnIndex, Ref x)");
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        throw new UnsupportedOperationException("updateRef(String columnName, Ref x)");
    }

    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException("updateRow");
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new UnsupportedOperationException("updateShort(int columnIndex, short x)");
    }

    public void updateShort(String columnName, short x) throws SQLException {
        throw new UnsupportedOperationException("updateShort(String columnName, short x)");
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        throw new UnsupportedOperationException("updateString(int columnIndex, String x)");
    }

    public void updateString(String columnName, String x) throws SQLException {
        throw new UnsupportedOperationException("updateString(String columnName, String x)");
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new UnsupportedOperationException("updateTime(int columnIndex, Time x)");
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        throw new UnsupportedOperationException("updateTime(String columnName, Time x)");
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new UnsupportedOperationException("updateTimestamp(int columnIndex, Timestamp x)");
    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        throw new UnsupportedOperationException("updateTimestamp(String columnName, Timestamp x)");
    }

    public boolean wasNull() throws SQLException {
        throw new UnsupportedOperationException("wasNull");
    }

}
