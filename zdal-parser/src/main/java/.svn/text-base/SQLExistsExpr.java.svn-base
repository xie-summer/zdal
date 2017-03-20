/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import java.io.Serializable;

import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLExistsExpr.java, v 0.1 2012-11-17 ÏÂÎç3:17:09 xiaoqing.zhouxq Exp $
 */
public class SQLExistsExpr extends SQLExprImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean            not              = false;
    public SQLSelect          subQuery;

    public SQLExistsExpr() {

    }

    public SQLExistsExpr(SQLSelect subQuery) {

        this.subQuery = subQuery;
    }

    public SQLExistsExpr(SQLSelect subQuery, boolean not) {

        this.subQuery = subQuery;
        this.not = not;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public SQLSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(SQLSelect subQuery) {
        this.subQuery = subQuery;
    }

    public void output(StringBuffer buf) {
        if (this.not) {
            buf.append("NOT ");
        }
        buf.append("EXISTS (");
        this.subQuery.output(buf);
        buf.append(")");
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.subQuery);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (not ? 1231 : 1237);
        result = prime * result + ((subQuery == null) ? 0 : subQuery.hashCode());
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
        SQLExistsExpr other = (SQLExistsExpr) obj;
        if (not != other.not) {
            return false;
        }
        if (subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        } else if (!subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
    }
}
