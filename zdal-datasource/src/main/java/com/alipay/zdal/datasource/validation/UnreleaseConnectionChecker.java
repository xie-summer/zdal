/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.alipay.zdal.datasource.validation;

/**
 *
 */
public interface UnreleaseConnectionChecker {

    /**
     * 对没有释放的数据库连接进行检查
     */
    void connectionCheck();

}
