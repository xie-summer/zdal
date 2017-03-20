package com.alipay.zdal.test.ut.sqlparser.mysql;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.parser.Token;

public class HelpTest  extends TestCase {
	
    public void test_help_0() throws Exception {
        String sql = "HELP 'contents'";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("HELP 'contents'", output);
    }

}
