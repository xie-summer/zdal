package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class LogicalOperatorsTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT 10 IS TRUE;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 10 IS true;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT -10 IS TRUE;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT -10 IS true;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT 'string' IS NOT NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'string' IS NOT NULL;", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT NOT 10;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT NOT 10;", text);
	    }
	@Test
	    public void test_4() throws Exception {
	        String sql = "SELECT NOT 0;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT NOT 0;", text);
	    }
	@Test
	    public void test_5() throws Exception {
	        String sql = "SELECT NOT NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT NOT NULL;", text);
	    }
	@Test
	    public void test_6() throws Exception {
	        String sql = "SELECT ! (1+1);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT !(1 + 1);", text);
	    }
	@Test
	    public void test_7() throws Exception {
	        String sql = "SELECT ! 1+1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT !(1 + 1);", text);
	    }
	@Test
	    public void test_8() throws Exception {
	        String sql = "SELECT 1 && 1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 1\n\tAND 1;", text);
	    }
	@Test
	    public void test_9() throws Exception {
	        String sql = "SELECT 1 AND NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 1\n\tAND NULL;", text);
	    }
	@Test
	    public void test_10() throws Exception {
	        String sql = "SELECT 0 OR NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0\n\tOR NULL;", text);
	    }
	@Test
	    public void test_11() throws Exception {
	        String sql = "SELECT 0 || NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0 || NULL;", text);
	    }
	@Test
	    public void test_12() throws Exception {
	        String sql = "SELECT 0 XOR NULL;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0 XOR NULL;", text);
	    }
	@Test
	    public void test_13() throws Exception {
	        String sql = "SELECT 1 XOR 1 XOR 1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 1 XOR 1 XOR 1;", text);
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
