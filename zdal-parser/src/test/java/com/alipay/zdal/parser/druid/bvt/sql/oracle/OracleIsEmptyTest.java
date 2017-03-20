package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleIsEmptyTest.java, v 0.1 2012-5-17 ÉÏÎç10:18:36 xiaoqing.zhouxq Exp $
 */
public class OracleIsEmptyTest extends TestCase {

    public void test_isEmpty() throws Exception {
        String sql = "SELECT product_id, TO_CHAR(ad_finaltext) FROM print_media WHERE ad_textdocs_ntab IS NOT EMPTY;";

        String expect = "SELECT product_id, TO_CHAR(ad_finaltext)\n" + "FROM print_media\n"
                        + "WHERE ad_textdocs_ntab IS NOT EMPTY;\n";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals(expect, text);

        System.out.println(text);
    }
}
