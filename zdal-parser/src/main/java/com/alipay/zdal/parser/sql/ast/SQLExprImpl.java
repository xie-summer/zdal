/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLExprImpl.java, v 0.1 2012-11-17 обнГ3:12:52 xiaoqing.zhouxq Exp $
 */
public abstract class SQLExprImpl extends SQLObjectImpl implements SQLExpr {

    private static final long serialVersionUID = 1278977287415092601L;

    public SQLExprImpl() {

    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}
