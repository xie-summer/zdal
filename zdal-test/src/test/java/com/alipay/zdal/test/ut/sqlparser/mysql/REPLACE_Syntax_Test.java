package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class REPLACE_Syntax_Test {
	@Test
	public void test_0() throws Exception {
        String sql = "REPLACE INTO T SELECT * FROM T;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("REPLACE INTO T\n\tSELECT *\n\tFROM T;", text);
    }
	@Test
    public void test_1() throws Exception {
        String sql = "REPLACE DELAYED INTO `online_users` SET `session_id`='3580cc4e61117c0785372c426eddd11c', `user_id` = 'XXX', `page` = '/', `lastview` = NOW();";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);
        System.out.println(text);
        Assert
            .assertEquals(
                "REPLACE DELAYED INTO `online_users` (`session_id`, `user_id`, `page`, `lastview`)\nVALUES ('3580cc4e61117c0785372c426eddd11c', 'XXX', '/', NOW());",
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
