/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlDescribeStatement.java, v 0.1 2012-11-17 ÏÂÎç3:32:31 Exp $
 */
public class MySqlDescribeStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLName           object;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, object);
        }
        visitor.endVisit(this);
    }

    public SQLName getObject() {
        return object;
    }

    public void setObject(SQLName object) {
        this.object = object;
    }

}
