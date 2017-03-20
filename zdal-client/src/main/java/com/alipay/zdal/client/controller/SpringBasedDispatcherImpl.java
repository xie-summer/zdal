/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.client.RouteCondition;
import com.alipay.zdal.client.dispatcher.DispatcherResult;
import com.alipay.zdal.client.dispatcher.EXECUTE_PLAN;
import com.alipay.zdal.client.dispatcher.Matcher;
import com.alipay.zdal.client.dispatcher.Result;
import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.client.dispatcher.impl.DatabaseAndTablesDispatcherResultImp;
import com.alipay.zdal.client.util.condition.DBSelectorRouteCondition;
import com.alipay.zdal.client.util.condition.DummySqlParcerResult;
import com.alipay.zdal.client.util.condition.JoinCondition;
import com.alipay.zdal.client.util.condition.RuleRouteCondition;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.output.OutputHandlerConsist;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.parser.visitor.OrderByEle;
import com.alipay.zdal.rule.LogicTableRule;
import com.alipay.zdal.rule.bean.LogicTable;
import com.alipay.zdal.rule.bean.ZdalRoot;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

/**
 * 主要负责root中拿到需要的信息。然后用matcher进行匹配。
 * 
 * 最后返回需要的结果
 * 
 * @author xiaoqing.zhouxq
 * 
 */
public class SpringBasedDispatcherImpl implements SqlDispatcher {
    /**
     * 需要注入的sql 解析器对象
     */
    private SQLParser                        parser  = null;

    /**
     * Zdal的根节点
     */
    ZdalRoot                                 root;

    private final Matcher                    matcher = new SpringBasedRuleMatcherImpl();

    /**
     * 反向输出的类型对象
     */
    public final static OutputHandlerConsist consist = new OutputHandlerConsist();

    /** 
     * @see com.alipay.zdal.client.dispatcher.SqlDispatcher#getDBAndTables(java.lang.String, java.util.List)
     */
    public DispatcherResult getDBAndTables(String sql, List<Object> args)
                                                                         throws ZdalCheckedExcption {

        DBType dbType = getDBType();
        SqlParserResult sqlParserResult = parser.parse(sql, dbType);
        // TODO: 反向查找，也就是先拿sql里面的一列表名，然后去规则里
        // 查看哪个表名match，如果有一个表示单表操作。
        // 如果有多个表示要join，join里有两种情况是可以支持但没有支持的
        String logicTableName = sqlParserResult.getTableName();
        int index = logicTableName.indexOf(".");
        if (index >= 0) {
            logicTableName = logicTableName.substring(index + 1);
        }
        // 从root对象中拿到逻辑表
        LogicTableRule rule = root.getLogicTableMap(logicTableName);
        if (rule == null) {
            throw new IllegalArgumentException("未能找到对应规则,逻辑表:" + logicTableName);
        }

        boolean isAllowReverseOutput = rule.isAllowReverseOutput();
        MatcherResult matcherResult = matcher.match(sqlParserResult.getComparativeMapChoicer(),
            args, rule);
        return getDispatcherResult(matcherResult, sqlParserResult, args, dbType, rule
            .getUniqueColumns(), isAllowReverseOutput, true);

    }

    //TODO:以后考虑加一个sql的状态，就表名这条sql的执行属性，这样就不用所有地方都搞一次了
    //	private boolean validIsSingleDBandSingleTable(List<TargetDB> targetDB){
    //		
    //	}
    /**
     * 根据匹配结果，进行最终给TStatement的结果的拼装,不同的matcher可以共用
     * 
     * @param matcherResult
     * @return
     */
    protected DispatcherResult getDispatcherResult(MatcherResult matcherResult,
                                                   SqlParserResult sqlParserResult,
                                                   List<Object> args, DBType dbType,
                                                   List<String> uniqueColumnSet,
                                                   boolean isAllowReverseOutput, boolean isSqlParser) {
        DispatcherResultImp dispatcherResult = getTargetDatabaseMetaDataBydatabaseGroups(
            matcherResult.getCalculationResult(), sqlParserResult, args, isAllowReverseOutput);

        //虽然判断sql输入输出的逻辑应该放到规则里，但因为觉得没必要在走了规则以后就放在TargetDBList里面多传递一次
        //在这里搞一次就可以了

        ControllerUtils.buildExecutePlan(dispatcherResult, matcherResult.getCalculationResult());

        validGroupByFunction(sqlParserResult, dispatcherResult);

        //TODO:reverseoutput也可以使用上面的结果
        if (isSqlParser) {
            //做过sql parse才有可能做反向输出
            ControllerUtils.buildReverseOutput(args, sqlParserResult, dispatcherResult.getMax(),
                dispatcherResult.getSkip(), dispatcherResult, DBType.MYSQL.equals(dbType));
        }

        return dispatcherResult;
    }

