/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.statement.SQLConstaint;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleAlterTableAddConstaint.java, v 0.1 2012-11-17 ÏÂÎç3:45:08 Exp $
 */
public class OracleAlterTableAddConstaint extends OracleAlterTableItem {

    private static final long serialVersionUID = 1L;

    private SQLConstaint      constraint;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, constraint);
        }
        visitor.endVisit(this);
    }

    public SQLConstaint getConstraint() {
        return constraint;
    }

    public void setConstraint(SQLConstaint constraint) {
        this.constraint = constraint;
    }

}
