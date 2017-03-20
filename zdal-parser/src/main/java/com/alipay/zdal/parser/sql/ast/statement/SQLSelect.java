/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSelect.java, v 0.1 2012-11-17 ÏÂÎç3:23:19 xiaoqing.zhouxq Exp $
 */
public class SQLSelect extends SQLObjectImpl {

    private static final long serialVersionUID = 1L;

    protected SQLSelectQuery  query;
    protected SQLOrderBy      orderBy;

    public SQLSelect() {

    }

    public SQLSelectQuery getQuery() {
        return this.query;
    }

    public void setQuery(SQLSelectQuery query) {
        this.query = query;
    }

    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public void output(StringBuffer buf) {
        this.query.output(buf);
        buf.append(" ");

        if (this.orderBy != null)
            this.orderBy.output(buf);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.query);
            acceptChild(visitor, this.orderBy);
        }

        visitor.endVisit(this);
    }

}
