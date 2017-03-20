/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleConstraint.java, v 0.1 2012-11-17 ÏÂÎç3:46:27 Exp $
 */
public abstract class OracleConstraint extends OracleSQLObjectImpl {

    private static final long       serialVersionUID = 1L;

    protected OracleConstraintState state;
    protected SQLName               name;

    public OracleConstraint() {

    }

    public SQLName getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null)
            this.name = null;
        else
            this.name = new SQLIdentifierExpr(name);
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public OracleConstraintState getState() {
        return this.state;
    }

    public void setState(OracleConstraintState state) {
        this.state = state;
    }
}
