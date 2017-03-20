/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSubqueryTableSource.java, v 0.1 2012-11-17 ÏÂÎç3:24:16 xiaoqing.zhouxq Exp $
 */
public class SQLSubqueryTableSource extends SQLTableSourceImpl {

    private static final long serialVersionUID = 1L;

    protected SQLSelect       select;

    public SQLSubqueryTableSource() {

    }

    public SQLSubqueryTableSource(String alias) {
        super(alias);
    }

    public SQLSubqueryTableSource(SQLSelect select, String alias) {
        super(alias);
        this.select = select;
    }

    public SQLSubqueryTableSource(SQLSelect select) {

        this.select = select;
    }

    public SQLSelect getSelect() {
        return this.select;
    }

    public void setSelect(SQLSelect select) {
        this.select = select;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, select);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("(");
        this.select.output(buf);
        buf.append(")");
    }
}
