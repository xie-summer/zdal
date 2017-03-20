/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.sql.SQLException;

import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;

/**
 * A GenericExceptionSorter returning true for all exceptions.
 * 
 * @author 伯牙
 * @version $Id: GenericExceptionSorter.java, v 0.1 2014-1-6 下午05:28:18 Exp $
 */
public class GenericExceptionSorter implements ExceptionSorter {

    /** 
    * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ExceptionSorter#isExceptionFatal(java.sql.SQLException)
    */
    public boolean isExceptionFatal(final SQLException e) {
        return true;
    }

}
