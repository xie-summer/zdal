/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;

public class SetRefHandler implements ParameterHandler {
    public void setParameter(PreparedStatement stmt, Object[] args) throws SQLException {
        stmt.setRef((Integer) args[0], (Ref) args[1]);
    }
}
