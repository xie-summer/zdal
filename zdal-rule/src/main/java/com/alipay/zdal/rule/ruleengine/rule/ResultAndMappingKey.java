/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

/**
 * 因为计算以后会拿到结果，对于映射规则则可以多拿到一个mappingKey
 * 
 *
 */
public class ResultAndMappingKey {
    public ResultAndMappingKey(String result) {
        this.result = result;
    }

    final String result;
    /**
     * 一个结果只能支持由一个mapping key映射产生，不支持多个。
     * 这个属性只有在mappingrule的时候才有用
     */
    Object       mappingKey;

    String       mappingTargetColumn;
}
