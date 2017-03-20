/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher.impl;

import java.util.List;

import com.alipay.zdal.client.dispatcher.Result;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public class DatabaseAndTablesDispatcherResultImp implements Result {
    final String         logicTableName;
    final List<TargetDB> target;

    public List<TargetDB> getTarget() {
        return target;
    }

    public DatabaseAndTablesDispatcherResultImp(List<TargetDB> target, String logicTableName) {
        this.target = (List<TargetDB>) target;
        this.logicTableName = logicTableName;
    }

    public String getLogicTableName() {
        return logicTableName;
    }

    public GroupFunctionType getGroupFunctionType() {
        return GroupFunctionType.NORMAL;
    }

    public String getVirtualTableName() {
        return logicTableName;
    }

    public int getMax() {
        return DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    }

    public int getSkip() {
        return DefaultSqlParserResult.DEFAULT_SKIP_MAX;
    }

}
