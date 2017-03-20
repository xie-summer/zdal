/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import java.util.EventListener;

/**
 * The ConnectionEventListener interface provides for a callback mechanism to
 * enable objects to listen for events of the ConnectionEvent class.
 * 
 * An Application server uses these events to manage its connection pools.
 */
public interface ConnectionEventListener extends EventListener {

    /**
     * Notifies the listener that a connection has been closed
     * 
     * @param event the closed event
    */
    void connectionClosed(ConnectionEvent event);

    /**
     * Local transaction has been started
     * 
     * @param event the local transaction started event
     */
    void localTransactionStarted(ConnectionEvent event);

    /**
     * Local transaction has been committed
     * 
     * @param event the local transaction committed event
     */
    void localTransactionCommitted(ConnectionEvent event);

    /**
     * Local transaction has been rolled back
     * 
     * @param the local transaction rolled back event
     */
    void localTransactionRolledback(ConnectionEvent event);

    /**
     * Connection error has occurred
     * 
     * @param the connection error event
     */
    void connectionErrorOccurred(ConnectionEvent event);
}