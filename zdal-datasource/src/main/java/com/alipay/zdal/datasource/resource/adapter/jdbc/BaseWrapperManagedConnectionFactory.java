/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.PrintWriter;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.NullExceptionSorter;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionManager;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.spi.ValidatingManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.spi.security.PasswordCredential;

/**
 * BaseWrapperManagedConnectionFactory
 *
 * @author ²®ÑÀ
 * @version $Id: BaseWrapperManagedConnectionFactory.java, v 0.1 2014-1-6 ÏÂÎç05:27:24 Exp $
 */
public abstract class BaseWrapperManagedConnectionFactory implements ManagedConnectionFactory,
                                                         ValidatingManagedConnectionFactory,
                                                         Serializable {
    /** @since 4.0.1 */
    static final long                serialVersionUID            = -84923705377702088L;

    public static final int          TRACK_STATEMENTS_FALSE_INT  = 0;
    public static final int          TRACK_STATEMENTS_TRUE_INT   = 1;
    public static final int          TRACK_STATEMENTS_NOWARN_INT = 2;

    public static final String       TRACK_STATEMENTS_FALSE      = "false";
    public static final String       TRACK_STATEMENTS_TRUE       = "true";
    public static final String       TRACK_STATEMENTS_NOWARN     = "nowarn";

    protected final Logger           log                         = Logger.getLogger(getClass());

    protected String                 userName;
    protected String                 password;
    protected String                 encPassword;

    protected int                    transactionIsolation        = -1;

    protected int                    preparedStatementCacheSize  = 0;

    protected boolean                doQueryTimeout              = false;

    /**
     * The variable <code>newConnectionSQL</code> holds an SQL
     * statement which if not null is executed when a new Connection is
     * obtained for a new ManagedConnection.
     */
    protected String                 newConnectionSQL;

    /**
     * The variable <code>checkValidConnectionSQL</code> holds an sql
     * statement that may be executed whenever a managed connection is
     * removed from the pool, to check that it is still valid.  This
     * requires setting up an mbean to execute it when notified by the
     * ConnectionManager.
     */
    protected String                 checkValidConnectionSQL;

    /**
     * The classname used to check whether a connection is valid
     */
    protected String                 validConnectionCheckerClassName;

    /**
     * The instance of the valid connection checker
     */
    protected ValidConnectionChecker connectionChecker;

    private String                   exceptionSorterClassName;

    private ExceptionSorter          exceptionSorter;

    protected int                    trackStatements             = TRACK_STATEMENTS_NOWARN_INT;

    /** Whether to share cached prepared statements */
    protected boolean                sharePS                     = false;

    protected boolean                isTransactionQueryTimeout   = false;

    protected int                    queryTimeout                = 0;

    private boolean                  validateOnMatch;

    //This is used by Local wrapper for all properties, and is left
    //in this class for ease of writing getConnectionProperties,
    //which always holds the user/pw.
    protected final Properties       connectionProps             = new Properties();

    public Properties getConnectionProps() {
        return connectionProps;
    }

    /**
     * 
     */
    public BaseWrapperManagedConnectionFactory() {

    }

    public PrintWriter getLogWriter() throws ResourceException {
        // TODO: implement this javax.resource.spi.ManagedConnectionFactory method
        return null;
    }

    public void setLogWriter(PrintWriter param1) throws ResourceException {
        // TODO: implement this javax.resource.spi.ManagedConnectionFactory method
    }

    /**
     * 
     * @param cm
     * @param dataSourceName
     * @param zdatasource
     * @return
     * @throws ResourceException
     */
    public Object createConnectionFactory(ConnectionManager cm, String dataSourceName,
                                          ZDataSource zdatasource) throws ResourceException {
        return new WrapperDataSource(this, cm, dataSourceName, zdatasource);
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory#createConnectionFactory()
     */
    public Object createConnectionFactory() throws ResourceException {
        throw new JBossResourceException("NYI");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getPreparedStatementCacheSize() {
        return preparedStatementCacheSize;
    }

    public void setPreparedStatementCacheSize(int size) {
        preparedStatementCacheSize = size;
    }

    public boolean getSharePreparedStatements() {
        return sharePS;
    }

    public void setSharePreparedStatements(boolean sharePS) {
        this.sharePS = sharePS;
    }

    public String getTransactionIsolation() {
        switch (this.transactionIsolation) {
            case Connection.TRANSACTION_NONE:
                return "TRANSACTION_NONE";
            case Connection.TRANSACTION_READ_COMMITTED:
                return "TRANSACTION_READ_COMMITTED";
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return "TRANSACTION_READ_UNCOMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ:
                return "TRANSACTION_REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE:
                return "TRANSACTION_SERIALIZABLE";
            case -1:
                return "DEFAULT";
            default:
                return Integer.toString(transactionIsolation);
        }
    }

    public void setTransactionIsolation(String transactionIsolation) {
        if (transactionIsolation.toUpperCase().equals("TRANSACTION_NONE")) {
            this.transactionIsolation = Connection.TRANSACTION_NONE;
        } else if (transactionIsolation.toUpperCase().equals("TRANSACTION_READ_COMMITTED")) {
            this.transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
        } else if (transactionIsolation.toUpperCase().equals("TRANSACTION_READ_UNCOMMITTED")) {
            this.transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;
        } else if (transactionIsolation.toUpperCase().equals("TRANSACTION_REPEATABLE_READ")) {
            this.transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ;
        } else if (transactionIsolation.toUpperCase().equals("TRANSACTION_SERIALIZABLE")) {
            this.transactionIsolation = Connection.TRANSACTION_SERIALIZABLE;
        } else {
            try {
                this.transactionIsolation = Integer.parseInt(transactionIsolation);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Setting Isolation level to unknown state: "
                                                   + transactionIsolation);
            }
        }
    }

    public String getNewConnectionSQL() {
        return newConnectionSQL;
    }

    public void setNewConnectionSQL(String newConnectionSQL) {
        this.newConnectionSQL = newConnectionSQL;
    }

    public String getCheckValidConnectionSQL() {
        return checkValidConnectionSQL;
    }

    public void setCheckValidConnectionSQL(String checkValidConnectionSQL) {
        this.checkValidConnectionSQL = checkValidConnectionSQL;
    }

    public String getTrackStatements() {
        if (trackStatements == TRACK_STATEMENTS_FALSE_INT) {
            return TRACK_STATEMENTS_FALSE;
        } else if (trackStatements == TRACK_STATEMENTS_TRUE_INT) {
            return TRACK_STATEMENTS_TRUE;
        }

        return TRACK_STATEMENTS_NOWARN;
    }

    public boolean getValidateOnMatch() {
        return this.validateOnMatch;
    }

    public void setValidateOnMatch(boolean validateOnMatch) {
        this.validateOnMatch = validateOnMatch;
    }

    public void setTrackStatements(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value for trackStatements");
        }

        String trimmed = value.trim();
        if (trimmed.equalsIgnoreCase(TRACK_STATEMENTS_FALSE)) {
            trackStatements = TRACK_STATEMENTS_FALSE_INT;
        } else if (trimmed.equalsIgnoreCase(TRACK_STATEMENTS_TRUE)) {
            trackStatements = TRACK_STATEMENTS_TRUE_INT;
        } else {
            trackStatements = TRACK_STATEMENTS_NOWARN_INT;
        }
    }

    public String getExceptionSorterClassName() {
        return exceptionSorterClassName;
    }

    public void setExceptionSorterClassName(String exceptionSorterClassName) {
        this.exceptionSorterClassName = exceptionSorterClassName;
    }

    public String getValidConnectionCheckerClassName() {
        return validConnectionCheckerClassName;
    }

    public void setValidConnectionCheckerClassName(String value) {
        validConnectionCheckerClassName = value;
    }

    public boolean isTransactionQueryTimeout() {
        return isTransactionQueryTimeout;
    }

    public void setTransactionQueryTimeout(boolean value) {
        isTransactionQueryTimeout = value;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int timeout) {
        queryTimeout = timeout;
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.ValidatingManagedConnectionFactory#getInvalidConnections(java.util.Set)
     */
    public Set getInvalidConnections(final Set connectionSet) throws ResourceException {
        final Set invalid = new HashSet();

        for (Iterator iter = connectionSet.iterator(); iter.hasNext();) {
            Object anonymous = iter.next();

            if (anonymous instanceof BaseWrapperManagedConnection) {
                BaseWrapperManagedConnection mc = (BaseWrapperManagedConnection) anonymous;
                if (!mc.checkValid()) {
                    invalid.add(mc);
                }
            }
        }
        return invalid;
    }

    /**
     * Gets full set of connection properties, i.e. whatever is provided
     * in config plus "user" and "password" from subject/cri.
     *
     * <p>Note that the set is used to match connections to datasources as well
     * as to create new managed connections.
     *
     * <p>In fact, we have a problem here. Theoretically, there is a possible
     * name collision between config properties and "user"/"password".
     */
    protected Properties getConnectionProperties(Subject subject, ConnectionRequestInfo cri)
                                                                                            throws ResourceException {
        if (cri != null && cri.getClass() != WrappedConnectionRequestInfo.class) {
            throw new JBossResourceException("Wrong kind of ConnectionRequestInfo: "
                                             + cri.getClass());
        }

        Properties props = new Properties();
        props.putAll(connectionProps);
        if (subject != null) {
            if (SubjectActions.addMatchingProperties(subject, props, this) == true) {
                return props;
            }

            throw new JBossResourceException("No matching credentials in Subject!");
        }
        WrappedConnectionRequestInfo lcri = (WrappedConnectionRequestInfo) cri;
        if (lcri != null) {
            props.setProperty("user", (lcri.getUserName() == null) ? "" : lcri.getUserName());
            props.setProperty("password", (lcri.getPassword() == null) ? "" : lcri.getPassword());
            return props;
        }
        if (userName != null) {
            props.setProperty("user", userName);
            props.setProperty("password", (password == null) ? "" : password);
        }
        return props;
    }

    boolean isExceptionFatal(SQLException e) {
        try {
            if (exceptionSorter != null) {
                return exceptionSorter.isExceptionFatal(e);
            }

            if (exceptionSorterClassName != null) {
                try {
                    //                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    ClassLoader cl = this.getClass().getClassLoader();
                    Class<?> clazz = cl.loadClass(exceptionSorterClassName);
                    exceptionSorter = (ExceptionSorter) clazz.newInstance();
                    return exceptionSorter.isExceptionFatal(e);
                } catch (Exception e2) {
                    log.warn("exception trying to create exception sorter (disabling):", e2);
                    exceptionSorter = new NullExceptionSorter();
                }
            }
        } catch (Throwable t) {
            log.warn("Error checking exception fatality: ", t);
        }
        return false;
    }

    /**
     * Checks whether a connection is valid
     */
    SQLException isValidConnection(Connection c) {
        // Already got a checker
        if (connectionChecker != null) {
            return connectionChecker.isValidConnection(c);
        }

        // Class specified
        if (validConnectionCheckerClassName != null) {
            try {
                //                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                ClassLoader cl = this.getClass().getClassLoader();
                Class<?> clazz = cl.loadClass(validConnectionCheckerClassName);
                connectionChecker = (ValidConnectionChecker) clazz.newInstance();
                return connectionChecker.isValidConnection(c);
            } catch (Exception e) {
                log.warn("Exception trying to create connection checker (disabling):", e);
                connectionChecker = new NullValidConnectionChecker();
            }
        }

        // SQL statement specified
        if (checkValidConnectionSQL != null) {
            connectionChecker = new CheckValidConnectionSQL(checkValidConnectionSQL);
            return connectionChecker.isValidConnection(c);
        }

        // No Check
        return null;
    }

    static class SubjectActions implements PrivilegedAction {
        Subject                  subject;

        Properties               props;

        ManagedConnectionFactory mcf;

        SubjectActions(Subject subject, Properties props, ManagedConnectionFactory mcf) {
            this.subject = subject;
            this.props = props;
            this.mcf = mcf;
        }

        /** 
         * @see java.security.PrivilegedAction#run()
         */
        public Object run() {
            Iterator i = subject.getPrivateCredentials().iterator();
            while (i.hasNext()) {
                Object o = i.next();
                if (o instanceof PasswordCredential) {
                    PasswordCredential cred = (PasswordCredential) o;
                    if (cred.getManagedConnectionFactory().equals(mcf)) {
                        props.setProperty("user", (cred.getUserName() == null) ? "" : cred
                            .getUserName());
                        if (cred.getPassword() != null)
                            props.setProperty("password", new String(cred.getPassword()));
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }

        static boolean addMatchingProperties(Subject subject, Properties props,
                                             ManagedConnectionFactory mcf) {
            SubjectActions action = new SubjectActions(subject, props, mcf);
            Boolean matched = (Boolean) AccessController.doPrivileged(action);
            return matched.booleanValue();
        }
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }
}
