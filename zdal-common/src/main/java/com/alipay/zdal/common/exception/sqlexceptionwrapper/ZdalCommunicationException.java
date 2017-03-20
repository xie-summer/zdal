/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.sqlexceptionwrapper;

import java.sql.SQLException;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ZdalCommunicationException.java, v 0.1 2014-1-6 ÏÂÎç05:19:07 Exp $
 */
public class ZdalCommunicationException extends ZdalSQLExceptionWrapper {

    public ZdalCommunicationException(String message, SQLException targetSQLESqlException) {
        super(message, targetSQLESqlException);
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3502922457609932248L;

}
