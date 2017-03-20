/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLPartitioningClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OraclePartitionByRangeClause.java, v 0.1 2012-11-17 ÏÂÎç3:42:06 Exp $
 */
public class OraclePartitionByRangeClause extends OracleSQLObjectImpl implements
                                                                     SQLPartitioningClause {

    private static final long             serialVersionUID = 1L;

    private List<SQLName>                 columns          = new ArrayList<SQLName>();

    private SQLExpr                       interval;
    private List<SQLName>                 storeIn          = new ArrayList<SQLName>();

    private List<OracleRangeValuesClause> ranges           = new ArrayList<OracleRangeValuesClause>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, interval);
            acceptChild(visitor, storeIn);
            acceptChild(visitor, ranges);
        }
        visitor.endVisit(this);
    }

    public List<OracleRangeValuesClause> getRanges() {
        return ranges;
    }

    public void setRanges(List<OracleRangeValuesClause> ranges) {
        this.ranges = ranges;
    }

    public List<SQLName> getStoreIn() {
        return storeIn;
    }

    public void setStoreIn(List<SQLName> storeIn) {
        this.storeIn = storeIn;
    }

    public SQLExpr getInterval() {
        return interval;
    }

    public void setInterval(SQLExpr interval) {
        this.interval = interval;
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLName> columns) {
        this.columns = columns;
    }

}
