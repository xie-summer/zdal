/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLJoinTableSource.java, v 0.1 2012-11-17 ÏÂÎç3:22:45 xiaoqing.zhouxq Exp $
 */
public class SQLJoinTableSource extends SQLTableSourceImpl {

    private static final long serialVersionUID = 1L;

    protected SQLTableSource  left;
    protected JoinType        joinType;
    protected SQLTableSource  right;
    protected SQLExpr         condition;

    public SQLJoinTableSource(String alias) {
        super(alias);
    }

    public SQLJoinTableSource() {

    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.left);
            acceptChild(visitor, this.right);
            acceptChild(visitor, this.condition);
        }

        visitor.endVisit(this);
    }

    public JoinType getJoinType() {
        return this.joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public SQLTableSource getLeft() {
        return this.left;
    }

    public void setLeft(SQLTableSource left) {
        this.left = left;
    }

    public SQLTableSource getRight() {
        return this.right;
    }

    public void setRight(SQLTableSource right) {
        this.right = right;
    }

    public SQLExpr getCondition() {
        return this.condition;
    }

    public void setCondition(SQLExpr condition) {
        this.condition = condition;
    }

    public void output(StringBuffer buf) {
        this.left.output(buf);
        buf.append(' ');
        buf.append(JoinType.toString(this.joinType));
        buf.append(' ');
        this.right.output(buf);

        if (this.condition != null) {
            buf.append(" ON ");
            this.condition.output(buf);
        }
    }

    public static enum JoinType {
        COMMA(","), // 
        JOIN("JOIN"), //
        INNER_JOIN("INNER JOIN"), // 
        CROSS_JOIN("CROSS JOIN"), // 
        NATURAL_JOIN("NATURAL JOIN"), // 
        NATURAL_INNER_JOIN("NATURAL INNER JOIN"), // 
        LEFT_OUTER_JOIN("LEFT JOIN"), RIGHT_OUTER_JOIN("RIGHT JOIN"), FULL_OUTER_JOIN("FULL JOIN"), STRAIGHT_JOIN(
                                                                                                                  "STRAIGHT_JOIN");

        public final String name;

        JoinType(String name) {
            this.name = name;
        }

        public static String toString(JoinType joinType) {
            return joinType.name;
        }
    }
}
