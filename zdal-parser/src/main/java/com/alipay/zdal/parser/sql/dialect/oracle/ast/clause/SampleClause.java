/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SampleClause.java, v 0.1 2012-11-17 ÏÂÎç3:42:30 Exp $
 */
public class SampleClause extends OracleSQLObjectImpl {

    private static final long serialVersionUID = 1L;

    private boolean           block            = false;

    private List<SQLExpr>     percent          = new ArrayList<SQLExpr>();

    private SQLExpr           seedValue;

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public List<SQLExpr> getPercent() {
        return percent;
    }

    public void setPercent(List<SQLExpr> percent) {
        this.percent = percent;
    }

    public SQLExpr getSeedValue() {
        return seedValue;
    }

    public void setSeedValue(SQLExpr seedValue) {
        this.seedValue = seedValue;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, seedValue);
            acceptChild(visitor, percent);
        }
        visitor.endVisit(this);
    }

}
