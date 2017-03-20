/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.RouteCondition;
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.config.DataSourceConfigType;
import com.alipay.zdal.client.controller.RuleController;
import com.alipay.zdal.client.controller.TargetDBMeta;
import com.alipay.zdal.client.dispatcher.DispatcherResult;
import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.client.jdbc.DBSelector.AbstractDataSourceTryer;
import com.alipay.zdal.client.jdbc.DBSelector.DataSourceTryer;
import com.alipay.zdal.client.jdbc.parameter.ParameterContext;
import com.alipay.zdal.client.jdbc.resultset.CountTResultSet;
import com.alipay.zdal.client.jdbc.resultset.DummyTResultSet;
import com.alipay.zdal.client.jdbc.resultset.EmptySimpleTResultSet;
import com.alipay.zdal.client.jdbc.resultset.MaxTResultSet;
import com.alipay.zdal.client.jdbc.resultset.MinTResultSet;
import com.alipay.zdal.client.jdbc.resultset.OrderByTResultSet;
import com.alipay.zdal.client.jdbc.resultset.SimpleTResultSet;
import com.alipay.zdal.client.jdbc.resultset.SumTResultSet;
import com.alipay.zdal.client.util.ExceptionUtils;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.SqlType;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.exception.sqlexceptionwrapper.ZdalCommunicationException;
import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.ParserCache;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.visitor.OrderByEle;
import com.alipay.zdal.rule.config.beans.AppRule;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;
import com.alipay.zdal.rule.ruleengine.exception.RuleRuntimeExceptionWrapper;
import com.alipay.zdal.rule.ruleengine.exception.ZdalRuleCalculateException;
import com.alipay.zdal.rule.ruleengine.rule.EmptySetRuntimeException;

/**
 * 
 * @author 伯牙
 * @version $Id: ZdalStatement.java, v 0.1 2014-1-6 下午01:19:26 Exp $
 */
public class ZdalStatement implements Statement {
    //TODO: 添加一个选项boolean值，来对statlog进行检测
    private static final Logger       log                            = Logger
                                                                         .getLogger(ZdalStatement.class);
    private static final Logger       sqlLog                         = Logger
                                                                         .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    /**
     * 用于判断是否是一个select ... for update的sql
     */
    private static final Pattern      SELECT_FOR_UPDATE_PATTERN      = Pattern
                                                                         .compile(
                                                                             "^select\\s+.*\\s+for\\s+update.*$",
                                                                             Pattern.CASE_INSENSITIVE);

    /**从DB2的系统表中获取sequence和事件的sql.  */
    private static final Pattern      SELECT_FROM_SYSTEMIBM          = Pattern
                                                                         .compile(
                                                                             "^select\\s+.*\\s+from\\s+sysibm.*$",
                                                                             Pattern.CASE_INSENSITIVE);
    private static final Pattern      SELECT_FROM_DUAL_PATTERN       = Pattern
                                                                         .compile(
                                                                             "^select\\s+.*\\s+from\\s+dual.*$",
                                                                             Pattern.CASE_INSENSITIVE);

    /**
     * 默认的每个表执行sql的超时时间
     */
    public static final long          DEFAULT_TIMEOUT_FOR_EACH_TABLE = 100;

    private static final ParserCache  globalCache                    = ParserCache.instance();

    protected Map<String, DBSelector> dbSelectors;
    protected DBSelector              groupDBSelector                = null;
    protected RuleController          ruleController;
    protected final SqlDispatcher     writeDispatcher;
    protected final SqlDispatcher     readDispatcher;

    //记录当前的操作是写库操作还是读库操作
    protected DB_OPERATION_TYPE       operation_type;

    public enum DB_OPERATION_TYPE {
        WRITE_INTO_DB, READ_FROM_DB;
    }

    private ZdalConnection         connectionProxy;

    protected List<Statement>      actualStatements     = new ArrayList<Statement>();
    protected ResultSet            results;
    protected boolean              moreResults;
    protected int                  updateCount;
    protected boolean              closed;
    /*
     *  是否替换hint中的逻辑表名，默认是不替换
     */
    private boolean                isHintReplaceSupport = false;
    /**
     * 重试次数，外部指定
     */
    protected int                  retryingTimes;
    /**
     * fetchsize 默认为10 
     */
    private int                    fetchSize            = 10;

    private int                    resultSetType        = -1;
    private int                    resultSetConcurrency = -1;
    private int                    resultSetHoldability = -1;

    protected boolean              autoCommit           = true;

    /**
     * 缓存的batch操作的dbId
     */
    private String                 batchDataBaseId      = null;

    private boolean                readOnly;

    /**
     * 将原来ResultSet接口下放到Dummy级别。这样才能支持自定义方法
     */
    protected Set<ResultSet>       openResultSets       = new HashSet<ResultSet>();

    protected List<Object>         batchedArgs;

    private long                   timeoutForEachTable  = DEFAULT_TIMEOUT_FOR_EACH_TABLE;

    protected DataSourceConfigType dbConfigType         = null;

    private int                    autoGeneratedKeys;
    private int[]                  columnIndexes;
    private String[]               columnNames;

    /**数据源的名称.  */
    protected String               appDsName            = null;

    protected static void dumpSql(String originalSql, Map<String, SqlAndTable[]> targets) {
        dumpSql(originalSql, targets, null);
    }

    public ZdalStatement(SqlDispatcher writeDispatcher, SqlDispatcher readDispatcher) {
        this.writeDispatcher = writeDispatcher;
        this.readDispatcher = readDispatcher;
    }

