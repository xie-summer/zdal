package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleEXTRACTTest.java, v 0.1 2012-5-17 ÉÏÎç10:16:47 xiaoqing.zhouxq Exp $
 */
public class OracleEXTRACTTest extends TestCase {

    public void test_Extract() throws Exception {
        String sql = "SELECT EXTRACT(YEAR FROM DATE '1998-03-07') FROM DUAL;";

        String expect = "SELECT EXTRACT(YEAR FROM DATE '1998-03-07')\n" + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
