/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.common.OperationDBType;

public class RetryableTPreparedStatement extends ZdalPreparedStatement {
    private static final Logger logger = Logger.getLogger(RetryableTPreparedStatement.class);

    public RetryableTPreparedStatement(SqlDispatcher writeDispatcher, SqlDispatcher readDispatcher) {
        super(writeDispatcher, readDispatcher);
    }

    /*	
     * 规范没 说允许update时运行select语句。
     * @Override
    	protected int executeUpdateAndCountAffectRows(String dbSelectorId,
    			String targetSql, RetringContext retringContext,
    			Connection connection, int rows) throws SQLException {
    		try{
    		return super.executeUpdateAndCountAffectRows(dbSelectorId, targetSql,
    				retringContext, connection, rows);
    		}catch (SQLException e) {
    			retringContext.addSQLException(e);
    			validRetryable(retringContext, e);
    			connection = tryToConnectToOtherAvaluableDataSource(dbSelectorId,
    					retringContext, e);
    			return executeUpdateAndCountAffectRows(dbSelectorId, targetSql, retringContext, connection, rows);
    		}
    	}*/
    @Override
    protected void executeQueryAndAddIntoCollection(
                                                    String dbSelectorId,
                                                    String targetSql,
                                                    Map<DataSource, SQLException> failedDataSources,
                                                    Connection connection,
                                                    List<ResultSet> actualResultSets)
                                                                                     throws SQLException {
        try {
            super.executeQueryAndAddIntoCollection(dbSelectorId, targetSql, failedDataSources,
                connection, actualResultSets);
        } catch (SQLException e) {
            logger.warn("zdal欲进入读重试状态，重复执行executeQueryAndAddIntoCollection()方法, targetSql="
                        + targetSql, e);
            validRetryable(dbSelectorId, e, OperationDBType.readFromDb);
            if (failedDataSources != null) {
                ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
                    .getConnectionAndDatasourceByDBSelectorID(dbSelectorId);
                failedDataSources.put(connectionAndDatasource.parentDataSource, e);
            } else {
                //当在事务中时不需要重试，failedDataSources为null 值，直接将异常抛出
                //added by fanzeng.
                logger.warn("事务中failedDataSources=null, zdal并未进入读重试状态,targetSql=" + targetSql);
                throw e;
            }
            //为什么不现在判断是否为非fatal后抛出，而要退后，这样影响阅读和理解
            connection = tryToConnectToOtherAvailableDataSource(dbSelectorId, failedDataSources);
            executeQueryAndAddIntoCollection(dbSelectorId, targetSql, failedDataSources,
                connection, actualResultSets);
        }

    }

    /**
     * 写库重试
     */
    @Override
    protected int executeUpdateAndCountAffectRows(String dbSelectorId, String targetSql,
                                                  Map<DataSource, SQLException> failedDataSources,
                                                  Connection connection, int rows)
                                                                                  throws SQLException {
        try {
            rows = super.executeUpdateAndCountAffectRows(dbSelectorId, targetSql,
                failedDataSources, connection, rows);
        } catch (SQLException e) {
            //            logger.error("sql执行失败,targetSql=" + targetSql, e);
            throw e;
            /* validRetryable(dbSelectorId, e, operationDBType.writeIntoDb);
             if (failedDataSources != null) {
                 ConnectionAndDatasource connectionAndDatasource = getConnectionProxy()
                     .getConnectionAndDatasourceByDBSelectorID(dbSelectorId);
                 failedDataSources.put(connectionAndDatasource.parentDataSource, e);
             } else {
                 //在事务中不需要重试，failedDataSources为null值，直接将异常抛出
                 //added by fanzeng.
                 logger.warn("事务中failedDataSources=null， zdal并未进入写重试状态,targetSql=" + targetSql);
                 throw e;
             }
             //为什么不现在判断是否为非fatal后抛出，而要退后，这样影响阅读和理解
             connection = tryToConnectToOtherAvailableDataSource(dbSelectorId, failedDataSources);
             rows = executeUpdateAndCountAffectRows(dbSelectorId, targetSql, failedDataSources,
                 connection, rows);*/
        }
        return rows;
    }

}
