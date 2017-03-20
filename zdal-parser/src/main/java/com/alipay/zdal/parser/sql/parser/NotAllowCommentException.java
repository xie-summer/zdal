/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: NotAllowCommentException.java, v 0.1 2012-11-17 ÏÂÎç3:53:51 Exp $
 */
public class NotAllowCommentException extends ParserException {

    private static final long serialVersionUID = 1L;

    public NotAllowCommentException() {
        super();
    }

    public NotAllowCommentException(String message, Throwable e) {
        super(message, e);
    }

    public NotAllowCommentException(String message) {
        super(message);
    }

}
