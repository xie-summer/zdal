/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.visitor;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.ast.expr.SQLBetweenExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLInListExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLMethodInvokeExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectGroupByClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectItem;
import com.alipay.zdal.parser.sql.visitor.ExportParameterVisitor;
import com.alipay.zdal.parser.sql.visitor.ExportParameterVisitorUtils;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleExportParameterVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:52:52 Exp $
 */
public class OracleExportParameterVisitor extends OracleASTVisitorAdapter implements
                                                                         ExportParameterVisitor {

    private final List<Object> parameters;

    public OracleExportParameterVisitor() {
        this(new ArrayList<Object>());
    }

    public OracleExportParameterVisitor(List<Object> parameters) {
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public boolean visit(SQLSelectItem x) {
        return false;
    }

    @Override
    public boolean visit(SQLOrderBy x) {
        return false;
    }

    @Override
    public boolean visit(SQLSelectGroupByClause x) {
        return false;
    }

    @Override
    public boolean visit(SQLMethodInvokeExpr x) {
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getParameters());

        return true;
    }

    @Override
    public boolean visit(SQLInListExpr x) {
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getTargetList());

        return true;
    }

    @Override
    public boolean visit(SQLBetweenExpr x) {
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
        return true;
    }

}
