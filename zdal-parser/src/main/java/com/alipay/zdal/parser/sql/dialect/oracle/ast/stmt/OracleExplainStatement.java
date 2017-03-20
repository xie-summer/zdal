/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleExplainStatement.java, v 0.1 2012-11-17 ÏÂÎç3:47:20 Exp $
 */
public class OracleExplainStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLCharExpr       statementId;
    private SQLExpr           into;
    private SQLStatement      forStatement;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statementId);
            acceptChild(visitor, into);
            acceptChild(visitor, forStatement);
        }
        visitor.endVisit(this);
    }

    public SQLCharExpr getStatementId() {
        return statementId;
    }

    public void setStatementId(SQLCharExpr statementId) {
        this.statementId = statementId;
    }

    public SQLExpr getInto() {
        return into;
    }

    public void setInto(SQLExpr into) {
        this.into = into;
    }

    public SQLStatement getForStatement() {
        return forStatement;
    }

    public void setForStatement(SQLStatement forStatement) {
        this.forStatement = forStatement;
    }

}
