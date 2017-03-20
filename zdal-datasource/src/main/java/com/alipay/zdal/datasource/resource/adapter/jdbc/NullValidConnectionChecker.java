/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Does not check the connection. Used if instantiation of named connection checker fails.
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: NullValidConnectionChecker.java, v 0.1 2014-1-6 ÏÂÎç05:29:21 Exp $
 */
public class NullValidConnectionChecker implements ValidConnectionChecker, Serializable {

    /** The serialVersionUID */
    private static final long serialVersionUID = -223752863430216887L;

    /** 
     * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker#isValidConnection(java.sql.Connection)
     */
    public SQLException isValidConnection(Connection c) {
        return null;
    }
}
