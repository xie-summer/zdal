/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCommentHint.java, v 0.1 2012-11-17 ÏÂÎç3:11:29 xiaoqing.zhouxq Exp $
 */
public class SQLCommentHint extends SQLObjectImpl implements SQLHint {

    private static final long serialVersionUID = 1L;

    private String            text;

    public SQLCommentHint() {

    }

    public SQLCommentHint(String text) {

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
