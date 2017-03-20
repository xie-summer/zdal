/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLPartitioningClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleCreateTableStatement.java, v 0.1 2012-11-17 ÏÂÎç3:46:54 Exp $
 */
public class OracleCreateTableStatement extends SQLCreateTableStatement implements
                                                                       OracleDDLStatement {

    private static final long     serialVersionUID  = 1L;

    private SQLName               tablespace;

    private SQLSelect             select;

    private boolean               inMemoryMetadata;

    private boolean               cursorSpecificSegment;

    // NOPARALLEL
    private Boolean               parallel;

    private OracleStorageClause   storage;

    private boolean               organizationIndex = false;

    private SQLExpr               ptcfree;
    private SQLExpr               pctused;
    private SQLExpr               initrans;
    private SQLExpr               maxtrans;

    private Boolean               logging;
    private Boolean               compress;
    private boolean               onCommit;
    private boolean               preserveRows;

    private Boolean               cache;

    private SQLPartitioningClause partitioning;

    public SQLPartitioningClause getPartitioning() {
        return partitioning;
    }

    public void setPartitioning(SQLPartitioningClause partitioning) {
        this.partitioning = partitioning;
    }

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public boolean isOnCommit() {
        return onCommit;
    }

    public void setOnCommit(boolean onCommit) {
        this.onCommit = onCommit;
    }

    public boolean isPreserveRows() {
        return preserveRows;
    }

    public void setPreserveRows(boolean preserveRows) {
        this.preserveRows = preserveRows;
    }

    public Boolean getLogging() {
        return logging;
    }

    public void setLogging(Boolean logging) {
        this.logging = logging;
    }

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public SQLExpr getPtcfree() {
        return ptcfree;
    }

    public void setPtcfree(SQLExpr ptcfree) {
        this.ptcfree = ptcfree;
    }

    public SQLExpr getPctused() {
        return pctused;
    }

    public void setPctused(SQLExpr pctused) {
        this.pctused = pctused;
    }

    public SQLExpr getInitrans() {
        return initrans;
    }

    public void setInitrans(SQLExpr initrans) {
        this.initrans = initrans;
    }

    public SQLExpr getMaxtrans() {
        return maxtrans;
    }

    public void setMaxtrans(SQLExpr maxtrans) {
        this.maxtrans = maxtrans;
    }

    public boolean isOrganizationIndex() {
        return organizationIndex;
    }

    public void setOrganizationIndex(boolean organizationIndex) {
        this.organizationIndex = organizationIndex;
    }

    public Boolean getParallel() {
        return parallel;
    }

    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }

    public boolean isCursorSpecificSegment() {
        return cursorSpecificSegment;
    }

    public void setCursorSpecificSegment(boolean cursorSpecificSegment) {
        this.cursorSpecificSegment = cursorSpecificSegment;
    }

    public boolean isInMemoryMetadata() {
        return inMemoryMetadata;
    }

    public void setInMemoryMetadata(boolean inMemoryMetadata) {
        this.inMemoryMetadata = inMemoryMetadata;
    }

    public SQLName getTablespace() {
        return tablespace;
    }

    public void setTablespace(SQLName tablespace) {
        this.tablespace = tablespace;
    }

    public SQLSelect getSelect() {
        return select;
    }

    public void setSelect(SQLSelect select) {
        this.select = select;
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public OracleStorageClause getStorage() {
        return storage;
    }

    public void setStorage(OracleStorageClause storage) {
        this.storage = storage;
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSource);
            this.acceptChild(visitor, tableElementList);
            this.acceptChild(visitor, tablespace);
            this.acceptChild(visitor, select);
            this.acceptChild(visitor, storage);
            this.acceptChild(visitor, partitioning);
        }
        visitor.endVisit(this);
    }

}
