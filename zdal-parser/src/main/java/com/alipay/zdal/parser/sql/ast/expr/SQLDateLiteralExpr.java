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
 * @version $Id: SQLDateLiteralExpr.java, v 0.1 2012-11-17 ÏÂÎç3:16:45 xiaoqing.zhouxq Exp $
 */
public class SQLDateLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long   serialVersionUID = 1L;

    private SQLDateLiteralValue value            = new SQLDateLiteralValue();

    public SQLDateLiteralExpr() {

    }

    public SQLDateLiteralValue getValue() {
        return value;
    }

    public void setValue(SQLDateLiteralValue value) {
        this.value = value;
    }

    public void output(StringBuffer buf) {
        buf.append("DATE'");
        this.value.output(buf);
        buf.append("'");
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLDateLiteralExpr other = (SQLDateLiteralExpr) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
