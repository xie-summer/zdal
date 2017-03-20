/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleAlterTableModify.java, v 0.1 2012-11-17 ÏÂÎç3:45:22 Exp $
 */
public class OracleAlterTableModify extends OracleAlterTableItem {

    private static final long         serialVersionUID = 1L;

    private List<SQLColumnDefinition> columns          = new ArrayList<SQLColumnDefinition>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
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
