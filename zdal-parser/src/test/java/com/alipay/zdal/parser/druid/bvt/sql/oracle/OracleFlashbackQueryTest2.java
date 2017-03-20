package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleFlashbackQueryTest2.java, v 0.1 2012-5-17 ÉÏÎç10:16:54 xiaoqing.zhouxq Exp $
 */
public class OracleFlashbackQueryTest2 extends TestCase {

    public void test_isEmpty() throws Exception {
        String sql = "SELECT salary FROM employees\n"
                     + "VERSIONS BETWEEN TIMESTAMP SYSTIMESTAMP - INTERVAL '10' MINUTE AND SYSTIMESTAMP - INTERVAL '1' MINUTE\n"
                     + "WHERE last_name = 'Chung';";

        String expect = "SELECT salary\n"
                        + "FROM employees\n"
                        + "VERSIONS BETWEEN TIMESTAMP SYSTIMESTAMP - INTERVAL '10' MINUTE AND SYSTIMESTAMP - INTERVAL '1' MINUTE\n"
                        + "WHERE last_name = 'Chung';\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
