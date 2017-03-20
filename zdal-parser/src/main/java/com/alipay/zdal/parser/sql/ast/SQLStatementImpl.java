/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLStatementImpl.java, v 0.1 2012-11-17 ÏÂÎç3:14:16 xiaoqing.zhouxq Exp $
 */
public abstract class SQLStatementImpl extends SQLObjectImpl implements SQLStatement {

    private static final long serialVersionUID = 1L;

    public SQLStatementImpl() {

    }

    public void output(StringBuffer buf) {
        buf.append(super.toString());
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
}
