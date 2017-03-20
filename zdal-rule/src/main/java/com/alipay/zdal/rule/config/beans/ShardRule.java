/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * 一份完整的分库分表规则配置，一套库一份
 *  
 */
public class ShardRule implements Cloneable {
    private Map<String/*逻辑表名*/, TableRule> tableRules;
    private String                          defaultDbIndex;

    /**
     * 无逻辑的getter/setter
     */
    public Map<String, TableRule> getTableRules() {
        return tableRules;
    }

    public void setTableRules(Map<String, TableRule> tableRules) {
        this.tableRules = tableRules;
    }

    public String getDefaultDbIndex() {
        return defaultDbIndex;
    }

    public void setDefaultDbIndex(String defaultDbIndex) {
        this.defaultDbIndex = defaultDbIndex;
    }

    /**
     * tableRules新创建map，每个TableRule对象中的DbIndex深clone
     */
    @Override
    public ShardRule clone() throws CloneNotSupportedException {
        ShardRule r = (ShardRule) super.clone();

        Map<String, TableRule> tbrs = new HashMap<String, TableRule>(tableRules.size());
        for (Map.Entry<String, TableRule> e : tableRules.entrySet()) {
            TableRule tbr = e.getValue().clone();
            String[] oldIndexes = tbr.getDbIndexArray();
            String[] newIndexes = new String[oldIndexes.length];
            System.arraycopy(oldIndexes, 0, newIndexes, 0, oldIndexes.length);
            tbr.setDbIndexArray(newIndexes);
            tbrs.put(e.getKey(), tbr);
        }
        r.setDefaultDbIndex(defaultDbIndex);
        r.setTableRules(tbrs);
        return r;
    }

    /*	   public boolean equals(Object obj) {
    	        
    	        if (readwriteRule != null) {
    	            return false;
    	        }
    	        AppRule appRule = (AppRule) obj;
    	        ShardRule masterRule = ((AppRule) appRule).getMasterRule();
    	        ShardRule slaveRule = appRule.getSlaveRule();
    	        if (!this.masterRule.equals(masterRule)) {
    	            return false;
    	        }
    	        if (!this.slaveRule.equals(slaveRule)) {
    	            return false;
    	        }
    	        return true;
    	    }*/

    public boolean equals(Object obj) {
        //  tableRules
        ShardRule shardRule = (ShardRule) obj;
        Map<String/*逻辑表名*/, TableRule> tableRules = shardRule.getTableRules();
        if (tableRules == null || this.tableRules == null
            || tableRules.size() != this.tableRules.size()) {
            return false;
        }
        for (Map.Entry<String, TableRule> entry : tableRules.entrySet()) {
            String key = entry.getKey();
            TableRule tr = entry.getValue();
            TableRule tr2 = this.tableRules.get(key);
            if (tr2 == null) {
                return false;
            }
            if (tr2 != tr) {
                return false;
            }
        }
        return true;
    }

}
