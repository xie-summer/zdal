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

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.common.OperationDBType;

/**
 * 只有一个数据源的DBSelector
 * //private String id, 作为dbSelector的id和db的dsKey
 */
public class OneDBSelector extends AbstractDBSelector {
    private static final Logger logger              = Logger.getLogger(OneDBSelector.class);

    private DataSource          db;

    private static final String indexSuffix         = "_0";
    public static final int     DEFAULT_WEIGHT_INIT = 10;
    private static volatile int OneDbRunTimeWeight  = DEFAULT_WEIGHT_INIT;

    public OneDBSelector(String id, DataSource dataSource) {
        super(id);
        this.db = dataSource;
    }

    public DataSource select() {
        return db;
    }

    /*public DBSelector copyAndRemove(DataSource removedDataSource) {
    	return null;
    }*/

    public <T> T tryExecute(Map<DataSource, SQLException> failedDataSources,
                            DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            Object... args) throws SQLException {
        List<SQLException> exceptions;
        if (failedDataSources != null && failedDataSources.containsKey(db)) {
            //throw new SQLException("没有可用的数据源了");
            exceptions = new ArrayList<SQLException>(failedDataSources.size());
            exceptions.addAll(failedDataSources.values());
            return tryer.onSQLException(exceptions, this.exceptionSorter, args);
        }
        try {
            if (OneDbRunTimeWeight <= 0) {
                throw new SQLException("Mark_down or readonly!");
            }
            //获取到db的名字，然后缓存起来，业务会用到该名字
            Map<String, DataSource> map = new HashMap<String, DataSource>();
            //加上前缀，by冰魂 20130903
            map.put(getAppDsName() + "." + getId(), null);
            ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);

            return tryer.tryOnDataSource(db, getId(), args);
        } catch (SQLException e) {
            exceptions = new ArrayList<SQLException>(1);
            exceptions.add(e);
        }
        return tryer.onSQLException(exceptions, this.exceptionSorter, args);
    }

    public <T> T tryExecute(DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            Object... args) throws SQLException {
        return this.tryExecute(null, tryer, times, null, args);
    }

    public void setWeight(Map<String, Integer> weightMap) {
        // 为了可以动态的停掉某个逻辑库，加入了以下代码 
        //从父类得到id
        String id = getId();
        String id_db = id + indexSuffix;
        Integer weight = weightMap.get(id_db);
        if (weight != null) {
            logger.warn("Change OneDbRunTimeWeight from " + OneDbRunTimeWeight + "to " + weight);
            OneDbRunTimeWeight = weight;
            logger.warn(" Chage the OneDbRunTimeWeight success!");
        } else {
            logger.warn("We Can't find weight by id :" + id_db);
            return;
        }
    }

    public boolean isSupportRetry(OperationDBType type) {
        return false;
    }

    public DataSource getDb() {
        return db;
    }

    public void setDb(DataSource db) {
        this.db = db;
    }
}
