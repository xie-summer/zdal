package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleIsASetTest   {
@Test
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
