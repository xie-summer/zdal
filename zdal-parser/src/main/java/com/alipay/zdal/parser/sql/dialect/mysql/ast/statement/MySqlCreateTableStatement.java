/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.parser.sql.ast.SQLCommentHint;
import com.alipay.zdal.parser.sql.ast.SQLPartitioningClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlCreateTableStatement.java, v 0.1 2012-11-17 ÏÂÎç3:32:12 Exp $
 */
@SuppressWarnings("serial")
public class MySqlCreateTableStatement extends SQLCreateTableStatement implements MySqlStatement {

    private boolean               ifNotExiists = false;

    private Map<String, String>   tableOptions = new LinkedHashMap<String, String>();

    protected SQLSelect           query;

    private SQLPartitioningClause partitioning;

    public MySqlCreateTableStatement() {

    }

    private List<SQLCommentHint> hints = new ArrayList<SQLCommentHint>();

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }

    public void setTableOptions(Map<String, String> tableOptions) {
        this.tableOptions = tableOptions;
    }

    public SQLPartitioningClause getPartitioning() {
        return partitioning;
    }

    public void setPartitioning(SQLPartitioningClause partitioning) {
        this.partitioning = partitioning;
    }

    public Map<String, String> getTableOptions() {
        return tableOptions;
    }

    public SQLSelect getQuery() {
        return query;
    }

    public void setQuery(SQLSelect query) {
        this.query = query;
    }

    @Override
    public void output(StringBuffer buf) {
        if (Type.GLOBAL_TEMPORARY.equals(this.type)) {
            buf.append("CREATE TEMPORARY TABLE ");
        } else {
            buf.append("CREATE TABLE ");
        }

        if (ifNotExiists) {
            buf.append("IF NOT EXISTS ");
        }

        this.tableSource.output(buf);
        buf.append(" ");
        buf.append("(");
        for (int i = 0, size = tableElementList.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            tableElementList.get(i).output(buf);
        }
        buf.append(")");

        if (query != null) {
            buf.append(" ");
            query.output(buf);
        }
    }

    public boolean isIfNotExiists() {
        return ifNotExiists;
    }

    public void setIfNotExiists(boolean ifNotExiists) {
        this.ifNotExiists = ifNotExiists;
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

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, getHints());
            this.acceptChild(visitor, getTableSource());
            this.acceptChild(visitor, getTableElementList());
        }
        visitor.endVisit(this);
    }
}
