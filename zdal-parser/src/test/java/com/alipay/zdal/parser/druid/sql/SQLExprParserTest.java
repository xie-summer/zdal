package com.alipay.zdal.parser.druid.sql;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.parser.SQLExprParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLExprParserTest.java, v 0.1 2012-5-17 ÉÏÎç10:24:10 xiaoqing.zhouxq Exp $
 */
public class SQLExprParserTest extends TestCase {
    public void test_binary() throws Exception {
        SQLExprParser exprParser = new SQLExprParser("AGE > ?");
        SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) exprParser.expr();

        Assert.assertEquals(SQLBinaryOperator.GreaterThan, binaryOpExpr.getOperator());

        SQLIdentifierExpr left = (SQLIdentifierExpr) binaryOpExpr.getLeft();
        SQLVariantRefExpr right = (SQLVariantRefExpr) binaryOpExpr.getRight();

        Assert.assertEquals("AGE", left.getName());
        Assert.assertEquals("?", right.getName());
    }
}
