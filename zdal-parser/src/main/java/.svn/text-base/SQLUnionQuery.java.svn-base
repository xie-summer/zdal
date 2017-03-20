/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLUnionQuery.java, v 0.1 2012-11-17 ÏÂÎç3:28:24 Exp $
 */
public class SQLUnionQuery extends SQLSelectQuery {

    private static final long serialVersionUID = 1L;

    private SQLSelectQuery    left;
    private SQLSelectQuery    right;
    private SQLUnionOperator  operator         = SQLUnionOperator.UNION;
    private SQLOrderBy        orderBy;

    public SQLUnionOperator getOperator() {
        return operator;
    }

    public void setOperator(SQLUnionOperator operator) {
        this.operator = operator;
    }

    public SQLUnionQuery() {

    }

    public SQLSelectQuery getLeft() {
        return left;
    }

    public void setLeft(SQLSelectQuery left) {
        this.left = left;
    }

    public SQLSelectQuery getRight() {
        return right;
    }

    public void setRight(SQLSelectQuery right) {
        this.right = right;
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, left);
            acceptChild(visitor, right);
            acceptChild(visitor, orderBy);
        }
        visitor.endVisit(this);
    }

}
