/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlRenameTableStatement.java, v 0.1 2012-11-17 ÏÂÎç3:33:55 Exp $
 */
public class MySqlRenameTableStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private List<Item>        items            = new ArrayList<Item>(2);

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, items);
        }
        visitor.endVisit(this);
    }

    public static class Item extends MySqlObjectImpl {

        private static final long serialVersionUID = 1L;
        private SQLExpr           name;
        private SQLExpr           to;

        public SQLExpr getName() {
            return name;
        }

        public void setName(SQLExpr name) {
            this.name = name;
        }

        public SQLExpr getTo() {
            return to;
        }

        public void setTo(SQLExpr to) {
            this.to = to;
        }

        @Override
        public void accept0(MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, name);
                acceptChild(visitor, to);
            }
            visitor.endVisit(this);
        }

    }
}
