/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import java.util.BitSet;

import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * SQL-92
 * <p>
 * &ltbit string literal> ::= B &ltquote> [ &ltbit> ... ] &ltquote> [ { &ltseparator> ... &ltquote> [ &ltbit> ... ]
 * &ltquote> }... ]
 * </p>
 * 
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLBitStringLiteralExpr.java, v 0.1 2012-11-17 ÏÂÎç3:16:02 xiaoqing.zhouxq Exp $
 */
public class SQLBitStringLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    private BitSet            value;

    public SQLBitStringLiteralExpr() {

    }

    public BitSet getValue() {
        return value;
    }

    public void setValue(BitSet value) {
        this.value = value;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("b'");
        for (int i = 0; i < value.length(); ++i) {
            buf.append(value);
        }
        buf.append("'");
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
        SQLBitStringLiteralExpr other = (SQLBitStringLiteralExpr) obj;
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
