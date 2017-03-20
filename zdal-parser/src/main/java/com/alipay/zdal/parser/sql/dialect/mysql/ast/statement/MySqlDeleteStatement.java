/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.ast.statement.SQLDeleteStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableSource;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlDeleteStatement.java, v 0.1 2012-11-17 ÏÂÎç3:32:25 Exp $
 */
public class MySqlDeleteStatement extends SQLDeleteStatement {

    private static final long serialVersionUID = 1L;

    private boolean           lowPriority      = false;
    private boolean           quick            = false;
    private boolean           ignore           = false;

    private SQLTableSource    from;
    private SQLTableSource    using;
    private SQLOrderBy        orderBy;
    private Limit             limit;

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isQuick() {
        return quick;
    }

    public void setQuick(boolean quick) {
        this.quick = quick;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public SQLTableSource getFrom() {
        return from;
    }

    public SQLTableSource getUsing() {
        return using;
    }

    public void setUsing(SQLTableSource using) {
        this.using = using;
    }

    public void setFrom(SQLTableSource from) {
        this.from = from;
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : "
                                               + visitor.getClass().getName());
        }
    }

    public void output(StringBuffer buf) {
        new MySqlOutputVisitor(buf).visit(this);
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getTableSource());
            acceptChild(visitor, getWhere());
            acceptChild(visitor, getFrom());
            acceptChild(visitor, getUsing());
            acceptChild(visitor, orderBy);
            acceptChild(visitor, limit);
        }

        visitor.endVisit(this);
    }
}
