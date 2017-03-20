package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class AssignmentOperatorsTest {
	@Test
	  public void test_0() throws Exception {
	        String sql = "SELECT @var1, @var2;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT @var1, @var2;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT @var1 := 1, @var2;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT @var1 := 1, @var2;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT @var1, @var2 := @var1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT @var1, @var2 := @var1;", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT @var1:=COUNT(*) FROM t1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT @var1 := COUNT(*)\nFROM t1;", text);
	    }
	@Test
	    public void test_4() throws Exception {
	        String sql = "UPDATE t1 SET c1 = 2 WHERE c1 = @var1:= 1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("UPDATE t1\nSET c1 = 2\nWHERE c1 = (@var1 := 1);", text);
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
