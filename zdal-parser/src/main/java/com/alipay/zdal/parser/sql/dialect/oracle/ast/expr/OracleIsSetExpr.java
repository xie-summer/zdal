/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleIsSetExpr.java, v 0.1 2012-11-17 ÏÂÎç3:44:14 Exp $
 */
public class OracleIsSetExpr extends SQLExprImpl implements OracleExpr {

    private static final long serialVersionUID = 1L;

    private SQLExpr           nestedTable;

    public OracleIsSetExpr() {
    }

    public OracleIsSetExpr(SQLExpr nestedTable) {
        this.nestedTable = nestedTable;
    }

    public SQLExpr getNestedTable() {
        return nestedTable;
    }

    public void setNestedTable(SQLExpr nestedTable) {
        this.nestedTable = nestedTable;
    }

    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, nestedTable);
        }
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nestedTable == null) ? 0 : nestedTable.hashCode());
        return result;
    }

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
        OracleIsSetExpr other = (OracleIsSetExpr) obj;
        if (nestedTable == null) {
            if (other.nestedTable != null) {
                return false;
            }
        } else if (!nestedTable.equals(other.nestedTable)) {
            return false;
        }
        return true;
    }

}
