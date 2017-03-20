/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.statement.SQLUpdateStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateStatement.java, v 0.1 2012-11-17 ÏÂÎç3:51:37 Exp $
 */
public class OracleUpdateStatement extends SQLUpdateStatement implements OracleStatement {

    private static final long   serialVersionUID = 1L;

    private final List<SQLHint> hints            = new ArrayList<SQLHint>(1);
    private boolean             only             = false;
    private String              alias;
    private SQLExpr             where;

    private List<SQLExpr>       returning        = new ArrayList<SQLExpr>();
    private List<SQLExpr>       returningInto    = new ArrayList<SQLExpr>();

    public OracleUpdateStatement() {

    }

    public List<SQLExpr> getReturning() {
        return returning;
    }

    public void setReturning(List<SQLExpr> returning) {
        this.returning = returning;
    }

    public List<SQLExpr> getReturningInto() {
        return returningInto;
    }

    public void setReturningInto(List<SQLExpr> returningInto) {
        this.returningInto = returningInto;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }

        super.accept(visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, tableSource);
            acceptChild(visitor, items);
            acceptChild(visitor, where);
            acceptChild(visitor, returning);
            acceptChild(visitor, returningInto);
        }

        visitor.endVisit(this);
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public SQLExpr getWhere() {
        return this.where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public boolean isOnly() {
        return this.only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public List<SQLHint> getHints() {
        return this.hints;
    }
}
