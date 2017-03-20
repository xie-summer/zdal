/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLObjectCreateExpr.java, v 0.1 2012-11-17 ÏÂÎç3:18:59 xiaoqing.zhouxq Exp $
 */
public class SQLObjectCreateExpr extends SQLExprImpl implements Serializable {

    private static final long  serialVersionUID = 1L;
    public String              objType;
    public final List<SQLExpr> paramList        = new ArrayList<SQLExpr>();

    public SQLObjectCreateExpr() {

    }

    public SQLObjectCreateExpr(String objType) {

        this.objType = objType;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.paramList);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objType == null) ? 0 : objType.hashCode());
        result = prime * result + ((paramList == null) ? 0 : paramList.hashCode());
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
        SQLObjectCreateExpr other = (SQLObjectCreateExpr) obj;
        if (objType == null) {
            if (other.objType != null) {
                return false;
            }
        } else if (!objType.equals(other.objType)) {
            return false;
        }
        if (paramList == null) {
            if (other.paramList != null) {
                return false;
            }
        } else if (!paramList.equals(other.paramList)) {
            return false;
        }
        return true;
    }
}
