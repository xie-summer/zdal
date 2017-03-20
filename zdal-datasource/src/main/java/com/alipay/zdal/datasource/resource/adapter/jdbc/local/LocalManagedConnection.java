/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.local;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.transaction.xa.XAResource;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.adapter.jdbc.BaseWrapperManagedConnection;
import com.alipay.zdal.datasource.resource.spi.LocalTransaction;

/**
 * LocalManagedConnection
 *
 * @author ²®ÑÀ
 * @version $Id: LocalManagedConnection.java, v 0.1 2014-1-6 ÏÂÎç05:32:00 Exp $
 */
public class LocalManagedConnection extends BaseWrapperManagedConnection implements
                                                                        LocalTransaction {
    private static final Logger logger = Logger.getLogger(LocalManagedConnection.class);

    /**
     * @param mcf
     * @param con
     * @param props
     * @param transactionIsolation
     * @param psCacheSize
     * @throws SQLException
     */
    public LocalManagedConnection(final LocalManagedConnectionFactory mcf, final Connection con,
                                  final Properties props, final int transactionIsolation,
                                  final int psCacheSize) throws SQLException {
        super(mcf, con, props, transactionIsolation, psCacheSize);
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        return this;
    }

    public XAResource getXAResource() throws ResourceException {
        throw new JBossResourceException("Local tx only!");
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#commit()
     */
    public void commit() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                inManagedTransaction = false;
        }
        try {
            con.commit();
        } catch (SQLException e) {
            checkException(e);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#rollback()
     */
    public void rollback() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                inManagedTransaction = false;
        }
        try {
            con.rollback();
        } catch (SQLException e) {
            try {
                checkException(e);
            } catch (Exception e2) {
                logger.error(e2);
            }
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#begin()
     */
    public void begin() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction == false) {
                try {
                    if (underlyingAutoCommit) {
                        underlyingAutoCommit = false;
                        con.setAutoCommit(false);
                    }
                    checkState();
                    inManagedTransaction = true;
                } catch (SQLException e) {
                    checkException(e);
                }
            } else
                throw new JBossResourceException("Trying to begin a nested local tx");
        }
    }

    Properties getProps() {
        return props;
    }
}
