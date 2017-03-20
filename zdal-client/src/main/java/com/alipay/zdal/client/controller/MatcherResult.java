/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.List;
import java.util.Map;

import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

/**
 * 匹配的结果对象，供给给Controller进行返回对象的拼装
 * 
 * 
 * 这些是纯粹的从匹配中可以获得的数据 主要是应该走哪些库哪些表，是否反向输出，分库分表参数
 * 
 *
 */
public interface MatcherResult {
    /**
     * 规则计算后的结果对象
     * @return
     */
    List<TargetDB> getCalculationResult();

    /**
     * 匹配的库参数是什么,不会出现Null值
     * @return
     */
    Map<String, Comparative> getDatabaseComparativeMap();

    /**
     * 匹配的表参数是什么,不会出现null值
     * @return
     */
    Map<String, Comparative> getTableComparativeMap();
}
