/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher;

import java.util.List;

import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

/**
 *
 * 执行结果返回.
 */
public interface Result {
    /**
     * 获取虚拟表名
     * @return
     */
    public String getVirtualTableName();

    /** 
     * 获取目标库和目标表的列表
     */
    public List<TargetDB> getTarget();

    /**
     * 获取当前sql的select | columns | from
     * 中columns的类型
     * 如果为max min count等，那么类型会有相应变化
     * 同时如果group function和其他列名字段混用，则这里会返回NORMAL
     * @return
     */
    public GroupFunctionType getGroupFunctionType();

    /**
     * 获取max值
     * 
     * @return max值，对应oracle里面rownum<= ? 或者rownum < ? .mysql对应Limint m,n里面的m+n，默认值是{@link DMLCommon.DEFAULT_SKIP_MAX}
     */
    public int getMax();

    /**
     * 获取skip值。
     * 
     * return skip值，对应oracle里面rownum>= ? 或者rownum > ? .mysql对应Limint m,n里面的m，默认值是{@link DMLCommon.DEFAULT_SKIP_MAX}
     * 
     */
    public int getSkip();
}
