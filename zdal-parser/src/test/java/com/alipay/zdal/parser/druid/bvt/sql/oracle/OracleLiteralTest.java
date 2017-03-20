package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleLiteralTest.java, v 0.1 2012-5-17 ÉÏÎç10:18:43 xiaoqing.zhouxq Exp $
 */
public class OracleLiteralTest extends TestCase {

    public void test_oracle() throws Exception {
        String sql = "SELECT FROM_TZ(TIMESTAMP '2007-11-20 08:00:00', '3:00') FROM DUAL;";

        String expect = "SELECT FROM_TZ(TIMESTAMP '2007-11-20 08:00:00', '3:00')\n"
                        + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }

    public void test_date() throws Exception {
        String sql = "SELECT DATE '1998-12-25' FROM DUAL;";

        String expect = "SELECT DATE '1998-12-25'\n" + "FROM DUAL;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }

}
