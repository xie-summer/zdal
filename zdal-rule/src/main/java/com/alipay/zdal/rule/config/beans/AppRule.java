/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

/**
 * 一份完整的读写分离和分库分表规则配置
 * 一个业务一份
 *  
 */
public class AppRule {
    public static final String DBINDEX_SUFFIX_READ  = "_r";
    public static final String DBINDEX_SUFFIX_WRITE = "_w";
    private ShardRule          masterRule;
    private ShardRule          slaveRule;
    private ShardRule          readwriteRule;

    public void init() {
        if (readwriteRule == null) {
            return;
        }
        if (slaveRule != null || masterRule != null) {
            throw new IllegalArgumentException("readwriteRule 不能和masterRule或slaveRule同时配置");
        }
        try {
            masterRule = (ShardRule) readwriteRule.clone();
            slaveRule = (ShardRule) readwriteRule.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("should not happen!!", e);
        }
        addDbIndexSuffix(masterRule, DBINDEX_SUFFIX_WRITE);
        addDbIndexSuffix(slaveRule, DBINDEX_SUFFIX_READ);
    }

    private void addDbIndexSuffix(ShardRule shardRule, String suffix) {
        for (TableRule tbr : shardRule.getTableRules().values()) {
            String[] dbIndexes = tbr.getDbIndexArray();
            for (int i = 0; i < dbIndexes.length; i++) {
                dbIndexes[i] = dbIndexes[i] + suffix;
            }
        }
    }

    public ShardRule getMasterRule() {
        return masterRule;
    }

    public void setMasterRule(ShardRule masterRule) {
        this.masterRule = masterRule;
    }

    public ShardRule getSlaveRule() {
        return slaveRule;
    }

    public void setSlaveRule(ShardRule slaveRule) {
        this.slaveRule = slaveRule;
    }

    public ShardRule getReadwriteRule() {
        return readwriteRule;
    }

    public void setReadwriteRule(ShardRule readwriteRule) {
        this.readwriteRule = readwriteRule;
    }

    public boolean equals(Object obj) {

        if (readwriteRule != null) {
            return false;
        }
        AppRule appRule = (AppRule) obj;
        ShardRule masterRule = ((AppRule) appRule).getMasterRule();
        ShardRule slaveRule = appRule.getSlaveRule();
        if (this.masterRule == null || this.slaveRule == null) {
            return false;
        }
        if (!this.masterRule.equals(masterRule)) {
            return false;
        }
        if (!this.slaveRule.equals(slaveRule)) {
            return false;
        }
        return true;
    }
}
