/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.parser.visitor.OrderByEle;

/**
 * 
 * @author 伯牙
 * @version $Id: SimpleCondition.java, v 0.1 2014-1-6 下午05:16:45 Exp $
 */
public class SimpleCondition implements RuleRouteCondition, ComparativeMapChoicer {

    private String                   virtualTableName;
    private DBType                   dbType;
    public static final int          EQ                = Comparative.Equivalent;
    private Map<String, Comparative> parameters        = new HashMap<String, Comparative>();

    private List<OrderByEle>         orderBys          = Collections.emptyList();
    private List<OrderByEle>         groupBys          = Collections.emptyList();
    private int                      max               = DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    private int                      skip              = DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    GroupFunctionType                groupFunctionType = GroupFunctionType.NORMAL;

    public String getVirtualTableName() {
        return virtualTableName;
    }

    public SqlParserResult getSqlParserResult() {
        //return new DummySqlParcerResult(this, virtualTableName);

        return new DummySqlParcerResult(this);
    }

    //	public MatcherResult match(LogicTableRule rule)
    //			throws TDLCheckedExcption {
    //		long currentTime = System.currentTimeMillis();
    //		Set<RuleChain> ruleChains = rule.getRuleChain();
    //		for(RuleChain ruleChain:ruleChains){
    //			
    //		}
    //		TargetDBMetaData targetDBMetaData = provider.getDBAndTabs(virtualTableName, parameters);
    //		long elapsedTime = System.currentTimeMillis();
    //		StringBuilder key2Builder = new StringBuilder();
    //		key2Builder.append("DIRECT_DB|").append(virtualTableName).append("|")
    //		.append("SimpleCondition").append("|").append(virtualTableName);
    //		add(buildTableKey1(virtualTableName), buildExecuteSqlKey2(key2Builder.toString()), KEY3_GET_DB_AND_TABLES,
    //				currentTime - elapsedTime, currentTime - elapsedTime);
    //		return targetDBMetaData;
    //	}
    public ComparativeMapChoicer getCompMapChoicer() {
        return this;
    }

    public Map<String, Comparative> getColumnsMap(List<Object> arguments, Set<String> partnationSet) {
        Map<String, Comparative> retMap = new HashMap<String, Comparative>(parameters.size());
        for (String str : partnationSet) {
            if (str != null) {
                //因为groovy是大小写敏感的，因此这里只是在匹配的时候转为小写，放入map中的时候仍然使用原来的大小写
                Comparative comp = parameters.get(str.toLowerCase());
                if (comp != null) {
                    retMap.put(str, comp);
                }
            }
        }
        return retMap;
    }

    public static Comparative getComparative(int i, Comparable<?> c) {
        return new Comparative(i, c);
    }

    /**
     * 设置虚拟表名
     * 
     * @param virtualTableName
     *            虚拟表名
     */
    public void setVirtualTableName(String virtualTableName) {
        if (virtualTableName == null) {
            throw new IllegalArgumentException("请输入逻辑表名");
        }
        this.virtualTableName = virtualTableName.toLowerCase();
    }

    public Map<String, Comparative> getParameters() {
        return parameters;
    }

    /**
     * 添加一个默认为=的参数对
     * 
     * @param str
     *            参数项名字
     * @param comp
     *            参数项值，一般为基本类型或可比较类型
     */
    public void put(String key, Comparable<?> parameter) {
        if (key == null) {
            throw new IllegalArgumentException("key为null");
        }
        if (parameter instanceof Comparative) {
            parameters.put(key.toLowerCase(), (Comparative) parameter);
        } else if (parameter instanceof Comparable<?>) {
            parameters.put(key.toLowerCase(), getComparative(EQ, parameter));
        }

    }

    public DBType getDBType() {
        return dbType;
    }

    public void setDBType(DBType dbType) {
        this.dbType = dbType;
    }

    public List<OrderByEle> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderByEle> orderBys) {
        this.orderBys = orderBys;
    }

    public List<OrderByEle> getGroupBys() {
        return groupBys;
    }

    public void setGroupBys(List<OrderByEle> groupBys) {
        this.groupBys = groupBys;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public GroupFunctionType getGroupFunctionType() {
        return groupFunctionType;
    }

    public void setGroupFunctionType(GroupFunctionType groupFunctionType) {
        this.groupFunctionType = groupFunctionType;
    }

}
