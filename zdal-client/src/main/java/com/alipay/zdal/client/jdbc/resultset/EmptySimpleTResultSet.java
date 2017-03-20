/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.alipay.zdal.client.jdbc.ZdalStatement;

/**
 * 调用rs.next永远返回false的空结果集。
 * 主要用于一些特殊的情况
 * 
 */
public class EmptySimpleTResultSet extends AbstractTResultSet {

    public EmptySimpleTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets) {
        super(statementProxy, resultSets);
    }

    @Override
    public boolean next() throws SQLException {
        return false;
    }

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return null;
	}
}
