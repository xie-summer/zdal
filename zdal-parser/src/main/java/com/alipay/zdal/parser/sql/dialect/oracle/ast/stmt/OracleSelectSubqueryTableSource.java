/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.ast.statement.SQLSubqueryTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectSubqueryTableSource.java, v 0.1 2012-11-17 ÏÂÎç3:50:10 Exp $
 */
public class OracleSelectSubqueryTableSource extends SQLSubqueryTableSource implements
                                                                           OracleSelectTableSource {

    private static final long       serialVersionUID = 1L;

    protected OracleSelectPivotBase pivot;

    protected FlashbackQueryClause  flashback;

    public OracleSelectSubqueryTableSource() {
    }

    public FlashbackQueryClause getFlashback() {
        return flashback;
    }

    public void setFlashback(FlashbackQueryClause flashback) {
        this.flashback = flashback;
    }

    public OracleSelectSubqueryTableSource(String alias) {
        super(alias);
    }

    public OracleSelectSubqueryTableSource(SQLSelect select, String alias) {
        super(select, alias);
    }

    public OracleSelectSubqueryTableSource(SQLSelect select) {
        super(select);
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
            acceptChild(visitor, this.getHints());
            acceptChild(visitor, this.select);
            acceptChild(visitor, this.pivot);
            acceptChild(visitor, this.flashback);
        }
        visitor.endVisit(this);
    }

}
