/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLExprTableSource.java, v 0.1 2012-11-17 ÏÂÎç3:22:12 xiaoqing.zhouxq Exp $
 */
public class SQLExprTableSource extends SQLTableSourceImpl {

    private static final long serialVersionUID = 1L;

    protected SQLExpr         expr;

    public SQLExprTableSource() {

    }

    public SQLExprTableSource(SQLExpr expr) {
        this.expr = expr;
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        this.expr.output(buf);
    }
}
