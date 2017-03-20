/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.abstractentities;

import java.util.List;
import java.util.Set;

import com.alipay.zdal.rule.ruleengine.rule.ListAbstractResultRule;

/**
 * 
 * 规则链抽象。
 * 
 * 需要注意的是如果规则链中的两个属性完全相同:--->他所持有的规则列表以及是否是数据库规则。
 * 那么我们就认为规则链完全相同，因为
 * 
 * 1.输入参数相同，sql是一样的，因此能够提供给规则进行运算的参数是一致的。
 * 2.规则一致 计算结果也一致，因此不需要进行多次计算。
 * 
 *
 */
public interface RuleChain {
    boolean isDatabaseRuleChain();

    //	/**
    //	 * 根据index和参数进行计算
    //	 * 
    //	 * @param index
    //	 * @param args
    //	 * @return 非null的List
    //	 */
    //	Map<String/*column*/,Map<String/*结果的值*/,Set<Object>/*得到该结果的描点值名*/>> calculate(int index,Map<String, Comparative> args);
    //	
    /**
     * 根据index获取对应规则
     * 
     * @param index 如果index为-1则返回null
     * @return
     */
    ListAbstractResultRule getRuleByIndex(int index);

    /**
     * 获取按照级别排序的参数列表
     * @return
     */
    List<Set<String>> getRequiredArgumentSortByLevel();

    /**
     * 获取规则链中的规则列表
     * @return
     */
    List<ListAbstractResultRule> getListResultRule();

    void init();

}
