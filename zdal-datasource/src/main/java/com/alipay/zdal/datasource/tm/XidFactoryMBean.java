/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import javax.transaction.xa.Xid;

/**
 * MBean interface.
 */
public interface XidFactoryMBean {

    /**
     * mbean get-set pair for field BaseGlobalId Get the value of BaseGlobalId
     * @return value of BaseGlobalId
     */
    String getBaseGlobalId();

    /**
     * Set the value of BaseGlobalId
     * @param BaseGlobalId Value to assign to BaseGlobalId
     */
    void setBaseGlobalId(String baseGlobalId);

    /**
     * mbean get-set pair for field globalIdNumber Get the value of globalIdNumber
     * @return value of globalIdNumber
     */
    long getGlobalIdNumber();

    /**
     * Set the value of globalIdNumber
     * @param globalIdNumber Value to assign to globalIdNumber
     */
    void setGlobalIdNumber(long globalIdNumber);

    /**
     * mbean get-set pair for field pad Get the value of pad
     * @return value of pad
     */
    boolean isPad();

    /**
     * Set the value of pad
     * @param pad Value to assign to pad
     */
    void setPad(boolean pad);

    /**
     * mbean get-set pair for field instance Get the value of instance
     * @return value of instance
     */
    XidFactoryMBean getInstance();

    /**
     * Describe <code>newXid</code> method here.
     * @return a <code>XidImpl</code> value
     */
    XidImpl newXid();

    /**
     * Describe <code>newBranch</code> method here.
     * @param xid a <code>XidImpl</code> value
     * @param branchIdNum a <code>long</code> value
     * @return a <code>XidImpl</code> value
     */
    XidImpl newBranch(XidImpl xid, long branchIdNum);

    /**
     * Extracts the local id contained in a global id.
     * @param globalId a global id
     * @return the local id extracted from the global id
     */
    long extractLocalIdFrom(byte[] globalId);

    /**
     * Describe <code>toString</code> method here.
     * @param xid a <code>Xid</code> value
     * @return a <code>String</code> value
     */
    String toString(Xid xid);

}
