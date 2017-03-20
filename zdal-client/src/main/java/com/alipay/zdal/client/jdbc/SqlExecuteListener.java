/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.SQLException;
import java.util.List;

public interface SqlExecuteListener {
    /**
     * 用来处理统一配置的传递
     */
    void init(ZdalDataSource tDataSource);

    void beforeSqlExecute(List<SqlExecuteEvent> event) throws SQLException;

    void afterSqlExecute(List<SqlExecuteEvent> event) throws SQLException;

    void afterTxCommit(List<SqlExecuteEvent> events) throws SQLException;

    void afterTxRollback(List<SqlExecuteEvent> events) throws SQLException;
}
