/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLUpdateSetItem.java, v 0.1 2012-11-17 ÏÂÎç3:28:42 Exp $
 */
public class SQLUpdateSetItem extends SQLObjectImpl {

    private static final long serialVersionUID = 1L;

    private SQLExpr           column;
    private SQLExpr           value;

    public SQLUpdateSetItem() {

    }

    public SQLExpr getColumn() {
        return column;
    }

    public void setColumn(SQLExpr column) {
        this.column = column;
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    public void output(StringBuffer buf) {
        column.output(buf);
        buf.append(" = ");
        value.output(buf);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, column);
            acceptChild(visitor, value);
        }

        visitor.endVisit(this);
    }

}
