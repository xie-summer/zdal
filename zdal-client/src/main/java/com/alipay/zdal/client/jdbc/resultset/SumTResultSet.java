/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.alipay.zdal.client.jdbc.ZdalStatement;

/**
 * 
 * @author 伯牙
 * @version $Id: SumTResultSet.java, v 0.1 2014-1-6 下午05:15:05 Exp $
 */
public class SumTResultSet extends AbstractTResultSet {
    private Object  value;
    private int     cursor;
    private boolean isNull;

    public SumTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets)
                                                                                  throws SQLException {
        super(statementProxy, resultSets);
    }

    @Override
    public boolean next() throws SQLException {
        if (cursor > 0) {
            return false;
        }
        reducer();
        cursor++;
        return true;
    }

    protected ResultSet reducer() throws SQLException {
        ResultSet resultSet;
        Object sum = null;
        Add<Object> add = null;

        for (int i = 0; i < actualResultSets.size(); i++) {
            resultSet = actualResultSets.get(i);
            resultSet.next();
            Object data = resultSet.getObject(1);
            if (data != null) {
                if (sum == null) {
                    sum = data;
                    if (sum instanceof Integer) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Integer) left + (Integer) right;
                            }
                        };
                    } else if (sum instanceof Float) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Float) left + (Float) right;
                            }
                        };
                    } else if (sum instanceof Long) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Long) left + (Long) right;
                            }
                        };
                    } else if (sum instanceof Double) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Double) left + (Double) right;
                            }
                        };
                    } else if (sum instanceof BigDecimal) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return ((BigDecimal) left).add((BigDecimal) right);
                            }
                        };
                    } else if (sum instanceof Short) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Short) left + (Short) right;
                            }
                        };
                    } else if (sum instanceof Byte) {
                        add = new Add<Object>() {
                            public Object add(Object left, Object right) {
                                return (Byte) left + (Byte) right;
                            }
                        };
                    } else {
                        throw new SQLException(
                            "The group function 'SUM' does not supported the type '"
                                    + sum.getClass() + "'");
                    }
                } else {
                    sum = add.add(sum, resultSet.getObject(1));
                }
            }
        }

        this.value = sum;
        this.isNull = sum == null;
        return null;
    }

    private static interface Add<T> {
        T add(T left, T right);
    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        checkCursor();
        if (!columnName.equals(actualResultSets.get(0).getMetaData().getColumnName(1))) {
            throw new SQLException("Column '" + columnName + "' not found");
        }
        return 1;
    }

    private void checkCursor() throws SQLException {
        if (cursor != 1) {
            throw new SQLException();
        }
    }

    private void checkIndex(int columnIndex) throws SQLException {
        checkCursor();
        if (columnIndex < 1) {
            throw new SQLException("Column Index out of range, " + columnIndex + " < 1");
        } else if (columnIndex > 1) {
            throw new SQLException("Column Index out of range, " + columnIndex + " > 1");
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        return (BigDecimal) value;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return getBigDecimal(findColumn(columnName));
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).byteValueExact();
        } else {
            return (Byte) value;
        }
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        return getByte(findColumn(columnName));
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else {
            return (Double) value;
        }
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        return getDouble(findColumn(columnName));
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        } else {
            return (Float) value;
        }
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        return getFloat(findColumn(columnName));
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValueExact();
        } else {
            return (Integer) value;
        }
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        return getInt(findColumn(columnName));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValueExact();
        } else {
            return (Long) value;
        }
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        return getLong(findColumn(columnName));
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        return value;
    }

    @Override
    public Object getObject(String columnName) throws SQLException {
        return getObject(findColumn(columnName));
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        checkIndex(columnIndex);
        if (value == null) {
            return 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).shortValueExact();
        } else {
            return (Short) value;
        }
    }

    @Override
    public short getShort(String columnName) throws SQLException {
        return getShort(findColumn(columnName));
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return actualResultSets.get(0).getMetaData();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return this.isNull;
    }

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
