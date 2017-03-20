package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class OracleIsEmptyTest   {
@Test
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
