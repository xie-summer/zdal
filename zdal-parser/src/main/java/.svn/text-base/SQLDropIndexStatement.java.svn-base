/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLDropIndexStatement.java, v 0.1 2012-11-17 ÏÂÎç3:21:55 xiaoqing.zhouxq Exp $
 */
public class SQLDropIndexStatement extends SQLStatementImpl implements SQLDDLStatement {

    private static final long serialVersionUID = 1L;

    private SQLExpr           indexName;
    private SQLExpr           tableName;

    public SQLExpr getIndexName() {
        return indexName;
    }

    public void setIndexName(SQLExpr indexName) {
        this.indexName = indexName;
    }

    public SQLExpr getTableName() {
        return tableName;
    }

    public void setTableName(SQLExpr tableName) {
        this.tableName = tableName;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, indexName);
            acceptChild(visitor, tableName);
        }
        visitor.endVisit(this);
    }
}
