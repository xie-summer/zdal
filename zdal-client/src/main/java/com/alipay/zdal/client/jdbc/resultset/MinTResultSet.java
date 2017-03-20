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
 * 
 * @author ����
 * @version $Id: MinTResultSet.java, v 0.1 2014-1-6 ����05:14:40 Exp $
 */
public class MinTResultSet extends MaxMinTResultSet {

    public MinTResultSet(ZdalStatement statementProxy, List<ResultSet> resultSets)
                                                                                  throws SQLException {
        super(statementProxy, resultSets);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultSet reducer() throws SQLException {
        ResultSet minResultSet = actualResultSets.get(0);
        minResultSet.next();
        Comparable<Object> min = (Comparable<Object>) minResultSet.getObject(1);

        for (int i = 1; i < actualResultSets.size(); i++) {
            ResultSet resultSet = actualResultSets.get(i);
            resultSet.next();
            Comparable<Object> comp = (Comparable<Object>) resultSet.getObject(1);
            if (min == null || comp == null) {
                if (comp != null) {
                    minResultSet = resultSet;
                    min = comp;
                }
            } else if (min.compareTo(comp) > 0) {
                minResultSet = resultSet;
                min = comp;
            }
        }
        return minResultSet;
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
