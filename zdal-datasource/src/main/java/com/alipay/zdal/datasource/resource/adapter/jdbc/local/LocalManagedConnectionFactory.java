/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.local;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.security.auth.Subject;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.adapter.jdbc.BaseWrapperManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.spi.ConnectionManager;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.util.NestedRuntimeException;

/**
 * LocalManagedConnectionFactory
 *
 * 
 * @author 伯牙
 * @version $Id: LocalManagedConnectionFactory.java, v 0.1 2014-1-6 下午05:32:13 Exp $
 */
public class LocalManagedConnectionFactory extends BaseWrapperManagedConnectionFactory {
    static final long        serialVersionUID = 4698955390505160469L;

    private String           driverClass;

    private transient Driver driver;

    private String           connectionURL;

    protected String         connectionProperties;

    /**
     * Get the value of ConnectionURL.
     *
     * @return value of ConnectionURL.
     */
    public String getConnectionURL() {
        return connectionURL;
    }

    /**
     * Set the value of ConnectionURL.
     *
     * @param connectionURL  Value to assign to ConnectionURL.
     */
    public void setConnectionURL(final String connectionURL) {
        this.connectionURL = connectionURL;
    }

    /**
     * Get the DriverClass value.
     *
     * @return the DriverClass value.
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Set the DriverClass value.
     *
     * @param driverClass The new DriverClass value.
     */
    public synchronized void setDriverClass(final String driverClass) {
        this.driverClass = driverClass;
        driver = null;
    }

    /**
     * Get the value of connectionProperties.
     *
     * @return value of connectionProperties.
     */
    public String getConnectionProperties() {
        return connectionProperties;
    }

