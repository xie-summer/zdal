/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast;

import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSQLObjectImpl.java, v 0.1 2012-11-17 ÏÂÎç3:41:31 Exp $
 */
public abstract class OracleSQLObjectImpl extends SQLObjectImpl implements OracleSQLObject {

    private static final long serialVersionUID = 1L;

    public OracleSQLObjectImpl() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public abstract void accept0(OracleASTVisitor visitor);
}
