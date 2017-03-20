package com.alipay.zdal.test.ut.sqlparser.mysql;

import junit.framework.TestCase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

public class OnlineSQLTest2  extends TestCase {
	
	 private String url      = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/tddl_0";
	    private String user     = "mysql";
	    private String password = "mysql";

	    public void test_list_sql() throws Exception {

	        Connection conn = DriverManager.getConnection(url, user, password);

	        int count = 0;
	        String sql = "select user_id,name from  users LIMIT 100";
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            int id = rs.getInt(1);
	            String value = rs.getString(2);

	            if (value.indexOf('ги') != -1) {
	                continue;
	            }

	            boolean sqlFlag = false;
	            String lowerSql = value.toLowerCase();
	            if (lowerSql.startsWith("insert") || lowerSql.startsWith("select") || lowerSql.startsWith("upate")
	                || lowerSql.startsWith("delete") || lowerSql.startsWith("create") || lowerSql.startsWith("drop")) {
	                sqlFlag = true;
	            }

	            if (!sqlFlag) {
	                continue;
	            }

	            System.out.println(value);
	            mysqlStat(id, lowerSql);
	            System.out.println();
	            count++;
	        }
	        rs.close();
	        stmt.close();

	        System.out.println("COUNT : " + count);

	        conn.close();
	    }

	    void mysqlStat(int id, String sql) throws Exception {
	        sql = sql.trim();
	        boolean sqlFlag = false;
	        String lowerSql = sql.toLowerCase();
	        if (lowerSql.startsWith("insert") || lowerSql.startsWith("select") || lowerSql.startsWith("upate")
	            || lowerSql.startsWith("delete") || lowerSql.startsWith("create") || lowerSql.startsWith("drop")) {
	            sqlFlag = true;
	        }

	        if (!sqlFlag) {
	            return;
	        }

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	    }

}
