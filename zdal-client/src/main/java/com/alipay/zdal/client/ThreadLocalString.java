/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client;

public class ThreadLocalString {
    /** 线程变量,用于绕过sql解析，执行复杂的sql语句. */
    public static final String ROUTE_CONDITION     = "ROUTE_CONDITION";
    /**
    * 即选择某个读库来执行某条sql
    */
    public static final String DATABASE_INDEX      = "DATABASE_INDEX";
    /**
     * 在分库分表的情况下，选择写库来执行某条sql
     */
    public static final String SELECT_DATABASE     = "SELECT_DATABASE";
    /**
     * 最后的sql是在哪个库执行的，以及该库的标识id
     */
    public static final String GET_ID_AND_DATABASE = "GET_ID_AND_DATABASE";

}
