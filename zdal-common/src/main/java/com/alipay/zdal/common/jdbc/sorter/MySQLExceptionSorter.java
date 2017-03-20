/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.jdbc.sorter;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * MySQLExceptionSorter
 * 
 * This is a basic exception sorter for the MySQL RDBMS. All error
 * codes are taken from the MySQL Connector Java 3.0.16 SQLError class. 
 *
 * @author 伯牙
 * @version $Id: MySQLExceptionSorter.java, v 0.1 2014-1-6 下午05:20:10 Exp $
 */
public class MySQLExceptionSorter implements ExceptionSorter, Serializable {
    private static final long serialVersionUID = 2375890129763721017L;

    //    public boolean isExceptionFatal(SQLException e) {
    //        int loopCount = 20; //防止人为失误，当两个Throwable互为对方的initCause()时，造成死循环
    //
    //        Throwable cause = e;
    //        while (cause != null) {
    //            if (cause instanceof SQLException) {
    //                SQLException sqlException = (SQLException) cause;
    //
    //                if (isExceptionFatal0(sqlException))
    //                    return true;
    //            }
    //
    //            cause = cause.getCause();
    //
    //            if (--loopCount < 0)
    //                break;
    //        }
    //
    //        return false;
    //    }

    public boolean isExceptionFatal(SQLException e) {
        String sqlState = e.getSQLState();
        if (sqlState != null && sqlState.startsWith("08")) {
            return true;
        }
        switch (e.getErrorCode()) {
            // Communications Errors
            case 1040: // ER_CON_COUNT_ERROR
            case 1042: // ER_BAD_HOST_ERROR
            case 1043: // ER_HANDSHAKE_ERROR
            case 1047: // ER_UNKNOWN_COM_ERROR
            case 1081: // ER_IPSOCK_ERROR
            case 1129: // ER_HOST_IS_BLOCKED
            case 1130: // ER_HOST_NOT_PRIVILEGED

                // Authentication Errors
            case 1045: // ER_ACCESS_DENIED_ERROR

                // Resource errors
            case 1004: // ER_CANT_CREATE_FILE
            case 1005: // ER_CANT_CREATE_TABLE
            case 1015: // ER_CANT_LOCK
            case 1021: // ER_DISK_FULL
            case 1041: // ER_OUT_OF_RESOURCES

                // Out-of-memory errors
            case 1037: // ER_OUTOFMEMORY
            case 1038: // ER_OUT_OF_SORTMEMORY
            case ROLLBACK_ERRORCODE:// rollback失败.
                return true;
        }

        final String error_text = (e.getMessage()).toUpperCase();
        if (error_text.indexOf("COMMUNICATIONS LINK FAILURE") > -1
            || error_text.indexOf("COULD NOT CREATE CONNECTION") > -1
            || error_text.indexOf("ACCESS DENIED FOR USER") > -1
            || error_text.indexOf("NO DATASOURCE") > -1
            || error_text.indexOf("NO ALIVE DATASOURCE") > -1) {// errorCode忽略并且异常信息为连接出错
            return true;
        }

        return false;
    }
}