/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SqlParserRuntimeException.java, v 0.1 2012-11-17 ÏÂÎç3:11:13 xiaoqing.zhouxq Exp $
 */
public class SqlParserRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SqlParserRuntimeException() {
        super();
    }

    public SqlParserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlParserRuntimeException(String message) {
        super(message);
    }

    public SqlParserRuntimeException(Throwable cause) {
        super(cause);
    }

}
