/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLUpdateStatement.java, v 0.1 2012-11-17 ÏÂÎç3:28:50 Exp $
 */
public class SQLUpdateStatement extends SQLStatementImpl {

    private static final long              serialVersionUID = 1L;

    protected final List<SQLUpdateSetItem> items            = new ArrayList<SQLUpdateSetItem>();
    protected SQLExpr                      where;

    protected SQLTableSource               tableSource;

    public SQLUpdateStatement() {

    }

    public SQLTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLExpr expr) {
        this.setTableSource(new SQLExprTableSource(expr));
    }

    public void setTableSource(SQLTableSource tableSource) {
        this.tableSource = tableSource;
    }

    public SQLName getTableName() {
        if (tableSource instanceof SQLExprTableSource) {
            SQLExprTableSource exprTableSource = (SQLExprTableSource) tableSource;
            return (SQLName) exprTableSource.getExpr();
        }
        return null;
    }

    public SQLExpr getWhere() {
        return where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public List<SQLUpdateSetItem> getItems() {
        return items;
    }

    @Override
    public void output(StringBuffer buf) {
        buf.append("UPDATE ");

        this.tableSource.output(buf);

        buf.append(" SET ");
        for (int i = 0, size = items.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            items.get(i).output(buf);
        }

        if (this.where != null) {
            buf.append(" WHERE ");
            this.where.output(buf);
        }
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSource);
            acceptChild(visitor, items);
            acceptChild(visitor, where);
        }
        visitor.endVisit(this);
    }
}
