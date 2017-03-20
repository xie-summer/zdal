/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLPartitioningClause;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlPartitionByKey.java, v 0.1 2012-11-17 ÏÂÎç3:33:44 Exp $
 */
public class MySqlPartitionByKey extends MySqlObjectImpl implements SQLPartitioningClause {

    private static final long serialVersionUID = 1L;

    private List<SQLName>     columns          = new ArrayList<SQLName>();

    private SQLExpr           partitionCount;

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, partitionCount);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(SQLExpr partitionCount) {
        this.partitionCount = partitionCount;
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLName> columns) {
        this.columns = columns;
    }

}
