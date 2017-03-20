/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Checks that a connection is valid
 *
 * @author ²®ÑÀ
 * @version $Id: ValidConnectionChecker.java, v 0.1 2014-1-6 ÏÂÎç05:29:59 Exp $
 */
public interface ValidConnectionChecker {
    /**
     * Checks the connection is valid
     *
     * @param c the connection
     * @return Exception when not valid, null when valid
     */
    SQLException isValidConnection(Connection c);
}
