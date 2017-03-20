/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLUniqueConstraintImpl.java, v 0.1 2012-11-17 ÏÂÎç3:28:36 Exp $
 */
@SuppressWarnings("serial")
public class SQLUniqueConstraintImpl extends SQLConstaintImpl implements SQLUniqueConstraint {

    public SQLUniqueConstraintImpl() {

    }

    private List<SQLExpr> columns = new ArrayList<SQLExpr>();

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }
}
