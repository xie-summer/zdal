/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlShowCreateEventStatement.java, v 0.1 2012-11-17 ÏÂÎç3:35:35 Exp $
 */
public class MySqlShowCreateEventStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           eventName;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, eventName);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getEventName() {
        return eventName;
    }

    public void setEventName(SQLExpr eventName) {
        this.eventName = eventName;
    }

}
