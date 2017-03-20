package com.alipay.zdal.test.ut.sqlparser.mysql;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.parser.Token;

import junit.framework.TestCase;

public class MySqlAlterTableTest extends TestCase {
	
	 public void test_alter_0() throws Exception {
	        String sql = "ALTER TABLE `test`.`tb1` CHANGE COLUMN `fname` `fname1` VARCHAR(45) NULL DEFAULT NULL  ;";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        SQLStatement stmt = parser.parseStatementList().get(0);
	        parser.match(Token.EOF);
	        String output = SQLUtils.toMySqlString(stmt);
	        Assert.assertEquals("ALTER TABLE `test`.`tb1`\n\tCHANGE COLUMN `fname` `fname1` VARCHAR(45) NULL", output);
	    }
	    
	    public void test_alter_1() throws Exception {
	        String sql = "ALTER TABLE `test`.`tb1` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        SQLStatement stmt = parser.parseStatementList().get(0);
	        parser.match(Token.EOF);
	        String output = SQLUtils.toMySqlString(stmt);
	        Assert.assertEquals("ALTER TABLE `test`.`tb1`\n\tCHARACTER SET = utf8, COLLATE = utf8_general_ci", output);
	    }
	    
	    public void test_alter_2() throws Exception {
	        String sql = "ALTER TABLE `test`.`tb1` ADD INDEX `f` (`fname` ASC) ;";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        SQLStatement stmt = parser.parseStatementList().get(0);
	        parser.match(Token.EOF);
	        String output = SQLUtils.toMySqlString(stmt);
	        Assert.assertEquals("ALTER TABLE `test`.`tb1`\n\tADD INDEX `f` (`fname` ASC)", output);
	    }
	    
	    public void test_alter_3() throws Exception {
	        String sql = "ALTER TABLE `test`.`tb1` ENGINE = InnoDB ;";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        SQLStatement stmt = parser.parseStatementList().get(0);
	        parser.match(Token.EOF);
	        String output = SQLUtils.toMySqlString(stmt);
	        Assert.assertEquals("ALTER TABLE `test`.`tb1`\n\tENGINE = InnoDB", output);
	    }
	    
	    public void test_alter_4() throws Exception {
	        String sql = "ALTER TABLE `test`.`tb1` COLLATE = utf8_general_ci , PACK_KEYS = Pack All , ENGINE = InnoDB ;";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        SQLStatement stmt = parser.parseStatementList().get(0);
	        parser.match(Token.EOF);
	        String output = SQLUtils.toMySqlString(stmt);
	        Assert.assertEquals("ALTER TABLE `test`.`tb1`\n\tCOLLATE = utf8_general_ci,\n\tPACK_KEYS = PACK ALL,\n\tENGINE = InnoDB", output);
	    }

}
