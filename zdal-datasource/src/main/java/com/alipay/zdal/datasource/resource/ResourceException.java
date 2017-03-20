/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource;

import com.alipay.zdal.datasource.resource.util.id.SerialVersion;

/**
 * This is the root exception for the exception hierarchy defined for the
 * connector architecture.
 *
 * A ResourceException contains three items, the first two of which are set from
 * the constructor. The first is a standard message string which is accessed via
 * the getMessage() method. The second is an errorCode which is accessed via the
 * getErrorCode() method. The third is a linked exception which provides more
 * information from a lower level in the resource manager. Linked exceptions are
 * accessed via get/setLinkedException.
 */
/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ResourceException.java, v 0.1 2014-1-6 ÏÂÎç05:26:57 Exp $
 */
public class ResourceException extends Exception {
    /** @since 4.0.2 */
    static final long serialVersionUID;
    static {
        if (SerialVersion.version == SerialVersion.LEGACY)
            serialVersionUID = 4770679801401540475L;
        else
            serialVersionUID = 547071213627824490L;
    }

    /**
     * The error code
     */
    private String    errorCode;

    /**
     * The linked exception
     */
    private Exception linkedException;

    /**
     * Create an exception with a null reason.
     */
    public ResourceException() {
        super();
    }

    /**
     * Create an exception with a reason.
     * @param reason the reason
     */
    public ResourceException(String reason) {
        super(reason);
    }

    /**
     * Create an exception with a reason and an errorCode.
     * @param reason the reason
     * @param errorCode the error code
     */
    public ResourceException(String reason, String errorCode) {
        super(reason);
        this.errorCode = errorCode;
    }

    /**
     * Create an exception with a reason and an errorCode.
     * @param reason the reason
     * @param throwable the linked error
     */
    public ResourceException(String reason, Throwable throwable) {
        super(reason, throwable);
    }

    /**
     * Create an exception with a reason and an errorCode.
     * @param throwable the linked error
     */
    public ResourceException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Get the error code.
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Get any linked exception.
     * @return the linked exception
     */
    public Exception getLinkedException() {
        return linkedException;
    }

    /**
     * Get the message composed of the reason and error code.
     * 
     * @return message composed of the reason and error code.
     */
    public String getMessage() {
        String msg = super.getMessage();
        String ec = getErrorCode();
        if ((msg == null) && (ec == null)) {
            return null;
        }
        if ((msg != null) && (ec != null)) {
            return (msg + ", error code: " + ec);
        }
        return ((msg != null) ? msg : ("error code: " + ec));
    }

    /**
     * Set the error code.
     * @param errorCode code the error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Set a linked exception.
     * @param linkedException the linked exception
     * @deprecated use initCause
     */
    public void setLinkedException(Exception linkedException) {
        this.linkedException = linkedException;
        initCause(linkedException);
    }
}