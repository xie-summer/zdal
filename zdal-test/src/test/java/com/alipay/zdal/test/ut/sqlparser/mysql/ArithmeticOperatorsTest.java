package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class ArithmeticOperatorsTest {
	@Test
	public void test_0() throws Exception {
        String sql = "SELECT 3+5;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 3 + 5;", text);
    }
	@Test
    public void test_1() throws Exception {
        String sql = "SELECT 3-5;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 3 - 5;", text);
    }
	@Test
    public void test_2() throws Exception {
        String sql = "SELECT - 2;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT -2;", text);
    }
	@Test
    public void test_3() throws Exception {
        String sql = "SELECT 3*5;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 3 * 5;", text);
    }
	@Test
    public void test_4() throws Exception {
        String sql = "SELECT 18014398509481984*18014398509481984.0;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 18014398509481984 * 18014398509481984.0;", text);
    }
	@Test
    public void test_5() throws Exception {
        String sql = "SELECT 18014398509481984*18014398509481984;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 18014398509481984 * 18014398509481984;", text);
    }
	@Test
    public void test_6() throws Exception {
        String sql = "SELECT 3/5;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 3 / 5;", text);
    }
	@Test
    public void test_7() throws Exception {
        String sql = "SELECT 102/(1-1);";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 102 / (1 - 1);", text);
    }
	@Test
    public void test_8() throws Exception {
        String sql = "SELECT 102/1-1;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 102 / 1 - 1;", text);
    }
	@Test
    public void test_9() throws Exception {
        String sql = "SELECT a + b + c;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT a + b + c;", text);
    }
	@Test
    public void test_10() throws Exception {
        String sql = "SELECT a + (b + c);";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT a + (b + c);", text);
    }
	@Test
    public void test_11() throws Exception {
        String sql = "SELECT N  % M;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT N % M;", text);
    }
	@Test
    public void test_12() throws Exception {
        String sql = "SELECT 1 = 0 OR A > ?;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 1 = 0\n\tOR A > ?;", text);
    }
	@Test
    public void test_13() throws Exception {
        String sql = "SELECT 1 = 0 AND ID = ?;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 1 = 0\n\tAND ID = ?;", text);
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
