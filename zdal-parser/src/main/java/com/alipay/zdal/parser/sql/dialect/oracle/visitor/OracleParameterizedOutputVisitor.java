/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.visitor;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLInListExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIntegerExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNullExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNumberExpr;
import com.alipay.zdal.parser.sql.visitor.ParameterizedOutputVisitorUtils;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleParameterizedOutputVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:53:03 Exp $
 */
public class OracleParameterizedOutputVisitor extends OracleOutputVisitor {

    public OracleParameterizedOutputVisitor() {
        this(new StringBuilder());
    }

    public OracleParameterizedOutputVisitor(Appendable appender) {
        super(appender);
    }

    public OracleParameterizedOutputVisitor(Appendable appender, boolean printPostSemi) {
        super(appender, printPostSemi);
    }

    public boolean visit(SQLInListExpr x) {
        return ParameterizedOutputVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        x = ParameterizedOutputVisitorUtils.merge(x);

        return super.visit(x);
    }

    public boolean visit(SQLNullExpr x) {
        print('?');
        return false;
    }

    public boolean visit(SQLIntegerExpr x) {
        if (Boolean.TRUE.equals(x.getAttribute(ParameterizedOutputVisitorUtils.ATTR_PARAMS_SKIP))) {
            return super.visit(x);
        }

        print('?');
        return false;
    }

    public boolean visit(SQLNumberExpr x) {
        if (Boolean.TRUE.equals(x.getAttribute(ParameterizedOutputVisitorUtils.ATTR_PARAMS_SKIP))) {
            return super.visit(x);
        }

        print('?');
        return false;
    }

    public boolean visit(SQLCharExpr x) {
        if (Boolean.TRUE.equals(x.getAttribute(ParameterizedOutputVisitorUtils.ATTR_PARAMS_SKIP))) {
            return super.visit(x);
        }

        print('?');
        return false;
    }

    public boolean visit(SQLNCharExpr x) {
        if (Boolean.TRUE.equals(x.getAttribute(ParameterizedOutputVisitorUtils.ATTR_PARAMS_SKIP))) {
            return super.visit(x);
        }

        print('?');
        return false;
    }

}
