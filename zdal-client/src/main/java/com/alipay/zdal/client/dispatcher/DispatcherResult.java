/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher;

import java.util.List;

import com.alipay.zdal.client.controller.ColumnMetaData;
import com.alipay.zdal.client.controller.OrderByMessages;

/**
 * 考虑到如果要支持两种实现，前端用起来会比较麻烦。因此订立公共的接口
 * 内部实现不需要care.
 * 
 *TODO:没有group by element暴露，不过问题不大\
 */
public interface DispatcherResult extends Result {

    /**
     * 是否允许反向输出
     * 
     * @return
     */
    public boolean allowReverseOutput();

    /**
     * 获取分库键，如果需要行复制的话
     * 需要注意的是
     * primaryKey和分库键和分表键之间的列是不会重复的。
     * 如果primaryKey和分表中出现的，那么分库中就不会出现
     * 如果分库中和primaryKey中出现的，那么分表就不会出现
     * @return
     */
    public List<ColumnMetaData> getSplitDB();

    /**
     * 获取分表键，如果需要行复制的话
     * primaryKey和分库键和分表键之间的列是不会重复的。
     * 如果primaryKey和分表中出现的，那么分库中就不会出现
     * 如果分库中和primaryKey中出现的，那么分表就不会出现
     * @return
     */
    public List<ColumnMetaData> getSplitTab();

    /**
     * 获取主键，如果需要行复制的话
     * primaryKey和分库键和分表键之间的列是不会重复的。
     * 如果primaryKey和分表中出现的，那么分库中就不会出现
     * 如果分库中和primaryKey中出现的，那么分表就不会出现
     * @return
     */
    public ColumnMetaData getPrimaryKey();

    /**
     * 获取order by 信息
     * @return
     */
    public OrderByMessages getOrderByMessages();

    /**
     * 获取skip值
     * 因为Zdal不是数据库，所以这里做了一个假定：
     * 在所有含义为skip的数据中，最大的那个永远是有意义的。
     * 
     * 多层嵌套中也是如此。
     * @return
     */
    public int getSkip();

    /**
     * 获取max值。
     * 因为Zdal不是数据库，所以这里做了一个假定：
     * 在所有含义为max的数据中，最大的那个永远是有意义的。
     * 
     * 多层嵌套中也是如此。
     * @return
     */
    public int getMax();

    /**
     * 允许反向输出
     * @param needReverseOutput
     */
    public void needAllowReverseOutput(boolean needReverseOutput);

    /**
     * 获取数据库执行计划
     * @return
     */
    public EXECUTE_PLAN getDatabaseExecutePlan();

    /**
     * 设置
     * @param executePlan
     */
    public void setDatabaseExecutePlan(EXECUTE_PLAN executePlan);

    public EXECUTE_PLAN getTableExecutePlan();

    public void setTableExecutePlan(EXECUTE_PLAN executePlan);

    /**
     *专给DiskWriter用的
     */
    //	List<SetElement> getSetElements();

    /**
     * 记录被join的虚拟表名
     * @author jiangping
     * @param virtualJoinTableNames
     */
    public void setVirtualJoinTableNames(List<String> virtualJoinTableNames);

    /**
     * 获取被join的虚拟表名
     * @author jiangping
     * @return
     */
    public List<String> getVirtualJoinTableNames();

}
