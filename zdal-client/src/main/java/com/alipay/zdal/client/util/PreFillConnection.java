/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.datasource.ZDataSource;

/**
 * 预热mysql，oracle的最小连接数.
 * @author 伯牙
 * @version $Id: PreFillConnection.java, v 0.1 2013-6-7 上午09:48:02 Exp $
 */
public class PreFillConnection {

    private static final Logger logger             = Logger
                                                       .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    /** mysql数据库预热连接的sql语句 */
    private static final String MYSQL_PREFILL_SQL  = "select 1";

    /** oracle数据库预热连接的sql语句 */
    private static final String ORACLE_PREFILL_SQL = "select sysdate from dual";

    /**
     * 初始化所有数据源的最小连接数.
     * @param dataSources
     * @param dbType
     */
    public static void prefillConnection(final Collection<ZDataSource> dataSources,
                                         final DBType dbType) {
        if (dataSources == null || dataSources.isEmpty()) {
            return;
        }
        if (dbType.isMysql() || dbType.isOracle()) {
            for (ZDataSource dataSource : dataSources) {
                Connection conn = null;
                Statement statement = null;
                ResultSet rs = null;
                try {
                    conn = dataSource.getConnection();
                    statement = conn.createStatement();
                    if (dbType.isMysql()) {
                        rs = statement.executeQuery(MYSQL_PREFILL_SQL);
                    } else {
                        rs = statement.executeQuery(ORACLE_PREFILL_SQL);
                    }
                    logger.warn("WANR ## prefill " + dataSource.getDsName()
                                + " min connection success");
                } catch (Exception e) {
                    logger.error("ERROR ## prefill " + dataSource.getDsName()
                                 + " min connection failured,the dbType = " + dbType, e);
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                            rs = null;
                        }
                        if (statement != null) {
                            statement.close();
                            statement = null;
                        }
                        if (conn != null) {
                            conn.close();
                            conn = null;
                        }
                    } catch (Exception e) {
                        logger.error("ERROR ## close the connection resource has an error", e);
                    }
                }
            }
        }
    }
}
