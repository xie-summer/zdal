/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.jdbc.sorter;

import java.io.Serializable;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * A DB2ExceptionSorter current only supporting the Type 4 Universal driver.
 * Note, currently the DB2 JDBC developers guide only reports a few error codes.
 * The code -9999 implies that the condition does not have a related code.
 * 
 * TODO DB2 CLI
 * 需要根据DB2的错误码判断是否需要把对应的连接踢掉.
 * @author 伯牙
 * @version $Id: DB2ExceptionSorter.java, v 0.1 2014-1-1 下午08:29:16 Exp $
 */
public class DB2ExceptionSorter implements ExceptionSorter, Serializable {

    /** The logger */
    private static final Logger  logger           = Logger.getLogger(DB2ExceptionSorter.class);

    /** The trace */
    private static final boolean trace            = logger.isTraceEnabled();

    /** The serialVersionUID */
    private static final long    serialVersionUID = -4724550353693159378L;

    public boolean isExceptionFatal(final SQLException e) {

        final int code = Math.abs(e.getErrorCode());
        boolean isFatal = false;

        if (code == 4499) {
            isFatal = true;
        }

        if (trace) {
            logger.trace("Evaluated SQL error code " + code + " isException returned " + isFatal);
        }

        return isFatal;

    }

}
