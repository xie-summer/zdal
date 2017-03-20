/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.jdbc.sorter;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Does not check the exception. Used if the named exception sorter cannot be instantiated.
 *
 * @author ²®ÑÀ
 * @version $Id: NullExceptionSorter.java, v 0.1 2014-1-6 ÏÂÎç05:20:17 Exp $
 */
public class NullExceptionSorter implements ExceptionSorter, Serializable {
    /** The serialVersionUID */
    private static final long serialVersionUID = 202928214888283717L;

    /** 
    * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ExceptionSorter#isExceptionFatal(java.sql.SQLException)
    */
    public boolean isExceptionFatal(SQLException e) {
        return false;

    }
}
