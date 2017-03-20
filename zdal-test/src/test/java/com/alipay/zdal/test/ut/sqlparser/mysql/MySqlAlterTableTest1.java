package com.alipay.zdal.test.ut.sqlparser.mysql;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.parser.Token;

import junit.framework.TestCase;

public class MySqlAlterTableTest1 extends TestCase {
	
	public void test_alter_0() throws Exception {
        String sql = "ALTER TABLE t1 RENAME t2;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("RENAME TABLE t1 TO t2", output);
    }
    
    public void test_alter_1() throws Exception {
        String sql = "ALTER TABLE t2 ADD d TIMESTAMP;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("ALTER TABLE t2\n\tADD COLUMN d TIMESTAMP", output);
    }
    
    public void test_alter_2() throws Exception {
        String sql = "ALTER TABLE t2 ADD INDEX (d), ADD UNIQUE (a);";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("ALTER TABLE t2\n\tADD INDEX (d),\n\tADD UNIQUE (a)", output);
    }

}
