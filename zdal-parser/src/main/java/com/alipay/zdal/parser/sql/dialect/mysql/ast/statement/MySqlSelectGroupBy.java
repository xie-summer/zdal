/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectGroupByClause;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlSelectGroupBy.java, v 0.1 2012-11-17 ÏÂÎç3:34:18 Exp $
 */
public class MySqlSelectGroupBy extends SQLSelectGroupByClause {

    private static final long serialVersionUID = 1L;

    private boolean           rollUp           = false;

    public boolean isRollUp() {
        return rollUp;
    }

    public void setRollUp(boolean rollUp) {
        this.rollUp = rollUp;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.getItems());
                acceptChild(visitor, this.getHaving());
            }

            visitor.endVisit(this);
        }
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getItems());
            acceptChild(visitor, this.getHaving());
        }

        visitor.endVisit(this);
    }
}
