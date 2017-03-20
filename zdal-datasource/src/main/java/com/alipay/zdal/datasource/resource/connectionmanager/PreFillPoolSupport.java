/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import javax.security.auth.Subject;

import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;

/**
 * PreFillPoolSupport allows for prefilling connection pools.
 * 
 * @author ²®ÑÀ
 * @version $Id: PreFillPoolSupport.java, v 0.1 2014-1-6 ÏÂÎç05:36:08 Exp $
 */
public interface PreFillPoolSupport {

    /**
     * Prefill the connection pool 
     * 
     */
    public void prefill();

    /**
     * Prefill the connection pool
     * 
     * @param noTxSeperatePool whether or not we are seperating non transaction and transaction pools
     */
    public void prefill(boolean noTxSeperatePool);

    /**
     * Prefill the connection pool
     * 
     * @param subject the subject the subject 
     * @param cri the connection request info
     * @param noTxnSeperatePool whether or not we are seperating non transaction and transaction pools
     *   
     */
    public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxnSeperatePool);

    /**
     * Get the flag indicating whether or not to attempt to prefill this pool.
     * 
     * @return true or false depending on whether or not to prefill this pool.
     */
    public boolean shouldPreFill();
}
