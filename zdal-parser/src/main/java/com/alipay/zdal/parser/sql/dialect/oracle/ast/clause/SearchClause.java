/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleOrderByItem;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SearchClause.java, v 0.1 2012-11-17 ÏÂÎç3:42:34 Exp $
 */
public class SearchClause extends OracleSQLObjectImpl {

    private static final long serialVersionUID = 1L;

    public static enum Type {
        DEPTH, BREADTH
    }

    private Type                          type;

    private final List<OracleOrderByItem> items = new ArrayList<OracleOrderByItem>();

    private SQLIdentifierExpr             orderingColumn;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<OracleOrderByItem> getItems() {
        return items;
    }

    public SQLIdentifierExpr getOrderingColumn() {
        return orderingColumn;
    }

    public void setOrderingColumn(SQLIdentifierExpr orderingColumn) {
        this.orderingColumn = orderingColumn;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, items);
            acceptChild(visitor, orderingColumn);
        }
        visitor.endVisit(this);
    }

}
