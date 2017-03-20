/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

import java.util.Map;
import java.util.Set;

import com.alipay.zdal.common.sqljep.function.Comparative;

public abstract class ListAbstractResultRule extends AbstractRule {
    /**
     * 用作分库
     * @param sharedValueElementMap
     * @return 返回的map不会为null,但有可能为空的map，如果map不为空，则内部的子map必定不为空。最少会有一个值
     */
    public abstract Map<String/*column*/, Field> eval(
                                                       Map<String, Comparative> sharedValueElementMap);

    /**
     * 用作分表。不带有对计算出当前值的函数的源的追踪信息
     * 
     * @param enumeratedMap 列名->枚举 对应表
     * @param mappingTargetColumn 映射规则列
     * @param mappingKeys 映射规则值
     * 
     * @return 结果集字段，不会为空 如果子类方法设置了在set为空时抛异常，则会自动抛出
     */
    public abstract Set<String> evalWithoutSourceTrace(Map<String, Set<Object>> enumeratedMap,
                                                       String mappingTargetColumn,
                                                       Set<Object> mappingKeys);

}
