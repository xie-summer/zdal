/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

import com.alipay.zdal.parser.sql.SqlParserRuntimeException;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLParserException.java, v 0.1 2012-11-17 ÏÂÎç3:54:25 Exp $
 */
public class SQLParserException extends SqlParserRuntimeException {

    private static final long serialVersionUID = 1L;

    public SQLParserException() {
        super();
    }

    public SQLParserException(String message) {
        super(message);
    }

    public SQLParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
