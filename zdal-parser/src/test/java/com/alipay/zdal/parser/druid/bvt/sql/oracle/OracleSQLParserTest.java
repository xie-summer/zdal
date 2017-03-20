package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSQLParserTest.java, v 0.1 2012-5-17 ÉÏÎç10:22:18 xiaoqing.zhouxq Exp $
 */
public class OracleSQLParserTest extends TestCase {

    public void test_1() throws Exception {
        String sql = "SELECT employees_seq.nextval FROM DUAL;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        Assert.assertEquals(1, statementList.size());

        String text = output(statementList);
        System.out.println(text);
    }

    public void test_2() throws Exception {
        String sql = "SELECT LPAD(' ',2*(LEVEL-1)) || last_name org_chart, employee_id, manager_id, job_id FROM employees WHERE job_id != 'FI_MGR' START WITH job_id = 'AD_VP' CONNECT BY PRIOR employee_id = manager_id; ";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        Assert.assertEquals(1, statementList.size());

        String text = output(statementList);
        System.out.println(text);
    }

    private String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        OracleOutputVisitor visitor = new OracleOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }
}
