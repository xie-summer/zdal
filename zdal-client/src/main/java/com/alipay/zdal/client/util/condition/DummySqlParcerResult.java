/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.parser.visitor.OrderByEle;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: DummySqlParcerResult.java, v 0.1 2014-1-6 ÏÂÎç05:16:27 Exp $
 */
public class DummySqlParcerResult implements SqlParserResult {

    final ComparativeMapChoicer choicer;
    final String                logicTableName;
    List<OrderByEle>            orderBys          = Collections.emptyList();
    List<OrderByEle>            groupBys          = Collections.emptyList();
    int                         max               = DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    int                         skip              = DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    GroupFunctionType           groupFunctionType = GroupFunctionType.NORMAL;

    public DummySqlParcerResult(ComparativeMapChoicer choicer, String logicTableName) {
        this.choicer = choicer;
        this.logicTableName = logicTableName;
    }

    public DummySqlParcerResult(SimpleCondition simpleCondition) {
        this.logicTableName = simpleCondition.getVirtualTableName();
        this.choicer = simpleCondition;
        this.orderBys = simpleCondition.getOrderBys();
        this.groupBys = simpleCondition.getGroupBys();
        this.max = simpleCondition.getMax();
        this.skip = simpleCondition.getSkip();
        this.groupFunctionType = simpleCondition.getGroupFunctionType();
    }

    public ComparativeMapChoicer getComparativeMapChoicer() {
        return choicer;
    }

    public GroupFunctionType getGroupFuncType() {
        return groupFunctionType;
    }

    public int getMax(List<Object> param) {
        return max;
    }

    public List<OrderByEle> getOrderByEles() {
        return orderBys;
    }

    public int getSkip(List<Object> param) {
        return skip;
    }

    public String getTableName() {
        return logicTableName;
    }

    public List<OrderByEle> getGroupByEles() {
        return groupBys;
    }

    public boolean isDML() {
        return false;
    }

    @Override
    public void getSqlReadyToRun(Set<String> tables, List<Object> args, Number skip, Number max,
                                 Map<Integer, Object> modifiedMap) {
    }

    @Override
    public int isRowCountBind() {
        return -1;
    }

    @Override
    public int isSkipBind() {
        return -1;
    }

}
