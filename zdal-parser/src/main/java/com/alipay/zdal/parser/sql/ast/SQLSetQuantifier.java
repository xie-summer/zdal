/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLSetQuantifier.java, v 0.1 2012-11-17 обнГ3:13:59 xiaoqing.zhouxq Exp $
 */
public interface SQLSetQuantifier {

    // SQL 92
    public final static int ALL         = 1;
    public final static int DISTINCT    = 2;

    public final static int UNIQUE      = 3;
    public final static int DISTINCTROW = 4;

    // <SetQuantifier>
}
