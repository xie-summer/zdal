/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.alipay.zdal.client.jdbc.ZdalStatement;


public class MaxTResultSet extends MaxMinTResultSet {

    public MaxTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets)
                                                                                  throws SQLException {
        super(statementProxy, resultSets);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultSet reducer() throws SQLException {
        ResultSet maxResultSet = actualResultSets.get(0);
        maxResultSet.next();
        Comparable<Object> max = (Comparable<Object>) maxResultSet.getObject(1);

        for (int i = 1; i < actualResultSets.size(); i++) {
            ResultSet resultSet = actualResultSets.get(i);
            resultSet.next();
            Comparable<Object> comp = (Comparable<Object>) resultSet.getObject(1);
            if (max == null || comp == null) {
                if (comp != null) {
                    maxResultSet = resultSet;
                    max = comp;
                }
            } else if (max.compareTo(comp) < 0) {
                maxResultSet = resultSet;
                max = comp;
            }
        }
        return maxResultSet;
    }

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
