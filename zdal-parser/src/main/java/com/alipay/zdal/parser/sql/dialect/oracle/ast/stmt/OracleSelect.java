/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SubqueryFactoringClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelect.java, v 0.1 2012-11-17 ÏÂÎç3:49:23 Exp $
 */
public class OracleSelect extends SQLSelect {

    private static final long       serialVersionUID = 1L;

    private SubqueryFactoringClause factoring;
    private OracleSelectForUpdate   forUpdate;
    private OracleSelectRestriction restriction;

    public OracleSelect() {

    }

    public SubqueryFactoringClause getFactoring() {
        return factoring;
    }

    public void setFactoring(SubqueryFactoringClause factoring) {
        this.factoring = factoring;
    }

    public OracleSelectRestriction getRestriction() {
        return this.restriction;
    }

    public void setRestriction(OracleSelectRestriction restriction) {
        this.restriction = restriction;
    }

    public OracleSelectForUpdate getForUpdate() {
        return this.forUpdate;
    }

    public void setForUpdate(OracleSelectForUpdate forUpdate) {
        this.forUpdate = forUpdate;
    }

    public void output(StringBuffer buf) {
        this.query.output(buf);
        buf.append(" ");

        if (this.orderBy != null)
            this.orderBy.output(buf);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.factoring);
            acceptChild(visitor, this.query);
            acceptChild(visitor, this.restriction);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.forUpdate);
        }
        visitor.endVisit(this);
    }
}
