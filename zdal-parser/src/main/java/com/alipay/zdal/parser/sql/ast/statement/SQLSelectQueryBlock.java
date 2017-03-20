/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLSetQuantifier;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSelectQueryBlock.java, v 0.1 2012-11-17 ÏÂÎç3:23:51 xiaoqing.zhouxq Exp $
 */
public class SQLSelectQueryBlock extends SQLSelectQuery {

    private static final long           serialVersionUID = 1L;

    protected int                       distionOption;
    protected final List<SQLSelectItem> selectList       = new ArrayList<SQLSelectItem>();

    protected SQLTableSource            from;
    protected SQLExprTableSource        into;
    protected SQLExpr                   where;
    protected SQLSelectGroupByClause    groupBy;

    public SQLSelectQueryBlock() {

    }

    public SQLExprTableSource getInto() {
        return into;
    }

    public void setInto(SQLExpr into) {
        this.into = new SQLExprTableSource(into);
    }

    public void setInto(SQLExprTableSource into) {
        this.into = into;
    }

    public SQLSelectGroupByClause getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(SQLSelectGroupByClause groupBy) {
        this.groupBy = groupBy;
    }

    public SQLExpr getWhere() {
        return this.where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public int getDistionOption() {
        return this.distionOption;
    }

    public void setDistionOption(int distionOption) {
        this.distionOption = distionOption;
    }

    public List<SQLSelectItem> getSelectList() {
        return this.selectList;
    }

    public SQLTableSource getFrom() {
        return this.from;
    }

    public void setFrom(SQLTableSource from) {
        this.from = from;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.selectList);
            acceptChild(visitor, this.from);
            acceptChild(visitor, this.where);
            acceptChild(visitor, this.groupBy);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("SELECT ");

        if (SQLSetQuantifier.ALL == this.distionOption)
            buf.append("ALL ");
        else if (SQLSetQuantifier.DISTINCT == this.distionOption)
            buf.append("DISTINCT ");
        else if (SQLSetQuantifier.UNIQUE == this.distionOption) {
            buf.append("UNIQUE ");
        }

        int i = 0;
        for (int size = this.selectList.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            ((SQLSelectItem) this.selectList.get(i)).output(buf);
        }

        buf.append(" FROM ");
        if (this.from == null) {
            buf.append("DUAL");
        } else {
            this.from.output(buf);
        }

        if (this.where != null) {
            buf.append(" WHERE ");
            this.where.output(buf);
        }

        if (this.groupBy != null) {
            buf.append(" ");
            this.groupBy.output(buf);
        }
    }
}
