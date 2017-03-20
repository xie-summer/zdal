package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.util.JdbcUtils;

public class TestOnlineSQLTest3 extends TestCase {

    private String       url      = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/tddl_0";
    private String       user     = "mysql";
    private String       password = "mysql";

    protected Connection conn;

    public void setUp() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);
    }

    public void tearDown() throws Exception {
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    public void test_0() throws Exception {
//        ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
//        JdbcUtils.printResultSet(rs);
        
        String sql = "select benchmark( 1, sha1( 'test' ) )";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        JdbcUtils.printResultSet(rs);
        
        stmt.close();
    }
}
