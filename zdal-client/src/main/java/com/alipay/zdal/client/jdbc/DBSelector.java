/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.client.util.ExceptionUtils;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.OperationDBType;
import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;

/**
 * 对等数据库选择器。
 * 在数据完全相同的一组库中选择一个库
 * 用于对HA/RAC情况,多个读库中取一个读的操作
 * 
 */
public interface DBSelector {

    /**
     * @return 返回该Selector的标识
     */
    String getId();

    /**
     * added by fanzeng.
     * 在一组对等库中选择的库的名字.
     * 比如假设存在一组对等库，其Id为 dbs_w, 包括 dps_w_0, dps_w_1...多个真正的ds，
     * 仅仅知道了Id，只能知道是在哪一组dbSelector上执行的，
     * 但是业务方需要知道sql最终在哪个db上执行的
     */
    void setSelectedDSName(String name);

    /**
     * added by fanzeng.
     *在执行sql之前，获取之前设置的selectedDSName
     *然后放在threadLocal里让业务方来取 
     */
    String getSelectedDSName();

    /**
     * 对等数据库选择器。
     * 在数据完全相同的一组库中选择一个库
     * 用于对HA/RAC情况,多个读库中取一个读的操作
     */

    DataSource select();

    /**
     * 设置权重
     * @param weightMap 
     *    key为权重键，由使用者和具体实现决定
     *    value为权重值
     */
    void setWeight(Map<String, Integer> weightMap);

    /**
     * 设置数据库类型：目前只用来选择exceptionSorter 
     */
    void setDbType(DBType dbType);

    /**
     * 以选择到的DataSource和传入的args，重试执行
     *    tryer.tryOnDataSource(DataSource ds, Object... args), 每次选择DataSource会排除上次重试失败的
     * 直到达到指定的重试次数，或期间抛出非数据库不可用异常
     * 
     * 抛出异常后，以历次重试异常列表，和最初的args，调用
     *    tryer.onSQLException(List<SQLException> exceptions, Object... args)
     * 
     * @param tryer
     * @param times
     * @param args
     * @throws SQLException
     */
    <T> T tryExecute(DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                     Object... args) throws SQLException;

    /**
     * @param failedDataSources: 在调用该方法前，已经得知试过失败的DataSource和对应的SQLException
     * 存在这个参数的原因，是因为数据库操作割裂为getConnection/createStatement/execute几步，而并不是在一个大的try catch中
     * failedDataSources == null 表示不需要重试，遇到任何异常直接抛出。如在写库上的操作
     */
    <T> T tryExecute(Map<DataSource, SQLException> failedDataSources, DataSourceTryer<T> tryer,
                     int times, DB_OPERATION_TYPE operationType, Object... args)
                                                                                throws SQLException;

    /**
     * 是否支持重试
     * @return
     */
    boolean isSupportRetry(OperationDBType type);

    //	/**
    //	 * 将当前数据源数组拷贝一份，然后新建一个新的随机选择数组
    //	 * 
    //	 * 如果返回为DBSelector表示内部还有可以进行选择的数据源。
    //	 * 
    //	 * 而如果返回null 则表示内部没有可进行选择的数据源
    //	 * 
    //	 * @param removedDataSource
    //	 * @return
    //	 */
    //	DBSelector copyAndRemove(DataSource removedDataSource);

    /**
     * 在DBSelector管理的数据源上重试执行操作的回调接口
     */
    public static interface DataSourceTryer<T> {
        T tryOnDataSource(DataSource ds, String name, Object... args) throws SQLException;

        /**
         * tryExecute中重试调用tryOnDataSource遇到非数据库不可用异常，或用完重试次数时，会调用该方法
         * @param exceptions 历次重试失败抛出的异常。
         *    最后一个异常可能是数据库不可用的异常，也可能是普通的SQL异常
         *    最后一个之前的异常是数据库不可用的异常
         * @param exceptionSorter 当前用到的判断Exception类型的分类器
         * @param args 与tryOnDataSource时的args相同，都是用户调用tryExecute时传入的arg
         * @return 用户（实现者）觉得是否返回什么值
         * @throws SQLException
         */
        T onSQLException(List<SQLException> exceptions, ExceptionSorter exceptionSorter,
                         Object... args) throws SQLException;
    }

    /**
     * DataSourceTryer.onSQLException 直接抛出异常
     */
    public static abstract class AbstractDataSourceTryer<T> implements DataSourceTryer<T> {
        public T onSQLException(List<SQLException> exceptions, ExceptionSorter exceptionSorter,
                                Object... args) throws SQLException {
            ExceptionUtils.throwSQLException(exceptions, null, null);
            return null;
        }
    }

    /*public class RetringContext{
    	public Map<DataSource,SQLException> failed;
    	public boolean needRetry;
    }*/
}
