/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLConstaintImpl;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableConstaint;
import com.alipay.zdal.parser.sql.ast.statement.SQLUniqueConstraint;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlKey.java, v 0.1 2012-11-17 ÏÂÎç3:29:27 Exp $
 */
@SuppressWarnings("serial")
public class MySqlKey extends SQLConstaintImpl implements SQLUniqueConstraint, SQLTableConstaint {

    private List<SQLExpr> columns = new ArrayList<SQLExpr>();
    private String        indexType;

    public MySqlKey() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        }
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

}
