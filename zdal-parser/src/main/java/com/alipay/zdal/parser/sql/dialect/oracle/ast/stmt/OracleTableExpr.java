/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleTableExpr.java, v 0.1 2012-11-17 ÏÂÎç3:50:51 Exp $
 */
public class OracleTableExpr extends SQLExprImpl {

    private static final long   serialVersionUID = 1L;

    private SQLExpr             table;
    private String              dbLink;
    private SQLName             partition;
    private SQLName             subPartition;
    private final List<SQLName> partitionFor     = new ArrayList<SQLName>(1);
    private final List<SQLName> subPartitionFor  = new ArrayList<SQLName>(1);

    public OracleTableExpr() {

    }

    public SQLName getPartition() {
        return this.partition;
    }

    public void setPartition(SQLName partition) {
        this.partition = partition;
    }

    public SQLName getSubPartition() {
        return this.subPartition;
    }

    public void setSubPartition(SQLName subPartition) {
        this.subPartition = subPartition;
    }

    public List<SQLName> getSubPartitionFor() {
        return this.subPartitionFor;
    }

    public List<SQLName> getPartitionFor() {
        return this.partitionFor;
    }

    public SQLExpr getTable() {
        return this.table;
    }

    public void setTable(SQLExpr table) {
        this.table = table;
    }

    public String getDbLink() {
        return this.dbLink;
    }

    public void setDbLink(String dbLink) {
        this.dbLink = dbLink;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.table);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dbLink == null) ? 0 : dbLink.hashCode());
        result = prime * result + ((partition == null) ? 0 : partition.hashCode());
        result = prime * result + ((partitionFor == null) ? 0 : partitionFor.hashCode());
        result = prime * result + ((subPartition == null) ? 0 : subPartition.hashCode());
        result = prime * result + ((subPartitionFor == null) ? 0 : subPartitionFor.hashCode());
        result = prime * result + ((table == null) ? 0 : table.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OracleTableExpr other = (OracleTableExpr) obj;
        if (dbLink == null) {
            if (other.dbLink != null) {
                return false;
            }
        } else if (!dbLink.equals(other.dbLink)) {
            return false;
        }
        if (partition == null) {
            if (other.partition != null) {
                return false;
            }
        } else if (!partition.equals(other.partition)) {
            return false;
        }
        if (partitionFor == null) {
            if (other.partitionFor != null) {
                return false;
            }
        } else if (!partitionFor.equals(other.partitionFor)) {
            return false;
        }
        if (subPartition == null) {
            if (other.subPartition != null) {
                return false;
            }
        } else if (!subPartition.equals(other.subPartition)) {
            return false;
        }
        if (subPartitionFor == null) {
            if (other.subPartitionFor != null) {
                return false;
            }
        } else if (!subPartitionFor.equals(other.subPartitionFor)) {
            return false;
        }
        if (table == null) {
            if (other.table != null) {
                return false;
            }
        } else if (!table.equals(other.table)) {
            return false;
        }
        return true;
    }
}
