/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableAddIndex;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObject;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlAlterTableAddUnique.java, v 0.1 2012-11-17 ÏÂÎç3:31:25 Exp $
 */
public class MySqlAlterTableAddUnique extends SQLAlterTableAddIndex implements SQLAlterTableItem,
                                                                   MySqlObject {

    private static final long serialVersionUID = 1L;

    private String            using;

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
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
            acceptChild(visitor, getName());
            acceptChild(visitor, getItems());
        }
        visitor.endVisit(this);
    }

}
