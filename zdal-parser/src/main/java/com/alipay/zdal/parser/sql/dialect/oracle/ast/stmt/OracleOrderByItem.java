/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLOrderingSpecification;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectOrderByItem;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleOrderByItem.java, v 0.1 2012-11-17 ÏÂÎç3:48:59 Exp $
 */
public class OracleOrderByItem extends SQLSelectOrderByItem {

    private static final long serialVersionUID = 1L;

    private NullsOrderType    nullsOrderType;

    public OracleOrderByItem() {

    }

    public NullsOrderType getNullsOrderType() {
        return this.nullsOrderType;
    }

    public void setNullsOrderType(NullsOrderType nullsOrderType) {
        this.nullsOrderType = nullsOrderType;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
        } else {
            super.accept0(visitor);
        }
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        this.expr.output(buf);
        if (SQLOrderingSpecification.ASC.equals(this.type)) {
            buf.append(" ASC");
        } else if (SQLOrderingSpecification.DESC.equals(this.type)) {
            buf.append(" DESC");
        }
        if (NullsOrderType.NullsFirst.equals(this.nullsOrderType)) {
            buf.append(" NULLS FIRST");
        } else if (NullsOrderType.NullsLast.equals(this.nullsOrderType)) {
            buf.append(" NULLS LAST");
        }
    }

    public static enum NullsOrderType {
        NullsFirst, NullsLast;

        public String toFormalString() {
            if (NullsFirst.equals(this)) {
                return "NULLS FIRST";
            }

            if (NullsLast.equals(this)) {
                return "NULLS LAST";
            }

            throw new IllegalArgumentException();
        }
    }

}
