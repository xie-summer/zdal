package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;

public abstract class MysqlTest extends TestCase {
    protected String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);

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
