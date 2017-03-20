/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.rule.ruleengine.entities.abstractentities.RuleChain;
import com.alipay.zdal.rule.ruleengine.entities.inputvalue.CalculationContextInternal;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

public interface LogicTableRule {
    Set<RuleChain> getRuleChainSet();

    boolean isAllowReverseOutput();

    /**
     * 不同的节点领走自己的结果，并根据结果进行1对多映射
     * @param map
     * @return
     */
    public List<TargetDB> calculate(Map<RuleChain, CalculationContextInternal> map);

    public List<String> getUniqueColumns();
}
