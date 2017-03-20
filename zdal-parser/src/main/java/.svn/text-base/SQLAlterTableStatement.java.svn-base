/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLAlterTableStatement.java, v 0.1 2012-11-17 ÏÂÎç3:20:17 xiaoqing.zhouxq Exp $
 */
public class SQLAlterTableStatement extends SQLStatementImpl implements SQLDDLStatement {

    private static final long       serialVersionUID = 1L;

    private SQLExprTableSource      tableSource;
    private List<SQLAlterTableItem> items            = new ArrayList<SQLAlterTableItem>();

    public List<SQLAlterTableItem> getItems() {
        return items;
    }

    public void setItems(List<SQLAlterTableItem> items) {
        this.items = items;
    }

    public SQLExprTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLExprTableSource tableSource) {
        this.tableSource = tableSource;
    }

    public SQLName getName() {
        if (getTableSource() == null) {
            return null;
        }
        return (SQLName) getTableSource().getExpr();
    }

    public void setName(SQLName name) {
        this.setTableSource(new SQLExprTableSource(name));
    }
}
