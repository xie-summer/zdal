package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class START_TRANSACTION_Test {
	@Test
	  public void test_0() throws Exception {
	        String sql = "START TRANSACTION;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("START TRANSACTION;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "START TRANSACTION WITH CONSISTENT SNAPSHOT;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("START TRANSACTION WITH CONSISTENT SNAPSHOT;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "START TRANSACTION BEGIN;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("START TRANSACTION BEGIN;", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "START TRANSACTION BEGIN WORK;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("START TRANSACTION BEGIN WORK;", text);
	    }
	    @Test
	    public void test_4() throws Exception {
	        String sql = "COMMIT;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("COMMIT;", text);
	    }
	    @Test
	    public void test_5() throws Exception {
	        String sql = "COMMIT WORK;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("COMMIT WORK;", text);
	    }
	    @Test
	    public void test_6() throws Exception {
	        String sql = "ROLLBACK;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("ROLLBACK;", text);
	    }
	    @Test
	    public void test_7() throws Exception {
	        String sql = "SET autocommit = 0;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SET @@autocommit = 0;", text);
	    }

	    private String output(List<SQLStatement> stmtList) {
	        StringBuilder out = new StringBuilder();

	        for (SQLStatement stmt : stmtList) {
	            stmt.accept(new MySqlOutputVisitor(out));
	            out.append(";");
	        }

	        return out.toString();
	    }
}
