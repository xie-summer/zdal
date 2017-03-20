/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implements check valid connection sql
 *
 * @author ²®ÑÀ
 * @version $Id: CheckValidConnectionSQL.java, v 0.1 2014-1-6 ÏÂÎç05:28:09 Exp $
 */
public class CheckValidConnectionSQL implements ValidConnectionChecker, Serializable {
    private static final long serialVersionUID = -222752863430216887L;

    String                    sql;

    public CheckValidConnectionSQL() {
    }

    public CheckValidConnectionSQL(String sql) {
        this.sql = sql;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker#isValidConnection(java.sql.Connection)
     */
    public SQLException isValidConnection(Connection c) {
        try {
            Statement s = c.createStatement();
            try {
                s.execute(sql);
                return null;
            } finally {
                s.close();
            }
        } catch (SQLException e) {
            return e;
        }
    }
}
