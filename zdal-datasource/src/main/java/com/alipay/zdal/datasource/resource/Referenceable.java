/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource;

import javax.naming.Reference;

/**
 * The Referenceable interface extends the javax.naming.Referenceable
 * interface. It enables support for the JNDI Reference mechanism for the
 * registration of the connection factory in the JNDI name space. Note that the
 * implementation and structure of a Reference is specific to an application
 * server.
 * 
 * The implementation class for a connection factory interface is required to
 * implement both the java.io.Serializable and the javax.resource.Referenceable
 * interfaces to support JNDI registration.
 */
/**
 * 
 * @author ²®ÑÀ
 * @version $Id: Referenceable.java, v 0.1 2014-1-6 ÏÂÎç05:26:49 Exp $
 */
public interface Referenceable extends javax.naming.Referenceable {
    /**
     * Sets the reference instance
     * 
     * @param reference the reference
     */
    void setReference(Reference reference);
}
