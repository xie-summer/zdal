package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class RegularExpressionsTest {
	@Test
	public void test_0() throws Exception {
        String sql = "SELECT 'Monty!' REGEXP '.*'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'Monty!' REGEXP '.*';", text);
    }
	@Test
    public void test_1() throws Exception {
        String sql = "SELECT 'new*\n*line' REGEXP 'new\\\\*.\\\\*line'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'new*\n*line' REGEXP 'new\\*.\\*line';", text);
    }
	@Test
    public void test_2() throws Exception {
        String sql = "SELECT 'a' REGEXP 'A', 'a' REGEXP BINARY 'A'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'a' REGEXP 'A', 'a' REGEXP BINARY 'A';", text);
    }
	@Test
    public void test_3() throws Exception {
        String sql = "SELECT 'a' REGEXP '^[a-d]'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'a' REGEXP '^[a-d]';", text);
    }
	@Test
    public void test_4() throws Exception {
        String sql = "SELECT 'fo\nfo' REGEXP '^fo$'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'fo\nfo' REGEXP '^fo$';", text);
    }
	@Test
    public void test_5() throws Exception {
        String sql = "SELECT 'fofo' REGEXP '^fo'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT 'fofo' REGEXP '^fo';", text);
    }
	@Test
    public void test_6() throws Exception {
        String sql = "SELECT '~' REGEXP '[[.tilde.]]'";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("SELECT '~' REGEXP '[[.tilde.]]';", text);
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
