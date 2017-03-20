/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.vendor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker;

/**
 * Implements check valid connection sql Requires MySQL driver 3.1.8 or later.
 * This should work on just about any version of the database itself but will
 * only be "fast" on version 3.22.1 and later. Prior to that version it just
 * does "SELECT 1" anyhow.
 * 
 * @author ²®ÑÀ
 * @version $Id: MySQLValidConnectionChecker.java, v 0.1 2014-1-6 ÏÂÎç05:32:36 Exp $
 */
public class MySQLValidConnectionChecker implements ValidConnectionChecker, Serializable {
    private static final long   serialVersionUID    = -2227528634302168878L;

    private static final Logger log                 = Logger
                                                        .getLogger(MySQLValidConnectionChecker.class);

    private Method              ping;
    private boolean             driverHasPingMethod = false;

    // The timeout (apparently the timeout is ignored?)
    private static Object[]     params              = new Object[] {};

    /**
     * 
     */
    public MySQLValidConnectionChecker() {
        try {
            Class mysqlConnection = Thread.currentThread().getContextClassLoader().loadClass(
                "com.mysql.jdbc.Connection");
            ping = mysqlConnection.getMethod("ping", new Class[] {});
            if (ping != null) {
                driverHasPingMethod = true;
            }
        } catch (Exception e) {
            log
                .warn(
                    "Cannot resolve com.mysq.jdbc.Connection.ping method.  Will use 'SELECT 1' instead.",
                    e);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.adapter.jdbc.ValidConnectionChecker#isValidConnection(java.sql.Connection)
     */
    public SQLException isValidConnection(Connection c) {
        //if there is a ping method then use it, otherwise just use a 'SELECT 1' statement
        if (driverHasPingMethod) {
            try {
                ping.invoke(c, params);
            } catch (Exception e) {
                if (e instanceof SQLException) {
                    return (SQLException) e;
                } else {
                    log.warn("Unexpected error in ping", e);
                    return new SQLException("ping failed: " + e.toString());
                }
            }

        } else {

            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = c.createStatement();
                rs = stmt.executeQuery("SELECT 1");
            } catch (Exception e) {
                if (e instanceof SQLException) {
                    return (SQLException) e;
                } else {
                    log.warn("Unexpected error in ping (SELECT 1)", e);
                    return new SQLException("ping (SELECT 1) failed: " + e.toString());
                }
            } finally {
                //cleanup the Statment
                try {
                    if (rs != null)
                        rs.close();
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }

        }
        return null;
    }
}
