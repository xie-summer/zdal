/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateSetValueClause.java, v 0.1 2012-11-17 ÏÂÎç3:51:32 Exp $
 */
public class OracleUpdateSetValueClause extends OracleUpdateSetClause {

    private static final long serialVersionUID = 1L;

    public OracleUpdateSetValueClause() {

    }

    public void accept0(OracleASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }
}
