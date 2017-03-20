/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlStartTransactionStatement.java, v 0.1 2012-11-17 ÏÂÎç3:39:00 Exp $
 */
public class MySqlStartTransactionStatement extends MySqlStatementImpl {

    private static final long serialVersionUID   = 1L;

    private boolean           consistentSnapshot = false;

    private boolean           begin              = false;
    private boolean           work               = false;

    public boolean isConsistentSnapshot() {
        return consistentSnapshot;
    }

    public void setConsistentSnapshot(boolean consistentSnapshot) {
        this.consistentSnapshot = consistentSnapshot;
    }

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }
}
