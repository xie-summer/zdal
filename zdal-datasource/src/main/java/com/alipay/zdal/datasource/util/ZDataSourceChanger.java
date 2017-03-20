/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.ZDataSourceFactory;
import com.alipay.zdal.datasource.resource.adapter.jdbc.local.LocalTxDataSource;

/**
 * 
 * 
 * @author liangjie.li
 * @version $Id: ZDataSourceChanger.java, v 0.1 2012-8-20 下午8:08:03 liangjie.li Exp $
 */
public class ZDataSourceChanger {
    private static final Logger logger = Logger.getLogger(ZDataSourceChanger.class);

    public static boolean configChange(Map<String, String> properties, ZDataSource zds) {
        boolean urlChange = ZDataSourceUtil.isChanged(properties, Parameter.JDBC_URL); //url是否改变
        boolean driverChange = ZDataSourceUtil.isChanged(properties, Parameter.DRIVER_CLASS);//driverclass 是否改变

        //先复制一份,在用推送的新值覆盖
        LocalTxDataSourceDO newDO = new LocalTxDataSourceDO();
        newDO.setDsName(zds.getDsName());
        ZDataSourceUtil.copyDS2DO(zds.getLocalTxDataSource(), newDO);
        ZDataSourceUtil.replaceValueFromMap(newDO, properties);

        if (driverChange && !urlChange) {//jboss会为url缓存driver
            logger.error("driverClass发生推送操作时，connectionUrl也需推送，本次推送忽略");
            return false;
        }
        //connectionproperties是否改变
        boolean propChange = ZDataSourceUtil.solveConnectionProperties(newDO
            .getConnectionProperties(), zds.getLocalTxDataSource().getConnectionProperties());

        boolean dsFileChange = ZDataSourceUtil.isNeedReCreate(newDO, zds.getLocalTxDataSource(),
            propChange);

        if (dsFileChange) {
            return reCreatePool(newDO, zds);
        }
        return true;
    }

    /**
     * 重建数据源连接池，先建立一个新的连接池，然后和旧的交换，最后销货旧的
     * 
     * @param newDO
     * @return
     */
    static boolean reCreatePool(LocalTxDataSourceDO newDO, ZDataSource zds) {
        try {
            long t1 = System.currentTimeMillis();
            LocalTxDataSource newDs = ZDataSourceFactory.createLocalTxDataSource(newDO, zds);
            LocalTxDataSource oldDs = zds.getLocalTxDataSource();

            zds.setLocalTxDataSource(newDs);

            oldDs.destroy();
            oldDs = null;

            logger.warn("连接池已经重建, cost " + (System.currentTimeMillis() - t1) + " ms");
            return true;
        } catch (Exception e) {
            logger.error("连接池重建失败", e);
            return false;
        }

    }
}