    /**
     * 如果有groupby函数并且执行计划不为单库单表或单库无表或无库无表
     * 则允许通过
     * @param sqlParserResult
     * @param dispatcherResult
     */
    protected void validGroupByFunction(SqlParserResult sqlParserResult,
                                        DispatcherResult dispatcherResult) {
        List<OrderByEle> groupByElement = sqlParserResult.getGroupByEles();
        if (groupByElement.size() != 0) {
            if (dispatcherResult.getDatabaseExecutePlan() == EXECUTE_PLAN.MULTIPLE) {
                throw new IllegalArgumentException("多库的情况下，不允许使用group by 函数");
            }
            if (dispatcherResult.getTableExecutePlan() == EXECUTE_PLAN.MULTIPLE) {
                throw new IllegalArgumentException("多表的情况下，不允许使用group by函数");
            }
        }
    }

    protected DispatcherResultImp getTargetDatabaseMetaDataBydatabaseGroups(
                                                                            List<TargetDB> targetDatabases,
                                                                            SqlParserResult sqlParserResult,
                                                                            List<Object> arguments,

                                                                            boolean isAllowReverseOutput) {
        // targetDatabase.set
        DispatcherResultImp dispatcherResultImp = new DispatcherResultImp(sqlParserResult
            .getTableName(), targetDatabases, isAllowReverseOutput, sqlParserResult
            .getSkip(arguments), sqlParserResult.getMax(arguments), new OrderByMessagesImp(
            sqlParserResult.getOrderByEles()), sqlParserResult.getGroupFuncType());
        return dispatcherResultImp;
    }

    @SuppressWarnings("unchecked")
    public DispatcherResult getDBAndTables(RouteCondition rc) {
        String logicTableName = rc.getVirtualTableName();
        List<String> uniqueColumns = Collections.emptyList();
        SqlParserResult sqlParserResult = null;
        if (rc instanceof RuleRouteCondition) {
            //需要模拟sqlparser走规则的 condition
            sqlParserResult = ((RuleRouteCondition) rc).getSqlParserResult();
            try {
                LogicTableRule rule = root.getLogicTableMap(logicTableName);
                uniqueColumns = rule.getUniqueColumns();
                MatcherResult matcherResult = matcher.match(sqlParserResult
                    .getComparativeMapChoicer(), null, rule);

                DispatcherResult result = getDispatcherResult(matcherResult, sqlParserResult,
                    Collections.emptyList(), null, rule.getUniqueColumns(), false, false);
                //如果是JoinCondition 那么要在DispatcherResult中添加join表的虚拟表名;
                if (rc instanceof JoinCondition) {
                    result
                        .setVirtualJoinTableNames(((JoinCondition) rc).getVirtualJoinTableNames());
                }

                return result;
            } catch (ZdalCheckedExcption e) {
                throw new RuntimeException(e);
            }
        } else if (rc instanceof DBSelectorRouteCondition) {
            final DBSelectorRouteCondition dBSelectorRouteCondition = (DBSelectorRouteCondition) rc;
            List<TargetDB> targetDBs = new ArrayList<TargetDB>(1);
            TargetDB targetDB = new TargetDB();
            targetDB.setDbIndex(dBSelectorRouteCondition.getDBSelectorID());
            Set<String> targetDBSet = new HashSet<String>();
            targetDBSet.addAll(dBSelectorRouteCondition.getTableList());
            targetDB.setTableNames(targetDBSet);
            targetDBs.add(targetDB);
            ComparativeMapChoicer choicer = new ComparativeMapChoicer() {

                public Map<String, Comparative> getColumnsMap(List<Object> arguments,
                                                              Set<String> partnationSet) {
                    return Collections.emptyMap();
                }
            };
            //建立伪装类
            sqlParserResult = new DummySqlParcerResult(choicer, logicTableName);
            MatcherResult matcherResult = matcher.buildMatcherResult(Collections.EMPTY_MAP,
                Collections.EMPTY_MAP, targetDBs);
            return getDispatcherResult(matcherResult, sqlParserResult, Collections.emptyList(),
                null, uniqueColumns, false, false);

        } else {
            throw new IllegalArgumentException("wrong RouteCondition type:"
                                               + rc.getClass().getName());
        }
    }

    public Result getAllDatabasesAndTables(String logicTableName) {
        LogicTable logicTable = root.getLogicTable((logicTableName.toLowerCase()));
        if (logicTable == null) {
            throw new IllegalArgumentException("逻辑表名未找到");
        }
        return new DatabaseAndTablesDispatcherResultImp(logicTable.getAllTargetDBList(),
            logicTableName);
    }

    /**
     * 无逻辑的getter/setter
     */
    public SQLParser getParser() {
        return parser;
    }

    public void setParser(SQLParser parser) {
        this.parser = parser;
    }

    public ZdalRoot getRoot() {
        return root;
    }

    public void setRoot(ZdalRoot root) {
        this.root = root;
    }

    public DBType getDBType() {
        return root.getDBType();
    }

}
