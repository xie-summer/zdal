package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: CursorTest.java, v 0.1 2012-5-17 ÉÏÎç10:12:34 xiaoqing.zhouxq Exp $
 */
public class CursorTest extends TestCase {

    public void test_cursor() throws Exception {
        String sql = "SELECT department_name, CURSOR(SELECT salary, commission_pct FROM employees e WHERE e.department_id = d.department_id) "
                     + "FROM departments d;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);

        String text = TestUtils.outputOracle(stmt);

        Assert.assertEquals("SELECT department_name, CURSOR(\n\t\tSELECT salary, commission_pct\n"
                            + "\t\tFROM employees e\n"
                            + "\t\tWHERE e.department_id = d.department_id\n\t)\n"
                            + "FROM departments d;\n", text);

        System.out.println(text);
    }
}
