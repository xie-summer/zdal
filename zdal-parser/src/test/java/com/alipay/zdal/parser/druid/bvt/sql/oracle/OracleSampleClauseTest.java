package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSampleClauseTest.java, v 0.1 2012-5-17 ÉÏÎç10:19:38 xiaoqing.zhouxq Exp $
 */
public class OracleSampleClauseTest extends TestCase {

    public void test_0() throws Exception {
        String sql = "SELECT COUNT(*) * 10 FROM orders SAMPLE (10);";

        String expected = "SELECT COUNT(*) * 10\n" + "FROM orders SAMPLE (10);\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

    public void test_1() throws Exception {
        String sql = "SELECT COUNT(*) * 10 FROM orders SAMPLE (10) SEED (1);";

        String expected = "SELECT COUNT(*) * 10\n" + "FROM orders SAMPLE (10) SEED (1);\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expected, text);

        System.out.println(text);
    }

}
