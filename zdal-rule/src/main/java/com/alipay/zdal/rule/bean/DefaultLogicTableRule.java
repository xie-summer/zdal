/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.rule.LogicTableRule;
import com.alipay.zdal.rule.ruleengine.entities.abstractentities.RuleChain;
import com.alipay.zdal.rule.ruleengine.entities.inputvalue.CalculationContextInternal;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

/**
 * 默认的LogicTableRule计算,
 * 
 * 
 */
public class DefaultLogicTableRule implements LogicTableRule, Cloneable {
    private final String defaultTable;
    private final String databases;

    public DefaultLogicTableRule(String databases, String defaultTable) {
        this.databases = databases;
        this.defaultTable = defaultTable;
    }

    //	/**
    //	 * 包内可见，外部不允许设置这个属性
    //	 * @param defaultTable
    //	 */
    //	void setDefaultTableInternal(String defaultTable) {
    //		if(this.defaultTable != null){
    //			throw new IllegalArgumentException("should not be here ,default table 有值? 内部持有的值"+this.defaultTable
    //					+"，输入的值"+defaultTable);
    //		}
    //		this.defaultTable = defaultTable;
    //	}

    public String getDatabases() {
        return databases;
    }

    //	public void setDatabases(String databases) {
    //		if (databases == null || databases.length() == 0) {
    //			throw new IllegalArgumentException("database is null");
    //		}
    //		if (databases.contains(",")) {
    //			throw new IllegalArgumentException("不支持使用多个数据源作为默认数据源");
    //		}
    //		this.databases = databases;
    //	}

    public List<TargetDB> calculate(Map<RuleChain, CalculationContextInternal> map) {
        List<TargetDB> targetDBs = new ArrayList<TargetDB>(0);

        TargetDB targetDB = new TargetDB();
        targetDB.setDbIndex(databases);
        Set<String> tableNames = new HashSet<String>(1);
        tableNames.add(defaultTable);
        targetDB.setTableNames(tableNames);
        targetDBs.add(targetDB);
        return targetDBs;
    }

    public Set<RuleChain> getRuleChainSet() {
        return Collections.emptySet();
    }

    public List<String> getUniqueColumns() {
        return Collections.emptyList();
    }

    public boolean isAllowReverseOutput() {
        return false;
    }

    public boolean isNeedRowCopy() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if (databases == null) {
            throw new IllegalArgumentException("databases == null || defaultTable == null");
        }
        return super.clone();
    }

}
