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
 * @version $Id: OracleAlterTablespaceStatement.java, v 0.1 2012-11-17 ÏÂÎç3:45:47 Exp $
 */
public class OracleAlterTablespaceStatement extends OracleStatementImpl {

    private static final long         serialVersionUID = 1L;

    private SQLName                   name;
    private OracleAlterTablespaceItem item;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, item);
        }
        visitor.endVisit(this);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public OracleAlterTablespaceItem getItem() {
        return item;
    }

    public void setItem(OracleAlterTablespaceItem item) {
        this.item = item;
    }

}
