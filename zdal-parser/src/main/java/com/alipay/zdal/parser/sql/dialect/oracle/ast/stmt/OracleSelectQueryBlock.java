/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLCommentHint;
import com.alipay.zdal.parser.sql.ast.SQLSetQuantifier;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectQueryBlock.java, v 0.1 2012-11-17 ÏÂÎç3:49:58 Exp $
 */
public class OracleSelectQueryBlock extends SQLSelectQueryBlock {

    private static final long                  serialVersionUID = 1L;

    private final List<SQLCommentHint>         hints            = new ArrayList<SQLCommentHint>(1);

    private OracleSelectHierachicalQueryClause hierachicalQueryClause;
    private ModelClause                        modelClause;

    public OracleSelectQueryBlock() {

    }

    public ModelClause getModelClause() {
        return modelClause;
    }

    public void setModelClause(ModelClause modelClause) {
        this.modelClause = modelClause;
    }

    public OracleSelectHierachicalQueryClause getHierachicalQueryClause() {
        return this.hierachicalQueryClause;
    }

    public void setHierachicalQueryClause(OracleSelectHierachicalQueryClause hierachicalQueryClause) {
        this.hierachicalQueryClause = hierachicalQueryClause;
    }

    public List<SQLCommentHint> getHints() {
        return this.hints;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }

        if (visitor.visit(this)) {
            acceptChild(visitor, this.selectList);
            acceptChild(visitor, this.into);
            acceptChild(visitor, this.from);
            acceptChild(visitor, this.where);
            acceptChild(visitor, this.groupBy);
        }
        visitor.endVisit(this);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, this.selectList);
            acceptChild(visitor, this.into);
            acceptChild(visitor, this.from);
            acceptChild(visitor, this.where);
            acceptChild(visitor, this.hierachicalQueryClause);
            acceptChild(visitor, this.groupBy);
            acceptChild(visitor, this.modelClause);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("SELECT ");

        if (SQLSetQuantifier.ALL == this.distionOption)
            buf.append("ALL ");
        else if (SQLSetQuantifier.DISTINCT == this.distionOption)
            buf.append("DISTINCT ");
        else if (SQLSetQuantifier.UNIQUE == this.distionOption) {
            buf.append("UNIQUE ");
        }

        int i = 0;
        for (int size = this.selectList.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            ((SQLSelectItem) this.selectList.get(i)).output(buf);
        }

        buf.append(" FROM ");
        if (this.from != null) {
            buf.append("DUAL");
        } else {
            this.from.output(buf);
        }

        if (this.where != null) {
            buf.append(" WHERE ");
            this.where.output(buf);
        }

        if (this.hierachicalQueryClause != null) {
            buf.append(" ");
            this.hierachicalQueryClause.output(buf);
        }

        if (this.groupBy != null) {
            buf.append(" ");
            this.groupBy.output(buf);
        }
    }
}
