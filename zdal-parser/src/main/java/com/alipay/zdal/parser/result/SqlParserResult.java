/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.result;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.parser.visitor.OrderByEle;
/**
 * sql-parser解析后的结果.
 * @author xiaoqing.zhouxq
 * @version $Id: SqlParserResult.java, v 0.1 2012-5-21 下午03:29:18 xiaoqing.zhouxq Exp $
 */
public interface SqlParserResult {

    /**
     * 获取当前表名,如果sql中包含多张表，默认只返回第一张表.
     * @return
     */
    String getTableName();

    /**
     * 获取order by 的信息
     * @return
     */
    List<OrderByEle> getOrderByEles();

    /**
     * 获取group by 信息
     * @return
     */
    List<OrderByEle> getGroupByEles();

    /**
     * insert/update/delete/select.
     * @return
     */
    boolean isDML();

    /**
     * 获取sql的SKIP值如果有的话，没有的情况下会返回DEFAULT值
     * @param arguments 参数值列表.
     * @return
     */
    int getSkip(List<Object> arguments);

    /**
     * 返回skip绑定变量的下标,如果没有就返回-1.
     * @return
     */
    int isSkipBind();

    /**
     * 获取sql的max值如果有的话，没有的话会返回DEFAULT值
     * @param arguments 参数值列表.
     * @return
     */
    int getMax(List<Object> arguments);

    /**
     * 返回rowCount绑定变量的下标,如果没有就返回-1.
     * @return
     */
    int isRowCountBind();

    /**
     * 或许当前sql的最外层的group function.如果有且仅有一个group function,那么使用该function
     * 如果没有group function或者有多个group function.则返回NORMAL
     * 
     * @return
     */
    GroupFunctionType getGroupFuncType();

    /**
     * 反向输出的接口
     * @param tables
     * @param args
     * @param skip
     * @param max
     * @param outputType
     * @param modifiedMap
     * @return
     */
    void getSqlReadyToRun(Set<String> tables, List<Object> args, Number skip,
                                               Number max, 
                                               Map<Integer, Object> modifiedMap);
    


    /**
     * 获取结果集筛选器
    * @return
    */
    ComparativeMapChoicer getComparativeMapChoicer();
    
    
}
