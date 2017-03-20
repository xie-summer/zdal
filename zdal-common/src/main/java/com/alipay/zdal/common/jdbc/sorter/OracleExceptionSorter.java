/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.jdbc.sorter;

import java.io.Serializable;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * OracleExceptionSorter.java
 *
 *
 * Created: Fri Mar 14 21:54:23 2003
 *
 * @author 伯牙
 * @version $Id: OracleExceptionSorter.java, v 0.1 2014-1-6 下午05:20:41 Exp $
 */
public class OracleExceptionSorter implements ExceptionSorter, Serializable {

    private static final long   serialVersionUID = 573723525408205079L;

    private static final Logger logger           = Logger.getLogger(OracleExceptionSorter.class);

    public OracleExceptionSorter() {
    }

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

    public boolean isExceptionFatal(final SQLException e) {
        final int error_code = Math.abs(e.getErrorCode()); // I can't remember if the errors are negative or positive.
        if (logger.isDebugEnabled()) {
            logger.debug("INFO ## the errorCode = " + error_code + ",error_text = "
                         + e.getMessage());
        }
        if ((error_code == 28) //session has been killed
            || (error_code == 600) //Internal oracle error
            || (error_code == 1012) //not logged on
            || (error_code == 1014) //Oracle shutdown in progress
            || (error_code == 1033) //Oracle initialization or shutdown in progress
            || (error_code == 1034) //Oracle not available
            || (error_code == 1035) //ORACLE only available to users with RESTRICTED SESSION privilege
            || (error_code == 1089) //immediate shutdown in progress - no operations are permitted
            || (error_code == 1090) //shutdown in progress - connection is not permitted
            || (error_code == 1092) //ORACLE instance terminated. Disconnection forced
            || (error_code == 1094) //ALTER DATABASE CLOSE in progress. Connections not permitted
            || (error_code == 2396) //exceeded maximum idle time, please connect again
            || (error_code == 3106) //fatal two-task communication protocol error
            || (error_code == 3111) //break received on communication channel
            || (error_code == 3113) //end-of-file on communication channel
            || (error_code == 3114) //not connected to ORACLE
            || (error_code >= 12100 && error_code <= 12299) // TNS issues
            || (error_code == 17002) //connection reset
            || (error_code == 17008)//connection closed
            || (error_code == 28000)// the account is locked   
            || (error_code == 17410) //No more data to read from socket
            || (error_code == 17447) //OALL8 is in an inconsistent state
            || (error_code == 17401) || (error_code == 3137)//报文错误
            || (error_code == ROLLBACK_ERRORCODE)) {//rollback失败直接剔出连接.
            return true;
        }

        if (e.getMessage() == null) {
            return false;
        }
        final String error_text = (e.getMessage()).toUpperCase();

        // Exclude oracle user defined error codes (20000 through 20999) from consideration when looking for
        // certain strings.

        if ((error_code < 20000 || error_code >= 21000)
            && (error_text.indexOf("NO DATASOURCE") > -1
                || (error_text.indexOf("COULD NOT CREATE CONNECTION") > -1)
                || error_text.indexOf("NO ALIVE DATASOURCE") > -1 //兼容rjdbc抛出的错误
                || (error_text.indexOf("SOCKET") > -1) //for control socket error
                || (error_text.indexOf("CONNECTION HAS ALREADY BEEN CLOSED") > -1)
                || (error_text.indexOf("BROKEN PIPE") > -1)
                || (error_text.indexOf("TNS") > -1 && error_text.indexOf("ORA-") > -1)
                || (error_text.indexOf("Closed Connection") > -1)
                || (error_text.indexOf("关闭的连接") > -1) || (error_text.indexOf("套接字") > -1))) {
            return true;
        }

        return false;
    }
    //    public static void main(String[] args) {
    //        int error_code = 0;
    //        String error_text = "关闭的连接";
    //        boolean result = false;
    //        if ((error_code < 20000 || error_code >= 21000)
    //            && (error_text.indexOf("NO DATASOURCE") > -1
    //                || (error_text.indexOf("COULD NOT CREATE CONNECTION") > -1)
    //                || error_text.indexOf("NO ALIVE DATASOURCE") > -1 //兼容rjdbc抛出的错误
    //                || (error_text.indexOf("SOCKET") > -1) //for control socket error
    //                || (error_text.indexOf("CONNECTION HAS ALREADY BEEN CLOSED") > -1)
    //                || (error_text.indexOf("BROKEN PIPE") > -1)
    //                || (error_text.indexOf("TNS") > -1 && error_text.indexOf("ORA-") > -1)
    //                || (error_text.indexOf("Closed Connection") > -1) || (error_text.indexOf("关闭的连接") > -1))) {
    //            result = true;
    //        }
    //
    //        System.out.println(result);
    //    }
}