/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.retvalue;

import java.util.List;

public class TargetDBMetaData {

    /**
     * 是否允许反向输出
     */
    private boolean              allowReverseOutput;
    /**
     * 目标库
     */
    private final List<TargetDB> target;

    /**
     * 虚拟表名
     */
    private final String         virtualTableName;

    public TargetDBMetaData(String virtualTableName, List<TargetDB> targetdbs,
                            boolean allowReverseOutput) {
        this.virtualTableName = virtualTableName;
        this.target = targetdbs;

        this.allowReverseOutput = allowReverseOutput;
    }

    public List<TargetDB> getTarget() {
        return target;
    }

    public String getVirtualTableName() {
        return virtualTableName;
    }

    public boolean allowReverseOutput() {
        return allowReverseOutput;
    }

    public void needAllowReverseOutput(boolean reverse) {
        this.allowReverseOutput = reverse;
    }
}
