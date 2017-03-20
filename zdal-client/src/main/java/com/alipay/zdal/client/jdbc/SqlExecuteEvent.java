/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import com.alipay.zdal.common.SqlType;

public interface SqlExecuteEvent {
    SqlType getSqlType();

    String getLogicTableName();

    String getPrimaryKeyColumn();

    Object getPrimaryKeyValue();

    String getDatabaseShardColumn();

    Object getDatabaseShardValue();

    String getTableShardColumn();

    Object getTableShardValue();

    String getSql();

    long getAfterMainDBSqlExecuteTime();

    void setAfterMainDBSqlExecuteTime(long afterMainDBSqlExecuteTime);
}