    /**
     * Set the value of connectionProperties.
     *
     * @param connectionProperties  Value to assign to connectionProperties.
     */
    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
        connectionProps.clear();
        if (connectionProperties != null) {
            // Map any \ to \\
            connectionProperties = connectionProperties.replaceAll("\\\\", "\\\\\\\\");

            InputStream is = new ByteArrayInputStream(connectionProperties.getBytes());
            try {
                connectionProps.load(is);
            } catch (IOException ioe) {
                throw new NestedRuntimeException("Could not load connection properties", ioe);
            }
        }
    }

    public void setConnectionProperties(Map<String, String> connectionProperties) {
        connectionProps.clear();
        connectionProps.putAll(connectionProperties);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> property : connectionProperties.entrySet()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(property.getKey()).append('=').append(property.getValue());
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject, com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cri)
                                                                                                throws com.alipay.zdal.datasource.resource.ResourceException {
        Properties props = getConnectionProperties(subject, cri);
        // Some friendly drivers (Oracle, you guessed right) modify the props you supply.
        // Since we use our copy to identify compatibility in matchManagedConnection, we need
        // a pristine copy for our own use.  So give the friendly driver a copy.
        Properties copy = (Properties) props.clone();
        if (log.isDebugEnabled()) {
            // Make yet another copy to mask the password
            Properties logCopy = copy;
            if (copy.getProperty("password") != null) {
                logCopy = (Properties) props.clone();
                logCopy.setProperty("password", "--hidden--");
            }
            log.debug("Using properties: " + logCopy);
        }

        try {
            String url = getConnectionURL();
            Driver d = getDriver(url);
            Connection con = d.connect(url, copy);
            if (con == null) {
                throw new JBossResourceException("Wrong driver class for this connection URL");
            }
            String stz = copy.getProperty("sessionTimeZone");//支持oracle-driver中设置timestamp字段的属性.
            if (stz != null && stz.trim().length() > 0
                && (con instanceof oracle.jdbc.OracleConnection)) {
                ((oracle.jdbc.OracleConnection) con).setSessionTimeZone(stz);
            }

            return new LocalManagedConnection(this, con, props, transactionIsolation,
                preparedStatementCacheSize);
        } catch (Exception e) {
            throw new JBossResourceException("Could not create connection,the url = "
                                             + getConnectionURL(), e);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set, javax.security.auth.Subject, com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection matchManagedConnections(final Set mcs, final Subject subject,
                                                     final ConnectionRequestInfo cri)
                                                                                     throws ResourceException {
        Properties newProps = getConnectionProperties(subject, cri);

        for (Iterator i = mcs.iterator(); i.hasNext();) {
            Object o = i.next();

            if (o instanceof LocalManagedConnection) {
                LocalManagedConnection mc = (LocalManagedConnection) o;

                //First check the properties
                if (mc.getProps().equals(newProps)) {
                    //Next check to see if we are validating on matchManagedConnections
                    if ((getValidateOnMatch() && mc.checkValid()) || !getValidateOnMatch()) {

                        return mc;

                    }

                }
            }
        }

        return null;
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = result * 37 + ((connectionURL == null) ? 0 : connectionURL.hashCode());
        result = result * 37 + ((driverClass == null) ? 0 : driverClass.hashCode());
        result = result * 37 + ((userName == null) ? 0 : userName.hashCode());
        result = result * 37 + ((password == null) ? 0 : password.hashCode());
        result = result * 37 + transactionIsolation;
        return result;
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (getClass() != other.getClass()) {
            return false;
        }

        LocalManagedConnectionFactory otherMcf = (LocalManagedConnectionFactory) other;
        return this.connectionURL.equals(otherMcf.connectionURL)
               && this.driverClass.equals(otherMcf.driverClass)
               && ((this.userName == null) ? otherMcf.userName == null : this.userName
                   .equals(otherMcf.userName))
               && ((this.password == null) ? otherMcf.password == null : this.password
                   .equals(otherMcf.password))
               && this.transactionIsolation == otherMcf.transactionIsolation;

    }

    /**
     * Check the driver for the given URL.  If it is not registered already
     * then register it.
     *
     * @param url   The JDBC URL which we need a driver for.
     */
    protected synchronized Driver getDriver(final String url) throws ResourceException {
        // don't bother if it is loaded already
        if (driver != null) {
            return driver;
        }
        if (log.isDebugEnabled()) {
            log.debug("Checking driver for URL: " + url);
        }

        if (driverClass == null) {
            throw new JBossResourceException("No Driver class specified!");
        }

        // Check if the driver is already loaded, if not then try to load it

        if (isDriverLoadedForURL(url)) {
            return driver;
        } // end of if ()

        try {
            if (isDriverLoadedForURL(url)) {
                //return immediately, some drivers (Cloudscape) do not let you create an instance.
                return driver;
            }
            //            //We loaded the class, but either it didn't register
            //            //and is not spec compliant, or is the wrong class.
            //            if (url.startsWith("jdbc:oracle:oci") && driverClass.equals("oracle.jdbc.OracleDriver")) {//只有在oracle-oci的时候才需要从cloundengine中获取driver.
            //                driver = ZDataSourceOciDriver.getDriver(driverClass, url);
            //            } else {
            Class<?> clazz = Class.forName(driverClass, true, Thread.currentThread()
                .getContextClassLoader());
            driver = (Driver) clazz.newInstance();
            //            }
            DriverManager.registerDriver(driver);
            if (isDriverLoadedForURL(url)) {
                return driver;
            }
            //We can even instantiate one, it must be the wrong class for the URL.
        } catch (Exception e) {
            throw new JBossResourceException("ERROR ## Failed to register driver for: "
                                             + driverClass, e);
        }

        throw new JBossResourceException(
            "ERROR ## Apparently wrong driver class specified for URL: class: " + driverClass
                    + ", url: " + url);
    }

    private boolean isDriverLoadedForURL(String url) {

        try {
            driver = DriverManager.getDriver(url);
            if (log.isDebugEnabled()) {
                log.debug("Driver already registered for url: " + url);
            }

            return true;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Driver not yet registered for url: " + url);
            }

            return false;
        }
    }

    protected String internalGetConnectionURL() {
        return connectionURL;
    }

    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        throw new JBossResourceException("NYI");
    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager, String dataSourceName)
                                                                                             throws ResourceException {
        return null;
    }
}
