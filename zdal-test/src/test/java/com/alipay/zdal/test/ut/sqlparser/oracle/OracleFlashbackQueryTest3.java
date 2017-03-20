package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleFlashbackQueryTest3   {
@Test
    public void test_isEmpty() throws Exception {
        String sql = "SELECT DECODE(GROUPING(department_name), 1, 'All Departments', department_name) AS department,"
                     + "DECODE(GROUPING(job_id), 1, 'All Jobs', job_id) AS job, COUNT(*) AS \"Total Empl\", AVG(salary) * 12 AS \"Average Sal\" "
                     + "FROM employees e, departments d\n"
                     + "WHERE d.department_id = e.department_id\n"
                     + "GROUP BY ROLLUP (department_name, job_id);\n";

        String expect = "SELECT DECODE(GROUPING(department_name), 1, 'All Departments', department_name) AS department, "
                        + "DECODE(GROUPING(job_id), 1, 'All Jobs', job_id) AS job, COUNT(*) AS \"Total Empl\", AVG(salary) * 12 AS \"Average Sal\"\n"
                        + "FROM employees e, departments d\n"
                        + "WHERE d.department_id = e.department_id\n"
                        + "GROUP BY ROLLUP(department_name, job_id);\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
