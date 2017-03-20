/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SetTimestamp1Handler implements ParameterHandler {
    public void setParameter(PreparedStatement stmt, Object[] args) throws SQLException {
        stmt.setTimestamp((Integer) args[0], (Timestamp) args[1]);
    }
}
