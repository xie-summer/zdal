/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.statement.SQLDeleteStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleDeleteStatement.java, v 0.1 2012-11-17 ÏÂÎç3:47:05 Exp $
 */
public class OracleDeleteStatement extends SQLDeleteStatement {

    private static final long     serialVersionUID = 1L;

    private boolean               only             = false;

    private final List<SQLHint>   hints            = new ArrayList<SQLHint>();
    private OracleReturningClause returning        = null;

    public OracleDeleteStatement() {

    }

    public OracleReturningClause getReturning() {
        return returning;
    }

    public void setReturning(OracleReturningClause returning) {
        this.returning = returning;
    }

    public List<SQLHint> getHints() {
        return this.hints;
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, this.tableSource);
            acceptChild(visitor, this.getWhere());
            acceptChild(visitor, returning);
        }

        visitor.endVisit(this);
    }

    public boolean isOnly() {
        return this.only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

}
