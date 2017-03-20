package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class InsertSyntaxTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "INSERT INTO tbl_name () VALUES();";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("INSERT INTO tbl_name\nVALUES ();", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "INSERT INTO tbl_name (col1,col2) VALUES(15,col1*2);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("INSERT INTO tbl_name (col1, col2)\nVALUES (15, col1 * 2);", text);
	    }
	    @Test
	    public void test_2() throws Exception {
	        String sql = "INSERT INTO tbl_name (col1,col2) VALUES(col2*2,15);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("INSERT INTO tbl_name (col1, col2)\nVALUES (col2 * 2, 15);", text);
	    }
	    @Test
	    public void test_3() throws Exception {
	        String sql = "INSERT INTO tbl_name (a,b,c) VALUES(1,2,3),(4,5,6),(7,8,9);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals(
	            "INSERT INTO tbl_name (a, b, c)\nVALUES (1, 2, 3), (4, 5, 6), (7, 8, 9);", text);
	    }
	    @Test
	    public void test_4() throws Exception {
	        String sql = "INSERT INTO tbl_name (a,b,c) VALUES(1,2,3,4,5,6,7,8,9);;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        //        Assert.assertEquals("INSERT INTO tbl_name (a, b, c)\nVALUES (1,2,3,4,5,6,7,8,9);", text);
	    }
	    @Test
	    public void test_5() throws Exception {
	        String sql = "INSERT LOW_PRIORITY DELAYED HIGH_PRIORITY IGNORE INTO tbl_name (a,b,c) VALUES(1,2,3);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert
	            .assertEquals(
	                "INSERT LOW_PRIORITY DELAYED HIGH_PRIORITY IGNORE INTO tbl_name (a, b, c)\nVALUES (1, 2, 3);",
	                text);
	    }
	    @Test
	    public void test_6() throws Exception {
	        String sql = "INSERT tbl_name (a,b,c) VALUES(1,2,3) ON DUPLICATE KEY UPDATE c=c+1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals(
	            "INSERT INTO tbl_name (a, b, c)\nVALUES (1, 2, 3) ON DUPLICATE KEY UPDATE c = c + 1;",
	            text);
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
