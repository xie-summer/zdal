package com.alipay.zdal.parser.druid.sql.test;

import java.util.Arrays;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: TestUtils.java, v 0.1 2012-5-17 ÉÏÎç10:25:40 xiaoqing.zhouxq Exp $
 */
public class TestUtils {

    public static String outputOracle(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        OracleOutputVisitor visitor = new OracleOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }

    public static String outputOracle(SQLStatement... stmtList) {
        return outputOracle(Arrays.asList(stmtList));
    }

    public static String output(SQLStatement... stmtList) {
        return output(Arrays.asList(stmtList));
    }

    public static String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = new SQLASTOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }
}
