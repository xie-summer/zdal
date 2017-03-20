/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.common;

/**
 * 
 * @author 伯牙
 * @version $Id: Constants.java, v 0.1 2013-1-10 下午03:31:24 Exp $
 */
public interface Constants {
    /** config信息变更时，记录的log名称. */
    public static final String CONFIG_LOG_NAME_LOGNAME           = "zdal-client-config";

    /** 打印zdatasource连接池状态的log名称. */
    public static final String ZDAL_DATASOURCE_POOL_LOGNAME      = "zdal-datasource-pool";

    /**  本地配置文件的 类型， DS OR　RULE*/
    public static final int    LOCAL_CONFIG_DS                   = 0;

    public static final int    LOCAL_CONFIG_RULE                 = 1;

    /**  本地配置文件的名称格式：appName-dbmode-ds.xml*/
    public static final String LOCAL_CONFIG_FILENAME_SUFFIX      = "{0}-{1}-ds.xml";

    /**  本地配置文件的名称格式：appName-dbmode-rule.xml*/
    public static final String LOCAL_RULE_CONFIG_FILENAME_SUFFIX = "{0}-{1}-rule.xml";

    public static final String DBINDEX_DSKEY_CONN_CHAR           = "_";

    public static final String DBINDEX_DS_GROUP_KEY_PREFIX       = "group_";

}
