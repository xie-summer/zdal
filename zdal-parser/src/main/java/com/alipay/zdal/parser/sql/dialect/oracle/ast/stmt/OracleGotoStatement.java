/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleGotoStatement.java, v 0.1 2012-11-17 ÏÂÎç3:48:06 Exp $
 */
public class OracleGotoStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLName           label;

    public OracleGotoStatement() {
    }

    public OracleGotoStatement(SQLName label) {
        this.label = label;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, label);
        }
        visitor.endVisit(this);
    }

    public SQLName getLabel() {
        return label;
    }

    public void setLabel(SQLName label) {
        this.label = label;
    }

}
