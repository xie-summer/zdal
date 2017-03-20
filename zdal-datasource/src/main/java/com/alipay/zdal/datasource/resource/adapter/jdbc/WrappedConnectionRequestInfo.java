/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;

/**
 * WrappedConnectionRequestInfo
 *
 * @author ²®ÑÀ
 * @version $Id: WrappedConnectionRequestInfo.java, v 0.1 2014-1-6 ÏÂÎç05:30:46 Exp $
 */
public class WrappedConnectionRequestInfo implements ConnectionRequestInfo {
    private final String user;

    private final String password;

    /**
    * @param user
    * @param password
    */
    public WrappedConnectionRequestInfo(final String user, final String password) {
        this.user = user;
        this.password = password;
    }

    /** 
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        return ((user == null) ? 37 : user.hashCode()) + 37
               * ((password == null) ? 37 : password.hashCode());
    }

    /** 
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other.getClass() == WrappedConnectionRequestInfo.class)) {
            return false;
        }
        WrappedConnectionRequestInfo cri = (WrappedConnectionRequestInfo) other;
        if (user == null) {
            if (cri.getUserName() != null) {
                return false;
            }
        } else {
            if (!user.equals(cri.getUserName())) {
                return false;
            }
        }
        if (password == null) {
            if (cri.getPassword() != null) {
                return false;
            }
        } else {
            if (!password.equals(cri.getPassword())) {
                return false;
            }
        }
        return true;
    }

    String getUserName() {
        return user;
    }

    String getPassword() {
        return password;
    }
}
