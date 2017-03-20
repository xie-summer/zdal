package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;


public class LargeOrTest   {
@Test
    public void test_largeOr() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT 1 FROM T WHERE ID = ?");
        for (int i = 0; i < 10000; ++i) {
            buf.append(" OR ID = ?");
        }
        String sql = buf.toString();
        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);
        SQLSelectQueryBlock select = (SQLSelectQueryBlock) stmt.getSelect().getQuery();
        SQLBinaryOpExpr where = (SQLBinaryOpExpr) select.getWhere();
        SQLBinaryOpExpr last = (SQLBinaryOpExpr) where.getRight();
        Assert.assertEquals(SQLBinaryOperator.Equality, last.getOperator());
    }
@Test
    public void test_largeAnd() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT 1 FROM T WHERE ID = ?");
        for (int i = 0; i < 10000; ++i) {
            buf.append(" AND ID = ?");
        }
        String sql = buf.toString();
        OracleStatementParser parser = new OracleStatementParser(sql);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatementList().get(0);
        SQLSelectQueryBlock select = (SQLSelectQueryBlock) stmt.getSelect().getQuery();
        SQLBinaryOpExpr where = (SQLBinaryOpExpr) select.getWhere();
        SQLBinaryOpExpr last = (SQLBinaryOpExpr) where.getRight();
        Assert.assertEquals(SQLBinaryOperator.Equality, last.getOperator());
    }
}
