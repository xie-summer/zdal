/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SampleClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectTableReference.java, v 0.1 2012-11-17 ÏÂÎç3:50:15 Exp $
 */
public class OracleSelectTableReference extends SQLExprTableSource implements
                                                                  OracleSelectTableSource {

    private static final long          serialVersionUID = 1L;

    private boolean                    only             = false;
    protected OracleSelectPivotBase    pivot;

    protected PartitionExtensionClause partition;
    protected SampleClause             sampleClause;

    protected FlashbackQueryClause     flashback;

    public OracleSelectTableReference() {

    }

    public FlashbackQueryClause getFlashback() {
        return flashback;
    }

    public void setFlashback(FlashbackQueryClause flashback) {
        this.flashback = flashback;
    }

    public PartitionExtensionClause getPartition() {
        return partition;
    }

    public void setPartition(PartitionExtensionClause partition) {
        this.partition = partition;
    }

    public boolean isOnly() {
        return this.only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public SampleClause getSampleClause() {
        return sampleClause;
    }

    public void setSampleClause(SampleClause sampleClause) {
        this.sampleClause = sampleClause;
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
            acceptChild(visitor, this.expr);
            acceptChild(visitor, this.partition);
            acceptChild(visitor, this.sampleClause);
            acceptChild(visitor, this.pivot);
        }
        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        if (this.only) {
            buf.append("ONLY (");
            this.expr.output(buf);
            buf.append(")");
        } else {
            this.expr.output(buf);
        }

        if (this.pivot != null) {
            buf.append(" ");
            this.pivot.output(buf);
        }

        if ((this.alias != null) && (this.alias.length() != 0)) {
            buf.append(this.alias);
        }
    }
}
