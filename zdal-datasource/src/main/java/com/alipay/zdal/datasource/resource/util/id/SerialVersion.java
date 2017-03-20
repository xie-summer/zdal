/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.id;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.log4j.Logger;

/**
 * Serialization version compatibility mode constants.<p>
 *
 * Contains static constants and attributes to help with serialization
 * versioning.<p>
 * 
 * Set the system property <pre>org.jboss.j2ee.LegacySerialization</pre>
 * to serialization compatibility with jboss-4.0.1 and earlier. The
 * serialVersionUID values were synched with the j2ee 1.4 ri classes and
 * explicitly set in jboss-4.0.2 which is what
 *
 * 
 * @author ²®ÑÀ
 * @version $Id: SerialVersion.java, v 0.1 2014-1-6 ÏÂÎç05:43:01 Exp $
 */
public class SerialVersion {
    private static final Logger logger    = Logger.getLogger(SerialVersion.class);
    // Static --------------------------------------------------------

    /** Legacy, jboss-4.0.1 through jboss-4.0.0 */
    public static final int     LEGACY    = 0;

    /** The serialization compatible with Sun's RI, jboss-4.0.2+ */
    public static final int     JBOSS_402 = 1;

    /**
     * The serialization version to use
     */
    public static int           version   = JBOSS_402;

    /** Determine the serialization version */
    static {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    if (System.getProperty("org.jboss.j2ee.LegacySerialization") != null)
                        version = LEGACY;
                } catch (Throwable ignored) {
                    logger.error(ignored);
                }
                return null;
            }
        });
    }
}
