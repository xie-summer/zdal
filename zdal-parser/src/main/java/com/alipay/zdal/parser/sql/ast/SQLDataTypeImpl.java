/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLDataTypeImpl.java, v 0.1 2012-11-17 ÏÂÎç3:12:33 xiaoqing.zhouxq Exp $
 */
public class SQLDataTypeImpl extends SQLObjectImpl implements SQLDataType {

    private static final long     serialVersionUID = -2783296007802532452L;

    protected String              name;
    protected final List<SQLExpr> arguments        = new ArrayList<SQLExpr>();

    public SQLDataTypeImpl() {

    }

    public SQLDataTypeImpl(String name) {

        this.name = name;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.arguments);
        }

        visitor.endVisit(this);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.arguments);
        }

        visitor.endVisit(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SQLExpr> getArguments() {
        return this.arguments;
    }

    public void output(StringBuffer buf) {
        buf.append(this.name);
        if (this.arguments.size() > 0) {
            buf.append("(");
            int i = 0;
            for (int size = this.arguments.size(); i < size; ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                ((SQLExpr) this.arguments.get(i)).output(buf);
            }
            buf.append(")");
        }
    }
}
