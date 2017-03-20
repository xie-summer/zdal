/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlSetCharSetStatement.java, v 0.1 2012-11-17 ÏÂÎç3:34:32 Exp $
 */
public class MySqlSetCharSetStatement extends MySqlStatementImpl {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean           isDefault        = false;
    private String            charSet;
    private String            collate;

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
