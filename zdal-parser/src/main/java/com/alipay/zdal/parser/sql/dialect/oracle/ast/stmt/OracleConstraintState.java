/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleConstraintState.java, v 0.1 2012-11-17 ÏÂÎç3:46:34 Exp $
 */
public class OracleConstraintState extends SQLObjectImpl {

    private static final long     serialVersionUID = 1L;

    private final List<SQLObject> states           = new ArrayList<SQLObject>();

    public OracleConstraintState() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.states);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getStates() {
        return this.states;
    }
}
