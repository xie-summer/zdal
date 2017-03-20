/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExprImpl;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLTextLiteralExpr.java, v 0.1 2012-11-17 ÏÂÎç3:19:24 xiaoqing.zhouxq Exp $
 */
public abstract class SQLTextLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    protected String          text;

    public SQLTextLiteralExpr() {

    }

    public SQLTextLiteralExpr(String text) {

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        SQLTextLiteralExpr other = (SQLTextLiteralExpr) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }
}
