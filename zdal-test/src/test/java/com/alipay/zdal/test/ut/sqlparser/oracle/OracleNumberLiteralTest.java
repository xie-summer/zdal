package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleNumberLiteralTest   {
@Test
    public void test_number_literal() throws Exception {
        String sql = "SELECT 7, +255, 0.5, +6.34,25e-03, +6.34F, 0.5d, -1D FROM DUAL";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(
            "SELECT 7, 255, 0.5, 6.34, 0.025\n\t, 6.34F, 0.5D, -1.0D\nFROM DUAL;\n", text);

        System.out.println(text);
    }
@Test
    public void test_number_literal_2() throws Exception {
        String sql = "SELECT BINARY_FLOAT_INFINITY FROM DUAL";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals("SELECT BINARY_FLOAT_INFINITY\nFROM DUAL;\n", text);

        System.out.println(text);
    }
}
