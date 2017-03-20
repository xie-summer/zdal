package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OraclePriorTest   {
@Test
    public void test_oracle() throws Exception {
        String sql = "SELECT employee_id, last_name, manager_id FROM employees CONNECT BY PRIOR employee_id = manager_id;";

        String expect = "SELECT employee_id, last_name, manager_id\n" + "FROM employees\n"
                        + "CONNECT BY PRIOR employee_id = manager_id;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
@Test
    public void test_oracle_2() throws Exception {
        String sql = "SELECT last_name, employee_id, manager_id, LEVEL\n" + "FROM employees\n"
                     + "START WITH employee_id = 100\n"
                     + "CONNECT BY PRIOR employee_id = manager_id\n"
                     + "ORDER SIBLINGS BY last_name;";

        String expect = "SELECT last_name, employee_id, manager_id, LEVEL\n" + "FROM employees\n"
                        + "START WITH employee_id = 100\n"
                        + "CONNECT BY PRIOR employee_id = manager_id\n"
                        + "ORDER SIBLINGS BY last_name;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
