/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.jdbc.sorter;

import java.sql.SQLException;

/**
 * The ExceptionSorter interface allows for <code>java.sql.SQLException</code>
 * evaluation to determine if an error is fatal. 
 *
 * 
 * 
 * @author 伯牙
 * @version $Id: ExceptionSorter.java, v 0.1 2014-1-6 下午05:20:01 Exp $
 */
public interface ExceptionSorter {

    /**
     * Evaluates a <code>java.sql.SQLException</code> to determine if
     * the error was fatal
     * 
     * @param e the <code>java.sql.SQLException</code>
     * 
     * @return whether or not the exception is vatal.
     */
    boolean isExceptionFatal(SQLException e);

    /** rollback失败的时候直接抛出这个errorcode，zdal-datasource对于这个异常直接剔出连接. */
    public static final int ROLLBACK_ERRORCODE = 999999;
}
