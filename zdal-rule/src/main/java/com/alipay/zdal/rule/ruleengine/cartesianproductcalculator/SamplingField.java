/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.cartesianproductcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 经过笛卡尔积以后的一组值，因为有多个参数，每个参数如果都是有范围的情况下，
 * 要覆盖所有情况只有进行笛卡尔积，枚举出所有可能的值，进行运算。
 * 这个就是枚举值中的一个。
 * columns是共享的列。无论samplingField复制几次，都会共享同一组列名。
 * 而enumFields则表示按照列名的顺序通过笛卡尔积的形式枚举出的一组值。
 * 
 *
 */
public class SamplingField {
    /**
     * 表示按照列名的顺序通过笛卡尔积的形式枚举出的一组值
     */
    final List<Object>         enumFields;

    /**
     * 一组列名
     */
    private final List<String> columns;

    private String             mappingTargetKey;

    private Object             mappingValue;
    final int                  capacity;

    public SamplingField(List<String> columns, int capacity) {
        this.enumFields = new ArrayList<Object>(capacity);
        this.capacity = capacity;
        this.columns = Collections.unmodifiableList(columns);
    }

    public void add(int index, Object value) {

        enumFields.add(index, value);
    }

    //	public final Object clone() throws CloneNotSupportedException {
    //		SamplingField samplingFiled = new SamplingField(columns,capacity);
    //		return samplingFiled;
    //	}

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getEnumFields() {
        return enumFields;
    }

    public void clear() {
        if (enumFields != null) {
            enumFields.clear();
        }
    }

    public String getMappingTargetKey() {
        return mappingTargetKey;
    }

    public void setMappingTargetKey(String mappingTargetKey) {
        this.mappingTargetKey = mappingTargetKey;
    }

    /*public void setEnumFields(List<Object> enumFields) {
    	this.enumFields = enumFields;
    }*/
    @Override
    public String toString() {
        return "columns:" + columns + "enumedFileds:" + enumFields;
    }

    public Object getMappingValue() {
        return mappingValue;
    }

    public void setMappingValue(Object mappingValue) {
        this.mappingValue = mappingValue;
    }

}
