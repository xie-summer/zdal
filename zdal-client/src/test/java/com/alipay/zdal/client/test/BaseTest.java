/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: BaseTest.java, v 0.1 2013-12-25 ÏÂÎç06:10:12 Exp $
 */
public class BaseTest {

    public void closeResource(PreparedStatement pst, Connection conn) throws SQLException {
        if (pst != null) {
            pst.close();
            pst = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    public void closeResource(ResultSet rs, PreparedStatement pst, Connection conn)
                                                                                   throws SQLException {
        if (rs != null) {
            rs.close();
            rs = null;
        }
        if (pst != null) {
            pst.close();
            pst = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }

    }

}
