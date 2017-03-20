/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: DBType.java, v 0.1 2014-1-6 ÏÂÎç05:17:17 Exp $
 */
public enum DBType {
    ORACLE, MYSQL, DB2;

    public boolean isOracle() {
        return this.equals(DBType.ORACLE);
    }

    public boolean isMysql() {
        return this.equals(DBType.MYSQL);
    }

    public boolean isDB2() {
        return this.equals(DBType.DB2);
    }

    public static DBType convert(String strType) {
        for (DBType t : values()) {
            if (t.toString().equalsIgnoreCase(strType)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid DBType:" + strType
                                           + " must to be [oracle,mysql,db2]");
    }

    public static void main(String[] args) {

        System.out.println(DBType.valueOf("MYSQL"));
    }
}