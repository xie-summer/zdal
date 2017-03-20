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
 * @author xiaoqing.zhouxq
 * @version $Id: SQLDropViewStatement.java, v 0.1 2012-11-17 ÏÂÎç3:22:06 xiaoqing.zhouxq Exp $
 */
public class SQLDropViewStatement extends SQLStatementImpl implements SQLDDLStatement {

    private static final long          serialVersionUID = 1L;

    protected List<SQLExprTableSource> tableSources     = new ArrayList<SQLExprTableSource>();

    public SQLDropViewStatement() {

    }

    public SQLDropViewStatement(SQLName name) {
        this(new SQLExprTableSource(name));
    }

    public SQLDropViewStatement(SQLExprTableSource tableSource) {
        this.tableSources.add(tableSource);
    }

    public List<SQLExprTableSource> getTableSources() {
        return tableSources;
    }

    public void setTableSources(List<SQLExprTableSource> tableSources) {
        this.tableSources = tableSources;
    }

    public void setName(SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }

    public void addTableSource(SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }

    public void addTableSource(SQLExprTableSource tableSource) {
        tableSources.add(tableSource);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSources);
        }
        visitor.endVisit(this);
    }
}
