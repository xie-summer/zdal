package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleAnyTest   {
@Test
    public void test_any() throws Exception {
        String sql = "SELECT country, prod, year, s FROM sales_view "
                     + "MODEL PARTITION BY (country) "
                     + "DIMENSION BY (prod, year) MEASURES (sale s) " + "IGNORE NAV "
                     + "UNIQUE DIMENSION RULES UPSERT SEQUENTIAL ORDER (s[ANY, 2000] = 0) "
                     + "ORDER BY country, prod, year;";

        String expect = "SELECT country, prod, year, s\n" + "FROM sales_view\n" + "MODEL\n"
                        + "\tPARTITION BY (country)\n" + "\tDIMENSION BY (prod, year)\n"
                        + "\tMEASURES (sale s)\n" + "\tIGNORE NAV\n" + "\tUNIQUE DIMENSION\n"
                        + "\tRULES UPSERT SEQUENTIAL ORDER (s[ANY, 2000] = 0)\n"
                        + "ORDER BY country, prod, year;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
