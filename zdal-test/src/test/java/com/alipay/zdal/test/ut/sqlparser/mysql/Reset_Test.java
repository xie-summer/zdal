package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;

import junit.framework.TestCase;

public class Reset_Test extends TestCase {

	 public void test_0() throws Exception {
	        String sql = "binlog  'str'";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("BINLOG 'str';", text);
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
