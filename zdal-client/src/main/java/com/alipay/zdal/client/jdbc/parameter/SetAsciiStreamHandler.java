/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetAsciiStreamHandler implements ParameterHandler {
    public void setParameter(PreparedStatement stmt, Object[] args) throws SQLException {
        stmt.setAsciiStream((Integer) args[0], (InputStream) args[1], (Integer) args[2]);
    }
}
