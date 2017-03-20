/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher;

import java.util.List;

import com.alipay.zdal.client.RouteCondition;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;

/**
 * 数据源和对应列表的选择器，可以通过sql和arg获取执行目标
 * 也可以通过rc获取，同时还可以通过这个接口获得所有的数据库和表
 * 
 * Result结构和内部实现无关，业务方可以进行修改 不会影响到Zdal内部实现。
 * 
 *
 */
public interface DatabaseChoicer {
    /**
     * 获取当前数据库和表。
     * @param sql
     * @param args
     * @return
     * @throws ZdalCheckedExcption
     */
    Result getDBAndTables(String sql, List<Object> args) throws ZdalCheckedExcption;

    /**
     * 不解析SQL，由ThreadLocal传入的指定对象（RouteCondition），决定库表目的地的接口
     * @param rc
     * @return
     */
    Result getDBAndTables(RouteCondition rc);

    /**
     * 获取全库全表信息
     * @param logicTableName
     * @return
     */
    Result getAllDatabasesAndTables(String logicTableName);
}
