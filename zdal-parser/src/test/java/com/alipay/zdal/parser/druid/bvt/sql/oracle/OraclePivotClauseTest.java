package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OraclePivotClauseTest.java, v 0.1 2012-5-17 ÉÏÎç10:19:32 xiaoqing.zhouxq Exp $
 */
public class OraclePivotClauseTest extends TestCase {

    public void test_0() throws Exception {
        String sql = "SELECT *\n"
                     + "FROM pivot_table\n"
                     + "UNPIVOT (yearly_total FOR order_mode IN (store AS 'direct', internet AS 'online'))\n"
                     + "ORDER BY year, order_mode;";

        String expected = "SELECT *\n"
                          + "FROM pivot_table\n"
                          + "UNPIVOT (yearly_total FOR order_mode IN (store AS 'direct', internet AS 'online'))\n"
                          + "ORDER BY year, order_mode;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

    public void test_pivot() throws Exception {
        String sql = "SELECT *\n"
                     + "FROM (SELECT EXTRACT(YEAR FROM order_date) year, order_mode, order_total FROM orders)\n"
                     + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        String expected = "SELECT *\n"
                          + "FROM (\n\tSELECT EXTRACT(YEAR FROM order_date) AS year, order_mode, order_total\n"
                          + "\tFROM orders\n"
                          + ")\n"
                          + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

    public void test_pivot_1() throws Exception {
        String sql = "SELECT *\n"
                     + "FROM (SELECT EXTRACT(YEAR FROM order_date) as year, order_mode, order_total FROM orders)\n"
                     + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        String expected = "SELECT *\n"
                          + "FROM (\n\tSELECT EXTRACT(YEAR FROM order_date) AS year, order_mode, order_total\n"
                          + "\tFROM orders\n"
                          + ")\n"
                          + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

    public void test_pivot_2() throws Exception {
        String sql = "SELECT *\n"
                     + "FROM (SELECT EXTRACT(YEAR FROM order_date) as day, order_mode, order_total FROM orders)\n"
                     + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        String expected = "SELECT *\n"
                          + "FROM (\n\tSELECT EXTRACT(YEAR FROM order_date) AS day, order_mode, order_total\n"
                          + "\tFROM orders\n"
                          + ")\n"
                          + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

    public void test_pivot_3() throws Exception {
        String sql = "SELECT *\n"
                     + "FROM (SELECT EXTRACT(YEAR FROM order_date) day, order_mode YEAR, order_total FROM orders)\n"
                     + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        String expected = "SELECT *\n"
                          + "FROM (\n\tSELECT EXTRACT(YEAR FROM order_date) AS day, order_mode AS YEAR, order_total\n"
                          + "\tFROM orders\n"
                          + ")\n"
                          + "PIVOT (SUM(order_total) FOR order_mode IN ('direct' AS Store, 'online' AS Internet));\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }
}
