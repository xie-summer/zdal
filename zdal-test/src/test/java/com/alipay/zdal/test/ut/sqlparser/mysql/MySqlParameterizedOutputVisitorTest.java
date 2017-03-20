package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;

public class MySqlParameterizedOutputVisitorTest {
	
	@Test
	public void test_0() throws Exception {
        validate("SELECT * FROM T WHERE ID IN (?, ?, ?)", "SELECT *\nFROM T\nWHERE ID IN (?)");
        validate("SELECT * FROM T WHERE ID = 5", "SELECT *\nFROM T\nWHERE ID = ?");
        validate("SELECT * FROM T WHERE 1 = 0 AND ID = 5",
            "SELECT *\nFROM T\nWHERE 1 = 0\nAND ID = ?");
        validate("SELECT * FROM T WHERE ID = ? OR ID = ?", "SELECT *\nFROM T\nWHERE ID = ?");
        validate("SELECT * FROM T WHERE A.ID = ? OR A.ID = ?", "SELECT *\nFROM T\nWHERE A.ID = ?");
        validate("SELECT * FROM T WHERE 1 = 0 OR a.id = ? OR a.id = ? OR a.id = ? OR a.id = ?",
            "SELECT *\nFROM T\nWHERE 1 = 0\nOR a.id = ?");
        validateOracle(
            "SELECT * FROM T WHERE 1 = 0 OR a.id = ? OR a.id = ? OR a.id = ? OR a.id = ?",
            "SELECT *\nFROM T\nWHERE 1 = 0\nOR a.id = ?");
        validateOracle("SELECT * FROM T WHERE A.ID = ? OR A.ID = ?",
            "SELECT *\nFROM T\nWHERE A.ID = ?");
        validate("INSERT INTO T (F1, F2) VALUES(?, ?), (?, ?), (?, ?)",
            "INSERT INTO T (F1, F2) VALUES (?, ?)");
        validate(
            "update net_device d, sys_user u set d.resp_user_id=u.id where d.resp_user_login_name=u.username and d.id in (42354)", //
            "UPDATE net_device d, sys_user u\nSET d.resp_user_id = u.id\nWHERE d.resp_user_login_name = u.username\nAND d.id IN (?)");

        // System.out.println("SELECT * FORM A WHERE ID = (##)".replaceAll("##", "?"));
    }

    void validate(String sql, String expect) {

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        StringBuilder out = new StringBuilder();
        MySqlParameterizedOutputVisitor visitor = new MySqlParameterizedOutputVisitor(out);
        statemen.accept(visitor);

        Assert.assertEquals(expect, out.toString());
    }

    void validateOracle(String sql, String expect) {

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        StringBuilder out = new StringBuilder();
        OracleParameterizedOutputVisitor visitor = new OracleParameterizedOutputVisitor(out, false);
        statemen.accept(visitor);

        Assert.assertEquals(expect, out.toString());
    }

}
