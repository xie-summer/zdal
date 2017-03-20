/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast;

import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleOrderByItem;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleOrderBy.java, v 0.1 2012-11-17 ÏÂÎç3:41:21 Exp $
 */
public class OracleOrderBy extends SQLOrderBy {

    private static final long serialVersionUID = 1L;

    private boolean           sibings;

    public OracleOrderBy() {

    }

    public boolean isSibings() {
        return this.sibings;
    }

    public void setSibings(boolean sibings) {
        this.sibings = sibings;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
        } else {
            super.accept0(visitor);
        }
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("ORDER ");
        if (this.sibings) {
            buf.append("SIBLINGS ");
        }
        buf.append("BY ");

        int i = 0;
        for (int size = this.items.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            ((OracleOrderByItem) this.items.get(i)).output(buf);
        }
    }
}
