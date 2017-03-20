/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi;

import java.io.PrintWriter;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.transaction.NotSupportedException;

/**
 * A ManagedConnection instance represents a connection to the underlying
 * recource. A ManagedConnection provides access to the two transaction
 * interfaces, XAResource and LocalTransaction. These interfaces are used to
 * manage transactions on the resource.
 */
public interface ManagedConnection {
    /**
     * Creates a new connection handle for the underlying connection.
     * 
     * @param subject the subject
     * @param cxRequestInfo the connection request info
     * @throws ResourceException for a generic error 
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     * @throws SecurityException for a security problem
     * @throws CommException for a communication failure with the EIS
     * @throws EISSystemException for an error from the EIS
     */
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo)
                                                                                     throws ResourceException;

    /**
     * Destroys the connection to the underlying resource.
     * 
     * @throws ResourceException for a generic error
     * @throws IllegalStateException if the connection is not a legal state for destruction 
     */
    public void destroy() throws ResourceException;

    /**
     * Application server calls this to force cleanup of connection.
     * @throws ResourceException for a generic error
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     * @throws IllegalStateException if the connection is not a legal state for cleanup 
     */
    public void cleanup() throws ResourceException;

    /**
     * Associates a new application level connection handle with the connection.
     * 
     * @param connection the connection
     * @throws ResourceException for a generic error
     * @throws IllegalStateException for an illegal state
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     */
    public void associateConnection(Object connection) throws ResourceException;

    /**
     * Adds a connection event listener
     * 
     * @param listener the listener
     */
    public void addConnectionEventListener(ConnectionEventListener listener);

    /**
     * Removes a connection event listener
     * 
     * @param listener the listener
     */
    public void removeConnectionEventListener(ConnectionEventListener listener);

    /**
     * Returns an XAResource instance.
     * 
     * @return the XAResource
     * @throws ResourceException for a generic error
     * @throws NotSupportedException if not supported
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     */
    public XAResource getXAResource() throws ResourceException;

    /**
     * Returns a LocalTransaction instance.
     * 
     * @return the local transaction
     * @throws ResourceException for a generic error
     * @throws NotSupportedException if not supported
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     */
    public LocalTransaction getLocalTransaction() throws ResourceException;

    /**
     * Gets metadata inormation for this instances underlying resource manager
     * instance.
     * 
     * @return the managed connection meta data
     * @throws ResourceException for a generic error
     * @throws NotSupportedException if not supported
     */
    public ManagedConnectionMetaData getMetaData() throws ResourceException;

    /**
     * Gets the logwriter for this instance.
     * 
     * @return the log writer
     * @throws ResourceException for a generic error
     */
    public PrintWriter getLogWriter() throws ResourceException;

    /**
     * Sets the logwriter for this instance.
     * 
     * @param out the writer
     * @throws ResourceException for a generic error
     * @throws ResourceAdapterInternalException for an internal error in the
     *            resource adapter
     */
    public void setLogWriter(PrintWriter out) throws ResourceException;
}