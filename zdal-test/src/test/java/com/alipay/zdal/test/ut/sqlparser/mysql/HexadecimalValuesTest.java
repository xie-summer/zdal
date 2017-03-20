package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class HexadecimalValuesTest {
	@Test
	  public void test_0() throws Exception {
	        String sql = "SELECT 0x5061756c;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0x5061756c;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT X'4D7953514C';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0x4D7953514C;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT x'4D7953514C';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0x4D7953514C;", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT 0x65 USING utf8;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0x65 USING utf8;", text);
	    }
	    @Test
	    public void test_4() throws Exception {
	        String sql = "SELECT 0x41, CAST(0x41 AS UNSIGNED)";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 0x41, CAST(0x41 AS UNSIGNED);", text);
	    }
	    @Test
	    public void test_5() throws Exception {
	        String sql = "SELECT HEX('cat')";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT HEX('cat');", text);
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
