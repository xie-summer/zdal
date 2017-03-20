/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.spi.security;

import java.io.Serializable;
import java.util.Arrays;

import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;

/**
 * The class PasswordCredential is a placeholder for username and password.
 */
public final class PasswordCredential implements Serializable {
    /** @since 4.0.0 */
    static final long                serialVersionUID = -1770833344350711674L;

    /** The userName */
    private String                   userName;
    /** The password */
    private char[]                   password;

    /** The managed connection factory */
    private ManagedConnectionFactory mcf              = null;

    /**
     * Constructor, creates a new password credential
     * 
     * @param userName the user name
     * @param password the password
     */
    public PasswordCredential(String userName, char[] password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Returns the username
     * 
     * @return Username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the password
     * 
     * @return password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * Get the managed connection factory associated with this username password
     * pair.
     * 
     * @return the managed connection factory
     */
    public ManagedConnectionFactory getManagedConnectionFactory() {
        return mcf;
    }

    /**
     * Set the managed connection factory associated with this username password
     * pair.
     * 
     * @param mcf the managed connection factory
     */
    public void setManagedConnectionFactory(ManagedConnectionFactory mcf) {
        this.mcf = mcf;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        final PasswordCredential otherCredential = (PasswordCredential) other;
        return userName.equals(otherCredential.userName)
               && Arrays.equals(password, otherCredential.password);
    }

    public int hashCode() {
        return userName.hashCode();
    }
}