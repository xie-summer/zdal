/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLAlterTableDropColumnItem.java, v 0.1 2012-11-17 ÏÂÎç3:20:07 xiaoqing.zhouxq Exp $
 */
public class SQLAlterTableDropColumnItem extends SQLObjectImpl implements SQLAlterTableItem {

    private static final long serialVersionUID = 1L;
    private SQLName           columnName;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columnName);
        }
        visitor.endVisit(this);
    }

    public SQLName getColumnName() {
        return columnName;
    }

    public void setColumnName(SQLName columnName) {
        this.columnName = columnName;
    }

}
