/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateSetListSingleColumnItem.java, v 0.1 2012-11-17 ÏÂÎç3:51:25 Exp $
 */
public class OracleUpdateSetListSingleColumnItem extends OracleUpdateSetListItem {

    private static final long serialVersionUID = 1L;

    private SQLExpr           column;
    private SQLExpr           value;

    public OracleUpdateSetListSingleColumnItem() {

    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.column);
            acceptChild(visitor, this.value);
        }

        visitor.endVisit(this);
    }

    public SQLExpr getColumn() {
        return this.column;
    }

    public void setColumn(SQLExpr column) {
        this.column = column;
    }

    public SQLExpr getValue() {
        return this.value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }
}
