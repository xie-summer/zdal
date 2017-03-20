/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.client.util;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.Constants;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.util.PoolCondition;

public class PoolConditionWriter implements Runnable {
    private static final Logger logger      = Logger
                                                .getLogger(Constants.ZDAL_DATASOURCE_POOL_LOGNAME);
    private ZDataSource         zdatasource = null;

    public PoolConditionWriter(ZDataSource zdatasource) {
        this.zdatasource = zdatasource;
    }

    @Override
    public void run() {
        if (zdatasource == null)
            return;
        PoolCondition poolCondition = zdatasource.getPoolCondition();
        if (poolCondition != null) {
            logger.warn(poolCondition);
        }
    }

}
