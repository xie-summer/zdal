package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class CursorTest {
@Test
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
