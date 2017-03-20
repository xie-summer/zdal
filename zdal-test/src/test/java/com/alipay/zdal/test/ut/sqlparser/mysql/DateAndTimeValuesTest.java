package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class DateAndTimeValuesTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT '2008-12-31 23:59:59' + INTERVAL 1 SECOND;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT '2008-12-31 23:59:59' + INTERVAL 1 SECOND;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT '2008-02-31' + INTERVAL 0 DAY";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT '2008-02-31' + INTERVAL 0 DAY;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT '2008-02-31' + INTERVAL 0 MONTH";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT '2008-02-31' + INTERVAL 0 MONTH;", text);
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
