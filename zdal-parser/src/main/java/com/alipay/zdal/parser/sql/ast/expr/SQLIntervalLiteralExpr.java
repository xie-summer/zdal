/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLIntervalLiteralExpr.java, v 0.1 2012-11-17 ÏÂÎç3:17:54 xiaoqing.zhouxq Exp $
 */
public class SQLIntervalLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    private Character         sign             = null;

    public Character getSign() {
        return sign;
    }

    public void setSign(Character sign) {
        this.sign = sign;
    }

    public SQLIntervalLiteralExpr() {

    }

    @Override
    public void output(StringBuffer buf) {
        buf.append("INTERVAL");
        if (sign != null) {
            buf.append(sign.charValue());
        }
        throw new SqlParserException("TODO");
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
        result = prime * result + ((sign == null) ? 0 : sign.hashCode());
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
        SQLIntervalLiteralExpr other = (SQLIntervalLiteralExpr) obj;
        if (sign == null) {
            if (other.sign != null) {
                return false;
            }
        } else if (!sign.equals(other.sign)) {
            return false;
        }
        return true;
    }

}
