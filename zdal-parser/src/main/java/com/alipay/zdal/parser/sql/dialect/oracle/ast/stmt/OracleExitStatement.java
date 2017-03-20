/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleExitStatement.java, v 0.1 2012-11-17 ÏÂÎç3:47:15 Exp $
 */
public class OracleExitStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;
    private SQLExpr           when;

    public SQLExpr getWhen() {
        return when;
    }

    public void setWhen(SQLExpr when) {
        this.when = when;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, when);
        }
        visitor.endVisit(this);
    }

}
