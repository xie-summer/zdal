/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.alipay.zdal.client.jdbc.ZdalStatement;

public abstract class MaxMinTResultSet extends AbstractTResultSet {

    private ResultSet value;
    private int       cursor;

    private void checkCursor() throws SQLException {
        if (cursor != 1) {
            throw new SQLException();
        }
    }

    public MaxMinTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets)
                                                                                     throws SQLException {
        super(statementProxy, resultSets);
    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        if (!columnName.equalsIgnoreCase(actualResultSets.get(0).getMetaData().getColumnName(1))) {
            throw new SQLException("Column '" + columnName + "' not found");
        }
        checkCursor();
        return 1;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        checkCursor();
        return value.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        checkCursor();
        return value.getBigDecimal(columnName);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        checkCursor();
        return value.getBoolean(columnIndex);
    }

    @Override
    public boolean getBoolean(String columnName) throws SQLException {
        checkCursor();
        return value.getBoolean(columnName);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        checkCursor();
        return value.getByte(columnIndex);
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        checkCursor();
        return value.getByte(columnName);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        checkCursor();
        return value.getDate(columnIndex);
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        checkCursor();
        return value.getDate(columnName);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        checkCursor();
        return value.getDouble(columnIndex);
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        checkCursor();
        return value.getDouble(columnName);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        checkCursor();
        return value.getFloat(columnIndex);
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        checkCursor();
        return value.getFloat(columnName);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        checkCursor();
        return value.getInt(columnIndex);
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        checkCursor();
        return value.getInt(columnName);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        checkCursor();
        return value.getLong(columnIndex);
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        checkCursor();
        return value.getLong(columnName);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        checkCursor();
        return value.getObject(columnIndex);
    }

    @Override
    public Object getObject(String columnName) throws SQLException {
        checkCursor();
        return value.getObject(columnName);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        checkCursor();
        return value.getShort(columnIndex);
    }

    @Override
    public short getShort(String columnName) throws SQLException {
        checkCursor();
        return value.getShort(columnName);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        checkCursor();
        return value.getString(columnIndex);
    }

    @Override
    public String getString(String columnName) throws SQLException {
        checkCursor();
        return value.getString(columnName);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        checkCursor();
        return value.getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        checkCursor();
        return value.getTime(columnIndex);
    }

    @Override
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        checkCursor();
        return value.getTime(columnName, cal);
    }

    @Override
    public Time getTime(String columnName) throws SQLException {
        checkCursor();
        return value.getTime(columnName);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        checkCursor();
        return value.getTimestamp(columnIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        checkCursor();
        return value.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        checkCursor();
        return value.getTimestamp(columnName, cal);
    }

    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        checkCursor();
        return value.getTimestamp(columnName);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return actualResultSets.get(0).getMetaData();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return value.wasNull();
    }

    @Override
    public boolean next() throws SQLException {
        if (cursor > 0) {
            return false;
        }
        value = reducer();
        cursor++;
        return true;
    }

    protected abstract ResultSet reducer() throws SQLException;

}
