/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.dbselector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.common.OperationDBType;
import com.alipay.zdal.common.WeightRandom;
import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;

/**
 * 按优先级选择的selector
 * 
 * 每次选择只从优先级最高的一组DB中选择，若都不可用，才继续在下一个优先级的DB组中选择
 * 
 * 优先级相同的DB还用随机选择
 * 
 * 原始需求：TC要求在每个dbgroup中优先读备库，当备库不可用时，自动读主库 
 * 扩展需求：一主多备，优先随机读备库。当备库都不可用时，才读主库
 * 
 * 为了方便处理和接口一致，有如下要求： 
 * 1. 目前只支持读分优先级组 
 * 2. 一个权重推送的信息中，。。。 
 * 3. 一个数据源只能在一个优先级组中？
 * 
 * 
 */
public class PriorityDbGroupSelector extends AbstractDBSelector {
    private static final Logger         logger = Logger.getLogger(PriorityDbGroupSelector.class);

    /**
     * 按优先级顺序存放数据库组。元素0优先级最高。每个EquityDbManager元素代表具有相同优先级的一组数据库
     */
    //private EquityDbManager[] priorityGroups;

    private PriorityGroupsDataSources[] priorityGroupsDataSourceHolder;

    public PriorityDbGroupSelector(String id, EquityDbManager[] priorityGroups) {
        super(id);
        // this.priorityGroups = priorityGroups;
        if (priorityGroupsDataSourceHolder == null) {
            priorityGroupsDataSourceHolder = new PriorityGroupsDataSources[priorityGroups.length];
        }
        for (int i = 0; i < priorityGroups.length; i++) {
            this.priorityGroupsDataSourceHolder[i] = new PriorityGroupsDataSources(
                priorityGroups[i]);
        }

        if (priorityGroupsDataSourceHolder == null || priorityGroupsDataSourceHolder.length == 0) {
            throw new IllegalArgumentException("PriorityGroupsDataSourceHolder is null or empty!");
        }
    }

    public DataSource select() {
        for (int i = 0; i < priorityGroupsDataSourceHolder.length; i++) {
            DataSource ds = getEquityDbManager(i).select();
            if (ds != null) {
                return ds;
            }
        }
        return null;
    }

    /**
     * 取每个级别的weightKey和总的weightKey的交集，挨个设置
     */
    public void setWeight(Map<String, Integer> weightMap) {
        for (int i = 0; i < priorityGroupsDataSourceHolder.length; i++) {
            Map<String, Integer> oldWeights = getEquityDbManager(i).getWeights();
            Map<String, Integer> newWeights = new HashMap<String, Integer>(oldWeights.size());
            for (Map.Entry<String, Integer> e : weightMap.entrySet()) {
                if (oldWeights.containsKey(e.getKey())) {
                    newWeights.put(e.getKey(), e.getValue());
                }
            }
            getEquityDbManager(i).setWeightRandom(new WeightRandom(newWeights));
        }
    }

    private static class DataSourceTryerWrapper<T> implements DataSourceTryer<T> {
        private final List<SQLException> historyExceptions;
        private final DataSourceTryer<T> tryer;

        public DataSourceTryerWrapper(DataSourceTryer<T> tryer, List<SQLException> historyExceptions) {
            this.tryer = tryer;
            this.historyExceptions = historyExceptions;
        }

        public T onSQLException(List<SQLException> exceptions, ExceptionSorter exceptionSorter,
                                Object... args) throws SQLException {
            Exception last = exceptions.get(exceptions.size() - 1);
            if (last instanceof NoMoreDataSourceException) {
                if (exceptions.size() > 1) {
                    exceptions.remove(exceptions.size() - 1);
                }
                historyExceptions.addAll(exceptions);
                throw (NoMoreDataSourceException) last;
            } else {
                return tryer.onSQLException(exceptions, exceptionSorter, args);
            }
        }

        public T tryOnDataSource(DataSource ds, String name, Object... args) throws SQLException {
            return tryer.tryOnDataSource(ds, name, args);
        }
    };

    /**
     * 基于EquityDbManager的tryExecute实现，对用户的tryer做一个包装，在wrapperTryer.onSQLException中
     * 检测到最后一个e是NoMoreDataSourceException时，不调原tryer的onSQLException, 转而重试其他优先级的
     */
    public <T> T tryExecute(Map<DataSource, SQLException> failedDataSources,
                            DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            Object... args) throws SQLException {
        final List<SQLException> historyExceptions = new ArrayList<SQLException>(0);
        DataSourceTryer<T> wrapperTryer = new DataSourceTryerWrapper<T>(tryer, historyExceptions);

        for (int i = 0; i < priorityGroupsDataSourceHolder.length; i++) {
            try {
                return priorityGroupsDataSourceHolder[i].tryExecute(failedDataSources,
                    wrapperTryer, times, operationType, i, args);
            } catch (NoMoreDataSourceException e) {
                logger.warn("NoMoreDataSource for retry for priority group " + i);
            }
        }
        //所有的优先级组都不可用，则抛出异常
        return tryer.onSQLException(historyExceptions, exceptionSorter, args);
    }

    //默认读写库都可以进行重试
    public boolean isSupportRetry(OperationDBType type) {
        return true;
    }

    public EquityDbManager[] getPriorityGroups() {
        EquityDbManager[] priorityGroups = new EquityDbManager[priorityGroupsDataSourceHolder.length];
        for (int i = 0; i < priorityGroupsDataSourceHolder.length; i++) {
            priorityGroups[i] = getEquityDbManager(i);
        }
        return priorityGroups;
    }

    private EquityDbManager getEquityDbManager(int i) {
        if (priorityGroupsDataSourceHolder[i] == null
            || priorityGroupsDataSourceHolder[i].getEquityDbManager() == null) {
            throw new IllegalArgumentException(
                "The priorityGroupsDataSourceHolder or equityDbManager can't be null!");
        }
        return priorityGroupsDataSourceHolder[i].getEquityDbManager();
    }
}
