/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLUnaryOperator.java, v 0.1 2012-11-17 обнГ3:19:37 xiaoqing.zhouxq Exp $
 */
public enum SQLUnaryOperator {
    Plus("+"), Negative("-"), Not("!"), Compl("~"), Prior("PRIOR"), ConnectByRoot("CONNECT BY"), NOT(
                                                                                                     "NOT");

    public final String name;

    SQLUnaryOperator(String name) {
        this.name = name;
    }
}
