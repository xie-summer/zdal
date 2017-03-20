package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;


import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleOuterTest   {
@Test
    public void test_oracle() throws Exception {
        String sql = "SELECT employee_id, manager_id\n" + "FROM employees\n"
                     + "WHERE employees.manager_id(+) = employees.employee_id;";

        String expect = "SELECT employee_id, manager_id\n" + "FROM employees\n"
                        + "WHERE employees.manager_id(+) = employees.employee_id;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }

}
