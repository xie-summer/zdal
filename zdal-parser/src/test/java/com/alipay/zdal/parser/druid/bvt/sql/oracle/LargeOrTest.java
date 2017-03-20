package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: LargeOrTest.java, v 0.1 2012-5-17 ÉÏÎç10:12:38 xiaoqing.zhouxq Exp $
 */
public class LargeOrTest extends TestCase {

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
