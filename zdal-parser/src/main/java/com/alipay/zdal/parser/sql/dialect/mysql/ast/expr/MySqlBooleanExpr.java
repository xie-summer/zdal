/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.expr;

import com.alipay.zdal.parser.sql.ast.expr.SQLLiteralExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlBooleanExpr.java, v 0.1 2012-11-17 ÏÂÎç3:30:04 Exp $
 */
public class MySqlBooleanExpr extends MySqlExprImpl implements MySqlExpr, SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    private boolean           value;

    public MySqlBooleanExpr() {

    }

    public MySqlBooleanExpr(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((MySqlASTVisitor) visitor);
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("x");
        buf.append(value ? "TRUE" : "FALSE");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value ? 1231 : 1237);
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
        MySqlBooleanExpr other = (MySqlBooleanExpr) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

}
