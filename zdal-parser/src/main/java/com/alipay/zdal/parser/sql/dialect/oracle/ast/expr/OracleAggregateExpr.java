/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import java.io.Serializable;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLAggregateExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleAggregateExpr.java, v 0.1 2012-11-17 ÏÂÎç3:42:47 Exp $
 */
public class OracleAggregateExpr extends SQLAggregateExpr implements Serializable, OracleExpr {

    private static final long serialVersionUID = 1L;
    private OracleAnalytic    over;
    private boolean           ignoreNulls      = false;

    public boolean isUnique() {
        return Option.UNIQUE.equals(this.getOption());
    }

    public void setUnique(boolean unique) {
        this.setOption(Option.UNIQUE);
    }

    public boolean isIgnoreNulls() {
        return this.ignoreNulls;
    }

    public void setIgnoreNulls(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
    }

    public OracleAggregateExpr(String methodName) {
        super(methodName);
    }

    public OracleAggregateExpr(String methodName, Option option) {
        super(methodName, option);
    }

    public OracleAnalytic getOver() {
        return this.over;
    }

    public void setOver(OracleAnalytic over) {
        this.over = over;
    }

    public void output(StringBuffer buf) {
        buf.append(this.methodName);
        buf.append("(");
        int i = 0;
        for (int size = this.arguments.size(); i < size; ++i) {
            ((SQLExpr) this.arguments.get(i)).output(buf);
        }
        buf.append(")");

        if (this.over != null) {
            buf.append(" ");
            this.over.output(buf);
        }
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor) visitor);
        } else {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.arguments);
                acceptChild(visitor, this.over);
            }
            visitor.endVisit(this);
        }
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.arguments);
            acceptChild(visitor, this.over);
        }
        visitor.endVisit(this);
    }
}
