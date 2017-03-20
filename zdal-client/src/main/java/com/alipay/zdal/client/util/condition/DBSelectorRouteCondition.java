/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.List;

import com.alipay.zdal.client.RouteCondition;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: DBSelectorRouteCondition.java, v 0.1 2014-1-6 ÏÂÎç05:16:15 Exp $
 */
public interface DBSelectorRouteCondition extends RouteCondition {
    public String getDBSelectorID();

    public List<String> getTableList();
}
