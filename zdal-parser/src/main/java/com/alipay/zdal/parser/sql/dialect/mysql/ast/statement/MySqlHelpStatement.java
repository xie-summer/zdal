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
 * @version $Id: MySqlHelpStatement.java, v 0.1 2012-11-17 ÏÂÎç3:33:00 Exp $
 */
public class MySqlHelpStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           content;

    public SQLExpr getContent() {
        return content;
    }

    public void setContent(SQLExpr content) {
        this.content = content;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, content);
        }
        visitor.endVisit(this);
    }
}
