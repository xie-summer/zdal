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
 * @version $Id: MySqlShowCreateFunctionStatement.java, v 0.1 2012-11-17 ÏÂÎç3:35:39 Exp $
 */
public class MySqlShowCreateFunctionStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           name;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getName() {
        return name;
    }

    public void setName(SQLExpr functionName) {
        this.name = functionName;
    }

}
