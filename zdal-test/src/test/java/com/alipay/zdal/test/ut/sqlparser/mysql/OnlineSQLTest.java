package com.alipay.zdal.test.ut.sqlparser.mysql;

import junit.framework.TestCase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;

public class OnlineSQLTest extends TestCase {
	
	 private String url      = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/tddl_0";
	    private String user     = "mysql";
	    private String password = "mysql";

	    public void test_list_sql() throws Exception {
	        // reset();

	        // 这些中文括号
	        // update(7216, "", 4);
	        // update(7223, "", 4);
	        // update(8387, "", 4);

	        // 语法错误
	        // update(17018, "", 4); //alarm_type&?
	        // update(17841, "", 4); //alarm_type&?
	        // update(17845, "", 4); //alarm_type&?
	        // update(18247, "", 4); //alarm_type&?
	        // update(19469, "", 4); //alarm_type&?
	        update(19730, "", 4); // alarm_type&?
	        update(20164, "", 4); // alarm_type&?
	        update(20386, "", 4); // alarm_type&?
	        update(20440, "", 4); // alarm_type&?
	        update(21208, "", 4); // alarm_type&?

	        // IBATIS NAME
	        update(18035, "", 4); // alarm_type&?

	        Connection conn = DriverManager.getConnection(url, user, password);

	        int count = 0;
	        String sql = "SELECT user_id,name from  users WHERE name IS NULL LIMIT 100";
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            int id = rs.getInt(1);
	            String value = rs.getString(2);

	            if (value.indexOf('（') != -1) {
	                update(id, "", 4);
	                continue;
	            }

	            System.out.println(value);
	            System.out.println();
	            try {
	                validateOracle(id, value);
	            } catch (Exception e) {
	                e.printStackTrace();
	                System.out.println("id : " + id);
	                continue;
	            }
	            count++;
	        }
	        rs.close();
	        stmt.close();

	        System.out.println("COUNT : " + count);

	        conn.close();
	    }

	    void reset() throws SQLException {
	        Connection conn = DriverManager.getConnection(url, user, password);

	        String sql = "UPDATE users SET user_id = NULL, name = NULL";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.executeUpdate();
	        stmt.close();

	        conn.close();
	    }

	    void update(int id, String value2, int flag) throws SQLException {
	        Connection conn = DriverManager.getConnection(url, user, password);

	        String sql = "UPDATE users SET user_id = ?, name = ? WHERE user_id = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, flag);
	        stmt.setString(2, value2);
	        stmt.setInt(3, id);
	        stmt.executeUpdate();
	        stmt.close();

	        conn.close();
	    }

	    void validate(int id, String sql) throws Exception {
	        sql = sql.trim();
	        boolean sqlFlag = false;
	        String lowerSql = sql.toLowerCase();
	        if (lowerSql.startsWith("insert") || lowerSql.startsWith("select") || lowerSql.startsWith("upate")
	            || lowerSql.startsWith("delete") || lowerSql.startsWith("create") || lowerSql.startsWith("drop")) {
	            sqlFlag = true;
	        }

	        if (!sqlFlag) {
	            update(id, sql, 2);
	            return;
	        }

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);

	        Assert.assertEquals(1, statementList.size());

	        StringBuilder out = new StringBuilder();
	        MySqlParameterizedOutputVisitor visitor = new MySqlParameterizedOutputVisitor(out);
	        statemen.accept(visitor);

	        update(id, out.toString(), 1);
	        System.out.println(sql);
	        System.out.println(out.toString());
	    }

	    void validateOracle(int id, String sql) throws Exception {
	        sql = sql.trim();
	        boolean sqlFlag = false;
	        String lowerSql = sql.toLowerCase();
	        if (lowerSql.startsWith("insert") || lowerSql.startsWith("select") || lowerSql.startsWith("upate")
	            || lowerSql.startsWith("delete") || lowerSql.startsWith("create") || lowerSql.startsWith("drop")) {
	            sqlFlag = true;
	        }

	        if (!sqlFlag) {
	            update(id, sql, 2);
	            return;
	        }

	        OracleStatementParser parser = new OracleStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);

	        Assert.assertEquals(1, statementList.size());

	        StringBuilder out = new StringBuilder();
	        OracleParameterizedOutputVisitor visitor = new OracleParameterizedOutputVisitor(out);
	        statemen.accept(visitor);

	        update(id, out.toString(), 1);
	        System.out.println(sql);
	        System.out.println(out.toString());
	    }

}
