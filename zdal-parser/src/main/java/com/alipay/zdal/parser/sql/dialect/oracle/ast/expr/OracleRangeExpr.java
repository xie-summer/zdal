/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleRangeExpr.java, v 0.1 2012-11-17 ÏÂÎç3:44:24 Exp $
 */
public class OracleRangeExpr extends OracleSQLObjectImpl implements SQLExpr {

    private static final long serialVersionUID = 1L;

    private SQLExpr           lowBound;
    private SQLExpr           upBound;

    public OracleRangeExpr() {

    }

    public OracleRangeExpr(SQLExpr lowBound, SQLExpr upBound) {
        this.lowBound = lowBound;
        this.upBound = upBound;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, lowBound);
            acceptChild(visitor, upBound);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getLowBound() {
        return lowBound;
    }

    public void setLowBound(SQLExpr lowBound) {
        this.lowBound = lowBound;
    }

    public SQLExpr getUpBound() {
        return upBound;
    }

    public void setUpBound(SQLExpr upBound) {
        this.upBound = upBound;
    }

}
