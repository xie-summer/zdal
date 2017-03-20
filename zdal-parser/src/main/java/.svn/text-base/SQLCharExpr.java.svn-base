/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCharExpr.java, v 0.1 2012-11-17 ÏÂÎç3:16:30 xiaoqing.zhouxq Exp $
 */
public class SQLCharExpr extends SQLTextLiteralExpr {

    private static final long serialVersionUID = 1L;

    public SQLCharExpr() {

    }

    public SQLCharExpr(String text) {
        super(text);
    }

    @Override
    public void output(StringBuffer buf) {
        if ((this.text == null) || (this.text.length() == 0)) {
            buf.append("NULL");
        } else {
            buf.append("'");
            buf.append(this.text.replaceAll("'", "''"));
            buf.append("'");
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
