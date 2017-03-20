/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.client.dispatcher.DispatcherResult;
import com.alipay.zdal.client.dispatcher.EXECUTE_PLAN;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDBMetaData;

/**
 * 分库分表源信息
 * 拉平这个事情在以后重构controller的时候再去做，现在为了保证兼容老实现并且改动尽量小，先放弃该改动
 * 
 * @author shenxun
 *
 */
public class TargetDBMeta implements DispatcherResult {
    private final TargetDBMetaData     dbMeta;
    /**
     * max值，如果sql中给定了limit m,n,或rownum<xx 则max值会随之变化为应用中的值
     * 需要注意的是，max本身是limitTo的含义，因此其实最后都会变为 xxx<max这样的语义
     * <p>对于oracle: rownum<=n max=n+1</p>
     * <p>对于mysql: limit m,n max=m+n</p>
     */
    private final int                  max;
    /**
     * skip值，如果sql中给定了limit m,n,或rownum>xx 则skip值会随之变化为应用中的值
     * 需要注意的是，skip本身是limitFrom的含义，因此其实最后都会变为 xxx>=m这样的语义
     * <p>对于oracle: rownum>n skip=n+1</p>
     * <p>对于mysql: limit m,n skip=m</p>
     */
    private final int                  skip;
    /**
     * sql 中的order by 信息
     */
    private final OrderByMessages      orderByMessages;
    /**
     * 在sql中最外层嵌套的select中的columns里面的group function信息。
     * 若该处有group function,则parser会对其进行判断，确保只有一个group function，没有其他列。如果有则抛出异常
     * 若经检查除了group function以外没有其他列存在，则会返回该group function对应的Type
     * 如果没有group function或者是其他类型的sql(insert update等)。则返回normal.
     */
    private final GroupFunctionType    groupFunctionType;
    /**
     * 主键，因为分库键本身是不允许多个的，所以是个ColumnMetaData对象.里面如果在xml中配置了表规则中的parameters项
     * ，则每一个用','分隔的项目都对应list中的一项。ColumnMetaData中的key对应了parameters里每一个用','分隔的项目
     * 而value对应已经通过计算并且绑定了变量以后的值，这个值允许为null,为null则表示用户没有在sql中给出对应 的参数。
     */
    private ColumnMetaData             primaryKey;
    /**
     * 分库键列表，因为分库键本身是允许多个的，所以是个list.里面如果在xml中配置了parameters项，则每一个
     * 用','分隔的项目都对应list中的一项。ColumnMetaData中的key对应了parameters里每一个用','分隔的项目
     * 而value对应已经通过计算并且绑定了变量以后的值，这个值允许为null,为null则表示用户没有在sql中给出对应 的参数。
     */
    private final List<ColumnMetaData> splitDB               = new ArrayList<ColumnMetaData>();
    /**
     * 分表键，因为分库键本身是不允许多个的，所以是个ColumnMetaData对象.里面如果在xml中配置了表规则中的parameters项
     * ，则每一个用','分隔的项目都对应list中的一项。ColumnMetaData中的key对应了parameters里每一个用','分隔的项目
     * 而value对应已经通过计算并且绑定了变量以后的值，这个值允许为null,为null则表示用户没有在sql中给出对应 的参数。
     */
    private final List<ColumnMetaData> splitTab              = new ArrayList<ColumnMetaData>();

    /** 被join的虚拟表名 */
    private final List<String>         virtualJoinTableNames = new ArrayList<String>(0);

    public TargetDBMeta(TargetDBMetaData dbMeta, int skip, int max,
                        OrderByMessages orderByMessages, GroupFunctionType groupFunctionType) {
        this.dbMeta = dbMeta;
        this.skip = skip;
        this.max = max;
        this.orderByMessages = orderByMessages;
        this.groupFunctionType = groupFunctionType;
    }

    public int getMax() {
        return max;
    }

    public int getSkip() {
        return skip;
    }

    public OrderByMessages getOrderByMessages() {
        return orderByMessages;
    }

    public String getVirtualTableName() {
        return dbMeta.getVirtualTableName();
    }

    public ColumnMetaData getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnMetaData primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnMetaData> getSplitDB() {
        return splitDB;
    }

    public void addSplitDB(ColumnMetaData splitDB) {
        this.splitDB.add(splitDB);
    }

    public List<ColumnMetaData> getSplitTab() {
        return this.splitTab;
    }

    public void addSplitTab(ColumnMetaData splitTab) {
        this.splitTab.add(splitTab);
    }

    public boolean allowReverseOutput() {
        return dbMeta.allowReverseOutput();
    }

    public void needAllowReverseOutput(boolean reverse) {
        dbMeta.needAllowReverseOutput(reverse);
    }

    public GroupFunctionType getGroupFunctionType() {
        return groupFunctionType;
    }

    public List<TargetDB> getTarget() {
        return dbMeta.getTarget();
    }

    public EXECUTE_PLAN getDatabaseExecutePlan() {
        throw new IllegalStateException("not support yet");
    }

    public EXECUTE_PLAN getTableExecutePlan() {
        throw new IllegalStateException("not support yet");
    }

    public void setDatabaseExecutePlan(EXECUTE_PLAN executePlan) {
        throw new IllegalStateException("not support yet");
    }

    public void setTableExecutePlan(EXECUTE_PLAN executePlan) {
        throw new IllegalStateException("not support yet");
    }

    public boolean mappingRuleReturnNullValue() {
        return false;
    }

    public List<String> getVirtualJoinTableNames() {
        return virtualJoinTableNames;
    }

    public void setVirtualJoinTableNames(List<String> virtualJoinTableNames) {
        this.virtualJoinTableNames.addAll(virtualJoinTableNames);
    }

}
