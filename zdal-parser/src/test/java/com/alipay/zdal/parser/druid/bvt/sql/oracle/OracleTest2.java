package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleTest2.java, v 0.1 2012-5-17 ÉÏÎç10:22:30 xiaoqing.zhouxq Exp $
 */
public class OracleTest2 extends TestCase {

    public void test_isEmpty() throws Exception {
        String sql = "SELECT NAME FROM V$ARCHIVED_LOG;";

        String expect = "SELECT NAME\nFROM V$ARCHIVED_LOG;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
