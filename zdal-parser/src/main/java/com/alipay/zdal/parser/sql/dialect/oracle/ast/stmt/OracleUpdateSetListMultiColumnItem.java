/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateSetListMultiColumnItem.java, v 0.1 2012-11-17 ÏÂÎç3:51:19 Exp $
 */
public class OracleUpdateSetListMultiColumnItem extends OracleUpdateSetListItem {

    private static final long   serialVersionUID = 1L;

    private final List<SQLName> columns          = new ArrayList<SQLName>();
    private OracleSelect        subQuery;

    public OracleUpdateSetListMultiColumnItem() {

    }

    public OracleUpdateSetListMultiColumnItem(OracleSelect subQuery) {

        this.subQuery = subQuery;
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.subQuery);
        }

        visitor.endVisit(this);
    }

    public OracleSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(OracleSelect subQuery) {
        this.subQuery = subQuery;
    }

    public List<SQLName> getColumns() {
        return this.columns;
    }
}