    protected static void dumpSql(String originalSql, Map<String, SqlAndTable[]> targets,
                                  Map<Integer, ParameterContext> parameters) {
        if (sqlLog.isDebugEnabled()) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("\n[original sql]:").append(originalSql.trim()).append("\n");
            for (Entry<String, SqlAndTable[]> entry : targets.entrySet()) {
                for (SqlAndTable targetSql : entry.getValue()) {
                    buffer.append(" [").append(entry.getKey()).append(".").append(targetSql.table)
                        .append("]:").append(targetSql.sql.trim()).append("\n");
                }
            }

            if (parameters != null && !parameters.isEmpty() && !parameters.values().isEmpty()) {
                buffer.append("[parameters]:").append(parameters.values().toString());
            }

            sqlLog.debug(buffer.toString());
        }
    }

    /**
     * 获得SQL语句种类
     *
     * @param sql SQL语句
     * @throws SQLException 当SQL语句不是SELECT、INSERT、UPDATE、DELETE语句时，抛出异常。
     */
    protected static SqlType getSqlType(String sql) throws SQLException {
        SqlType sqlType = globalCache.getSqlType(sql);
        if (sqlType == null) {
            String noCommentsSql = StringUtil.stripComments(sql, "'\"", "'\"", true, false, true,
                true).trim();

            if (StringUtil.startsWithIgnoreCaseAndWs(noCommentsSql, "select")) {
                if (SELECT_FROM_DUAL_PATTERN.matcher(noCommentsSql).matches()) {
                    sqlType = SqlType.SELECT_FROM_DUAL;
                } else if (SELECT_FOR_UPDATE_PATTERN.matcher(noCommentsSql).matches()) {
                    sqlType = SqlType.SELECT_FOR_UPDATE;
                } else if (SELECT_FROM_SYSTEMIBM.matcher(noCommentsSql).matches()) {
                    sqlType = SqlType.SELECT_FROM_SYSTEMIBM;
                } else {
                    sqlType = SqlType.SELECT;
                }
            } else if (StringUtil.startsWithIgnoreCaseAndWs(noCommentsSql, "insert")) {
                sqlType = SqlType.INSERT;
            } else if (StringUtil.startsWithIgnoreCaseAndWs(noCommentsSql, "update")) {
                sqlType = SqlType.UPDATE;
            } else if (StringUtil.startsWithIgnoreCaseAndWs(noCommentsSql, "delete")) {
                sqlType = SqlType.DELETE;
            } else {
                throw new SQLException("only select, insert, update, delete sql is supported");
            }
            sqlType = globalCache.setSqlTypeIfAbsent(sql, sqlType);

        }
        return sqlType;
    }

    /**
     * 替换SQL语句中虚拟表名为实际表名。 会 替换_tableName$ 替换_tableName_ 替换tableName.
     * 替换tableName(
     * 增加替换 _tableName, ,tableName, ,tableName_
     * 
     * @param originalSql
     *            SQL语句
     * @param virtualName
     *            虚拟表名
     * @param actualName
     *            实际表名
     * @return 返回替换后的SQL语句。
     */
    protected String replaceTableName(String originalSql, String virtualName, String actualName) {
        if (log.isDebugEnabled()) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("virtualName = ").append(virtualName).append(", ");
            buffer.append("actualName = ").append(actualName);
            log.debug(buffer.toString());
        }

        if (virtualName.equalsIgnoreCase(actualName)) {
            return originalSql;
        }
        //add by boya for testcase for schemaname.tablename to ignore replaceTablename.
        if (virtualName.contains(actualName)) {
            return originalSql;
        }

        List<String> sqlPieces = globalCache.getTableNameReplacement(originalSql);
        if (sqlPieces == null) {
            //替换   tableName$ 
            Pattern pattern1 = Pattern.compile(new StringBuilder("\\s").append(virtualName).append(
                "$").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces1 = new ArrayList<String>();
            Matcher matcher1 = pattern1.matcher(originalSql);
            int start1 = 0;
            while (matcher1.find(start1)) {
                pieces1.add(originalSql.substring(start1, matcher1.start() + 1));
                start1 = matcher1.end();
            }
            pieces1.add(originalSql.substring(start1));
            //替换   tableName  
            Pattern pattern2 = Pattern.compile(new StringBuilder("\\s").append(virtualName).append(
                "\\s").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces2 = new ArrayList<String>();
            for (String piece : pieces1) {
                Matcher matcher2 = pattern2.matcher(piece);
                int start2 = 0;
                while (matcher2.find(start2)) {
                    pieces2.add(piece.substring(start2 - 1 < 0 ? 0 : start2 - 1,
                        matcher2.start() + 1));
                    start2 = matcher2.end();
                }
                pieces2.add(piece.substring(start2 - 1 < 0 ? 0 : start2 - 1));
            }
            //替换   tableName. 
            Pattern pattern3 = Pattern.compile(new StringBuilder().append(virtualName)
                .append("\\.").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces3 = new ArrayList<String>();
            for (String piece : pieces2) {
                Matcher matcher3 = pattern3.matcher(piece);
                int start3 = 0;
                while (matcher3.find(start3)) {
                    pieces3.add(piece.substring(start3 - 1 < 0 ? 0 : start3 - 1, matcher3.start()));
                    start3 = matcher3.end();
                }
                pieces3.add(piece.substring(start3 - 1 < 0 ? 0 : start3 - 1));
            }
            //替换  tablename(
            Pattern pattern4 = Pattern.compile(new StringBuilder("\\s").append(virtualName).append(
                "\\(").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces4 = new ArrayList<String>();
            for (String piece : pieces3) {
                Matcher matcher4 = pattern4.matcher(piece);
                int start4 = 0;
                while (matcher4.find(start4)) {
                    pieces4.add(piece.substring(start4 - 1 < 0 ? 0 : start4 - 1,
                        matcher4.start() + 1));
                    start4 = matcher4.end();
                }
                pieces4.add(piece.substring(start4 - 1 < 0 ? 0 : start4 - 1));
            }

            //替换_tableName,
            Pattern pattern5 = Pattern.compile(new StringBuilder("\\s").append(virtualName).append(
                "\\,").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces5 = new ArrayList<String>();
            for (String piece : pieces4) {
                Matcher matcher5 = pattern5.matcher(piece);
                int start5 = 0;
                while (matcher5.find(start5)) {
                    pieces5.add(piece.substring(start5 - 1 < 0 ? 0 : start5 - 1,
                        matcher5.start() + 1));
                    start5 = matcher5.end();
                }
                pieces5.add(piece.substring(start5 - 1 < 0 ? 0 : start5 - 1));
            }

            //替换,tableName
            Pattern pattern6 = Pattern.compile(new StringBuilder("\\,").append(virtualName).append(
                "\\s").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces6 = new ArrayList<String>();
            for (String piece : pieces5) {
                Matcher matcher6 = pattern6.matcher(piece);
                int start6 = 0;
                while (matcher6.find(start6)) {
                    pieces6.add(piece.substring(start6 - 1 < 0 ? 0 : start6 - 1,
                        matcher6.start() + 1));
                    start6 = matcher6.end();
                }
                pieces6.add(piece.substring(start6 - 1 < 0 ? 0 : start6 - 1));
            }
            //替换 ,tableName,
            Pattern pattern7 = Pattern.compile(new StringBuilder("\\,").append(virtualName).append(
                "\\,").toString(), Pattern.CASE_INSENSITIVE);
            List<String> pieces7 = new ArrayList<String>();
            for (String piece : pieces6) {
                Matcher matcher7 = pattern7.matcher(piece);
                int start7 = 0;
                while (matcher7.find(start7)) {
                    pieces7.add(piece.substring(start7 - 1 < 0 ? 0 : start7 - 1,
                        matcher7.start() + 1));
                    start7 = matcher7.end();
                }
                pieces7.add(piece.substring(start7 - 1 < 0 ? 0 : start7 - 1));
            }

            sqlPieces = globalCache.setTableNameReplacementIfAbsent(originalSql, pieces7);

        }

        // 生成最终SQL
        StringBuilder buffer = new StringBuilder();
        boolean first = true;
        for (String piece : sqlPieces) {
            if (!first) {
                buffer.append(actualName);
            } else {
                first = false;
            }
            buffer.append(piece);
        }
        String sql_replace = buffer.toString();

        /*
         * added by fanzeng
         * 支付宝默认不替换HINT里的表名，如果需要替换，则必须在配置文件中指定
         * <property name="isHintReplaceSupport" value="true"/>
         * */
        if (log.isDebugEnabled()) {
            log.debug("是否支持替换hint的逻辑表名：isHintSupport = " + this.isHintReplaceSupport);
        }
        //替换  hint，格式不再进行限制 
        if (isHintReplaceSupport) {
            Pattern pattern8 = Pattern.compile(new StringBuilder("/\\s?\\*\\s?.*").append(
                virtualName).append(".*\\s?\\*\\s?/").toString(), Pattern.CASE_INSENSITIVE);
            String sql_pieces[] = new String[2];
            String hint = "";
            Matcher matcher8 = pattern8.matcher(sql_replace);

            int start8 = 0;
            if (matcher8.find(start8)) {
                sql_pieces[0] = sql_replace.substring(start8 - 1 < 0 ? 0 : start8 - 1, matcher8
                    .start());
                sql_pieces[1] = sql_replace.substring(matcher8.end());
                hint = sql_replace.substring(matcher8.start(), matcher8.end()).toUpperCase();

                hint = hint.replace(virtualName.toUpperCase(), actualName.toUpperCase());

                sql_replace = sql_pieces[0] + hint + sql_pieces[1];
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("替换表名后的sql为：" + sql_replace);
        }

        return sql_replace;
    }

    protected SqlExecutionContext getExecutionContext(String originalSql, List<Object> parameters)
                                                                                                  throws SQLException {
        SqlExecutionContext executionContext = null;
        try {
            executionContext = getExecutionContext1(originalSql, parameters); //新规则 
        } catch (RuleRuntimeExceptionWrapper e) {
            //因为RUleRuntimeException也是个RuntimeException,所以排在后续runtimeException前面
            SQLException sqlException = e.getSqlException();
            if (sqlException instanceof ZdalCommunicationException) {
                //不重复的进行包装，这个异常是zdal查询走分库时，分库重试次数到达上限时，自己会抛出的。业务需要这个异常
                throw e;
            } else {
                //对于非zdal作为规则引擎中mapping rule 低层数据库查询的场景，要对sqlException进行包装后抛出
                throw new ZdalCommunicationException("rule sql exceptoin.", sqlException);
            }

        } catch (ZdalRuleCalculateException e) {
            log.error("规则引擎计算错误，sql=" + originalSql, e);
            throw e;
        } catch (RuntimeException e) {
            String context = ExceptionUtils.getErrorContext(originalSql, parameters,
                "An error occerred on  routing or getExecutionContext,sql is :");
            //log.error(context, e);
            throw new RuntimeException(context, e);
        }
        return executionContext;
    }

    /**
     * @param dbSelectorID
     * @param retringContext
     * @throws SQLException
     */
    public void createConnectionByID(String dbSelectorID) throws SQLException {
        DBSelector dbSelector = this.dbSelectors.get(dbSelectorID);
        //			retringContext.setDbSelector(dbSelector);
        createConnection(dbSelector, dbSelectorID);
    }

    /**
     * 获取新的Connection和他对应的Datasource
     * 
     * datasource主要是用于在随机重试的时候排除已经挂掉的数据源
     * 
     * 不提供重试
     * @param ds
     * @return
     * @throws SQLException
     */
    private ConnectionAndDatasource getNewConnectionAndDataSource(DataSource ds,
                                                                  DBSelector dbSelector)
                                                                                        throws SQLException {
        ConnectionAndDatasource connectionAndDatasource = new ConnectionAndDatasource();
        connectionAndDatasource.parentDataSource = ds;
        connectionAndDatasource.dbSelector = dbSelector;
        long begin = System.currentTimeMillis();
        Connection conn = ds.getConnection();
        conn.setAutoCommit(autoCommit);
        long elapsed = System.currentTimeMillis() - begin;
        if (log.isDebugEnabled()) {
            log.debug("get the connection, elapsed time=" + elapsed + ",thread="
                      + Thread.currentThread().getName());
        }

        connectionAndDatasource.connection = conn;
        return connectionAndDatasource;
    }

    protected SqlDispatcher selectSqlDispatcher(boolean autoCommit, SqlType sqlType)
                                                                                    throws SQLException {
        SqlDispatcher sqlDispatcher;
        if (sqlType != SqlType.SELECT) {
            if (this.writeDispatcher == null) {
                throw new SQLException("分库不支持写入。请检查配置或SQL");
            }
            sqlDispatcher = this.writeDispatcher;
        } else {
            if (autoCommit) {
                String rc = (String) ThreadLocalMap.get(ThreadLocalString.SELECT_DATABASE);
                if (rc != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("rc=" + rc);
                    }
                    sqlDispatcher = this.writeDispatcher;
                } else {
                    sqlDispatcher = this.readDispatcher != null ? this.readDispatcher
                        : this.writeDispatcher;
                }
            } else {
                sqlDispatcher = this.writeDispatcher;
                if (sqlDispatcher == null) {
                    throw new SQLException("分库不支持写入。请不要使用事务");
                }
            }
        }
        if (sqlDispatcher == null) {
            throw new SQLException("没有分库规则或sqlDispatcher为null，请检查配置或SQL");
        }

        if (sqlDispatcher == this.writeDispatcher) {
            this.setOperation_type(DB_OPERATION_TYPE.WRITE_INTO_DB);
        } else if (sqlDispatcher == this.readDispatcher) {
            this.setOperation_type(DB_OPERATION_TYPE.READ_FROM_DB);
        } else {
            throw new SQLException("操作类型发生异常，超出正常范围！");
        }
        return sqlDispatcher;
    }

    /**
     * 1. 只支持单库的事务, 有事务同时目标库为多个时报错
     * 2. 只支持单库的事务, 有事务并且当前事务中已经关联的分库和本次解析的目标库不是同一个库时报错
     */
    protected SqlExecutionContext getExecutionContext1(String originalSql, List<Object> parameters)
                                                                                                   throws SQLException {
        SqlExecutionContext context = new SqlExecutionContext();

        SqlType sqlType = getSqlType(originalSql);

        RouteCondition rc = (RouteCondition) ThreadLocalMap.get(ThreadLocalString.ROUTE_CONDITION);

        ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, null);

        DispatcherResult metaData = null;
        List<TargetDB> targets = null;

        SqlDispatcher sqlDispatcher = selectSqlDispatcher(autoCommit, sqlType);

        /*
         * 查看sqlDispatcher是否为writeDispatcher.
         * 
         * writeDispatcher主要处理：insert ,update ,select for update,事务中select这
         * 4种情况，都不需要读重试。
         * 
         * 不为的情况有两种，第一种是内存中对象状态不一样，这种应该是readDispatcher。
         * 计算出的dispatcher不会为除了read和writeDispatcher以外的其他情况，
         * 由selectSqlDispatcher方法保证。
         * 
         * 还有一种是writeDispatcher为null 这时候 ，又因为计算出的
         * dispatcher只可能为 read或write。所以也可以保证正确的结果。
         */
        boolean ruleReturnNullValue = false;
        /**
         * 设置了autoCommit属性值，用于在根据数据源key权重配置随机选择组内db时，判定是不是在事务中
         * 如果在事务中，就将此次计算的值缓存起来，然后该事务中的其他sql执行随机算法时，直接将该值返回
         * 以达到在一个事务中禁止两次随机而有可能选择不同的db
         */
        //        ThreadLocalMap.put(ThreadLocalString.GET_AUTOCOMMIT_PROPERTY, autoCommit);
        try {
            metaData = getExecutionMetaData(originalSql, parameters, rc, sqlDispatcher);
            targets = metaData.getTarget();
        } catch (EmptySetRuntimeException e) {
            ruleReturnNullValue = true;
        }

        if (targets == null || targets.isEmpty()) {
            if (!ruleReturnNullValue) {
                throw new SQLException("找不到目标库，请检查配置,the originalSql = " + originalSql
                                       + " ,the parameters = " + parameters);
            } else {
                //如果是mapping rule则在计算中如果返回null则直接返回emptyResultSet
                context.setRuleReturnNullValue(ruleReturnNullValue);
            }
        } else {
            buildExecutionContext(originalSql, context, sqlType, metaData, targets);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    protected final ResultSet getEmptyResultSet() {
        return new EmptySimpleTResultSet(this, Collections.EMPTY_LIST);
    }

    private void buildExecutionContext(String originalSql, SqlExecutionContext context,
                                       SqlType sqlType, DispatcherResult metaData,
                                       List<TargetDB> targets) throws SQLException {
        // 只支持单库的事务
        if (!autoCommit && targets.size() != 1 && sqlType != SqlType.SELECT) {
            throw new SQLException("只支持单库的事务：target.size=" + targets.size());
        }

        // 事务启动
        setTransaction(targets, originalSql);

        for (TargetDB target : targets) {

            String dbIndex = target.getDbIndex();

            Set<String> actualTables = target.getTableNames();

            if (dbIndex == null || dbIndex.length() < 1) {
                throw new SQLException("invalid dbIndex:" + dbIndex);
            }

            if (actualTables == null || actualTables.isEmpty()) {
                throw new SQLException("找不到目标表");
            }

            // 重用连接
            //			这里处理获取并重用连接的问题
            DBSelector dbselector = dbSelectors.get(dbIndex);
            if (dbselector == null) {
                throw new IllegalStateException("没有为dbIndex[" + dbIndex + "]配置数据源");
            }
            createConnection(dbselector, dbIndex); //这里如果是重试后成功，后续重试时没有排除本次重试过的ds

            if (sqlType == SqlType.INSERT) {
                if (actualTables.size() != 1) {
                    if (actualTables.isEmpty()) {
                        throw new SQLException(
                            "Zdal need at least one table, but There is none selected ");
                    }

                    throw new SQLException("mapping many actual tables");
                }
            }

            if (!autoCommit && !dbIndex.equals(getConnectionProxy().getTxTarget())
                && sqlType != SqlType.SELECT) {
                throw new SQLException("zdal只支持单库的事务：dbIndex=" + dbIndex + ",txTarget="
                                       + getConnectionProxy().getTxTarget() + ",originalSql="
                                       + originalSql);
            }

            SqlAndTable[] targetSqls = new SqlAndTable[actualTables.size()];
            if (!metaData.allowReverseOutput()) {
                int i = 0;
                for (String tab : actualTables) {
                    SqlAndTable sqlAndTable = new SqlAndTable();
                    targetSqls[i] = sqlAndTable;
                    sqlAndTable.sql = replaceTableName(originalSql, metaData.getVirtualTableName(),
                        tab);
                    //如果metaData(也就是DispatcherResult)里面有join表名，那么就替换掉;
                    sqlAndTable.sql = replaceJoinTableName(metaData.getVirtualTableName(), metaData
                        .getVirtualJoinTableNames(), tab, sqlAndTable.sql);
                    sqlAndTable.table = tab;
                    i++;
                }
            } else {
                int i = 0;
                for (String tab : actualTables) {
                    SqlAndTable targetSql = new SqlAndTable();
                    targetSql.sql = replaceTableName(originalSql, metaData.getVirtualTableName(),
                        tab);
                    targetSql.table = tab;
                    targetSqls[i] = targetSql;
                    i++;
                }
                // 因为所有SQL绑定参数都一样，所以只要取第一个。
                context.setChangedParameters(target.getChangedParams());
            }
            context.getTargetSqls().put(dbIndex, targetSqls);

        }

        context.setOrderByColumns(metaData.getOrderByMessages().getOrderbyList());
        context.setSkip(metaData.getSkip() == DefaultSqlParserResult.DEFAULT_SKIP_MAX ? 0
            : metaData.getSkip());
        context.setMax(metaData.getMax() == DefaultSqlParserResult.DEFAULT_SKIP_MAX ? -1 : metaData
            .getMax());
        context.setGroupFunctionType(metaData.getGroupFunctionType());
        context.setVirtualTableName(metaData.getVirtualTableName());
        //这里需要注意的
        // boolean needRetry = SqlType.SELECT.equals(sqlType) && this.autoCommit;
        boolean needRetry = this.autoCommit;
        //boolean isMySQL = sqlDispatcher.getDBType() == DBType.MYSQL?true:false;
        //RetringContext retringContext = new RetringContext(isMySQL);
        //retringContext.setNeedRetry(needRetry);
        //context.setRetringContext(retringContext);
        Map<DataSource, SQLException> failedDataSources = needRetry ? new LinkedHashMap<DataSource, SQLException>(
            0)
            : null;
        context.setFailedDataSources(failedDataSources);
    }

    /**
     * @param tab 实际表名
     * @param vtab 虚拟表名
     * @return
     */
    private String getSuffix(String tab, String vtab) {
        String result = tab.substring(vtab.length());
        return result;
    }

    /**
     * 获取sql执行信息
     * @param originalSql
     * @param parameters
     * @param rc
     * @param metaData
     * @param sqlDispatcher
     * @return
     * @throws SQLException
     */
    protected DispatcherResult getExecutionMetaData(String originalSql, List<Object> parameters,
                                                    RouteCondition rc, SqlDispatcher sqlDispatcher)
                                                                                                   throws SQLException {
        DispatcherResult metaData;
        if (rc != null) {
            //不走解析SQL，由ThreadLocal传入的指定对象（RouteCondition），决定库表目的地
            metaData = sqlDispatcher.getDBAndTables(rc);
        } else {
            // 通过解析SQL来分库分表
            try {
                metaData = sqlDispatcher.getDBAndTables(originalSql, parameters);
            } catch (ZdalCheckedExcption e) {
                throw new SQLException(e.getMessage());
            }
        }
        return metaData;
    }

    /**
     * 事务启动
     * @param targets
     */
    private void setTransaction(List<TargetDB> targets, String originalSql) {
        if (!autoCommit && getConnectionProxy().getTxStart()) {
            getConnectionProxy().setTxStart(false);
            //getConnectionProxy().setTxTarget(targets.get(0).getWritePool()[0]);
            getConnectionProxy().setTxTarget(targets.get(0).getDbIndex());
            if (log.isDebugEnabled()) {
                log.debug("缓存事务数据库标识:Set the txStart property to false, and the dbIndex="
                          + targets.get(0).getDbIndex() + ",originalSql=" + originalSql);
            }
        }
    }

    private final DataSourceTryer<ConnectionAndDatasource> createConnectionTryer = new AbstractDataSourceTryer<ConnectionAndDatasource>() {
                                                                                     public ConnectionAndDatasource tryOnDataSource(
                                                                                                                                    DataSource ds,
                                                                                                                                    String name,
                                                                                                                                    Object... args)
                                                                                                                                                   throws SQLException {
                                                                                         DBSelector dbSelector = (DBSelector) args[0];
                                                                                         dbSelector
                                                                                             .setSelectedDSName(name);
                                                                                         return getNewConnectionAndDataSource(
                                                                                             ds,
                                                                                             dbSelector);
                                                                                     }

                                                                                     @Override
                                                                                     public ConnectionAndDatasource onSQLException(
                                                                                                                                   java.util.List<SQLException> exceptions,
                                                                                                                                   ExceptionSorter exceptionSorter,
                                                                                                                                   Object... args)
                                                                                                                                                  throws SQLException {
                                                                                         int size = exceptions
                                                                                             .size();
                                                                                         if (size <= 0) {
                                                                                             throw new IllegalArgumentException(
                                                                                                 "should not be here");
                                                                                         } else {
                                                                                             //正常情况下的处理
                                                                                             int lastElementIndex = size - 1;
                                                                                             //取最后一个exception.判断是否是数据库不可用异常.如果是，去掉最后一个异常，并将头异常包装为ZdalCommunicationException抛出
                                                                                             SQLException lastSQLException = exceptions
                                                                                                 .get(lastElementIndex);
                                                                                             if (exceptionSorter
                                                                                                 .isExceptionFatal(lastSQLException)) {
                                                                                                 exceptions
                                                                                                     .remove(lastElementIndex);
                                                                                                 exceptions
                                                                                                     .add(
                                                                                                         0,
                                                                                                         new ZdalCommunicationException(
                                                                                                             "zdal communicationException ",
                                                                                                             lastSQLException));
                                                                                             }
                                                                                         }
                                                                                         return super
                                                                                             .onSQLException(
                                                                                                 exceptions,
                                                                                                 exceptionSorter,
                                                                                                 args);
                                                                                     };
                                                                                 };

    /**
     * 如果重用连接 则重新设置autoCommit然后推送。
     * 
     * 如果不重用连接，则从Datasource里面选择一个Datasource后建立连接
     * 
     * 然后将链接放入parentConnection的可重用连接map里(getConnectionProxy.getAuctualConnections)
     * @param dbIndex
     * @return 
     * @throws SQLException
     */
    protected void createConnection(DBSelector dbSelector, String dbIndex) throws SQLException {
        this.createConnection(dbSelector, dbIndex, new LinkedHashMap<DataSource, SQLException>(0));
    }

    protected void createConnection(DBSelector dbSelector, String dbIndex,
                                    Map<DataSource, SQLException> failedDataSources)
                                                                                    throws SQLException {
        //		Map<String, ConnectionAndDatasource> connections = getConnectionProxy().getActualConnections();
        ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
            .getConnectionAndDatasourceByDBSelectorID(dbIndex);

        //DataSource datasource = null;
        //datasource = dbSelector.select();
        if (connectionAndDatasource == null) {
            if (dbIndex == null) {
                throw new SQLException(new StringBuilder("data source ").append(dbIndex).append(
                    " does not exist").toString());
            }
            //没有connection
            //try {
            //connectionAndDatasource = getNewConnectionAndDataSource(datasource,dbSelector);
            connectionAndDatasource = dbSelector.tryExecute(failedDataSources,
                createConnectionTryer, retryingTimes, operation_type, dbSelector);
            getConnectionProxy().put(dbIndex, connectionAndDatasource);
            //} catch (NullPointerException e) {
            //	throw new SQLException(new StringBuilder("data source ").append(dbIndex).append(" does not exist")
            //			.toString());
            //}catch (SQLException e) {
            //	throw new RetrySQLException(e, datasource);
            //}
        } else {
            //可重用connection
            //datasource = connectionAndDatasource.parentDataSource;
            try {
                connectionAndDatasource.connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                //throw new RetrySQLException(e, datasource);
                failedDataSources.put(connectionAndDatasource.parentDataSource, e);
                getConnectionProxy().removeConnectionAndDatasourceByID(dbIndex);
                createConnection(dbSelector, dbIndex, failedDataSources);
            }

        }
    }

    /**
     * 用当前连接建立statementd
     * 
     * @param connection 当前正在用的connection,本来可以从map中取但因为效率上的考虑所以还是不这样做
     * @param connections 
     * @param dbIndex
     * @param retringContext
     * @return
     * @throws SQLException
     */
    protected Statement createStatementInternal(Connection connection, String dbIndex,
                                                Map<DataSource, SQLException> failedDataSources)
                                                                                                throws SQLException {
        Statement stmt;
        if (this.resultSetType != -1 && this.resultSetConcurrency != -1
            && this.resultSetHoldability != -1) {
            stmt = connection.createStatement(this.resultSetType, this.resultSetConcurrency,
                this.resultSetHoldability);
        } else if (this.resultSetType != -1 && this.resultSetConcurrency != -1) {
            stmt = connection.createStatement(this.resultSetType, this.resultSetConcurrency);
        } else {
            stmt = connection.createStatement();
        }

        return stmt;
    }

    public Connection getConnection() throws SQLException {
        return connectionProxy;
    }

    private boolean executeInternal(String sql, int autoGeneratedKeys, int[] columnIndexes,
                                    String[] columnNames) throws SQLException {
        SqlType sqlType = getSqlType(sql);
        if (sqlType == SqlType.SELECT || sqlType == SqlType.SELECT_FROM_DUAL
            || sqlType == SqlType.SELECT_FOR_UPDATE) {
            if (this.dbConfigType == DataSourceConfigType.GROUP) {
                executeQuery0(sql, sqlType);
            } else {
                executeQuery(sql);
            }
            return true;
        } else if (sqlType == SqlType.INSERT || sqlType == SqlType.UPDATE
                   || sqlType == SqlType.DELETE) {
            if (autoGeneratedKeys == -1 && columnIndexes == null && columnNames == null) {
                executeUpdate(sql);
            } else if (autoGeneratedKeys != -1) {
                executeUpdate(sql, autoGeneratedKeys);
            } else if (columnIndexes != null) {
                executeUpdate(sql, columnIndexes);
            } else if (columnNames != null) {
                executeUpdate(sql, columnNames);
            } else {
                executeUpdate(sql);
            }

            return false;
        } else {
            throw new SQLException("only select, insert, update, delete sql is supported");
        }
    }

    public boolean execute(String sql) throws SQLException {
        return executeInternal(sql, -1, null, null);
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return executeInternal(sql, autoGeneratedKeys, null, null);
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return executeInternal(sql, -1, columnIndexes, null);
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return executeInternal(sql, -1, null, columnNames);
    }

    private ResultSet executeQuery0(String sql, SqlType sqlType) throws SQLException {
        checkClosed();
        this.setOperation_type(DB_OPERATION_TYPE.READ_FROM_DB);
        //获取连接
        DBSelector dbselector = getGroupDBSelector(sqlType);
        if (dbselector == null) {
            throw new IllegalStateException("load balance数据源配置类型错误");
        }

        //返回执行结果
        return dbselector.tryExecute(new LinkedHashMap<DataSource, SQLException>(0),
            this.executeQueryTryer, retryingTimes, operation_type, sql, sqlType);
    }

    protected DataSourceTryer<ResultSet> executeQueryTryer = new AbstractDataSourceTryer<ResultSet>() {
                                                               public ResultSet tryOnDataSource(
                                                                                                DataSource ds,
                                                                                                String name,
                                                                                                Object... args)
                                                                                                               throws SQLException {
                                                                   String sql = (String) args[0];
                                                                   SqlType sqlType = (SqlType) args[1];
                                                                   //获取连接
                                                                   Connection conn = ZdalStatement.this
                                                                       .getGroupConnection(ds,
                                                                           sqlType, name);
                                                                   return ZdalStatement.this
                                                                       .executeQueryOnConnection(
                                                                           conn, sql);
                                                               }

                                                           };

    private ResultSet executeQueryOnConnection(Connection conn, String sql) throws SQLException {
        Statement stmt = createStatementInternal(conn, null, null);
        actualStatements.add(stmt);
        List<ResultSet> actualResultSets = new ArrayList<ResultSet>();

        actualResultSets.add(stmt.executeQuery(sql));

        DummyTResultSet currentResultSet = new SimpleTResultSet(this, actualResultSets);

        openResultSets.add(currentResultSet);

        this.results = currentResultSet;
        this.moreResults = false;
        this.updateCount = -1;

        return this.results;

    }

    /**
     * 如果缓存的连接不空，直接返回该连接，否则创建新连接
     * @param ds
     * @return
     * @throws SQLException
     */
    public Connection getGroupConnection(DataSource ds, SqlType sqlType, String name)
                                                                                     throws SQLException {
        Connection conn = null;
        //避免在事务中多次select时，连接无法释放，所以公用同一个select的连接.
        if (this.getConnectionProxy().get(name) != null) {
            conn = this.getConnectionProxy().get(name).connection;
        } else {
            ConnectionAndDatasource connectionAndDatasource = new ConnectionAndDatasource();
            connectionAndDatasource.parentDataSource = ds;
            connectionAndDatasource.dbSelector = null;
            conn = ds.getConnection();
            connectionAndDatasource.connection = conn;
            this.getConnectionProxy().put(name, connectionAndDatasource);
        }
        conn.setAutoCommit(autoCommit);
        return conn;
    }

    /**
     * 该dbSelector是被loadbalance的配置调用
     * @return
     */
    public DBSelector getGroupDBSelector(SqlType sqlType) {
        DBSelector rGroupDBSelector = null, wGroupDBSelector = null;
        for (Map.Entry<String, DBSelector> dbSelector : dbSelectors.entrySet()) {
            if (dbSelector.getKey().endsWith(AppRule.DBINDEX_SUFFIX_READ)
                && dbSelector.getValue() != null) {
                rGroupDBSelector = dbSelector.getValue();
            } else if (dbSelector.getKey().endsWith(AppRule.DBINDEX_SUFFIX_WRITE)
                       && dbSelector.getValue() != null) {
                wGroupDBSelector = dbSelector.getValue();
            } else {
                throw new IllegalArgumentException("The dbSelector set error!");
            }
        }
        //如果是事务，则直接到写库
        if (sqlType != SqlType.SELECT) {
            return wGroupDBSelector;
        } else if (!autoCommit) {
            return wGroupDBSelector;
        } else {
            return rGroupDBSelector;
        }
    }

    /**
     * 该dbSelector是被loadbalance的配置调用
     * @return
     */
    public String getGroupDBSelectorID(SqlType sqlType) {
        String rGroupDBSelectorID = null, wGroupDBSelectorID = null;
        for (Map.Entry<String, DBSelector> dbSelector : dbSelectors.entrySet()) {
            if (dbSelector.getKey().endsWith(AppRule.DBINDEX_SUFFIX_READ)
                && dbSelector.getValue() != null) {
                rGroupDBSelectorID = dbSelector.getKey();
            } else if (dbSelector.getKey().endsWith(AppRule.DBINDEX_SUFFIX_WRITE)
                       && dbSelector.getValue() != null) {
                wGroupDBSelectorID = dbSelector.getKey();
            } else {
                throw new IllegalArgumentException("The dbSelector set error!");
            }
        }
        if (sqlType != SqlType.SELECT) {
            return wGroupDBSelectorID;
        } else {
            return rGroupDBSelectorID;
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {

        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            SqlType sqlType = getSqlType(sql);
            return this.executeQuery0(sql, sqlType);
        }

        checkClosed();

        SqlExecutionContext context = getExecutionContext(sql, null);
        /*
         * modified by shenxun:
         * 这里主要是处理mappingRule返回空的情况下，应该返回空结果集
         */
        if (context.mappingRuleReturnNullValue()) {
            ResultSet emptyResultSet = getEmptyResultSet();
            this.results = emptyResultSet;
            return emptyResultSet;
        }

        //        int tablesSize = 0;
        dumpSql(sql, context.getTargetSqls());

        List<SQLException> exceptions = null;
        List<ResultSet> actualResultSets = new ArrayList<ResultSet>();
        // int databaseSize = context.getTargetSqls().size();
        for (Entry<String/*dbSelectorID*/, SqlAndTable[]> entry : context.getTargetSqls()
            .entrySet()) {
            for (SqlAndTable targetSql : entry.getValue()) {
                //                tablesSize++;
                try {
                    //RetringContext retringContext = context.getRetringContext();
                    String dbSelectorId = entry.getKey();
                    Statement stmt = createStatementByDataSourceSelectorID(dbSelectorId, context
                        .getFailedDataSources());
                    //链接重用
                    actualStatements.add(stmt);

                    queryAndAddResultToCollection(dbSelectorId, actualResultSets, targetSql, stmt,
                        context.getFailedDataSources());

                } catch (SQLException e) {

                    //异常处理
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                    ExceptionUtils.throwSQLException(exceptions, sql, Collections.emptyList()); //直接抛出
                }
            }
        }

        ExceptionUtils.throwSQLException(exceptions, sql, Collections.emptyList());

        DummyTResultSet resultSet = mergeResultSets(context, actualResultSets);
        openResultSets.add(resultSet);

        this.results = resultSet;
        this.moreResults = false;
        this.updateCount = -1;

        return this.results;
    }

    protected void queryAndAddResultToCollection(String dbSelectorId,
                                                 List<ResultSet> actualResultSets,
                                                 SqlAndTable targetSql, Statement stmt,
                                                 Map<DataSource, SQLException> failedDataSources)
                                                                                                 throws SQLException {
        //added by fanzeng.
        //根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
        try {
            actualResultSets.add(stmt.executeQuery(targetSql.sql));
        } finally {
            Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
            ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);
        }
    }

    protected Connection getActualConnection(String key) {
        ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
            .getConnectionAndDatasourceByDBSelectorID(key);
        Connection conn = connectionAndDatasource.connection;
        return conn;
    }

    // 获取真正的 数据源的标识以及数据源
    protected Map<String, DataSource> getActualIdAndDataSource(String key) {
        ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
            .getConnectionAndDatasourceByDBSelectorID(key);
        Map<String, DataSource> map = new HashMap<String, DataSource>();
        if (connectionAndDatasource != null) {
            DBSelector dbSelector = connectionAndDatasource.dbSelector;
            DataSource ds = connectionAndDatasource.parentDataSource;
            if (dbSelector == null || ds == null) {
                throw new IllegalArgumentException("数据源不能为空，请检查！");
            }
            //加上数据源名称前缀，by冰魂 20130903
            map.put(appDsName + "." + dbSelector.getSelectedDSName(), ds);
        }
        return map;
    }

    protected DummyTResultSet mergeResultSets(SqlExecutionContext context,
                                              List<ResultSet> actualResultSets) throws SQLException {
        if (context.getOrderByColumns() != null && !context.getOrderByColumns().isEmpty()
            && context.getGroupFunctionType() != GroupFunctionType.NORMAL) {
            throw new SQLException("'group function' and 'order by' can't be together!");
        }
        if (context.getGroupFunctionType() == GroupFunctionType.AVG) {
            throw new SQLException("The group function 'AVG' is not supported now!");
        } else if (context.getGroupFunctionType() == GroupFunctionType.COUNT) {
            return new CountTResultSet(this, actualResultSets);
        } else if (context.getGroupFunctionType() == GroupFunctionType.MAX) {
            return new MaxTResultSet(this, actualResultSets);
        } else if (context.getGroupFunctionType() == GroupFunctionType.MIN) {
            return new MinTResultSet(this, actualResultSets);
        } else if (context.getGroupFunctionType() == GroupFunctionType.SUM) {
            return new SumTResultSet(this, actualResultSets);
        } else if (context.getOrderByColumns() != null && !context.getOrderByColumns().isEmpty()) {
            OrderByColumn[] orderByColumns = new OrderByColumn[context.getOrderByColumns().size()];
            int i = 0;
            for (OrderByEle element : context.getOrderByColumns()) {
                orderByColumns[i] = new OrderByColumn();
                orderByColumns[i].setColumnName(element.getName());
                orderByColumns[i++].setAsc(element.isASC());
            }
            OrderByTResultSet orderByTResultSet = new OrderByTResultSet(this, actualResultSets);
            orderByTResultSet.setOrderByColumns(orderByColumns);
            orderByTResultSet.setLimitFrom(context.getSkip());
            orderByTResultSet.setLimitTo(context.getMax());
            return orderByTResultSet;
        } else {
            SimpleTResultSet simpleTResultSet = new SimpleTResultSet(this, actualResultSets);
            simpleTResultSet.setLimitFrom(context.getSkip());
            simpleTResultSet.setLimitTo(context.getMax());
            return simpleTResultSet;
        }
    }

    protected Statement createStatementByDataSourceSelectorID(
                                                              String id,
                                                              Map<DataSource, SQLException> failedDataSources)
                                                                                                              throws SQLException {

        Connection connection = getActualConnection(id);
        Statement stmt = createStatementInternal(connection, id, failedDataSources);
        return stmt;
    }

    private int executeUpdateInternal0(String sql, int autoGeneratedKeys, int[] columnIndexes,
                                       String[] columnNames) throws SQLException {
        checkClosed();
        //获取数据源
        this.setOperation_type(DB_OPERATION_TYPE.WRITE_INTO_DB);
        //获取连接
        DBSelector dbselector = getGroupDBSelector(SqlType.DEFAULT_SQL_TYPE);
        if (dbselector == null) {
            throw new IllegalStateException("load balance数据源配置类型错误");
        }
        this.autoGeneratedKeys = autoGeneratedKeys;
        this.columnIndexes = columnIndexes;
        this.columnNames = columnNames;
        boolean needRetry = this.autoCommit;
        Map<DataSource, SQLException> failedDataSources = needRetry ? new LinkedHashMap<DataSource, SQLException>(
            0)
            : null;
        //返回执行结果
        return dbselector.tryExecute(failedDataSources, this.executeUpdateTryer, retryingTimes,
            operation_type, sql, SqlType.DEFAULT_SQL_TYPE);
    }

    private DataSourceTryer<Integer> executeUpdateTryer = new AbstractDataSourceTryer<Integer>() {
                                                            public Integer tryOnDataSource(
                                                                                           DataSource ds,
                                                                                           String name,
                                                                                           Object... args)
                                                                                                          throws SQLException {
                                                                SqlType sqlType = (SqlType) args[1];
                                                                //获取连接
                                                                Connection conn = ZdalStatement.this
                                                                    .getGroupConnection(ds,
                                                                        sqlType, name);
                                                                return ZdalStatement.this
                                                                    .executeUpdateOnConnection(
                                                                        conn, args);
                                                            }
                                                        };

    private int executeUpdateOnConnection(Connection conn, Object... args) throws SQLException {
        Statement stmt = createStatementInternal(conn, null, null);
        String sql = (String) args[0];
        int affectedRows = 0;
        if (autoGeneratedKeys == -1 && columnIndexes == null && columnNames == null) {
            affectedRows += stmt.executeUpdate(sql);
        } else if (autoGeneratedKeys != -1) {
            affectedRows += stmt.executeUpdate(sql, autoGeneratedKeys);
        } else if (columnIndexes != null) {
            affectedRows += stmt.executeUpdate(sql, columnIndexes);
        } else if (columnNames != null) {
            affectedRows += stmt.executeUpdate(sql, columnNames);
        } else {
            affectedRows += stmt.executeUpdate(sql);
        }
        return affectedRows;
    }

    private int executeUpdateInternal(String sql, int autoGeneratedKeys, int[] columnIndexes,
                                      String[] columnNames) throws SQLException {
        checkClosed();

        SqlExecutionContext context = getExecutionContext(sql, null);

        if (context.mappingRuleReturnNullValue()) {
            return 0;
        }

        dumpSql(sql, context.getTargetSqls());

        int affectedRows = 0;
        List<SQLException> exceptions = null;

        for (Entry<String, SqlAndTable[]> entry : context.getTargetSqls().entrySet()) {
            for (SqlAndTable targetSql : entry.getValue()) {
                //                tablesSize++;
                try {
                    String dbSelectorId = entry.getKey();
                    Statement stmt = createStatementByDataSourceSelectorID(dbSelectorId, context
                        .getFailedDataSources());
                    actualStatements.add(stmt);
                    //added by fanzeng.
                    //根据dbSelectorId获取对应的数据源的标识符以及数据源，然后放到threadlocal里
                    Map<String, DataSource> map = getActualIdAndDataSource(dbSelectorId);
                    ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);
                    if (autoGeneratedKeys == -1 && columnIndexes == null && columnNames == null) {
                        affectedRows += stmt.executeUpdate(targetSql.sql);
                    } else if (autoGeneratedKeys != -1) {
                        affectedRows += stmt.executeUpdate(targetSql.sql, autoGeneratedKeys);
                    } else if (columnIndexes != null) {
                        affectedRows += stmt.executeUpdate(targetSql.sql, columnIndexes);
                    } else if (columnNames != null) {
                        affectedRows += stmt.executeUpdate(targetSql.sql, columnNames);
                    } else {
                        affectedRows += stmt.executeUpdate(targetSql.sql);
                    }

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

        ExceptionUtils.throwSQLException(exceptions, sql, Collections.emptyList());

        return affectedRows;
    }

    public int executeUpdate(String sql) throws SQLException {
        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            return executeUpdateInternal0(sql, -1, null, null);
        }
        return executeUpdateInternal(sql, -1, null, null);
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            return executeUpdateInternal0(sql, autoGeneratedKeys, null, null);
        }
        return executeUpdateInternal(sql, autoGeneratedKeys, null, null);
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            return executeUpdateInternal0(sql, -1, columnIndexes, null);
        }
        return executeUpdateInternal(sql, -1, columnIndexes, null);
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        if (this.dbConfigType == DataSourceConfigType.GROUP) {
            return executeUpdateInternal0(sql, -1, null, columnNames);
        }
        return executeUpdateInternal(sql, -1, null, columnNames);
    }

    public void addBatch(String sql) throws SQLException {
        checkClosed();

        if (batchedArgs == null) {
            batchedArgs = new ArrayList<Object>();
        }

        if (sql != null) {
            batchedArgs.add(sql);
        }
    }

    /**
     * @param targetSqls: key:最终数据源ID; value:最终数据源上执行的物理表名的SQL
     * @throws ZdalCheckedExcption
     */
    protected void sortBatch0(String originalSql, Map<String, List<String>> targetSqls)
                                                                                       throws SQLException {
        SqlType sqlType = getSqlType(originalSql);
        String dbselectorID = getGroupDBSelectorID(sqlType);
        if (!targetSqls.containsKey(dbselectorID)) {
            targetSqls.put(dbselectorID, new ArrayList<String>());
        }
        List<String> sqls = targetSqls.get(dbselectorID);
        sqls.add(originalSql);
    }

    /**
     * @param targetSqls: key:最终数据源ID; value:最终数据源上执行的物理表名的SQL
     * @throws ZdalCheckedExcption
     */
    protected void sortBatch(String originalSql, Map<String, List<String>> targetSqls)
                                                                                      throws SQLException {
        //TODO:batch中如果使用了映射规则，映射规则没有返回结果时，会有错误。
        try {
            List<TargetDB> targets;
            String virtualTableName;
            List<String> virtualJoinTableNames;
            if (ruleController != null) {
                TargetDBMeta metaData = ruleController.getDBAndTables(originalSql, null);
                targets = metaData.getTarget();
                virtualTableName = metaData.getVirtualTableName();
                virtualJoinTableNames = metaData.getVirtualJoinTableNames();
            } else {
                SqlType sqlType = getSqlType(originalSql);
                SqlDispatcher sqlDispatcher = selectSqlDispatcher(autoCommit, sqlType);
                DispatcherResult dispatcherResult = getExecutionMetaData(originalSql, Collections
                    .emptyList(), null, sqlDispatcher);
                targets = dispatcherResult.getTarget();
                virtualTableName = dispatcherResult.getVirtualTableName();
                virtualJoinTableNames = dispatcherResult.getVirtualJoinTableNames();
            }
            for (TargetDB target : targets) {
                //这里做了新旧规则兼容
                String targetName = ruleController != null ? target.getWritePool()[0] : target
                    .getDbIndex();
                if (!targetSqls.containsKey(targetName)) {
                    targetSqls.put(targetName, new ArrayList<String>());
                }

                List<String> sqls = targetSqls.get(targetName);

                Set<String> actualTables = target.getTableNames();
                for (String tab : actualTables) {
                    String targetSql = replaceTableName(originalSql, virtualTableName, tab);
                    //如果metaData(也就是DispatcherResult)里面有join表名，那么就替换掉;
                    targetSql = replaceJoinTableName(virtualTableName, virtualJoinTableNames, tab,
                        targetSql);
                    sqls.add(targetSql);
                }
            }
        } catch (ZdalCheckedExcption e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * @param virtualTableName
     * @param virtualJoinTableNames
     * @param realTableName
     * @param targetSql
     * @return
     */
    private String replaceJoinTableName(String virtualTableName,
                                        List<String> virtualJoinTableNames, String realTableName,
                                        String targetSql) {
        if (virtualJoinTableNames.size() > 0) {
            String suffix = getSuffix(realTableName, virtualTableName);
            for (String vtab : virtualJoinTableNames) {
                //真实表名可以用,指定
                String repTab = vtab + suffix;
                String[] tabs = vtab.split(",");
                if (tabs.length == 2) {
                    vtab = tabs[0];
                    repTab = tabs[1];
                }
                targetSql = replaceTableName(targetSql, vtab, repTab);
            }
        }
        return targetSql;
    }

    public int[] executeBatch() throws SQLException {
        checkClosed();

        if (batchedArgs == null || batchedArgs.isEmpty()) {
            return new int[0];
        }

        List<SQLException> exceptions = null;

        try {
            Map<String/*数据源ID*/, List<String>/*数据源上执行的SQL*/> targetSqls = new HashMap<String, List<String>>();

            for (Object arg : batchedArgs) {
                if (this.dbConfigType == DataSourceConfigType.GROUP) {
                    sortBatch0((String) arg, targetSqls);
                } else {
                    sortBatch((String) arg, targetSqls);
                }

            }

            //Map<String, ConnectionAndDatasource> connections = getConnectionProxy().getActualConnections();

            for (Entry<String, List<String>> entry : targetSqls.entrySet()) {
                //如果没取到数据源
                String dbSelectorID = entry.getKey();
                //校验是否允许batch事务
                checkBatchDataBaseID(dbSelectorID);
                //retryContext为null的时候会直接抛出异常。
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

        ExceptionUtils.throwSQLException(exceptions, null, Collections.emptyList());

        // TODO: 忽略返回值
        return new int[0];
    }

    public void clearBatch() throws SQLException {
        checkClosed();

        if (batchedArgs != null) {
            batchedArgs.clear();
        }
    }

    public ResultSet getResultSet() throws SQLException {
        return results;
    }

    /**
     * 不支持多结果集查询，总是返回false
     */
    public boolean getMoreResults() throws SQLException {
        return moreResults;
    }

    public boolean getMoreResults(int current) throws SQLException {
        throw new UnsupportedOperationException("getMoreResults");
    }

    public int getUpdateCount() throws SQLException {
        return updateCount;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        throw new UnsupportedOperationException("getGeneratedKeys");
    }

    public void cancel() throws SQLException {
        throw new UnsupportedOperationException("cancel");
    }

    protected void checkClosed() throws SQLException {
        if (closed) {
            throw new SQLException("No operations allowed after statement closed.");
        }
    }

    public void closeInternal(boolean removeThis) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke close");
        }

        if (closed) {
            return;
        }

        List<SQLException> exceptions = null;

        try {
            for (ResultSet resultSet : openResultSets) {
                try {
                    //bug fix by shenxun :内部不让他remove,在TStatment中统一clear掉他们
                    ((DummyTResultSet) resultSet).closeInternal(false);
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }

            for (Statement stmt : actualStatements) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }
        } finally {
            closed = true;
            openResultSets.clear();
            actualStatements.clear();

            results = null;
            if (removeThis) {
                if (!getConnectionProxy().getOpenStatements().remove(this)) {
                    log.warn("open statement does not exist");
                }
            }
        }

        ExceptionUtils.throwSQLException(exceptions, null, Collections.emptyList());
    }

    public void close() throws SQLException {
        closeInternal(true);
    }

    /**
     * 在batch的事务里，每次都要检查是否同一个数据源标识,只进行逻辑库的判定
     * added by fanzeng，以支持batch的单库事务
     * @param dbSelectorID  逻辑数据源标识
     * @throws SQLException
     */
    public void checkBatchDataBaseID(String dbSelectorID) throws SQLException {
        if (StringUtil.isBlank(dbSelectorID)) {
            throw new SQLException("The dbSelectorID can't be null!");
        }
        //如果在事务中，第一次就设置batchDataBaseId的值,然后直接返回
        if (!isAutoCommit() && getBatchDataBaseId() == null) {
            setBatchDataBaseId(dbSelectorID);
            return;
        }
        //如果在事务中，并且当前的dbId和缓存的dbId不同，即抛出异常；         
        if (!isAutoCommit() && !dbSelectorID.equals(getBatchDataBaseId())) {
            throw new SQLException("batch操作只支持单库的事务,当前dbSelectorID=" + dbSelectorID + ",缓存的dbId="
                                   + getBatchDataBaseId());
        }
    }

    /**
     * 以下为不支持的方法
     */
    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException("getFetchDirection");
    }

    public void setFetchDirection(int fetchDirection) throws SQLException {
        throw new UnsupportedOperationException("setFetchDirection");
    }

    public int getFetchSize() throws SQLException {
        return this.fetchSize;
        //throw new UnsupportedOperationException("getFetchSize");
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        this.fetchSize = fetchSize;
        //throw new UnsupportedOperationException("setFetchSize");
    }

    public int getMaxFieldSize() throws SQLException {
        throw new UnsupportedOperationException("getMaxFieldSize");
    }

    public void setMaxFieldSize(int maxFieldSize) throws SQLException {
        throw new UnsupportedOperationException("setMaxFieldSize");
    }

    public int getMaxRows() throws SQLException {
        throw new UnsupportedOperationException("getMaxRows");
    }

    public void setMaxRows(int maxRows) throws SQLException {
        throw new UnsupportedOperationException("setMaxRows");
    }

    public int getQueryTimeout() throws SQLException {
        throw new UnsupportedOperationException("getQueryTimeout");
    }

    public void setQueryTimeout(int queryTimeout) throws SQLException {
        throw new UnsupportedOperationException("setQueryTimeout");
    }

    public void setCursorName(String cursorName) throws SQLException {
        throw new UnsupportedOperationException("setCursorName");
    }

    public void setEscapeProcessing(boolean escapeProcessing) throws SQLException {
        throw new UnsupportedOperationException("setEscapeProcessing");
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    /**
     * 以下为无逻辑的getter/setter
     */
    public int getResultSetType() throws SQLException {
        return resultSetType;
    }

    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
    }

    public int getResultSetConcurrency() throws SQLException {
        return resultSetConcurrency;
    }

    public void setResultSetConcurrency(int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    public int getResultSetHoldability() throws SQLException {
        return resultSetHoldability;
    }

    public void setResultSetHoldability(int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
    }

    public Map<String, DBSelector> getDataSourcePool() {
        return dbSelectors;
    }

    public void setDataSourcePool(Map<String, DBSelector> dbSelectors) {
        this.dbSelectors = dbSelectors;
    }

    public ZdalConnection getConnectionProxy() {
        return connectionProxy;
    }

    public void setConnectionProxy(ZdalConnection connectionProxy) {
        this.connectionProxy = connectionProxy;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Set<ResultSet> getTResultSets() {
        return openResultSets;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public long getTimeoutForEachTable() {
        return timeoutForEachTable;
    }

    public void setTimeoutForEachTable(long timeoutForEachTable) {
        this.timeoutForEachTable = timeoutForEachTable;
    }

    public int getRetryingTimes() {
        return retryingTimes;
    }

    public void setRetryingTimes(int retryingTimes) {
        this.retryingTimes = retryingTimes;
    }

    public void setOperation_type(DB_OPERATION_TYPE operation_type) {
        this.operation_type = operation_type;
    }

    public DB_OPERATION_TYPE getOperation_type() {
        return operation_type;
    }

    public String getBatchDataBaseId() {
        return batchDataBaseId;
    }

    public void setBatchDataBaseId(String batchDataBaseId) {
        this.batchDataBaseId = batchDataBaseId;
    }

    public boolean isHintReplaceSupport() {
        return isHintReplaceSupport;
    }

    public void setHintReplaceSupport(boolean isHintReplaceSupport) {
        this.isHintReplaceSupport = isHintReplaceSupport;
    }

    public DataSourceConfigType getDbConfigType() {
        return dbConfigType;
    }

    public void setDbConfigType(DataSourceConfigType dbConfigType) {
        this.dbConfigType = dbConfigType;
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

    public String getAppDsName() {
        return appDsName;
    }

    public void setAppDsName(String appDsName) {
        this.appDsName = appDsName;
    }

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
