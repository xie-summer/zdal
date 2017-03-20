/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLNCharExpr.java, v 0.1 2012-11-17 ÏÂÎç3:18:25 xiaoqing.zhouxq Exp $
 */
public class SQLNCharExpr extends SQLTextLiteralExpr {

    private static final long serialVersionUID = 1L;

    public SQLNCharExpr() {

    }

    public SQLNCharExpr(String text) {
        super(text);
    }

    public void output(StringBuffer buf) {
        if ((this.text == null) || (this.text.length() == 0)) {
            buf.append("NULL");
            return;
        }

        buf.append("N'");
        buf.append(this.text.replaceAll("'", "''"));
        buf.append("'");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
