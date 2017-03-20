/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLShardingVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:57:01 Exp $
 */
public class SQLShardingVisitor extends SQLASTVisitorAdapter {

    public void postVisit(SQLObject x) {
    }

    public void preVisit(SQLObject x) {
    }

    public boolean visit(SQLInsertStatement x) {
        return false;
    }
}
