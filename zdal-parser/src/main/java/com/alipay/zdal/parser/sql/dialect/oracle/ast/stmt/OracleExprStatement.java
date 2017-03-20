/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleExprStatement.java, v 0.1 2012-11-17 ÏÂÎç3:47:25 Exp $
 */
public class OracleExprStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           expr;

    public OracleExprStatement() {

    }

    public OracleExprStatement(SQLExpr expr) {
        this.expr = expr;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
        }
        visitor.endVisit(this);

    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

}
