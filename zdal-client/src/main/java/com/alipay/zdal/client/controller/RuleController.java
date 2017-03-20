/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.SqlType;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.sqljep.function.Comparative;

public interface RuleController {

    /**
     * 根据sql获取分库分表信息，必须先设置ruleUrl以后才能正确使用 
     * @param sql
     * @param args
     * @return
     * @throws ZdalCheckedExcption
     */
    public abstract TargetDBMeta getDBAndTables(String sql, List<Object> args)
                                                                              throws ZdalCheckedExcption;

    public TargetDBMeta getDBAndTables(String sql, List<Object> args, boolean isMysqlSQL)
                                                                                         throws ZdalCheckedExcption;

    public DBType getDBType();

    //	public abstract List<Integer> getSplitDbArgsPositions(String sql,
    //			boolean isPK);

    /**
     * 直接指定分库分表列的方式来走规则引擎
     * @param virtualTableName
     * @param map
     * @param sqlType
     * @return
     * @throws ZdalCheckedExcption
     */
    public abstract TargetDBMeta getTargetDB(String virtualTableName, Map<String, Comparative> map,
                                             SqlType sqlType) throws ZdalCheckedExcption;

    /**
     *	直接指定分库分表
     * @param virtualTableName
     * @param ruleID
     * @param tables
     * @param sqlType
     * @return
     * @throws ZdalCheckedExcption
     */
    public TargetDBMeta getTargetDB(String virtualTableName, String ruleID, Set<String> tables,
                                    SqlType sqlType) throws ZdalCheckedExcption;

    public abstract String getGeneratorUrl();

    public abstract void setGeneratorUrl(String generatorUrl);

    public abstract String getRuleUrl();

    public abstract void setRuleUrl(String ruleUrl);
}