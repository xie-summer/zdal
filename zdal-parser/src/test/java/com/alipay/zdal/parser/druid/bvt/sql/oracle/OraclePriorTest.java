package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OraclePriorTest.java, v 0.1 2012-5-17 ÉÏÎç10:19:35 xiaoqing.zhouxq Exp $
 */
public class OraclePriorTest extends TestCase {

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
