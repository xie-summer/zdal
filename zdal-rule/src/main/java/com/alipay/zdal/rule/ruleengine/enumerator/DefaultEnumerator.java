/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.enumerator;

import java.util.Set;

import com.alipay.zdal.common.sqljep.function.Comparative;

/**
 * 如果不能进行枚举，那么就是用默认的枚举器
 * 默认枚举器只支持comparativeOr条件，以及等于的关系。不支持大于小于等一系列关系。
 * 
 *
 */
public class DefaultEnumerator implements CloseIntervalFieldsEnumeratorHandler {

    public void mergeFeildOfDefinitionInCloseInterval(Comparative from, Comparative to,
                                                      Set<Object> retValue,
                                                      Integer cumulativeTimes,
                                                      Comparable<?> atomIncrValue) {
        throw new IllegalArgumentException("默认枚举器不支持穷举");

    }

    public void processAllPassableFields(Comparative source, Set<Object> retValue,
                                         Integer cumulativeTimes, Comparable<?> atomIncrValue) {
        throw new IllegalStateException("在没有提供步长和叠加次数的前提下，不能够根据当前范围条件选出"
                                        + "对应的定义域的枚举值，sql中不要出现> < >= <=");
    }
}
