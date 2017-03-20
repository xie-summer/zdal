/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.test.shardfailover;

import org.junit.Test;

import com.alipay.zdal.client.jdbc.ZdalDataSource;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: TestShardFailoverDataSource.java, v 0.1 2013-12-27 ÉÏÎç10:20:50 Exp $
 */
public class TestShardFailoverDataSource {
    private static final String   APPNAME    = "ShardFailover";

    private static final String   APPDSNAME  = "ShardFailoverDataSource";

    private static final String   DBMODE     = "dev";

    private static final String   CONFIGPATH = "./configs/ShardFailover";

    private static final String[] USER_IDS   = new String[] { "201312268302803810",
            "201312268302803811", "201312268302803812", "201312268302803813", "201312268302803814",
            "201312268302803815", "201312268302803816", "201312268302803817", "201312268302803818",
            "201312268302803819"            };

    @Test
    public void testShardFailoverDataSource() throws Throwable {
        ZdalDataSource dataSource = new ZdalDataSource();
        dataSource.setAppName(APPNAME);
        dataSource.setAppDsName(APPDSNAME);
        dataSource.setDbmode(DBMODE);
        dataSource.setConfigPath(CONFIGPATH);
        dataSource.init();

    }
}
