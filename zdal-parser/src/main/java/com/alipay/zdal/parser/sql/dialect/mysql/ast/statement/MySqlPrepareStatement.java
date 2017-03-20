/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlPrepareStatement.java, v 0.1 2012-11-17 ÏÂÎç3:33:50 Exp $
 */
public class MySqlPrepareStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;
    private SQLName           name;
    private SQLExpr           from;

    public MySqlPrepareStatement() {
    }

    public MySqlPrepareStatement(SQLName name, SQLExpr from) {
        this.name = name;
        this.from = from;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public SQLExpr getFrom() {
        return from;
    }

    public void setFrom(SQLExpr from) {
        this.from = from;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, from);
        }
        visitor.endVisit(this);
    }
}
