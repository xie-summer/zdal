/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateSetListClause.java, v 0.1 2012-11-17 ÏÂÎç3:51:08 Exp $
 */
public class OracleUpdateSetListClause extends OracleUpdateSetClause {

    private static final long                   serialVersionUID = 1L;

    private final List<OracleUpdateSetListItem> items            = new ArrayList<OracleUpdateSetListItem>();

    public OracleUpdateSetListClause() {

    }

    public List<OracleUpdateSetListItem> getItems() {
        return this.items;
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }
}
