package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class DeleteSyntaxTest {
	@Test
	public void test_0() throws Exception {
        String sql = "DELETE FROM somelog WHERE user = 'jcole' ORDER BY timestamp_column LIMIT 1;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals(
            "DELETE FROM somelog\nWHERE user = 'jcole'\nORDER BY timestamp_column\nLIMIT 1;", text);
    }
	@Test
    public void test_1() throws Exception {
        String sql = "DELETE t1 FROM t1 LEFT JOIN t2 ON t1.id=t2.id WHERE t2.id IS NULL;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals(
            "DELETE t1\nFROM t1 LEFT JOIN t2 ON t1.id = t2.id\nWHERE t2.id IS NULL;", text);
    }
	@Test
    public void test_2() throws Exception {
        String sql = "DELETE a1, a2 FROM t1 AS a1 INNER JOIN t2 AS a2 WHERE a1.id=a2.id";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("DELETE a1, a2\nFROM t1 a1 INNER JOIN t2 a2\nWHERE a1.id = a2.id;",
            text);
    }
	@Test
    public void test_3() throws Exception {
        String sql = "DELETE FROM a1, a2 USING t1 AS a1 INNER JOIN t2 AS a2 WHERE a1.id=a2.id";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals(
            "DELETE FROM a1, a2\nUSING t1 a1 INNER JOIN t2 a2\nWHERE a1.id = a2.id;", text);
    }
	@Test
    public void test_4() throws Exception {
        String sql = "DELETE LOW_PRIORITY QUICK IGNORE FROM T";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("DELETE LOW_PRIORITY QUICK IGNORE FROM T;", text);
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
