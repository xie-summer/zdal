/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleOrderBy;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleAnalytic.java, v 0.1 2012-11-17 ÏÂÎç3:42:54 Exp $
 */
public class OracleAnalytic extends SQLObjectImpl implements OracleExpr {

    private static final long       serialVersionUID = 1L;
    private final List<SQLExpr>     partitionBy      = new ArrayList<SQLExpr>();
    private OracleOrderBy           orderBy;
    private OracleAnalyticWindowing windowing;

    public OracleAnalytic() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.partitionBy);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.windowing);
        }
        visitor.endVisit(this);
    }

    public OracleOrderBy getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(OracleOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public OracleAnalyticWindowing getWindowing() {
        return this.windowing;
    }

    public void setWindowing(OracleAnalyticWindowing windowing) {
        this.windowing = windowing;
    }

    public List<SQLExpr> getPartitionBy() {
        return this.partitionBy;
    }
}
