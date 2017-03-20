package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleSampleClauseTest   {
@Test
    public void test_0() throws Exception {
        String sql = "SELECT COUNT(*) * 10 FROM orders SAMPLE (10);";

        String expected = "SELECT COUNT(*) * 10\n" + "FROM orders SAMPLE (10);\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }
@Test
    public void test_1() throws Exception {
        String sql = "SELECT COUNT(*) * 10 FROM orders SAMPLE (10) SEED (1);";

        String expected = "SELECT COUNT(*) * 10\n" + "FROM orders SAMPLE (10) SEED (1);\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

}
