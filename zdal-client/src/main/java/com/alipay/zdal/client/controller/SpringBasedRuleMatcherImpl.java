/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.client.dispatcher.Matcher;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.rule.LogicTableRule;
import com.alipay.zdal.rule.ruleengine.entities.abstractentities.RuleChain;
import com.alipay.zdal.rule.ruleengine.entities.inputvalue.CalculationContextInternal;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public class SpringBasedRuleMatcherImpl implements Matcher {

    public MatcherResult match(ComparativeMapChoicer comparativeMapChoicer, List<Object> args,
                               LogicTableRule rule) throws ZdalCheckedExcption {
        //规则链儿集合，包含了规则中所有规则链
        Set<RuleChain> ruleChainSet = rule.getRuleChainSet();
        //符合要求的数据库分库字段和对应的值，如果有多个那么都会放在一起
        Map<String, Comparative> comparativeMapDatabase = new HashMap<String, Comparative>(2);
        //符合要求的talbe分表字段和对应的值，如果有多个那么都会放在一起
        Map<String, Comparative> comparativeTable = new HashMap<String, Comparative>(2);

        Map<RuleChain, CalculationContextInternal/*待计算的结果*/> resultMap = new HashMap<RuleChain, CalculationContextInternal>(
            ruleChainSet.size());

        for (RuleChain ruleChain : ruleChainSet) {

            // 针对每一个规则链
            List<Set<String>/*每一条规则需要的参数*/> requiredArgumentSortByLevel = ruleChain
                .getRequiredArgumentSortByLevel();
            /*
             * 因为ruleChain本身的个数是一定的，个数与getRequiredArgumentSortByLevel list的size一样多，因此不会越界
             */
            int index = 0;

            for (Set<String> oneLevelArgument : requiredArgumentSortByLevel) {
                // 针对每一个规则链中的一个级别，级别是从低到高的首先查看是否满足规则要求，如果满足则进行运算
                /*当前参数要求的列名*/
                Map<String, Comparative> sqlArgs = comparativeMapChoicer.getColumnsMap(args,
                    oneLevelArgument);
                if (sqlArgs.size() == oneLevelArgument.size()) {
                    // 表示匹配,规则链作为key,value为结果
                    resultMap.put(ruleChain, new CalculationContextInternal(ruleChain, index,
                        sqlArgs));
                    if (ruleChain.isDatabaseRuleChain()) {
                        comparativeMapDatabase.putAll(sqlArgs);
                    } else {
                        // isTableRuleChain
                        comparativeTable.putAll(sqlArgs);
                    }
                    break;
                } else {
                    index++;
                }

            }
            /*如果一个规则链在sql中都没有被匹配到，那么这个规则链就不会出现在resultMap中，这样当一个一对多节点
             * 的规则在sql中没法进行计算的时候，会拿到一个空的List index.这时候就会使用默认规则或null来完成下述计算。
            */
        }
        //not null.确保在每次规则结束后，清空
        List<TargetDB> calc = rule.calculate(resultMap);
        return buildMatcherResult(comparativeMapDatabase, comparativeTable, calc);

    }

    public MatcherResult buildMatcherResult(Map<String, Comparative> comparativeMapDatabase,
                                            Map<String, Comparative> comparativeTable,
                                            List<TargetDB> targetDB) {
        MatcherResultImp result = new MatcherResultImp();
        result.setCalculationResult(targetDB);
        result.setDatabaseComparativeMap(comparativeMapDatabase);
        result.setTableComparativeMap(comparativeTable);
        return result;
    }

}
