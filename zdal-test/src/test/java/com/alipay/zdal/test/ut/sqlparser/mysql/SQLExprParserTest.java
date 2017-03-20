package com.alipay.zdal.test.ut.sqlparser.mysql;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIntegerExpr;
import com.alipay.zdal.parser.sql.parser.SQLExprParser;

public class SQLExprParserTest  extends TestCase {
	
    public void test_binary() throws Exception {
        SQLExprParser exprParser = new SQLExprParser("AGE > 5");
        SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) exprParser.expr();
        
        Assert.assertEquals(SQLBinaryOperator.GreaterThan, binaryOpExpr.getOperator());
        
        SQLIdentifierExpr left = (SQLIdentifierExpr) binaryOpExpr.getLeft();
        SQLIntegerExpr right = (SQLIntegerExpr) binaryOpExpr.getRight();
        
        Assert.assertEquals("AGE", left.getName());
        Assert.assertEquals(5, right.getNumber().intValue());
    }

}
