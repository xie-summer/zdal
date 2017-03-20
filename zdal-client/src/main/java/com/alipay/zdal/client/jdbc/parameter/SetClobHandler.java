/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetClobHandler implements ParameterHandler {
    public void setParameter(PreparedStatement stmt, Object[] args) throws SQLException {
        stmt.setClob((Integer) args[0], (Clob) args[1]);
    }
}
