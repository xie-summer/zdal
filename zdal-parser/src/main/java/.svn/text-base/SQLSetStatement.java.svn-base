/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSetStatement.java, v 0.1 2012-11-17 ÏÂÎç3:24:10 xiaoqing.zhouxq Exp $
 */
public class SQLSetStatement extends SQLStatementImpl {

    private static final long   serialVersionUID = 1L;

    private List<SQLAssignItem> items            = new ArrayList<SQLAssignItem>();

    public SQLSetStatement() {
    }

    public SQLSetStatement(SQLExpr target, SQLExpr value) {
        this.items.add(new SQLAssignItem(target, value));
    }

    public List<SQLAssignItem> getItems() {
        return items;
    }

    public void setItems(List<SQLAssignItem> items) {
        this.items = items;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("SET ");

        for (int i = 0; i < items.size(); ++i) {
            if (i != 0) {
                buf.append(", ");
            }

            SQLAssignItem item = items.get(i);
            item.output(buf);
        }
    }
}
