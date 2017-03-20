/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SqlType.java, v 0.1 2014-1-6 ÏÂÎç05:17:36 Exp $
 */
public enum SqlType {
    SELECT(0), INSERT(1), UPDATE(2), DELETE(3), SELECT_FOR_UPDATE(4), SELECT_FROM_DUAL(5), SELECT_FROM_SYSTEMIBM(
                                                                                                                 6), DEFAULT_SQL_TYPE(
                                                                                                                                      -100);
    private int i;

    private SqlType(int i) {
        this.i = i;
    }

    public int value() {
        return this.i;
    }

    public static SqlType valueOf(int i) {
        for (SqlType t : values()) {
            if (t.value() == i) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid SqlType:" + i);
    }
}
