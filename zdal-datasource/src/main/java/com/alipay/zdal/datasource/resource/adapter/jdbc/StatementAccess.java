/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

/**
 A simple interface that allow us to get the underlying driver specific
 statement implementation back from the wrapper.

 * 
 * @author ²®ÑÀ
 * @version $Id: StatementAccess.java, v 0.1 2014-1-6 ÏÂÎç05:29:45 Exp $
 */
public interface StatementAccess {
    /**
     * Get the underlying statement
     * 
     * @return the underlying statement
     * @throws SQLException when already closed
     */
    Statement getUnderlyingStatement() throws SQLException;
}
