package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleGroupingSetTest.java, v 0.1 2012-5-17 ÉÏÎç10:17:08 xiaoqing.zhouxq Exp $
 */
public class OracleGroupingSetTest extends TestCase {

    public void test_interval() throws Exception {
        String sql = "SELECT channel_desc, calendar_month_desc, co.country_id, "
                     + "TO_CHAR(sum(amount_sold) , '9,999,999,999') AS SALES$\n"
                     + "FROM sales, customers, times, channels, countries co\n"
                     + "WHERE sales.time_id = times.time_id AND sales.cust_id = customers.cust_id AND sales.channel_id = channels.channel_id "
                     + "AND customers.country_id = co.country_id AND channels.channel_desc IN ('Direct Sales', 'Internet') "
                     + "AND times.calendar_month_desc IN ('2000-09', '2000-10') "
                     + "AND co.country_id IN ('UK', 'US')\n"
                     + "GROUP BY GROUPING SETS((channel_desc, calendar_month_desc, co.country_id), (channel_desc, co.country_id), "
                     + "( calendar_month_desc, co.country_id) );\n";

        String expected = "SELECT channel_desc, calendar_month_desc, co.country_id, " //
                          + "TO_CHAR(sum(amount_sold), '9,999,999,999') AS SALES$\n" //
                          + "FROM sales, customers, times, channels, countries co\n" //
                          + "WHERE sales.time_id = times.time_id" //
                          + "\n\tAND sales.cust_id = customers.cust_id" //
                          + "\n\tAND sales.channel_id = channels.channel_id" //
                          + "\n\tAND customers.country_id = co.country_id" //
                          + "\n\tAND channels.channel_desc IN ('Direct Sales', 'Internet')" //
                          + "\n\tAND times.calendar_month_desc IN ('2000-09', '2000-10')"
                          + "\n\tAND co.country_id IN ('UK', 'US')\n"
                          + "GROUP BY GROUPING SETS ((channel_desc, calendar_month_desc, co.country_id), (channel_desc, co.country_id), "
                          + "(calendar_month_desc, co.country_id));\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        System.out.println(text);
    }

}
