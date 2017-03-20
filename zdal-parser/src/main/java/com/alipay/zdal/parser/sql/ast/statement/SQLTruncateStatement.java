/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLTruncateStatement.java, v 0.1 2012-11-17 ÏÂÎç3:27:55 Exp $
 */
public class SQLTruncateStatement extends SQLStatementImpl {

    private static final long          serialVersionUID = 1L;
    protected List<SQLExprTableSource> tableSources     = new ArrayList<SQLExprTableSource>(2);

    public List<SQLExprTableSource> getTableSources() {
        return tableSources;
    }

    public void setTableSources(List<SQLExprTableSource> tableSources) {
        this.tableSources = tableSources;
    }

    public void addTableSource(SQLName name) {
        this.tableSources.add(new SQLExprTableSource(name));
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSources);
        }
        visitor.endVisit(this);
    }
}
