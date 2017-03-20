/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.client.dispatcher.DispatcherResult;
import com.alipay.zdal.client.dispatcher.EXECUTE_PLAN;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public class ControllerUtils {

    public static String toLowerCaseIgnoreNull(String tobeDone) {
        if (tobeDone != null) {
            return tobeDone.toLowerCase();
        }
        return null;
    }

    /**
     * 创建执行计划
     * 
     * 其中表的执行计划，如果有多个库里面的多个表的个数不同，那么按照表的数量最多的那个值为准。
     * 即：如db1~5，表的个数分别为0,0,0,0,1:那么返回的表执行计划为SINGLE
     * 若，表的个数分别为0,1,2,3,4,5：那么返回表的执行计划为MULTIPLE.
     * @param dispatcherResult
     * @param targetDBList
     */
    public static void buildExecutePlan(DispatcherResult dispatcherResult,
                                        List<TargetDB> targetDBList) {
        if (targetDBList == null) {
            throw new IllegalArgumentException("targetDBList is null");
        }
        int size = targetDBList.size();
        switch (size) {
            case 0:
                dispatcherResult.setDatabaseExecutePlan(EXECUTE_PLAN.NONE);
                dispatcherResult.setTableExecutePlan(EXECUTE_PLAN.NONE);
                break;
            case 1:
                TargetDB targetDB = targetDBList.get(0);
                Set<String> set = targetDB.getTableNames();
                dispatcherResult.setTableExecutePlan(buildTableExecutePlan(set, null));
                //如果表为none，那么库也为none.如果表不为none，那么库为single
                if (dispatcherResult.getTableExecutePlan() != EXECUTE_PLAN.NONE) {
                    dispatcherResult.setDatabaseExecutePlan(EXECUTE_PLAN.SINGLE);
                } else {
                    dispatcherResult.setDatabaseExecutePlan(EXECUTE_PLAN.NONE);
                }
                break;
            default:
                EXECUTE_PLAN currentExeutePlan = EXECUTE_PLAN.NONE;
                for (TargetDB oneDB : targetDBList) {
                    currentExeutePlan = buildTableExecutePlan(oneDB.getTableNames(),
                        currentExeutePlan);
                }
                dispatcherResult.setTableExecutePlan(currentExeutePlan);
                if (dispatcherResult.getTableExecutePlan() != EXECUTE_PLAN.NONE) {
                    dispatcherResult.setDatabaseExecutePlan(EXECUTE_PLAN.MULTIPLE);
                } else {
                    dispatcherResult.setDatabaseExecutePlan(EXECUTE_PLAN.NONE);
                }
                break;
        }

    }

    private static EXECUTE_PLAN buildTableExecutePlan(Set<String> tableSet,
                                                      EXECUTE_PLAN currentExecutePlan) {
        if (currentExecutePlan == null) {
            currentExecutePlan = EXECUTE_PLAN.NONE;
        }
        EXECUTE_PLAN tempExecutePlan = null;
        if (tableSet == null) {
            throw new IllegalStateException("targetTab is null");
        }
        int tableSize = tableSet.size();
        //不可能为负数
        switch (tableSize) {
            case 0:
                tempExecutePlan = EXECUTE_PLAN.NONE;
                break;
            case 1:
                tempExecutePlan = EXECUTE_PLAN.SINGLE;
                break;
            default:
                tempExecutePlan = EXECUTE_PLAN.MULTIPLE;
        }
        return tempExecutePlan.value() > currentExecutePlan.value() ? tempExecutePlan
            : currentExecutePlan;
    }

    /**
     * 创建反向输出相关的context，反向输出目前主要是解决以下问题
     * 
     * :1.如果sql中带有了符合表名替换pattern的字段，并且不想被替换掉。
     * 2.如果sql中包含了跨表的limit m,n的操作，
     * 
     * 其余的情况因为反向输出本身也会带来风险因此不进行反向。
     * @param args
     * @param dmlc
     * @param max
     * @param skip
     * @param retMeta
     * @param isMySQL
     * @param needRowCopy
     */
    public static void buildReverseOutput(List<Object> args, SqlParserResult dmlc, int max,
                                          int skip, DispatcherResult retMeta, boolean isMySQL) {
        boolean allowReverseOutput = retMeta.allowReverseOutput();
        List<TargetDB> targetdbs = retMeta.getTarget();
        for (TargetDB targetDB : targetdbs) {
            Set<String> tabs = targetDB.getTableNames();
            Map<Integer, Object> modifiedMap = new HashMap<Integer, Object>();
            // 如果目标数据库为一个则有可能是单库单表或单库多表
            if (targetdbs.size() == 1) {
                Set<String> temp_tabs = targetdbs.get(0).getTableNames();
                if (temp_tabs.size() == 1) {
                    if (allowReverseOutput) {
                        // 单表只需要改表名 //TODO
                        dmlc.getSqlReadyToRun(temp_tabs, args, skip, max, modifiedMap);
                    }
                } else {
                    mutiTableReverseOutput(args, dmlc, max, skip, retMeta, allowReverseOutput,
                        temp_tabs, modifiedMap);
                }
            } else {
                mutiTableReverseOutput(args, dmlc, max, skip, retMeta, allowReverseOutput, tabs,
                    modifiedMap);
            }
            if (retMeta.allowReverseOutput()) {
                targetDB.setChangedParams(modifiedMap);
            }
        }
    }

    /**
     * 多库单表或多库多表或单库多表，需要改表名和页数
     */
    private static void mutiTableReverseOutput(List<Object> args, SqlParserResult dmlc, int max,
                                               int skip, DispatcherResult retMeta,
                                               boolean allowReverseOutput,

                                               Set<String> tabs, Map<Integer, Object> modifiedMap) {
        if (allowReverseOutput) {
            //多库单表或多库多表，需要改表名和页数
            dmlc.getSqlReadyToRun(tabs, args, 0, max, modifiedMap);
        } else {
            if (skip != DefaultSqlParserResult.DEFAULT_SKIP_MAX
                && max != DefaultSqlParserResult.DEFAULT_SKIP_MAX) {
                // skip max非默认值的情况下，不需要行复制的情况下的反向输出
                //多库单表或多库多表或单库多表情况下，虽然不允许反向输出 但仍然要强制反向输出以支持该功能
                dmlc.getSqlReadyToRun(tabs, args, 0, max, modifiedMap);
                retMeta.needAllowReverseOutput(true);
            }
        }
    }

}
