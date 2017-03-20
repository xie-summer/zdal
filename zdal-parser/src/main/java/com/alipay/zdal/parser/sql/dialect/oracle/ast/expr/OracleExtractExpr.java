/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleExtractExpr.java, v 0.1 2012-11-17 ÏÂÎç3:43:57 Exp $
 */
public class OracleExtractExpr extends SQLExprImpl implements OracleExpr {

    private static final long  serialVersionUID = 1L;
    private OracleDateTimeUnit unit;
    private SQLExpr            from;

    public OracleExtractExpr() {

    }

    public OracleDateTimeUnit getUnit() {
        return this.unit;
    }

    public void setUnit(OracleDateTimeUnit unit) {
        this.unit = unit;
    }

    public SQLExpr getFrom() {
        return this.from;
    }

    public void setFrom(SQLExpr from) {
        this.from = from;
    }

    public void output(StringBuffer buf) {
        buf.append("EXTRACT(");
        buf.append(this.unit.name());
        buf.append(" FROM ");
        this.from.output(buf);
        buf.append(")");
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.from);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
        OracleExtractExpr other = (OracleExtractExpr) obj;
        if (from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!from.equals(other.from)) {
            return false;
        }
        if (unit != other.unit) {
            return false;
        }
        return true;
    }

}
