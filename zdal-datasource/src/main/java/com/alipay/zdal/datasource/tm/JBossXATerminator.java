/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import javax.transaction.xa.Xid;

import com.alipay.zdal.datasource.resource.spi.XATerminator;
import com.alipay.zdal.datasource.resource.spi.work.Work;
import com.alipay.zdal.datasource.resource.spi.work.WorkCompletedException;

/**
 * Extends XATerminator to include registration calls
 *
 * @author ²®ÑÀ
 * @version $Id: JBossXATerminator.java, v 0.1 2014-1-6 ÏÂÎç05:47:25 Exp $
 */
public interface JBossXATerminator extends XATerminator {
    /**
     * Invoked for transaction inflow of work
     * 
     * @param work the work starting
     * @param xid the xid of the work
     * @param timeout the transaction timeout
     * @throws WorkCompletedException with error code WorkException.TX_CONCURRENT_WORK_DISALLOWED
     *         when work is already present for the xid or whose completion is in progress, only
     *         the global part of the xid must be used for this check.
     */
    void registerWork(Work work, Xid xid, long timeout) throws WorkCompletedException;

    /**
     * Invoked for transaction inflow of work
     * 
     * @param work the work starting
     * @param xid the xid of the work
     * @throws WorkCompletedException with error code WorkException.TX_RECREATE_FAILED if it is unable to recreate the transaction context
     */
    void startWork(Work work, Xid xid) throws WorkCompletedException;

    /**
     * Invoked when transaction inflow work ends
     * 
     * @param work the work ending
     * @param xid the xid of the work
     */
    void endWork(Work work, Xid xid);

    /**
     * Invoked when the work fails
     * 
     * @param work the work ending
     * @param xid the xid of the work
     */
    void cancelWork(Work work, Xid xid);
}
