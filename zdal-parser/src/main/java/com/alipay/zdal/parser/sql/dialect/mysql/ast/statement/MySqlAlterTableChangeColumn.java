/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlAlterTableChangeColumn.java, v 0.1 2012-11-17 ÏÂÎç3:31:30 Exp $
 */
public class MySqlAlterTableChangeColumn extends MySqlObjectImpl implements SQLAlterTableItem {

    private static final long   serialVersionUID = 1L;

    private SQLName             columnName;

    private SQLColumnDefinition newColumnDefinition;

    private Boolean             first;

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columnName);
            acceptChild(visitor, newColumnDefinition);
        }
    }

    public SQLName getColumnName() {
        return columnName;
    }

    public void setColumnName(SQLName columnName) {
        this.columnName = columnName;
    }

    public SQLColumnDefinition getNewColumnDefinition() {
        return newColumnDefinition;
    }

    public void setNewColumnDefinition(SQLColumnDefinition newColumnDefinition) {
        this.newColumnDefinition = newColumnDefinition;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

}
