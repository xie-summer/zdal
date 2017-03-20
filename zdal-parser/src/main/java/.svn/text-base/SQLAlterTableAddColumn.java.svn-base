/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLAlterTableAddColumn.java, v 0.1 2012-11-17 ÏÂÎç3:19:55 xiaoqing.zhouxq Exp $
 */
public class SQLAlterTableAddColumn extends SQLObjectImpl implements SQLAlterTableItem {

    private static final long         serialVersionUID = 1L;

    private List<SQLColumnDefinition> columns          = new ArrayList<SQLColumnDefinition>();

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public List<SQLColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLColumnDefinition> columns) {
        this.columns = columns;
    }

}
