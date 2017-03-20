/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.visitor;

import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlBooleanExpr;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySql2OracleOutputVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:40:32 Exp $
 */
public class MySql2OracleOutputVisitor extends MySqlOutputVisitor {

    public MySql2OracleOutputVisitor(Appendable appender) {
        super(appender);
    }

    public boolean visit(MySqlBooleanExpr x) {
        return true;
    }

    public void endVisit(MySqlBooleanExpr x) {
    }

}
