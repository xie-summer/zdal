/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common;

/**
 * 销毁外部资源.
 * 
 * @author 伯牙
 * @version $Id: Closable.java, v 0.1 2012-11-17 下午4:57:54 Exp $
 */
public interface Closable {
    /**
     * com.alipay.zdal.client.jdbc.ZdalDataSource,
     * com.alipay.zdal.datasource.Zdatasource需要调用来销毁外部资源.
     * @throws Throwable
     */
    void close() throws Throwable;
}
