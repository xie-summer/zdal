/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSelectGroupByClause.java, v 0.1 2012-11-17 ÏÂÎç3:23:25 xiaoqing.zhouxq Exp $
 */
public class SQLSelectGroupByClause extends SQLObjectImpl {

    private static final long   serialVersionUID = 1L;

    private final List<SQLExpr> items            = new ArrayList<SQLExpr>();
    private SQLExpr             having;

    public SQLSelectGroupByClause() {

    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.having);
        }

        visitor.endVisit(this);
    }

    public SQLExpr getHaving() {
        return this.having;
    }

    public void setHaving(SQLExpr having) {
        this.having = having;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }
}
