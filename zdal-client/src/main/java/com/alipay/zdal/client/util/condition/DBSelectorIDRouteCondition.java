/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author 伯牙
 * @version $Id: DBSelectorIDRouteCondition.java, v 0.1 2014-1-6 下午05:16:07 Exp $
 */
public class DBSelectorIDRouteCondition implements DBSelectorRouteCondition {
    final String       dbSelectorID;
    final List<String> tableList = new ArrayList<String>();
    final String       logicTableName;
    volatile boolean   used;

    /**
     * 逻辑表名和目标表名完全一致的简化方法
     * 
     * @param logicTableName
     * @param dbSelectorID
     */
    public DBSelectorIDRouteCondition(String logicTableName, String dbSelectorID) {
        this(logicTableName, dbSelectorID, logicTableName);
    }

    /**
     * 建立一个直接通过逻辑表名，数据库执行id和实际表名，执行SQL的RouteCondition
     * 
     * @param logicTableName
     * @param dbSelectorID
     * @param tables
     */
    public DBSelectorIDRouteCondition(String logicTableName, String dbSelectorID, String... tables) {
        this.dbSelectorID = dbSelectorID;
        this.logicTableName = logicTableName;
        List<String> list = Arrays.asList(tables);
        tableList.addAll(list);
    }

    public String getDBSelectorID() {
        //貌似没有地方set used = true;
        if (!used) {
            return dbSelectorID;
        } else {
            throw new IllegalArgumentException("一个route对象只能被使用一次，请重新建立" + "route对象");
        }
    }

    public void addTable(String table) {
        tableList.add(table);
    }

    public void addAllTable(Collection<String> tables) {
        tableList.addAll(tables);
    }

    public void addAllTable(String[] tables) {
        tableList.addAll(Arrays.asList(tables));
    }

    public List<String> getTableList() {
        return tableList;
    }

    public String getVirtualTableName() {
        return logicTableName;
    }

}
