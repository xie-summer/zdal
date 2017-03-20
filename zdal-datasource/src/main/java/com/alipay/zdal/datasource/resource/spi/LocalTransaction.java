/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import com.alipay.zdal.datasource.resource.ResourceException;

/**
 * The LocalTransaction interface is for transactions which are managed locally
 * to the underlying resource and don't need an external transaction manager.
 * 
 * If a resource implements the LocalTransaction interface then the Application
 * Server can choose to do local transacton optimization.
 */
public interface LocalTransaction {
    /**
     * Begins a local transaction on the userlying resource.
     * 
     * @throws ResourceException for a generic error
     * @throws LocalTransactionException for an error in transaciton management
     * @throws ResourceAdapterInternalException for an internal error in the resource adapter
     * @throws EISSystemException for an EIS specific exception 
     */
    public void begin() throws ResourceException;

    /**
     * Commits a local transaction on the userlying resource.
     * 
     * @throws ResourceException for a generic error
     * @throws LocalTransactionException for an error in transaciton management
     * @throws ResourceAdapterInternalException for an internal error in the resource adapter
     * @throws EISSystemException for an EIS specific exception 
     */
    public void commit() throws ResourceException;

    /**
     * Rolls back a local transaction on the userlying resource.
     * 
     * @throws ResourceException for a generic error
     * @throws LocalTransactionException for an error in transaciton management
     * @throws ResourceAdapterInternalException for an internal error in the resource adapter
     * @throws EISSystemException for an EIS specific exception 
     */
    public void rollback() throws ResourceException;
}