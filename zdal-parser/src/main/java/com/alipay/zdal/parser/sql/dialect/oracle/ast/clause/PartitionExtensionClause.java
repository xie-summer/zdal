/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: PartitionExtensionClause.java, v 0.1 2012-11-17 ÏÂÎç3:42:25 Exp $
 */
public class PartitionExtensionClause extends OracleSQLObjectImpl {

    private static final long   serialVersionUID = 1L;

    private boolean             subPartition;
    private SQLName             partition;
    private final List<SQLName> target           = new ArrayList<SQLName>();

    public boolean isSubPartition() {
        return subPartition;
    }

    public void setSubPartition(boolean subPartition) {
        this.subPartition = subPartition;
    }

    public SQLName getPartition() {
        return partition;
    }

    public void setPartition(SQLName partition) {
        this.partition = partition;
    }

    public List<SQLName> getFor() {
        return target;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partition);
            acceptChild(visitor, target);
        }
        visitor.endVisit(this);
    }

}
