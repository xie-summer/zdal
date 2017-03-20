/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.expr.SQLQueryExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlReplicateStatement.java, v 0.1 2012-11-17 ÏÂÎç3:34:01 Exp $
 */
public class MySqlReplicateStatement extends MySqlStatementImpl {

    private static final long   serialVersionUID = 1L;

    private boolean             lowPriority      = false;
    private boolean             delayed          = false;

    private SQLName             tableName;
    private final List<SQLExpr> columns          = new ArrayList<SQLExpr>();
    private List<ValuesClause>  valuesList       = new ArrayList<ValuesClause>();
    private SQLQueryExpr        query;

    public SQLName getTableName() {
        return tableName;
    }

    public void setTableName(SQLName tableName) {
        this.tableName = tableName;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public SQLQueryExpr getQuery() {
        return query;
    }

    public void setQuery(SQLQueryExpr query) {
        query.setParent(this);
        this.query = query;
    }

    public List<ValuesClause> getValuesList() {
        return valuesList;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableName);
            acceptChild(visitor, columns);
            acceptChild(visitor, valuesList);
            acceptChild(visitor, query);
        }
        visitor.endVisit(this);
    }
}
