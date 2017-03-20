/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.statement.SQLCreateIndexStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlCreateIndexStatement.java, v 0.1 2012-11-17 ÏÂÎç3:32:05 Exp $
 */
public class MySqlCreateIndexStatement extends SQLCreateIndexStatement implements MySqlStatement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String            using;

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((MySqlASTVisitor) visitor);
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getName());
            acceptChild(visitor, getTable());
            acceptChild(visitor, getItems());
        }
        visitor.endVisit(this);
    }
}
