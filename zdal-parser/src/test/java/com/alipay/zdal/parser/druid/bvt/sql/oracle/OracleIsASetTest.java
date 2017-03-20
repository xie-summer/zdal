package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleIsASetTest.java, v 0.1 2012-5-17 ÉÏÎç10:18:32 xiaoqing.zhouxq Exp $
 */
public class OracleIsASetTest extends TestCase {

    public void test_is_a_set() throws Exception {
        String sql = "SELECT customer_id, cust_address_ntab FROM customers_demo WHERE cust_address_ntab IS A SET;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals("SELECT customer_id, cust_address_ntab\n" + "FROM customers_demo\n"
                            + "WHERE cust_address_ntab IS A SET;\n", text);

        System.out.println(text);
    }
}
