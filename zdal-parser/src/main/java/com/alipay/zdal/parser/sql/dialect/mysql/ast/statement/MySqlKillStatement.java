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
 * @version $Id: MySqlKillStatement.java, v 0.1 2012-11-17 ÏÂÎç3:33:07 Exp $
 */
public class MySqlKillStatement extends MySqlStatementImpl {
    private static final long serialVersionUID = 1L;
    private Type              type;
    private SQLExpr           threadId;

    public static enum Type {
        CONNECTION, QUERY
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SQLExpr getThreadId() {
        return threadId;
    }

    public void setThreadId(SQLExpr threadId) {
        this.threadId = threadId;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, threadId);
        }
    }
}
