package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleOuterTest.java, v 0.1 2012-5-17 ÉÏÎç10:19:29 xiaoqing.zhouxq Exp $
 */
public class OracleOuterTest extends TestCase {

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
