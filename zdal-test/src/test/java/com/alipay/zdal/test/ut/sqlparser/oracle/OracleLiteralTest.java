package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleLiteralTest   {
@Test
    public void test_oracle() throws Exception {
        String sql = "SELECT FROM_TZ(TIMESTAMP '2007-11-20 08:00:00', '3:00') FROM DUAL;";

        String expect = "SELECT FROM_TZ(TIMESTAMP '2007-11-20 08:00:00', '3:00')\n"
                        + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
@Test
    public void test_date() throws Exception {
        String sql = "SELECT DATE '1998-12-25' FROM DUAL;";

        String expect = "SELECT DATE '1998-12-25'\n" + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }

}
