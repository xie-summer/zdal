/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine;

import java.util.Map;
import java.util.Set;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.PartitionElement;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDBMetaData;

public interface DBRuleProvider {
    /**
     * 获取目标数据库id和表名的整个数据结构。
     * 一个db数组，每个db包含多个表名。
     * @param virtualTabName 虚拟表名
     * @param comp where条件中每一个属性的范围条件。具体请参照{@link Main}中的实现方式
     * @param position 对where条件中的属性在comparable数组中位置的描述。
     * i.e:where条件中有三个条件：
     * comp[0]=gmt、comp[1]=id、comp[2]=route。
     * 那么这个参数应该为"gmt,id,route"
     * 
     * @return 目标数据库的相关属性List，List中的每一项对应一个分库规则，每一个分库
     * 规则中有零个或多个表名。不会为空
     * @throws ZdalCheckedExcption
     */
    public TargetDBMetaData getDBAndTabs(String virtualTabName, Map<String, Comparative> colMap)
                                                                                                throws ZdalCheckedExcption;

    public TargetDBMetaData getDBAndTabs(String virtualTableName, String databaseGroupsID,
                                         Set<String> tables) throws ZdalCheckedExcption;

    //	/**
    //	 * 获取分库列名
    //	 * @param virtualTabName 虚拟表名
    //	 *  @param isPK 如果需要获得pk则为true,否则为false
    //	 * @return  返回列名集
    //	 * 			返回empty set, if doesn't have split column
    //	 */
    //	public Set<String> getSplitDBColumns(String virtualTabName,boolean isPK);
    /**
     * 获取当前虚拟表的分库分表字段
     * @important 这个方法必须在getDb_type方法后调用
     * @param virtualTableName
     * @return
     */
    public PartitionElement getPartitionColumns(String virtualTableName);

    public TargetDBMetaData getDBAndTabs(String logicTableName, Map<String, Comparative> colMap,
                                         int databaseRuleIndex, int tableRuleIndex)
                                                                                   throws ZdalCheckedExcption;

    /**
     * 获取当前sql 的type.看是否是mysql还是oracle类型还是其他什么类型。
     * 为了性能考虑，这个方法必须优先于getPartitionColumns方法调用。因为会懒加载一次。
     * @return
     */
    public DBType getDBType();
}