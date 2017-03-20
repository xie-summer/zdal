/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSelectStatement.java, v 0.1 2012-11-17 ÏÂÎç3:23:58 xiaoqing.zhouxq Exp $
 */
public class SQLSelectStatement extends SQLStatementImpl {

    private static final long serialVersionUID = 1L;

    protected SQLSelect       select;

    public SQLSelectStatement() {

    }

    public SQLSelectStatement(SQLSelect select) {

        this.select = select;
    }

    public SQLSelect getSelect() {
        return this.select;
    }

    public void setSelect(SQLSelect select) {
        this.select = select;
    }

    public void output(StringBuffer buf) {
        this.select.output(buf);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.select);
        }
        visitor.endVisit(this);
    }
}
