package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class InformationFunctionsTest {
	
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT BENCHMARK(1000000,ENCODE('hello','goodbye'))";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT BENCHMARK(1000000, ENCODE('hello', 'goodbye'));", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT CHARSET('abc');";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CHARSET('abc');", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT CHARSET(CONVERT('abc' USING utf8));";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CHARSET(CONVERT('abc' USING utf8));", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT CHARSET(USER());";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CHARSET(USER());", text);
	    }
	@Test
	    public void test_4() throws Exception {
	        String sql = "SELECT COERCIBILITY('abc' COLLATE latin1_swedish_ci);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT COERCIBILITY('abc' COLLATE latin1_swedish_ci);", text);
	    }
	@Test
	    public void test_5() throws Exception {
	        String sql = "SELECT COLLATION('abc');";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT COLLATION('abc');", text);
	    }
	@Test
	    public void test_6() throws Exception {
	        String sql = "SELECT * FROM mysql.user;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT *\nFROM mysql.user;", text);
	    }
	@Test
	    public void test_7() throws Exception {
	        String sql = "SELECT CURRENT_USER();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CURRENT_USER();", text);
	    }
	@Test
	    public void test_8() throws Exception {
	        String sql = "SELECT DATABASE();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT DATABASE();", text);
	    }
	@Test
	    public void test_9() throws Exception {
	        String sql = "SELECT SQL_CALC_FOUND_ROWS * FROM tbl_name;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT SQL_CALC_FOUND_ROWS *\nFROM tbl_name;", text);
	    }
	@Test
	    public void test_10() throws Exception {
	        String sql = "SELECT FOUND_ROWS();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT FOUND_ROWS();", text);
	    }
	@Test
	    public void test_11() throws Exception {
	        String sql = "SELECT LAST_INSERT_ID();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT LAST_INSERT_ID();", text);
	    }
	@Test
	    public void test_12() throws Exception {
	        String sql = "SELECT ROW_COUNT();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT ROW_COUNT();", text);
	    }
	@Test
	    public void test_13() throws Exception {
	        String sql = "SELECT USER();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT USER();", text);
	    }
	@Test
	    public void test_14() throws Exception {
	        String sql = "SELECT VERSION();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT VERSION();", text);
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
