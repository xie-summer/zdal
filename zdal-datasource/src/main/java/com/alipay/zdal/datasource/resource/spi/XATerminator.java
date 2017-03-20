/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

/**
 * Transaction completion and crash recovery
 */
public interface XATerminator {
    /**
     * Commit the transaction
     *
     * @param xid the xid
     * @param onePhase true for one phase commit, false for two phase
     * @throws XAException for an error
     */
    void commit(Xid xid, boolean onePhase) throws XAException;

    /**
     * Forget the transaction
     *
     * @param xid the xid
     * @throws XAException for an error
     */
    void forget(Xid xid) throws XAException;

    /**
     * Prepare the transaction
     *
     * @param xid the xid
     * @return Either XA_RDONLY or XA_OK
     * @throws XAException for an error
     */
    int prepare(Xid xid) throws XAException;

    /**
     * Rollback the transaction
     *
     * @param xid the xid
     * @throws XAException for an error
     */
    void rollback(Xid xid) throws XAException;

    /**
     * Retrieve xids that are recoverable
     *
     * @param flag the recovery option
     * @throws XAException for an error
     */
    Xid[] recover(int flag) throws XAException;
}