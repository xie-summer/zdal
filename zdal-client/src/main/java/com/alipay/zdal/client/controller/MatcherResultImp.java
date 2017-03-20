/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.List;
import java.util.Map;

import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public class MatcherResultImp implements MatcherResult {
    List<TargetDB>           calculationResult;
    Map<String, Comparative> databaseComparativeMap;
    Map<String, Comparative> tableComparativeMap;

    public void setCalculationResult(List<TargetDB> calculationResult) {
        this.calculationResult = calculationResult;
    }

    public void setDatabaseComparativeMap(Map<String, Comparative> databaseComparativeMap) {
        this.databaseComparativeMap = databaseComparativeMap;
    }

    public void setTableComparativeMap(Map<String, Comparative> tableComparativeMap) {
        this.tableComparativeMap = tableComparativeMap;
    }

    public List<TargetDB> getCalculationResult() {
        return calculationResult;
    }

    public Map<String, Comparative> getDatabaseComparativeMap() {
        return databaseComparativeMap;
    }

    public Map<String, Comparative> getTableComparativeMap() {
        return tableComparativeMap;
    }

}
