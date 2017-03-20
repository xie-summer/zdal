/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSelectItem.java, v 0.1 2012-11-17 ÏÂÎç3:23:31 xiaoqing.zhouxq Exp $
 */
public class SQLSelectItem extends SQLObjectImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           expr;
    private String            alias;

    public SQLSelectItem() {

    }

    public SQLSelectItem(SQLExpr expr) {
        this(expr, null);
    }

    public SQLSelectItem(SQLExpr expr, String alias) {

        this.expr = expr;
        this.alias = alias;
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void output(StringBuffer buf) {
        this.expr.output(buf);
        if ((this.alias != null) && (this.alias.length() != 0)) {
            buf.append(" AS ");
            buf.append(this.alias);
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
}
