/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleArgumentExpr.java, v 0.1 2012-11-17 ÏÂÎç3:43:04 Exp $
 */
public class OracleArgumentExpr extends OracleSQLObjectImpl implements SQLExpr {

    private static final long serialVersionUID = 1L;
    private String            argumentName;
    private SQLExpr           value;

    public OracleArgumentExpr() {

    }

    public OracleArgumentExpr(String argumentName, SQLExpr value) {
        this.argumentName = argumentName;
        this.value = value;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, value);
        }
        visitor.endVisit(this);
    }

}
