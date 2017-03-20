/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;

/**
 * Information about a connection
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: ConnectionRecord.java, v 0.1 2014-1-6 ÏÂÎç05:33:59 Exp $
 */
public class ConnectionRecord {
    ConnectionListener          cl;
    final Object                connection;
    final ConnectionRequestInfo cri;

    /**
     * @param cl
     * @param connection
     * @param cri
     */
    public ConnectionRecord(final ConnectionListener cl, final Object connection,
                            final ConnectionRequestInfo cri) {
        this.cl = cl;
        this.connection = connection;
        this.cri = cri;
    }

    void setConnectionListener(final ConnectionListener cl) {
        this.cl = cl;
    }
}
