/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.common.OperationDBType;

/**
 * 
 * @author 伯牙
 * @version $Id: RetryableTStatement.java, v 0.1 2014-1-6 下午01:49:21 Exp $
 */
public class RetryableTStatement extends ZdalStatement {
    private static final Logger logger                  = Logger
                                                            .getLogger(RetryableTStatement.class);
    public boolean              redirectToWriteDatabase = false;

    public RetryableTStatement(SqlDispatcher writeDispatcher, SqlDispatcher readDispatcher) {
        super(writeDispatcher, readDispatcher);
    }

    //	@Override
    //	/*
    //	 * TEST required :测试1次重试，两次重试，尤其走到43行后递归调用中抛出异常的情况
    //	 * 
    //	 * 方法会保证dbIndex->Connection+datasource+dbselector map中的Datasource必定有值
    //	 */
    //	protected void createConnection(DBSelector dbselector, String dbIndex,
    //			RetringContext retringContext) throws SQLException {
    //
    //		try {
    //			// 建立连接并放到TConnection 的map dbSelectorID->connection+datasource+dbSelector中
    //			super.createConnection(dbselector, dbIndex, retringContext);
    //
    //		} catch (RetrySQLException e) {
    //
    //			retringContext.addSQLException(e.getSqlException());
    //
    //			validRetryable(retringContext, e.getSqlException());
    //			
    //			dbselector = removeCurrentDataSourceFromDbSelector( dbselector, e.getCurrentDataSource(),
    //					retringContext);
    //			
    //			// 从重用连接池中移走有问题的链接,如果不移除则循环嵌套时会因为能够取到ConnectionAndDataSource对象而不将有问题的链接剔除掉。
    //			getConnectionProxy().removeConnectionAndDatasourceByID(dbIndex);
    //			// 如果remove失败或为写库请求，已经直接抛出了 因此这里都是成功的
    //			createConnection(dbselector, dbIndex, retringContext);
    //		}
    //	}

    //	protected boolean reachMaxRetryableTimes(RetringContext retringContext) {
    //		return retringContext.getAlreadyRetringTimes() > retringTimes;
    //	}

    @Override
    protected Statement createStatementInternal(Connection connection, String dbIndex,
                                                Map<DataSource, SQLException> failedDataSources)
                                                                                                throws SQLException {
        try {
            return super.createStatementInternal(connection, dbIndex, failedDataSources);
        } catch (SQLException e) {
            //retringContext.addSQLException(e);
            //validRetryable(retringContext, e);
            //added by fanzeng, 需要进一步验证此处是否需要重试
            validRetryable(dbIndex, e, OperationDBType.readFromDb);
            if (failedDataSources != null) {
                ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
                    .getConnectionAndDatasourceByDBSelectorID(dbIndex);
                failedDataSources.put(connectionAndDatasource.parentDataSource, e);
            } else {
                //当在事务中时不需要重试，failedDataSources为null值，直接将异常抛出
                //added by fanzeng.

                throw e;
            }
            connection = tryToConnectToOtherAvailableDataSource(dbIndex, failedDataSources);
            return createStatementInternal(connection, dbIndex, failedDataSources);
        }
    }

    @Override
    protected void queryAndAddResultToCollection(String dbSelectorId,
                                                 List<ResultSet> actualResultSets,
                                                 SqlAndTable targetSql, Statement stmt,
                                                 Map<DataSource, SQLException> failedDataSources)
                                                                                                 throws SQLException {
        try {
            super.queryAndAddResultToCollection(dbSelectorId, actualResultSets, targetSql, stmt,
                failedDataSources);
        } catch (SQLException e) {
            //retringContext.addSQLException(e);
            //validRetryable(retringContext,e);
            validRetryable(dbSelectorId, e, OperationDBType.readFromDb);
            if (failedDataSources != null) {
                ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
                    .getConnectionAndDatasourceByDBSelectorID(dbSelectorId);
                failedDataSources.put(connectionAndDatasource.parentDataSource, e);
            } else {
                //当在事务中时不需要重试，failedDataSources为null值，直接将异常抛出
                //added by fanzeng.
                throw e;
            }
            Connection connection = tryToConnectToOtherAvailableDataSource(dbSelectorId,
                failedDataSources);
            stmt = createStatementInternal(connection, dbSelectorId, failedDataSources);
            queryAndAddResultToCollection(dbSelectorId, actualResultSets, targetSql, stmt,
                failedDataSources);
        }
    }

