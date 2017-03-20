/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLAllColumnExpr.java, v 0.1 2012-11-17 ÏÂÎç3:14:31 xiaoqing.zhouxq Exp $
 */
public class SQLAllColumnExpr extends SQLExprImpl {

    private static final long serialVersionUID = 1L;

    public SQLAllColumnExpr() {

    }

    public void output(StringBuffer buf) {
        buf.append("*");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof SQLAllColumnExpr;
    }
}
