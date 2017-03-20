/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.client.util;

/**
 * Zdatasource的常量设置类
 * 
 * @author liangjie.li
 * @version $Id: ZConstants.java, v 0.1 May 11, 2012 5:33:12 PM liangjie.li Exp $
 */
public class ZConstants {

    /** 异常码，表示数据库处于不可用状态*/
    public static final String ERROR_CODE_DB_NOT_AVAILABLE         = "100";
    /** 异常码，表示没有可用连接*/
    public static final String ERROR_CODE_CONNECTION_NOT_AVAILABLE = "101";
    /** 异常码，表示超时*/
    public static final String ERROR_CODE_CONNECTION_TIMEOUT       = "102";
    /**  打印连接池状态的间隔 */
    public static final int    LOGGER_DELAY                        = 30;

}
