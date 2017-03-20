/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectForUpdate.java, v 0.1 2012-11-17 ÏÂÎç3:49:29 Exp $
 */
public class OracleSelectForUpdate extends OracleSQLObjectImpl {

    private static final long   serialVersionUID = 1L;

    private final List<SQLExpr> of               = new ArrayList<SQLExpr>();

    private boolean             notWait          = false;
    private SQLExpr             wait;
    private boolean             skipLocked       = false;

    public OracleSelectForUpdate() {

    }

    public boolean isNotWait() {
        return notWait;
    }

    public void setNotWait(boolean notWait) {
        this.notWait = notWait;
    }

    public SQLExpr getWait() {
        return wait;
    }

    public void setWait(SQLExpr wait) {
        this.wait = wait;
    }

    public boolean isSkipLocked() {
        return skipLocked;
    }

    public void setSkipLocked(boolean skipLocked) {
        this.skipLocked = skipLocked;
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.of);
            acceptChild(visitor, this.wait);
        }

        visitor.endVisit(this);
    }

    public List<SQLExpr> getOf() {
        return this.of;
    }

    public static class SkipLock {
    }

    public static abstract class Type extends SQLObjectImpl {

        private static final long serialVersionUID = 1L;

        public Type() {

        }
    }
}
