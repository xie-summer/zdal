/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLJoinTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectJoin.java, v 0.1 2012-11-17 ÏÂÎç3:49:40 Exp $
 */
public class OracleSelectJoin extends SQLJoinTableSource implements OracleSelectTableSource {

    private static final long       serialVersionUID = 1L;

    private final List<SQLExpr>     using            = new ArrayList<SQLExpr>();
    protected OracleSelectPivotBase pivot;
    protected FlashbackQueryClause  flashback;

    public OracleSelectJoin(String alias) {
        super(alias);
    }

    public OracleSelectJoin() {

    }

    public FlashbackQueryClause getFlashback() {
        return flashback;
    }

    public void setFlashback(FlashbackQueryClause flashback) {
        this.flashback = flashback;
    }

    public OracleSelectPivotBase getPivot() {
        return pivot;
    }

    public void setPivot(OracleSelectPivotBase pivot) {
        this.pivot = pivot;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.left);
            acceptChild(visitor, this.right);
            acceptChild(visitor, this.condition);
            acceptChild(visitor, this.using);
            acceptChild(visitor, this.flashback);
        }

        visitor.endVisit(this);
    }

    public List<SQLExpr> getUsing() {
        return this.using;
    }

    public void output(StringBuffer buf) {
        this.left.output(buf);
        buf.append(JoinType.toString(this.joinType));
        this.right.output(buf);

        if (this.condition != null) {
            buf.append(" ON ");
            this.condition.output(buf);
        }

        if (this.using.size() > 0) {
            buf.append(" USING (");
            int i = 0;
            for (int size = this.using.size(); i < size; ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                ((SQLExpr) this.using.get(i)).output(buf);
            }
            buf.append(")");
        }
    }

}
