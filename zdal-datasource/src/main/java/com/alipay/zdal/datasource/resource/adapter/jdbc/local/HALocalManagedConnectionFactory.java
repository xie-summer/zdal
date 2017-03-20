/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.local;

import java.sql.Connection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.security.auth.Subject;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.util.JBossStringBuilder;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: HALocalManagedConnectionFactory.java, v 0.1 2014-1-6 ÏÂÎç05:31:48 Exp $
 */
public class HALocalManagedConnectionFactory extends LocalManagedConnectionFactory {
    private static final long serialVersionUID = -6506610639011749394L;

    private URLSelector       urlSelector;
    private String            urlDelimiter;

    public String getURLDelimiter() {
        return urlDelimiter;
    }

    public void setURLDelimiter(String urlDelimiter) {
        this.urlDelimiter = urlDelimiter;
        if (getConnectionURL() != null) {
            initUrlSelector();
        }
    }

    @Override
    public void setConnectionURL(String connectionURL) {
        super.setConnectionURL(connectionURL);
        if (urlDelimiter != null) {
            initUrlSelector();
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.adapter.jdbc.local.LocalManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject, com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo)
     */
    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cri)
                                                                                                throws ResourceException {
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

        return doCreateManagedConnection(copy, props);
    }

    /**
     * 
     * @param copy
     * @param props
     * @return
     * @throws JBossResourceException
     */
    private ManagedConnection doCreateManagedConnection(Properties copy, Properties props)
                                                                                          throws JBossResourceException {
        if (urlSelector == null) {
            JBossStringBuilder buffer = new JBossStringBuilder();
            buffer.append("Missing configuration for HA local datasource. ");
            if (getConnectionURL() == null)
                buffer.append("No connection-url. ");
            if (urlDelimiter == null)
                buffer.append("No url-delimiter. ");
            throw new JBossResourceException(buffer.toString());
        }

        // try to get a connection as many times as many urls we have in the list
        for (int i = 0; i < urlSelector.getUrlList().size(); ++i) {
            String url = urlSelector.getUrl();

            if (log.isDebugEnabled()) {
                log.debug("Trying to create a connection to " + url);
            }

            try {
                Driver d = getDriver(url);
                Connection con = d.connect(url, copy);
                if (con == null) {
                    log.warn("Wrong driver class for this connection URL: " + url);
                    urlSelector.failedUrl(url);
                } else {
                    return new LocalManagedConnection(this, con, props, transactionIsolation,
                        preparedStatementCacheSize);
                }
            } catch (Exception e) {
                log.warn("Failed to create connection for " + url + ": " + e.getMessage());
                urlSelector.failedUrl(url);
            }
        }

        // we have supposedly tried all the urls
        throw new JBossResourceException("Could not create connection using any of the URLs: "
                                         + urlSelector.getUrlList());
    }

    /**
     * 
     */
    private void initUrlSelector() {
        List<String> urlsList = new ArrayList<String>();
        String urlsStr = getConnectionURL();
        String url;
        int urlStart = 0;
        int urlEnd = urlsStr.indexOf(urlDelimiter);
        while (urlEnd > 0) {
            url = urlsStr.substring(urlStart, urlEnd);
            urlsList.add(url);
            urlStart = ++urlEnd;
            urlEnd = urlsStr.indexOf(urlDelimiter, urlEnd);
            if (log.isDebugEnabled()) {
                log.debug("added HA connection url: " + url);
            }
        }

        if (urlStart != urlsStr.length()) {
            url = urlsStr.substring(urlStart, urlsStr.length());
            urlsList.add(url);
            if (log.isDebugEnabled()) {
                log.debug("added HA connection url: " + url);
            }
        }

        this.urlSelector = new URLSelector(urlsList);
    }

    // Inner

    /**
     * 
     * @author sicong.shou
     * @version $Id: HALocalManagedConnectionFactory.java, v 0.1 2012-11-23 ÉÏÎç11:38:48 sicong.shou Exp $
     */
    public static class URLSelector {
        private final List urls;
        private int        urlIndex;
        private String     url;

        public URLSelector(List urls) {
            if (urls == null || urls.size() == 0) {
                throw new IllegalStateException(
                    "Expected non-empty list of connection URLs but got: " + urls);
            }
            this.urls = Collections.unmodifiableList(urls);
        }

        public synchronized String getUrl() {
            if (url == null) {
                if (urlIndex == urls.size()) {
                    urlIndex = 0;
                }
                url = (String) urls.get(urlIndex++);
            }
            return url;
        }

        public synchronized void failedUrl(String url) {
            if (url.equals(this.url)) {
                this.url = null;
            }
        }

        public List getUrlList() {
            return urls;
        }
    }
}
