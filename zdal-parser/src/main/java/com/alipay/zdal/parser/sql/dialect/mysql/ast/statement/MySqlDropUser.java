/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlDropUser.java, v 0.1 2012-11-17 ÏÂÎç3:32:41 Exp $
 */
public class MySqlDropUser extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private List<SQLExpr>     users            = new ArrayList<SQLExpr>(2);

    public List<SQLExpr> getUsers() {
        return users;
    }

    public void setUsers(List<SQLExpr> users) {
        this.users = users;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, users);
        }
    }
}
