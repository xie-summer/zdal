/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.config.DataSourceConfigType;
import com.alipay.zdal.client.controller.TargetDBMeta;
import com.alipay.zdal.client.dispatcher.DispatcherResult;
import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.client.jdbc.DBSelector.AbstractDataSourceTryer;
import com.alipay.zdal.client.jdbc.DBSelector.DataSourceTryer;
import com.alipay.zdal.client.jdbc.parameter.ParameterContext;
import com.alipay.zdal.client.jdbc.parameter.ParameterHandler;
import com.alipay.zdal.client.jdbc.parameter.ParameterMethod;
import com.alipay.zdal.client.jdbc.parameter.SetArrayHandler;
import com.alipay.zdal.client.jdbc.parameter.SetAsciiStreamHandler;
import com.alipay.zdal.client.jdbc.parameter.SetBigDecimalHandler;
import com.alipay.zdal.client.jdbc.parameter.SetBinaryStreamHandler;
import com.alipay.zdal.client.jdbc.parameter.SetBlobHandler;
import com.alipay.zdal.client.jdbc.parameter.SetBooleanHandler;
import com.alipay.zdal.client.jdbc.parameter.SetByteHandler;
import com.alipay.zdal.client.jdbc.parameter.SetBytesHandler;
import com.alipay.zdal.client.jdbc.parameter.SetCharacterStreamHandler;
import com.alipay.zdal.client.jdbc.parameter.SetClobHandler;
import com.alipay.zdal.client.jdbc.parameter.SetDate1Handler;
import com.alipay.zdal.client.jdbc.parameter.SetDate2Handler;
import com.alipay.zdal.client.jdbc.parameter.SetDoubleHandler;
import com.alipay.zdal.client.jdbc.parameter.SetFloatHandler;
import com.alipay.zdal.client.jdbc.parameter.SetIntHandler;
import com.alipay.zdal.client.jdbc.parameter.SetLongHandler;
import com.alipay.zdal.client.jdbc.parameter.SetNull1Handler;
import com.alipay.zdal.client.jdbc.parameter.SetNull2Handler;
import com.alipay.zdal.client.jdbc.parameter.SetObject1Handler;
import com.alipay.zdal.client.jdbc.parameter.SetObject2Handler;
import com.alipay.zdal.client.jdbc.parameter.SetObject3Handler;
import com.alipay.zdal.client.jdbc.parameter.SetRefHandler;
import com.alipay.zdal.client.jdbc.parameter.SetShortHandler;
import com.alipay.zdal.client.jdbc.parameter.SetStringHandler;
import com.alipay.zdal.client.jdbc.parameter.SetTime1Handler;
import com.alipay.zdal.client.jdbc.parameter.SetTime2Handler;
import com.alipay.zdal.client.jdbc.parameter.SetTimestamp1Handler;
import com.alipay.zdal.client.jdbc.parameter.SetTimestamp2Handler;
import com.alipay.zdal.client.jdbc.parameter.SetURLHandler;
import com.alipay.zdal.client.jdbc.parameter.SetUnicodeStreamHandler;
import com.alipay.zdal.client.jdbc.resultset.DummyTResultSet;
import com.alipay.zdal.client.jdbc.resultset.SimpleTResultSet;
import com.alipay.zdal.client.util.ExceptionUtils;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.common.SqlType;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public class ZdalPreparedStatement extends RetryableTStatement implements PreparedStatement {
    private static final Logger log = Logger.getLogger(ZdalPreparedStatement.class);

    public ZdalPreparedStatement(SqlDispatcher writeDispatcher, SqlDispatcher readDispatcher) {
        super(writeDispatcher, readDispatcher);
    }

    private static final Map<ParameterMethod, ParameterHandler> parameterHandlers = new HashMap<ParameterMethod, ParameterHandler>(
                                                                                      30);

    static {
        parameterHandlers.put(ParameterMethod.setArray, new SetArrayHandler());
        parameterHandlers.put(ParameterMethod.setAsciiStream, new SetAsciiStreamHandler());
        parameterHandlers.put(ParameterMethod.setBigDecimal, new SetBigDecimalHandler());
        parameterHandlers.put(ParameterMethod.setBinaryStream, new SetBinaryStreamHandler());
        parameterHandlers.put(ParameterMethod.setBlob, new SetBlobHandler());
        parameterHandlers.put(ParameterMethod.setBoolean, new SetBooleanHandler());
        parameterHandlers.put(ParameterMethod.setByte, new SetByteHandler());
        parameterHandlers.put(ParameterMethod.setBytes, new SetBytesHandler());
        parameterHandlers.put(ParameterMethod.setCharacterStream, new SetCharacterStreamHandler());
        parameterHandlers.put(ParameterMethod.setClob, new SetClobHandler());
        parameterHandlers.put(ParameterMethod.setDate1, new SetDate1Handler());
        parameterHandlers.put(ParameterMethod.setDate2, new SetDate2Handler());
        parameterHandlers.put(ParameterMethod.setDouble, new SetDoubleHandler());
        parameterHandlers.put(ParameterMethod.setFloat, new SetFloatHandler());
        parameterHandlers.put(ParameterMethod.setInt, new SetIntHandler());
        parameterHandlers.put(ParameterMethod.setLong, new SetLongHandler());
        parameterHandlers.put(ParameterMethod.setNull1, new SetNull1Handler());
        parameterHandlers.put(ParameterMethod.setNull2, new SetNull2Handler());
        parameterHandlers.put(ParameterMethod.setObject1, new SetObject1Handler());
        parameterHandlers.put(ParameterMethod.setObject2, new SetObject2Handler());
        parameterHandlers.put(ParameterMethod.setObject3, new SetObject3Handler());
        parameterHandlers.put(ParameterMethod.setRef, new SetRefHandler());
        parameterHandlers.put(ParameterMethod.setShort, new SetShortHandler());
        parameterHandlers.put(ParameterMethod.setString, new SetStringHandler());
        parameterHandlers.put(ParameterMethod.setTime1, new SetTime1Handler());
        parameterHandlers.put(ParameterMethod.setTime2, new SetTime2Handler());
        parameterHandlers.put(ParameterMethod.setTimestamp1, new SetTimestamp1Handler());
        parameterHandlers.put(ParameterMethod.setTimestamp2, new SetTimestamp2Handler());
        parameterHandlers.put(ParameterMethod.setUnicodeStream, new SetUnicodeStreamHandler());
        parameterHandlers.put(ParameterMethod.setURL, new SetURLHandler());
    }

    private String                                              sql;

    private int                                                 autoGeneratedKeys = -1;

    private int[]                                               columnIndexes;

    private String[]                                            columnNames;

    private Map<Integer, ParameterContext>                      parameterSettings = new TreeMap<Integer, ParameterContext>();

    protected PreparedStatement prepareStatementInternal(
                                                         Connection connection,
                                                         String targetSql,
                                                         String dbSelectID,
                                                         Map<DataSource, SQLException> failedDataSources)
                                                                                                         throws SQLException {
        PreparedStatement ps;
        if (getResultSetType() != -1 && getResultSetConcurrency() != -1
            && getResultSetHoldability() != -1) {
            ps = connection.prepareStatement(targetSql, getResultSetType(),
                getResultSetConcurrency(), getResultSetHoldability());
        } else if (getResultSetType() != -1 && getResultSetConcurrency() != -1) {
            ps = connection.prepareStatement(targetSql, getResultSetType(),
                getResultSetConcurrency());
        } else if (autoGeneratedKeys != -1) {
            ps = connection.prepareStatement(targetSql, autoGeneratedKeys);
        } else if (columnIndexes != null) {
            ps = connection.prepareStatement(targetSql, columnIndexes);
        } else if (columnNames != null) {
            ps = connection.prepareStatement(targetSql, columnNames);
        } else {
            ps = connection.prepareStatement(targetSql);
        }

        return ps;
    }

    private void changeParameters(Map<Integer, Object> changedParameters) {
        for (Map.Entry<Integer, Object> entry : changedParameters.entrySet()) {
            // 注意：SQL解析那边绑定参数从0开始计数，因此需要加1。
            ParameterContext context = parameterSettings.get(entry.getKey() + 1);
            if (context.getParameterMethod() != ParameterMethod.setNull1
                && context.getParameterMethod() != ParameterMethod.setNull2) {
                context.getArgs()[1] = entry.getValue();
            }
        }
    }

    protected void setParameters(PreparedStatement ps) throws SQLException {
        for (ParameterContext context : parameterSettings.values()) {
            parameterHandlers.get(context.getParameterMethod()).setParameter(ps, context.getArgs());
        }
    }

    private List<Object> getParameters() {
        List<Object> parameters = new ArrayList<Object>();
        for (ParameterContext context : parameterSettings.values()) {
            if (context.getParameterMethod() != ParameterMethod.setNull1
                && context.getParameterMethod() != ParameterMethod.setNull2) {
                parameters.add(context.getArgs()[1]);
            } else {
                parameters.add(null);
            }
        }

        return parameters;
    }

    public void clearParameters() throws SQLException {
        parameterSettings.clear();
    }

    public boolean execute() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke execute, sql = " + sql);
        }

        SqlType sqlType = getSqlType(sql);
        // SELECT_FOR_UPDATE、SELECT_FROM_DUAL虽然去写库执行，但是都必须执行executeQuery()接口
        if (sqlType == SqlType.SELECT || sqlType == SqlType.SELECT_FOR_UPDATE
            || sqlType == SqlType.SELECT_FROM_DUAL) {
            if (this.dbConfigType == DataSourceConfigType.GROUP) {
                executeQuery0(sql, sqlType);
            } else {
                executeQuery();
            }
            return true;
        } else if (sqlType == SqlType.INSERT || sqlType == SqlType.UPDATE
                   || sqlType == SqlType.DELETE) {
            if (super.dbConfigType == DataSourceConfigType.GROUP) {
                executeUpdate0();
            } else {
                executeUpdate();
            }
            return false;
        } else {
            throw new SQLException("only select, insert, update, delete sql is supported");
        }
    }

    private ResultSet executeQuery0(String sql, SqlType sqlType) throws SQLException {
        checkClosed();
        this.setOperation_type(DB_OPERATION_TYPE.READ_FROM_DB);
        // 获取连接
        DBSelector dbselector = getGroupDBSelector(sqlType);
        if (dbselector == null) {
            throw new IllegalStateException("load balance数据源配置类型错误");
        }
        // 返回执行结果
        boolean needRetry = this.autoCommit;
        Map<DataSource, SQLException> failedDataSources = needRetry ? new LinkedHashMap<DataSource, SQLException>(
            0)
            : null;
        return dbselector.tryExecute(failedDataSources, this.executeQueryTryer, retryingTimes,
            operation_type, sql, sqlType);
    }

    private DataSourceTryer<ResultSet> executeQueryTryer = new AbstractDataSourceTryer<ResultSet>() {
                                                             public ResultSet tryOnDataSource(
                                                                                              DataSource ds,
                                                                                              String name,
                                                                                              Object... args)
                                                                                                             throws SQLException {
                                                                 String sql = (String) args[0];
                                                                 SqlType sqlType = (SqlType) args[1];
                                                                 // 获取连接
                                                                 Connection conn = getGroupConnection(
                                                                     ds, sqlType, name);

                                                                 return executeQueryAndAddIntoCollection0(
                                                                     sql, conn);
                                                             }

                                                         };

    protected ResultSet executeQueryAndAddIntoCollection0(String targetSql, Connection connection)
                                                                                                  throws SQLException {
        PreparedStatement ps = preparedAndSetParameters(null, targetSql, null, connection);
        // added by fanzeng.
        // //根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
        // String dbSelectorId = null;
        // Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
        // ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);
        List<ResultSet> actualResultSets = new ArrayList<ResultSet>();
        actualResultSets.add(ps.executeQuery());
        // DummyTResultSet resultSet = mergeResultSets(context,
        // actualResultSets);
        DummyTResultSet simpleTResultSet = new SimpleTResultSet(this, actualResultSets);
        openResultSets.add(simpleTResultSet);

        this.results = simpleTResultSet;
        this.moreResults = false;
        this.updateCount = -1;
        return this.results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.PreparedStatement#executeQuery()
     * 
     * 这个方法当for update时会执行到。不应该进行重试
     */
    public ResultSet executeQuery() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke executeQuery, sql = " + sql);
        }
        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            SqlType sqlType = getSqlType(sql);
            return executeQuery0(sql, sqlType);
        }

        checkClosed();

        SqlExecutionContext context = getExecutionContext(sql, getParameters());

        if (context.mappingRuleReturnNullValue()) {
            ResultSet emptyResultSet = getEmptyResultSet();
            this.results = emptyResultSet;
            return emptyResultSet;
        }

        changeParameters(context.getChangedParameters());

        // int tablesSize = 0;

        dumpSql(sql, context.getTargetSqls(), parameterSettings);

        List<SQLException> exceptions = null;
        List<ResultSet> actualResultSets = new ArrayList<ResultSet>();

        for (Entry<String, SqlAndTable[]> entry : context.getTargetSqls().entrySet()) {
            for (SqlAndTable targetSql : entry.getValue()) {
                // tablesSize++;
                try {
                    String dbSelectorId = entry.getKey();
                    Connection connection = getActualConnection(dbSelectorId);
                    executeQueryAndAddIntoCollection(dbSelectorId, targetSql.sql, context
                        .getFailedDataSources(), connection, actualResultSets);

                    // TODO:添加到resultSet close()以前的时间统计
                } catch (SQLException e) {

                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                    ExceptionUtils.throwSQLException(exceptions, sql, getParameters()); // 直接抛出
                }
            }
        }

        ExceptionUtils.throwSQLException(exceptions, sql, getParameters());
        DummyTResultSet resultSet = mergeResultSets(context, actualResultSets);
        openResultSets.add(resultSet);

        this.results = resultSet;
        this.moreResults = false;
        this.updateCount = -1;

        return this.results;

    }

    /**
     * 建立preparedStatment然后将其放入statments队列中，并设置初值，然后返回。
     * 只用在executeUpdate和executeQuery中。
     * 
     * 重试则只针对在executeUpdate和query中需要重试的sql.
     * 
     * @param dbSelectorId
     * @param targetSql
     * @param retringContext
     * @param connection
     * @return
     * @throws SQLException
     */
    protected void executeQueryAndAddIntoCollection(
                                                    String dbSelectorId,
                                                    String targetSql,
                                                    Map<DataSource, SQLException> failedDataSources,
                                                    Connection connection,
                                                    List<ResultSet> actualResultSets)
                                                                                     throws SQLException {
        PreparedStatement ps = preparedAndSetParameters(dbSelectorId, targetSql, failedDataSources,
            connection);
        // added by fanzeng.
        // 根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
        Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
        ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);

        actualResultSets.add(ps.executeQuery());
    }

    protected int executeUpdateAndCountAffectRows(String dbSelectorId, String targetSql,
                                                  Map<DataSource, SQLException> failedDataSources,
                                                  Connection connection, int rows)
                                                                                  throws SQLException {
        PreparedStatement ps = preparedAndSetParameters(dbSelectorId, targetSql, failedDataSources,
            connection);

        // added by fanzeng.
        // 根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
        Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
        ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);

        rows += ps.executeUpdate();
        return rows;
    }

    protected int executeUpdateAndCountAffectRows0(String dbSelectorId, String targetSql,
                                                   Map<DataSource, SQLException> failedDataSources,
                                                   Connection connection, int rows)
                                                                                   throws SQLException {
        PreparedStatement ps = preparedAndSetParameters(dbSelectorId, targetSql, failedDataSources,
            connection);

        //        // added by fanzeng.
        //        // 根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
        //        Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
        //        ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);

        rows += ps.executeUpdate();
        return rows;
    }

    private PreparedStatement preparedAndSetParameters(
                                                       String dbSelectorId,
                                                       String targetSql,
                                                       Map<DataSource, SQLException> failedDataSources,
                                                       Connection connection) throws SQLException {
        PreparedStatement ps = prepareStatementInternal(connection, targetSql, dbSelectorId,
            failedDataSources);

        actualStatements.add(ps);
        ps.setFetchSize(this.getFetchSize());
        setParameters(ps);
        return ps;
    }

    public int executeUpdate0() throws SQLException {
        checkClosed();
        // 获取数据源
        this.setOperation_type(DB_OPERATION_TYPE.WRITE_INTO_DB);
        // 获取连接
        DBSelector dbselector = getGroupDBSelector(SqlType.DEFAULT_SQL_TYPE);
        if (dbselector == null) {
            throw new IllegalStateException("load balance数据源配置类型错误");
        }
        // 返回执行结果
        boolean needRetry = this.autoCommit;
        Map<DataSource, SQLException> failedDataSources = needRetry ? new LinkedHashMap<DataSource, SQLException>(
            0)
            : null;
        this.updateCount = dbselector.tryExecute(failedDataSources, executeUpdateTryer,
            retryingTimes, operation_type, sql, SqlType.DEFAULT_SQL_TYPE);
        return updateCount;
    }

    private DataSourceTryer<Integer> executeUpdateTryer = new AbstractDataSourceTryer<Integer>() {
                                                            public Integer tryOnDataSource(
                                                                                           DataSource ds,
                                                                                           String name,
                                                                                           Object... args)
                                                                                                          throws SQLException {
                                                                SqlType sqlType = (SqlType) args[1];
                                                                // 获取连接
                                                                Connection conn = getGroupConnection(
                                                                    ds, sqlType, name);
                                                                String sql = (String) args[0];
                                                                int rows = 0;
                                                                return executeUpdateAndCountAffectRows0(
                                                                    null, sql, null, conn, rows);
                                                            }
                                                        };

    public int executeUpdate() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke executeUpdate, sql = " + sql);
        }

        checkClosed();

        if (super.dbConfigType == DataSourceConfigType.GROUP) {
            return executeUpdate0();
        }

        SqlExecutionContext context = getExecutionContext(sql, getParameters());
        if (context.mappingRuleReturnNullValue()) {
            return 0;
        }
        changeParameters(context.getChangedParameters());

        dumpSql(sql, context.getTargetSqls(), parameterSettings);

        int affectedRows = 0;
        List<SQLException> exceptions = null;

        for (Entry<String, SqlAndTable[]> entry : context.getTargetSqls().entrySet()) {
            for (SqlAndTable targetSql : entry.getValue()) {
                // tablesSize++;

                try {

                    String dbSelectorID = entry.getKey();
                    Connection connection = getActualConnection(dbSelectorID);
                    affectedRows = executeUpdateAndCountAffectRows(dbSelectorID, targetSql.sql,
                        context.getFailedDataSources(), connection, affectedRows);
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }
        }

        this.results = null;
        this.moreResults = false;
        this.updateCount = affectedRows;

        ExceptionUtils.throwSQLException(exceptions, sql, getParameters());

        return affectedRows;
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException("getMetaData");
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new UnsupportedOperationException("getParameterMetaData");
    }

    public void setArray(int i, Array x) throws SQLException {
        parameterSettings.put(i, new ParameterContext(ParameterMethod.setArray,
            new Object[] { i, x }));
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setAsciiStream,
            new Object[] { parameterIndex, x, length }));
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBigDecimal,
            new Object[] { parameterIndex, x }));
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBinaryStream,
            new Object[] { parameterIndex, x, length }));
    }

    public void setBlob(int i, Blob x) throws SQLException {
        parameterSettings.put(i, new ParameterContext(ParameterMethod.setBlob,
            new Object[] { i, x }));
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBoolean,
            new Object[] { parameterIndex, x }));
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setByte,
            new Object[] { parameterIndex, x }));
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBytes,
            new Object[] { parameterIndex, x }));
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length)
                                                                                 throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(
            ParameterMethod.setCharacterStream, new Object[] { parameterIndex, reader, length }));
    }

    public void setClob(int i, Clob x) throws SQLException {
        parameterSettings.put(i, new ParameterContext(ParameterMethod.setClob,
            new Object[] { i, x }));
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDate1,
            new Object[] { parameterIndex, x }));
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDate2,
            new Object[] { parameterIndex, x, cal }));
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDouble,
            new Object[] { parameterIndex, x }));
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setFloat,
            new Object[] { parameterIndex, x }));
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setInt,
            new Object[] { parameterIndex, x }));
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setLong,
            new Object[] { parameterIndex, x }));
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setNull1,
            new Object[] { parameterIndex, sqlType }));
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        parameterSettings.put(paramIndex, new ParameterContext(ParameterMethod.setNull2,
            new Object[] { paramIndex, sqlType, typeName }));
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject1,
            new Object[] { parameterIndex, x }));
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject2,
            new Object[] { parameterIndex, x, targetSqlType }));
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
                                                                                     throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject3,
            new Object[] { parameterIndex, x, targetSqlType, scale }));
    }

    public void setRef(int i, Ref x) throws SQLException {
        parameterSettings.put(i,
            new ParameterContext(ParameterMethod.setRef, new Object[] { i, x }));
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setShort,
            new Object[] { parameterIndex, x }));
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setString,
            new Object[] { parameterIndex, x }));
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTime1,
            new Object[] { parameterIndex, x }));
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTime2,
            new Object[] { parameterIndex, x, cal }));
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTimestamp1,
            new Object[] { parameterIndex, x }));
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTimestamp2,
            new Object[] { parameterIndex, x, cal }));
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setURL,
            new Object[] { parameterIndex, x }));
    }

    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameterSettings.put(parameterIndex, new ParameterContext(
            ParameterMethod.setUnicodeStream, new Object[] { parameterIndex, x, length }));
    }

    public void addBatch() throws SQLException {
        if (batchedArgs == null) {
            batchedArgs = new ArrayList<Object>();
        }

        List<ParameterContext> batchedParameterSettings = new ArrayList<ParameterContext>();
        for (ParameterContext context : parameterSettings.values()) {
            batchedParameterSettings.add(context);
        }

        batchedArgs.add(batchedParameterSettings);
    }

    /*
     * 父类已有，不需要重新定义 public void addBatch(String sql) throws SQLException {
     * super.addBatch(sql); }
     */

    private static void setBatchParameters(PreparedStatement ps,
                                           List<ParameterContext> batchedParameters)
                                                                                    throws SQLException {
        for (ParameterContext context : batchedParameters) {
            parameterHandlers.get(context.getParameterMethod()).setParameter(ps, context.getArgs());
        }
    }

    private static List<Object> getBatchParameters(List<ParameterContext> batchedParameters) {
        List<Object> parameters = new ArrayList<Object>();
        for (ParameterContext context : batchedParameters) {
            parameters.add(context.getArgs()[1]);
        }

        return parameters;
    }

    /**
     * @param targetSqls
     *            : key0:最终数据源ID; value0:{key1:最终数据源上执行的具体表名的SQL; value1:绑定参数列表}
     *            key1的SQL为PreparedStatement本身的sql加上参数列表最终确定的具体表的sql
     *            通过本类的addBatch()方法，加入到batchedArgs中的List<ParameterContext>
     * @throws ZdalCheckedExcption
     */
    protected void sortPreparedBatch(
                                     List<ParameterContext> batchedParameters,
                                     Map<String, Map<String, List<List<ParameterContext>>>> targetSqls)
                                                                                                       throws SQLException {
        try {
            // TODO:batch中如果使用了映射规则，映射规则没有返回结果时，会有错误。
            // List<ParameterContext> batchedParameters =
            // (List<ParameterContext>) arg;
            List<TargetDB> targets;
            String virtualTableName;
            if (ruleController != null) {
                TargetDBMeta metaData = ruleController.getDBAndTables(sql,
                    getBatchParameters(batchedParameters));
                targets = metaData.getTarget();
                virtualTableName = metaData.getVirtualTableName();
            } else {
                SqlType sqlType = getSqlType(sql);
                SqlDispatcher sqlDispatcher = selectSqlDispatcher(autoCommit, sqlType);
                DispatcherResult dispatcherResult = getExecutionMetaData(sql,
                    getBatchParameters(batchedParameters), null, sqlDispatcher);
                targets = dispatcherResult.getTarget();
                virtualTableName = dispatcherResult.getVirtualTableName();
            }
            for (TargetDB target : targets) {
                // 这里做了新旧兼容
                String targetName = ruleController != null ? target.getWritePool()[0] : target
                    .getDbIndex();
                // String targetName = target.getWritePool()[0];
                if (!targetSqls.containsKey(targetName)) {
                    targetSqls.put(targetName, new HashMap<String, List<List<ParameterContext>>>());
                }

                Map<String, List<List<ParameterContext>>> sqls = targetSqls.get(targetName);

                Set<String> actualTables = target.getTableNames();
                for (String tab : actualTables) {
                    String targetSql = replaceTableName(sql, virtualTableName, tab);
                    if (!sqls.containsKey(targetSql)) {
                        List<List<ParameterContext>> paramsList = new ArrayList<List<ParameterContext>>();
                        sqls.put(targetSql, paramsList);
                    }
                    sqls.get(targetSql).add(batchedParameters);
                }
            }
        } catch (ZdalCheckedExcption e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * 
     * @param batchedParameters
     * @param targetSqls
     * @throws SQLException
     * @throws SQLException
     */
    protected void sortPreparedBatch0(
                                      List<ParameterContext> batchedParameters,
                                      Map<String, Map<String, List<List<ParameterContext>>>> targetSqls)
                                                                                                        throws SQLException {

        // 获取连接
        SqlType sqlType = getSqlType(sql);
        String dbselectorID = getGroupDBSelectorID(sqlType);
        if (StringUtil.isBlank(dbselectorID)) {
            throw new IllegalStateException("load balance数据源配置类型错误");
        }
        if (!targetSqls.containsKey(dbselectorID)) {
            targetSqls.put(dbselectorID, new HashMap<String, List<List<ParameterContext>>>());
        }

        Map<String, List<List<ParameterContext>>> sqls = targetSqls.get(dbselectorID);
        if (!sqls.containsKey(sql)) {
            List<List<ParameterContext>> paramsList = new ArrayList<List<ParameterContext>>();
            sqls.put(sql, paramsList);
        }
        sqls.get(sql).add((List<ParameterContext>) batchedParameters);
    }

    @SuppressWarnings("unchecked")
    public int[] executeBatch() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke executeBatch, sql = " + sql);
        }

        checkClosed();

        if (batchedArgs == null || batchedArgs.isEmpty()) {
            return new int[0];
        }

        List<SQLException> exceptions = null;

        /**
         * key: 最终数据源ID; value:最终数据源上执行的使用物理表名的SQL 通过父类的addBatch(String
         * sql)方法，加入到batchedArgs中的sql
         */
        Map<String, List<String>> plainTargetSqls = new HashMap<String, List<String>>();
        /**
         * key0:最终数据源ID; value0:{key1:数据源上执行的使用物理表名的SQL; value1:绑定参数列表}
         * key1的SQL为PreparedStatement本身的sql加上参数列表最终确定的使用物理表名的sql
         * 通过本类的addBatch()方法，加入到batchedArgs中的List<ParameterContext>
         * 
         * [ [setString(1, 0), setString(2, wangzhaofeng0), setString(3, 20)]
         * [setString(1, 1), setString(2, wangzhaofeng1), setString(3, 20)]
         * [setString(1, 2), setString(2, wangzhaofeng2), setString(3, 20)] ]
         * 
         */
        Map<String, Map<String, List<List<ParameterContext>>>> targetSqls = new HashMap<String, Map<String, List<List<ParameterContext>>>>();
        try {
            for (Object arg : batchedArgs) {
                if (arg instanceof List) {
                    if (super.dbConfigType == DataSourceConfigType.GROUP) {
                        this.sortPreparedBatch0((List<ParameterContext>) arg, targetSqls);
                    } else {
                        this.sortPreparedBatch((List<ParameterContext>) arg, targetSqls);
                    }
                } else if (arg instanceof String) {
                    if (super.dbConfigType == DataSourceConfigType.GROUP) {
                        super.sortBatch0((String) arg, plainTargetSqls);
                    } else {
                        super.sortBatch((String) arg, plainTargetSqls);
                    }
                }
            }

            for (Entry<String, Map<String, List<List<ParameterContext>>>> entry : targetSqls
                .entrySet()) {

                String dbSelectorID = entry.getKey();
                // 校验是否允许batch事务
                checkBatchDataBaseID(dbSelectorID);

                createConnectionByID(dbSelectorID);

                try {
                    for (Entry<String, List<List<ParameterContext>>> targetSql : entry.getValue()
                        .entrySet()) {
                        executeBatchNested(null, dbSelectorID, targetSql);
                    }
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }

            for (Entry<String, List<String>> entry : plainTargetSqls.entrySet()) {
                String dbSelectorID = entry.getKey();
                // 校验是否允许batch事务
                checkBatchDataBaseID(dbSelectorID);
                createConnectionByID(dbSelectorID);
                try {
                    Statement stmt = createStatementByDataSourceSelectorID(dbSelectorID, null);

                    actualStatements.add(stmt);

                    for (String targetSql : entry.getValue()) {
                        stmt.addBatch(targetSql);
                    }

                    // TODO: 忽略返回值
                    stmt.executeBatch();

                    stmt.clearBatch();
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }
        } finally {
            batchedArgs.clear();
        }

        ExceptionUtils.throwSQLException(exceptions, sql, getParameters());

        // TODO: 忽略返回值
        return new int[0];
    }

    protected void executeBatchNested(Map<DataSource, SQLException> failedDataSources,
                                      String dbSelectorID,
                                      Entry<String, List<List<ParameterContext>>> targetSql)
                                                                                            throws SQLException {
        Connection connection = getActualConnection(dbSelectorID);
        // batch的时候是不需要重试的。现在如此假定，因此不使用可重试的statment方法
        PreparedStatement ps = prepareStatementInternal(connection, targetSql.getKey(),
            dbSelectorID, failedDataSources);

        actualStatements.add(ps);

        for (List<ParameterContext> params : targetSql.getValue()) {
            setBatchParameters(ps, params);
            ps.addBatch();
        }

        // TODO: 忽略返回值
        ps.executeBatch();

        ps.clearBatch();
    }

    public void clearBatch() throws SQLException {
        super.clearBatch();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public int getAutoGeneratedKeys() {
        return autoGeneratedKeys;
    }

    public void setAutoGeneratedKeys(int autoGeneratedKeys) {
        this.autoGeneratedKeys = autoGeneratedKeys;
    }

    public int[] getColumnIndexes() {
        return columnIndexes;
    }

    public void setColumnIndexes(int[] columnIndexes) {
        this.columnIndexes = columnIndexes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
                                                                                 throws SQLException {
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length)
                                                                                  throws SQLException {
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length)
                                                                                  throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }
}
