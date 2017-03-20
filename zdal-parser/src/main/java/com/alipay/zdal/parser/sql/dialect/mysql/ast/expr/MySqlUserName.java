/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlUserName.java, v 0.1 2012-11-17 ÏÂÎç3:30:59 Exp $
 */
public class MySqlUserName extends MySqlExprImpl implements SQLName {

    private static final long serialVersionUID = 1L;
    private String            userName;
    private String            host;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getSimleName() {
        return userName + '@' + host;
    }

    public String toString() {
        return getSimleName();
    }
}
