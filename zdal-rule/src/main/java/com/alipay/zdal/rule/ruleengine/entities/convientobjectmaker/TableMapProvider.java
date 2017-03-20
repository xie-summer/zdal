/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker;

import java.util.Map;

import com.alipay.zdal.rule.ruleengine.entities.abstractentities.SharedElement;

/**
 * 
 * 用于创建database中持有多个表的map
 * 
 *
 */
public interface TableMapProvider {
    public Map<String, SharedElement> getTablesMap();

    public void setParentID(String parentID);

    public void setLogicTable(String logicTable);
}
