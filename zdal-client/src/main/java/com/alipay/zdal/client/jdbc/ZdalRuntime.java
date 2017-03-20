/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * 保持为Final类，只可重建，不可修改
 * 
 * 第一阶段：只支持数据源的动态，不支持规则和dbindex的动态
 * @author zhaofeng.wang
 * @version $Id: ZdalRuntime.java,v 0.1 2012-10-26 上午11:21:29 zhaofeng.wang Exp $
 */
public class ZdalRuntime {
    public final Map<String, DBSelector> dbSelectors;

    public ZdalRuntime(Map<String, DBSelector> dbSelectors) {
        this.dbSelectors = dbSelectors;
    }

    /**
     * 以原有的dbIndex去新的推送下来的map中查找，若找到则用新的 
     */
    public static ZdalRuntime resetDbSelectors(ZdalRuntime oldrt,
                                               Map<String, DBSelector> newDbSelectors) {
        Map<String, DBSelector> resSelectors = new HashMap<String, DBSelector>();
        for (Map.Entry<String, DBSelector> e : oldrt.dbSelectors.entrySet()) {
            DBSelector newdb = newDbSelectors.get(e.getKey());
            resSelectors.put(e.getKey(), newdb == null ? e.getValue() : newdb);
        }
        return new ZdalRuntime(resSelectors);
    }
}
