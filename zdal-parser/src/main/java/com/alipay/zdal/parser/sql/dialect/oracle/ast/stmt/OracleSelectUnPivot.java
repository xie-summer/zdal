/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectUnPivot.java, v 0.1 2012-11-17 ÏÂÎç3:50:29 Exp $
 */
public class OracleSelectUnPivot extends OracleSelectPivotBase {

    private static final long                  serialVersionUID = 1L;

    private NullsIncludeType                   nullsIncludeType;
    private final List<SQLExpr>                items            = new ArrayList<SQLExpr>();

    private final List<OracleSelectPivot.Item> pivotIn          = new ArrayList<Item>();

    public OracleSelectUnPivot() {

    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.pivotIn);
        }
        visitor.endVisit(this);
    }

    public List<OracleSelectPivot.Item> getPivotIn() {
        return this.pivotIn;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }

    public NullsIncludeType getNullsIncludeType() {
        return this.nullsIncludeType;
    }

    public void setNullsIncludeType(NullsIncludeType nullsIncludeType) {
        this.nullsIncludeType = nullsIncludeType;
    }

    public static enum NullsIncludeType {
        INCLUDE_NULLS, EXCLUDE_NULLS;

        public static String toString(NullsIncludeType type) {
            if (INCLUDE_NULLS.equals(type)) {
                return "INCLUDE NULLS";
            }
            if (EXCLUDE_NULLS.equals(type)) {
                return "EXCLUDE NULLS";
            }

            throw new IllegalArgumentException();
        }
    }
}
