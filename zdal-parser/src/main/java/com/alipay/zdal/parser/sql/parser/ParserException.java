/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

import java.io.Serializable;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ParserException.java, v 0.1 2012-11-17 ÏÂÎç3:53:57 Exp $
 */
public class ParserException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable e) {
        super(message, e);
    }

    public ParserException(Throwable ex, String ksql) {
        super("parse error. detail message is :\n" + ex.getMessage() + "\nsource sql is : \n"
              + ksql, ex);
    }
}
