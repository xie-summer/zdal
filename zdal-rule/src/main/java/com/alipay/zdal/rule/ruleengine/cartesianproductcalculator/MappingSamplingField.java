/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.cartesianproductcalculator;

import java.util.List;

/**
 * 添加了映射附带的字段，
 * 
 *
 */
public abstract class MappingSamplingField extends SamplingField {

    public MappingSamplingField(List<String> columns, int capacity) {
        super(columns, capacity);
    }

}
