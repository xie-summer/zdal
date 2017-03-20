package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleFlashbackQueryTest   {
@Test
    public void test_isEmpty() throws Exception {
        String sql = "SELECT salary FROM employees AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '1' DAY) WHERE last_name = 'Chung';";

        String expect = "SELECT salary\n" + "FROM employees\n"
                        + "AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '1' DAY)\n"
                        + "WHERE last_name = 'Chung';\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