    /**
     * 1.先从parent TConnection对象中获取connectionsMap.<br>
     * 2.根据DBSelectorId找到对应的connection信息。<br>
     * 3.移除connectionsMap中有问题的Datasource.<br>
     * 4.移除dbSelector中发生问题的datasource .(复制dbSelector一次，然后移除).<br>
     * 5.根据dbSelectorId和新的dbSelector.从新按照权重选择一个datasource.<br>
     * 6.获取连接，放入connectionsMap.<br>
     * 7.返回连接<br>
     * 
     * @param dbIndex
     * @param retringContext
     * @param e
     * @return
     * @throws SQLException
     */
    protected Connection tryToConnectToOtherAvailableDataSource(
                                                                String dbIndex,
                                                                Map<DataSource, SQLException> failedDataSources)
                                                                                                                throws SQLException {
        Connection connection;
        //从TConnection的connectionsMap获取所有connection

        ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
            .getConnectionAndDatasourceByDBSelectorID(dbIndex);
        DBSelector dbSelector = connectionAndDatasource.dbSelector;
        //DataSource ds = connectionAndDatasource.parentDataSource;
        //从dbSelector中移除当前有问题的datasource
        //dbSelector = removeCurrentDataSourceFromDbSelector(dbSelector, ds,
        //		retringContext);
        //从父类中移除
        getConnectionProxy().removeConnectionAndDatasourceByID(dbIndex);
        // 重新建立连接, exclueDataSource = connectionAndDatasource.parentDataSource
        createConnection(dbSelector, dbIndex, failedDataSources);
        connection = getConnectionProxy().getConnectionAndDatasourceByDBSelectorID(dbIndex).connection;
        return connection;
    }

    /**
     * dbselector不支持重试直接抛异常
     * @param dbSelectorId
     * @param currentException
     * @throws SQLException
     */
    protected void validRetryable(String dbSelectorId, SQLException currentException,
                                  OperationDBType type) throws SQLException {
        ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
            .getConnectionAndDatasourceByDBSelectorID(dbSelectorId);

        if (!connectionAndDatasource.dbSelector.isSupportRetry(type)) {
            logger.warn("不允许进行重试，dbSelectorId=" + dbSelectorId + ",type=" + type, currentException);
            throw currentException;
        }
    }

    /**
     * 从候选selector中移除当前datasource.
     * 
     * 为了保证不修改dbSelector里面selector里面原有的数据状态。每次移除时都会先复制一个 独立的dbSelector出来进行处理。
     * 
     * 如果已经没有备选库或为一个写库 则直接抛出异常
     * 
     * @param exceptions
     * @return
     * @throws SQLException
     */
    /*public DBSelector removeCurrentDataSourceFromDbSelector(
    		 DBSelector dbSelector,
    		DataSource currentDataSource, RetringContext retringContext)
    		throws SQLException {
    	dbSelector = dbSelector.copyAndRemove(currentDataSource);
    	if (dbSelector == null) {
    		// 表示没有多余的库可供选择
    		ExceptionUtils.throwSQLException(retringContext.getSqlExceptions(),
    				"getConnection", Collections.emptyList());
    	} else {
    		retringContext.addRetringTimes();
    	}
    	return dbSelector;
    }*/

    //	/**
    //	 * 验证是否需要重试
    //	 * 
    //	 * @param retringContext
    //	 * @param currentException
    //	 * @throws SQLException
    //	 */
    //	protected void validRetryable(RetringContext retringContext,
    //			SQLException currentException) throws SQLException {
    //		if(retringContext == null){
    //			ExceptionUtils.throwSQLException(currentException,
    //					"getConnection", Collections.emptyList());
    //		}
    //		if (!retringContext.isExceptionFatal(currentException)) {
    //			ExceptionUtils.throwSQLException(retringContext.getSqlExceptions(),
    //					"getConnection", Collections.emptyList());
    //		}
    //		if (!retringContext.isNeedRetry()) {
    //			// 如果是写请求,那么不需要重试，直接抛出
    //			ExceptionUtils.throwSQLException(retringContext.getSqlExceptions(),
    //					"getConnection", Collections.emptyList());
    //		}
    //		// 如果重试次数大于限制，直接抛错出去
    //		if (reachMaxRetryableTimes(retringContext)) {
    //			ExceptionUtils.throwSQLException(retringContext.getSqlExceptions(),
    //					"getConnection", Collections.emptyList());
    //		}
    //	}

}
