package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleEXTRACTTest   {
@Test
    public void test_Extract() throws Exception {
        String sql = "SELECT EXTRACT(YEAR FROM DATE '1998-03-07') FROM DUAL;";

        String expect = "SELECT EXTRACT(YEAR FROM DATE '1998-03-07')\n" + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
