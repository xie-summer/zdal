/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateIndexStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleCreateIndexStatement.java, v 0.1 2012-11-17 ÏÂÎç3:46:39 Exp $
 */
public class OracleCreateIndexStatement extends SQLCreateIndexStatement implements
                                                                       OracleDDLStatement {

    private static final long serialVersionUID  = 1L;

    private boolean           online            = false;

    private boolean           indexOnlyTopLevel = false;

    private boolean           noParallel;

    private SQLExpr           parallel;

    private SQLName           tablespace;

    public SQLName getTablespace() {
        return tablespace;
    }

    public void setTablespace(SQLName tablespace) {
        this.tablespace = tablespace;
    }

    public SQLExpr getParallel() {
        return parallel;
    }

    public void setParallel(SQLExpr parallel) {
        this.parallel = parallel;
    }

    public boolean isNoParallel() {
        return noParallel;
    }

    public void setNoParallel(boolean noParallel) {
        this.noParallel = noParallel;
    }

    public boolean isIndexOnlyTopLevel() {
        return indexOnlyTopLevel;
    }

    public void setIndexOnlyTopLevel(boolean indexOnlyTopLevel) {
        this.indexOnlyTopLevel = indexOnlyTopLevel;
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getName());
            acceptChild(visitor, getTable());
            acceptChild(visitor, getItems());
            acceptChild(visitor, getTablespace());
            acceptChild(visitor, parallel);
        }
        visitor.endVisit(this);
    }

    // public static enum Type {
    // UNIQUE, BITMAP
    // }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

}
