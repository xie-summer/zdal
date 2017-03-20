/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlExecuteStatement.java, v 0.1 2012-11-17 ÏÂÎç3:32:52 Exp $
 */
public class MySqlExecuteStatement extends MySqlStatementImpl {

    private static final long   serialVersionUID = 1L;

    private SQLName             statementName;
    private final List<SQLExpr> parameters       = new ArrayList<SQLExpr>();

    public SQLName getStatementName() {
        return statementName;
    }

    public void setStatementName(SQLName statementName) {
        this.statementName = statementName;
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statementName);
            acceptChild(visitor, parameters);
        }
        visitor.endVisit(this);
    }
}
