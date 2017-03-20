package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleAnalyticTest   {
@Test
    public void test_0() throws Exception {
        String sql = "SELECT last_name, salary, STDDEV(salary) OVER (ORDER BY hire_date) \"StdDev\" "
                     + "FROM employees " + "WHERE department_id = 30;";

        String expect = "SELECT last_name, salary, STDDEV(salary) OVER (ORDER BY hire_date) AS \"StdDev\"\n"
                        + "FROM employees\n" + "WHERE department_id = 30;\n";
        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }

    public void test_1() throws Exception {
        String sql = "SELECT submit_date, num_votes, TRUNC(AVG(num_votes) OVER(PARTITION BY submit_date ORDER BY submit_date ROWS UNBOUNDED PRECEDING)) AVG_VOTE_PER_DAY\n"
                     + "FROM vote_count\n" + "ORDER BY submit_date;\n";

        String expect = "SELECT submit_date, num_votes, TRUNC(AVG(num_votes) OVER (PARTITION BY submit_date ORDER BY submit_date ROWS UNBOUNDED PRECEDING)) AS AVG_VOTE_PER_DAY\n"
                        + "FROM vote_count\n" + "ORDER BY submit_date;\n";
        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
