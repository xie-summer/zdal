/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.common.Closable;

/**
 * Zdal 对外公布的数据源,支持动态调整数据源的配置信息，切换等功能<br>
 * 注意： 1,使用前请务必先设置appName,appDsName,dbmode,configPath的值，并且调用init方法进行初始化;
 * 2,从configPath获取配置信息: <br>
 * <bean id="testZdalDataSource" class="com.alipay.zdal.client.jdbc.ZdalDataSource" init-method="init" destroy-method="close"> 
 *      <property name="appName" value="appName"/> 
 *      <property name="appDsName" value="appDsName"/> 
 *      <property name="dbmode" value="dev"/> 
 *      <property name="configPath" value="/home/admin/appName-run/jboss/deploy"/> 
 * </bean>
 * 
 * @author 伯牙
 * @version $Id: ZdalDataSource.java, v 0.1 2012-11-17 下午4:08:43 Exp $
 */
public class ZdalDataSource extends AbstractZdalDataSource implements DataSource, Closable {

    public void init() {
        if (super.inited.get() == true) {
            throw new ZdalClientException("ERROR ## init twice");
        }
        try {
            super.initZdalDataSource();
        } catch (Exception e) {
            CONFIG_LOGGER.error("zdal init fail,config:" + this.toString(), e);
            throw new ZdalClientException(e);
        }

    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger(ZdalDataSource.class.getName());
	}
}