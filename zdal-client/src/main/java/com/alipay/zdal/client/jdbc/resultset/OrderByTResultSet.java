/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.alipay.zdal.client.jdbc.OrderByColumn;
import com.alipay.zdal.client.jdbc.ZdalStatement;

/**
 * bugfix by fanzeng.
 * 如果传进来的两个值都为null的时候，认为二者相等；
 * 如果其中一个为null，另一个不为null，则认为null较小
 * 只有两者都不为null的时候，会根据数据库该字段的类型来选择比较器
 * 如果两者之一为null，则sortType为null，选择null比较器来进行比较
 * @author zhaofeng.wang
 * @version $Id: OrderByTResultSet.java,v 0.1 2010-12-16 下午03:27:10 zhaofeng.wang Exp $
 */
public class OrderByTResultSet extends BaseTResultSet {

    public OrderByTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets) {
        super(statementProxy, resultSets);
    }

    private int                         needNext = -1;
    private boolean                     inited;

    private OrderByColumn[]             orderByColumns;
    private int[]                       sortIndexes;
    private SortedSet<Integer>          order;
    private Comparator<Integer>         setComparator;
    private List<Comparator<ResultSet>> sortFieldComparators;

    /** 
     * @see com.alipay.zdal.client.jdbc.resultset.BaseTResultSet#internNext()
     */
    @Override
    protected boolean internNext() throws SQLException {
        if (!inited) {
            inited = true;
            reduce();
        }
        try {
            if (needNext != -1) {
                if (actualResultSets.get(needNext).next()) {
                    order.add(needNext);
                }
            }
            if (order.isEmpty()) {
                return false;
            }
            Integer first = order.first();
            currentIndex = first;
            order.remove(first);
            needNext = first;
            limitTo--;
        } catch (RuntimeException exp) {
            Throwable cause = exp.getCause();
            if (cause instanceof SQLException) {
                throw (SQLException) cause;
            } else if (cause instanceof CompareTypeUnsupported) {
                SQLException sqlException = new SQLException(cause.toString());
                sqlException.setStackTrace(cause.getStackTrace());
                throw sqlException;
            } else {
                throw exp;
            }
        }
        return true;
    }

    protected ResultSet reduce() throws SQLException {
        if (actualResultSets.size() == 0) {
            throw new RuntimeException("This should not happen!!");
        }
        if (actualResultSets.size() == 1) {
            return actualResultSets.get(0);
        }

        initSort();
        skipLimitFrom();
        return null;
    }

    protected void initSort() throws SQLException {
        sortIndexes = new int[orderByColumns.length];
        for (int i = 0; i < sortIndexes.length; i++) {
            sortIndexes[i] = -1;
        }

        sortFieldComparators = new ArrayList<Comparator<ResultSet>>(orderByColumns.length);
        for (int i = 0; i < orderByColumns.length; i++) {
            sortFieldComparators.add(null);
        }

        setComparator = new Comparator<Integer>() {
            public int compare(Integer left, Integer right) {
                if (left == right) {
                    return 0;
                }
                ResultSet resultSet1, resultSet2;
                resultSet1 = actualResultSets.get(left);
                resultSet2 = actualResultSets.get(right);

                int ret;

                for (int indexOfOrderByColumn = 0; indexOfOrderByColumn < orderByColumns.length; indexOfOrderByColumn++) {
                    try {
                        if (sortIndexes[indexOfOrderByColumn] == -1) {
                            sortIndexes[indexOfOrderByColumn] = actualResultSets.get(0).findColumn(
                                orderByColumns[indexOfOrderByColumn].getColumnName());
                        }
                        final int sortIndex = sortIndexes[indexOfOrderByColumn];

                        Comparator<ResultSet> sortFieldComparator = sortFieldComparators
                            .get(indexOfOrderByColumn);
                        if (sortFieldComparator == null) {
                            Object o1 = resultSet1.getObject(sortIndex);
                            Object o2 = resultSet2.getObject(sortIndex);
                            Class<?> sortType = null;
                            if (null != o1 && null != o2) {
                                sortType = o1.getClass();
                            }
                            if (sortType == Integer.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            int l = r1.getInt(sortIndex);
                                            int r = r2.getInt(sortIndex);
                                            return l == r ? 0 : (l < r ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                };
                            } else if (sortType == BigDecimal.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            return r1.getBigDecimal(sortIndex).compareTo(
                                                r2.getBigDecimal(sortIndex));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == String.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            return r1.getString(sortIndex).compareTo(
                                                r2.getString(sortIndex));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Timestamp.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            return r1.getTimestamp(sortIndex).compareTo(
                                                r2.getTimestamp(sortIndex));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Short.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            short s1 = r1.getShort(sortIndex);
                                            short s2 = r2.getShort(sortIndex);
                                            return s1 == s2 ? 0 : (s1 < s2 ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Long.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            long l1 = r1.getLong(sortIndex);
                                            long l2 = r2.getLong(sortIndex);
                                            return l1 == l2 ? 0 : (l1 < l2 ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Float.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            float f1 = r1.getFloat(sortIndex);
                                            float f2 = r2.getFloat(sortIndex);
                                            return f1 == f2 ? 0 : (f1 < f2 ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Double.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            double d1 = r1.getDouble(sortIndex);
                                            double d2 = r2.getDouble(sortIndex);
                                            return d1 == d2 ? 0 : (d1 < d2 ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Byte.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            byte b1 = r1.getByte(sortIndex);
                                            byte b2 = r2.getByte(sortIndex);
                                            return b1 == b2 ? 0 : (b1 < b2 ? -1 : 1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Boolean.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            boolean b1 = r1.getBoolean(sortIndex);
                                            boolean b2 = r2.getBoolean(sortIndex);
                                            return b1 == b2 ? 0 : (b1 ? 1 : -1);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Date.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            return r1.getDate(sortIndex).compareTo(
                                                r2.getDate(sortIndex));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == Time.class) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        try {
                                            return r1.getTime(sortIndex).compareTo(
                                                r2.getTime(sortIndex));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            } else if (sortType == null) {
                                sortFieldComparator = new Comparator<ResultSet>() {
                                    public int compare(ResultSet r1, ResultSet r2) {
                                        int ret = 0;
                                        try {
                                            if ((r1.getObject(sortIndex) == null)
                                                && (r2.getObject(sortIndex) == null)) {
                                                ret = 0;
                                            } else if ((r1.getObject(sortIndex) == null)
                                                       && (r2.getObject(sortIndex) != null)) {
                                                ret = -1;
                                            } else if ((r1.getObject(sortIndex) != null)
                                                       && (r2.getObject(sortIndex) == null)) {
                                                ret = 1;
                                            }
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return ret;
                                    }
                                };
                            } else {
                                throw new RuntimeException(new CompareTypeUnsupported());
                            }

                            sortFieldComparators.set(indexOfOrderByColumn, sortFieldComparator);
                        }
                        ret = sortFieldComparator.compare(resultSet1, resultSet2);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (ret == 0 && resultSet1 != resultSet2) {
                        continue;
                    }
                    if (orderByColumns[indexOfOrderByColumn].isAsc()) {
                        return ret;
                    } else {
                        return -ret;
                    }
                }

                /*
                 * 由于TreeSet不允许存在相同的对象，所以利用hashCode把相同的对象区分开
                 * 如果存在hashCode也相同的2个对象，那他们的顺序是无关紧要的
                 */
                return System.identityHashCode(resultSet1) < System.identityHashCode(resultSet2) ? -1
                    : 1;
            }

        };

        try {
            order = new TreeSet<Integer>(setComparator);
            for (int i = 0; i < actualResultSets.size(); i++) {
                if (actualResultSets.get(i).next()) {
                    order.add(i);
                }
            }
        } catch (RuntimeException exp) {
            Throwable cause = exp.getCause();
            if (cause instanceof SQLException) {
                throw (SQLException) cause;
            } else if (cause instanceof CompareTypeUnsupported) {
                SQLException sqlException = new SQLException(cause.toString());
                sqlException.setStackTrace(cause.getStackTrace());
                throw sqlException;
            } else {
                throw exp;
            }
        }
    }

    protected void skipLimitFrom() throws SQLException {
        for (int i = 0; i < limitFrom; i++) {
            next();
        }
    }

    public void setOrderByColumns(OrderByColumn[] orderByColumns) {
        this.orderByColumns = orderByColumns;
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
