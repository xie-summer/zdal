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
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCaseExpr.java, v 0.1 2012-11-17 ÏÂÎç3:16:13 xiaoqing.zhouxq Exp $
 */
public class SQLCaseExpr extends SQLExprImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Item>  items            = new ArrayList<Item>();
    private SQLExpr           valueExpr;
    private SQLExpr           elseExpr;

    public SQLCaseExpr() {

    }

    public void output(StringBuffer buf) {
        buf.append("CASE ");
        if (this.valueExpr != null) {
            this.valueExpr.output(buf);
            buf.append(" ");
        }

        int i = 0;
        for (int size = this.items.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(" ");
            }
            ((Item) this.items.get(i)).output(buf);
        }

        if (this.elseExpr != null) {
            buf.append(" ELSE ");
            this.elseExpr.output(buf);
        }

        buf.append(" END");
    }

    public SQLExpr getValueExpr() {
        return this.valueExpr;
    }

    public void setValueExpr(SQLExpr valueExpr) {
        this.valueExpr = valueExpr;
    }

    public SQLExpr getElseExpr() {
        return this.elseExpr;
    }

    public void setElseExpr(SQLExpr elseExpr) {
        this.elseExpr = elseExpr;
    }

    public List<Item> getItems() {
        return this.items;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.valueExpr);
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.elseExpr);
        }
        visitor.endVisit(this);
    }

    public static class Item extends SQLObjectImpl implements Serializable {

        private static final long serialVersionUID = 1L;
        private SQLExpr           conditionExpr;
        private SQLExpr           valueExpr;

        public Item() {

        }

        public Item(SQLExpr conditionExpr, SQLExpr valueExpr) {

            this.conditionExpr = conditionExpr;
            this.valueExpr = valueExpr;
        }

        public SQLExpr getConditionExpr() {
            return this.conditionExpr;
        }

        public void setConditionExpr(SQLExpr conditionExpr) {
            this.conditionExpr = conditionExpr;
        }

        public SQLExpr getValueExpr() {
            return this.valueExpr;
        }

        public void setValueExpr(SQLExpr valueExpr) {
            this.valueExpr = valueExpr;
        }

        public void output(StringBuffer buf) {
            buf.append("WHEN ");
            this.conditionExpr.output(buf);
            buf.append(" THEN ");
            this.valueExpr.output(buf);
        }

        protected void accept0(SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.conditionExpr);
                acceptChild(visitor, this.valueExpr);
            }
            visitor.endVisit(this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elseExpr == null) ? 0 : elseExpr.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((valueExpr == null) ? 0 : valueExpr.hashCode());
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
        SQLCaseExpr other = (SQLCaseExpr) obj;
        if (elseExpr == null) {
            if (other.elseExpr != null) {
                return false;
            }
        } else if (!elseExpr.equals(other.elseExpr)) {
            return false;
        }
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!items.equals(other.items)) {
            return false;
        }
        if (valueExpr == null) {
            if (other.valueExpr != null) {
                return false;
            }
        } else if (!valueExpr.equals(other.valueExpr)) {
            return false;
        }
        return true;
    }

}
