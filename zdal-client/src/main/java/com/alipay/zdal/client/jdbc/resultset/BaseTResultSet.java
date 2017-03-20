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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.jdbc.ZdalStatement;

public abstract class BaseTResultSet extends AbstractTResultSet {
    public BaseTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets) {
        super(statementProxy, resultSets);
    }

    private static final Logger log            = Logger.getLogger(BaseTResultSet.class);
    private int                 fetchSize;
    private int                 fetchDirection = FETCH_FORWARD;

    protected int               currentIndex;

    protected int               limitTo        = -1;
    protected int               limitFrom      = 0;

    public int getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(int limitTo) {
        this.limitTo = limitTo;
    }

    public void setLimitFrom(int limitFrom) {
        this.limitFrom = limitFrom;
    }

    public int getLimitFrom() {
        return limitFrom;
    }

    public int findColumn(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).findColumn(columnName);
    }

    public Array getArray(int i) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getArray(i);
    }

    public Array getArray(String colName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getArray(colName);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getAsciiStream(columnName);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBigDecimal(columnName);
    }

    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBigDecimal(columnIndex, scale);
    }

    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBigDecimal(columnName, scale);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBinaryStream(columnName);
    }

    public Blob getBlob(int i) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBlob(i);
    }

    public Blob getBlob(String colName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBlob(colName);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBoolean(columnIndex);
    }

    public boolean getBoolean(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBoolean(columnName);
    }

    public byte getByte(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getByte(columnIndex);
    }

    public byte getByte(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getByte(columnName);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBytes(columnIndex);
    }

    public byte[] getBytes(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getBytes(columnName);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getCharacterStream(columnName);
    }

    public Clob getClob(int i) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getClob(i);
    }

    public Clob getClob(String colName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getClob(colName);
    }

    public int getConcurrency() throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getConcurrency();
    }

    public String getCursorName() throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getCursorName();
    }

    public Date getDate(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDate(columnIndex);
    }

    public Date getDate(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDate(columnName);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDate(columnIndex, cal);
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDate(columnName, cal);
    }

    public double getDouble(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDouble(columnIndex);
    }

    public double getDouble(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getDouble(columnName);
    }

    public float getFloat(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getFloat(columnIndex);
    }

    public float getFloat(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getFloat(columnName);
    }

    public int getInt(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getInt(columnIndex);
    }

    public int getInt(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getInt(columnName);
    }

    public long getLong(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getLong(columnIndex);
    }

    public long getLong(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getLong(columnName);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getMetaData();
    }

    public Object getObject(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getObject(columnIndex);
    }

    public Object getObject(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getObject(columnName);
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getObject(i, map);
    }

    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getObject(colName, map);
    }

    public Ref getRef(int i) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getRef(i);
    }

    public Ref getRef(String colName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getRef(colName);
    }

    public short getShort(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getShort(columnIndex);
    }

    public short getShort(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getShort(columnName);
    }

    public Statement getStatement() throws SQLException {
        return statementProxy;
    }

    public String getString(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getString(columnIndex);
    }

    public String getString(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getString(columnName);
    }

    public Time getTime(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTime(columnIndex);
    }

    public Time getTime(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTime(columnName);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTime(columnIndex, cal);
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTime(columnName, cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTimestamp(columnName);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getTimestamp(columnName, cal);
    }

    public int getType() throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getType();
    }

    public URL getURL(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getURL(columnIndex);
    }

    public URL getURL(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getURL(columnName);
    }

    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getUnicodeStream(columnIndex);
    }

    @Deprecated
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).getUnicodeStream(columnName);
    }

    public boolean next() throws SQLException {
        checkClosed();

        if (actualResultSets.size() == 1) {
            return actualResultSets.get(0).next();
        }

        if (limitTo == 0) {
            return false;
        }

        return internNext();
    }

    protected abstract boolean internNext() throws SQLException;

    public static class CompareTypeUnsupported extends Exception {
        private static final long serialVersionUID = 1L;
    }

    /**
     * TODO: unused
     */
    public int getFetchDirection() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke getFetchDirection");
        }

        checkClosed();

        return fetchDirection;
    }

    /**
     * TODO: unused
     */
    public void setFetchDirection(int direction) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke setFetchDirection");
        }

        checkClosed();

        if (direction != FETCH_FORWARD) {
            throw new SQLException("only support fetch direction FETCH_FORWARD");
        }

        this.fetchDirection = direction;
    }

    /**
     * TODO: unused
     */
    public int getFetchSize() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke getFetchSize");
        }

        checkClosed();

        return fetchSize;
    }

    /**
     * TODO: unused
     */
    public void setFetchSize(int rows) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke setFetchSize");
        }

        checkClosed();

        if (rows < 0) {
            throw new SQLException("fetch size must greater than or equal 0");
        }

        this.fetchSize = rows;
    }

    public boolean wasNull() throws SQLException {
        checkClosed();

        return actualResultSets.get(currentIndex).wasNull();
    }

}
