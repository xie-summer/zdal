/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

/**
 * 规则所需要的每一个参数所拥有的一些基本属性，包含枚举器所需要的一些信息
 * 
 *
 */
public class AdvancedParameter {
    /**
     * sql中的列名，必须是小写，这里在setter显示的设置成小写了
     */
    public String        key;
    /**
     * 自增，给枚举器用的
     */
    public Comparable<?> atomicIncreateValue;
    /**
     * 叠加次数，给枚举器用的
     */
    public Integer       cumulativeTimes;

    /**
     * 决定当前参数是否允许范围查询如>= <= ...
     */
    public boolean       needMergeValueInCloseInterval;

    public Integer getCumulativeTimes() {
        return cumulativeTimes;
    }

    public void setCumulativeTimes(Integer cumulativeTimes) {
        this.cumulativeTimes = cumulativeTimes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key.toLowerCase();
    }

    public Comparable<?> getAtomicIncreateValue() {
        return atomicIncreateValue;
    }

    public void setAtomicIncreateValue(Comparable<?> atomicIncreateValue) {
        this.atomicIncreateValue = atomicIncreateValue;
    }

    public boolean isNeedMergeValueInCloseInterval() {
        return needMergeValueInCloseInterval;
    }

    @Override
    public String toString() {
        return "AdvancedParameter [atomicIncreateValue=" + atomicIncreateValue
               + ", cumulativeTimes=" + cumulativeTimes + ", key=" + key
               + ", needMergeValueInCloseInterval=" + needMergeValueInCloseInterval + "]";
    }

}
