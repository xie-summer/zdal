package com.alipay.zdal.parser.druid.sql;

import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleTest.java, v 0.1 2012-5-17 ÉÏÎç10:24:07 xiaoqing.zhouxq Exp $
 */
public abstract class OracleTest extends TestCase {
    protected String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        OracleOutputVisitor visitor = new OracleOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }

    protected void print(List<SQLStatement> stmtList) {
        String text = output(stmtList);
        String outputProperty = System.getProperty("druid.output");
        if ("false".equals(outputProperty)) {
            return;
        }
        System.out.println(text);
    }

}
