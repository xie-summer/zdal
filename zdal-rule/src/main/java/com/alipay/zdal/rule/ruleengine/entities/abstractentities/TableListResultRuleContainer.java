/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.abstractentities;

import java.util.List;

import com.alipay.zdal.rule.ruleengine.rule.ListAbstractResultRule;

public interface TableListResultRuleContainer {
    /**
     * 将全局表规则设置给表规则容器。
     * 如果设置成功则返回true;
     * 如果设置失败则返回false;
     * 
     * @param listResultRule
     * @return
     */
    public boolean setTableListResultRule(List<ListAbstractResultRule> listResultRule);
}
