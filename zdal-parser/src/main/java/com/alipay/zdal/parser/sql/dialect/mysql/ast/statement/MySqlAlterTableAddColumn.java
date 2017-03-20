/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableAddColumn;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObject;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlAlterTableAddColumn.java, v 0.1 2012-11-17 ÏÂÎç3:31:15 Exp $
 */
public class MySqlAlterTableAddColumn extends SQLAlterTableAddColumn implements MySqlObject {

    private static final long serialVersionUID = 1L;

    private SQLName           after;

    public SQLName getAfter() {
        return after;
    }

    public void setAfter(SQLName after) {
        this.after = after;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : "
                                               + visitor.getClass().getName());
        }
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getColumns());
        }
        visitor.endVisit(this);
    }
}
