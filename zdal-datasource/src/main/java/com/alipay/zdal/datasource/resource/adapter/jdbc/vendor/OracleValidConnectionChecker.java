/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.vendor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker;
import com.alipay.zdal.datasource.resource.util.NestedRuntimeException;

/**
 * Implements check valid connection sql
 *
 * @author ²®ÑÀ
 * @version $Id: OracleValidConnectionChecker.java, v 0.1 2014-1-6 ÏÂÎç05:32:47 Exp $
 */
public class OracleValidConnectionChecker implements ValidConnectionChecker, Serializable {
    private static final long   serialVersionUID = -2227528634302168877L;

    private static final Logger log              = Logger
                                                     .getLogger(OracleValidConnectionChecker.class);

    private Method              ping;

    // The timeout (apparently the timeout is ignored?)
    private static Object[]     params           = new Object[] { new Integer(5000) };

    /**
     * 
     */
    public OracleValidConnectionChecker() {
        try {
            Class oracleConnection = Thread.currentThread().getContextClassLoader().loadClass(
                "oracle.jdbc.driver.OracleConnection");
            ping = oracleConnection.getMethod("pingDatabase", new Class[] { Integer.TYPE });
        } catch (Exception e) {
            throw new NestedRuntimeException("Unable to resolve pingDatabase method:", e);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker#isValidConnection(java.sql.Connection)
     */
    public SQLException isValidConnection(Connection c) {
        try {
            Integer status = (Integer) ping.invoke(c, params);

            // Error
            if (status.intValue() < 0)
                return new SQLException("pingDatabase failed status=" + status);
        } catch (Exception e) {
            // What do we do here? Assume it is a misconfiguration
            log.warn("Unexpected error in pingDatabase", e);
        }

        // OK
        return null;
    }
}
