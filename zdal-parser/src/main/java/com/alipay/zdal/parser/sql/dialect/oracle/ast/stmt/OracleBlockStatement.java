/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleParameter;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleBlockStatement.java, v 0.1 2012-11-17 ÏÂÎç3:46:16 Exp $
 */
public class OracleBlockStatement extends OracleStatementImpl {

    private static final long     serialVersionUID = 1L;

    private List<OracleParameter> parameters       = new ArrayList<OracleParameter>();

    private List<SQLStatement>    statementList    = new ArrayList<SQLStatement>();

    public List<SQLStatement> getStatementList() {
        return statementList;
    }

    public void setStatementList(List<SQLStatement> statementList) {
        this.statementList = statementList;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, parameters);
            acceptChild(visitor, statementList);
        }
        visitor.endVisit(this);
    }

    public List<OracleParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<OracleParameter> parameters) {
        this.parameters = parameters;
    }

}
