/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectPivotBase.java, v 0.1 2012-11-17 ÏÂÎç3:49:52 Exp $
 */
public abstract class OracleSelectPivotBase extends OracleSQLObjectImpl {

    private static final long     serialVersionUID = 1L;

    protected final List<SQLExpr> pivotFor         = new ArrayList<SQLExpr>();

    public OracleSelectPivotBase() {

    }

    public List<SQLExpr> getPivotFor() {
        return this.pivotFor;
    }
}
