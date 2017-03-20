/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelectOrderByItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleOrderByItem;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLOrderBy.java, v 0.1 2012-11-17 ÏÂÎç3:13:30 xiaoqing.zhouxq Exp $
 */
@SuppressWarnings("serial")
public class SQLOrderBy extends SQLObjectImpl {

    protected final List<SQLSelectOrderByItem> items = new ArrayList<SQLSelectOrderByItem>();

    public SQLOrderBy() {

    }

    public List<SQLSelectOrderByItem> getItems() {
        return this.items;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("ORDER ");
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
