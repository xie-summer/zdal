/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleStatementImpl.java, v 0.1 2012-11-17 ÏÂÎç3:50:45 Exp $
 */
public abstract class OracleStatementImpl extends SQLStatementImpl implements OracleStatement {

    private static final long serialVersionUID = 1L;

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public abstract void accept0(OracleASTVisitor visitor);
}
