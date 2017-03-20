package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class MiscellaneousFunctionsTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "UPDATE t SET i = DEFAULT(i)+1 WHERE id < 100;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("UPDATE t\nSET i = DEFAULT(i) + 1\nWHERE id < 100;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT GET_LOCK('lock1',10);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT GET_LOCK('lock1', 10);", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT INET_ATON('209.207.224.40');";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT INET_ATON('209.207.224.40');", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT UUID();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT UUID();", text);
	    }

	    private String output(List<SQLStatement> stmtList) {
	        StringBuilder out = new StringBuilder();
	        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);

	        for (SQLStatement stmt : stmtList) {
	            stmt.accept(visitor);
	            out.append(";");
	        }

	        return out.toString();
	    }

}
