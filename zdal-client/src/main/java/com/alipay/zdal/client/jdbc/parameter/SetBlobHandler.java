/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetBlobHandler implements ParameterHandler {
    public void setParameter(PreparedStatement stmt, Object[] args) throws SQLException {
        stmt.setBlob((Integer) args[0], (Blob) args[1]);
    }
}
