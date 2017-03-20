/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.util.LRUCachePolicy;

/**
 * LRU cache for PreparedStatements.  When ps ages out, close it.
 *
 * @author ²®ÑÀ
 * @version $Id: PreparedStatementCache.java, v 0.1 2014-1-6 ÏÂÎç05:29:33 Exp $
 */
public class PreparedStatementCache extends LRUCachePolicy {
    private static final Logger log = Logger.getLogger(PreparedStatementCache.class);

    /**
     * 
     * @author sicong.shou
     * @version $Id: PreparedStatementCache.java, v 0.1 2012-11-23 ÉÏÎç11:27:11 sicong.shou Exp $
     */
    public static class Key {
        /**  */
        public static final int PREPARED_STATEMENT = 1;
        /**  */
        public static final int CALLABLE_STATEMENT = 2;
        private final String    sql;
        private final int       type;
        private final int       resultSetType;
        private final int       resultSetConcurrency;

        /**
         * @param sql
         * @param type
         * @param resultSetType
         * @param resultSetConcurrency
         */
        public Key(String sql, int type, int resultSetType, int resultSetConcurrency) {
            this.sql = sql;
            this.type = type;
            this.resultSetType = resultSetType;
            this.resultSetConcurrency = resultSetConcurrency;
        }

        /** 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || o instanceof Key == false) {
                return false;
            }

            final Key key = (Key) o;

            if (resultSetConcurrency != key.resultSetConcurrency) {
                return false;
            }
            if (resultSetType != key.resultSetType) {
                return false;
            }
            if (type != key.type) {
                return false;
            }

            return !(sql != null ? !sql.equals(key.sql) : key.sql != null);

        }

        /** 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int result;
            result = (sql != null ? sql.hashCode() : 0);
            result = 29 * result + type;
            result = 29 * result + resultSetType;
            result = 29 * result + resultSetConcurrency;
            return result;
        }

        /** 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer tmp = new StringBuffer(super.toString());
            tmp.append('[');
            tmp.append("sql=");
            tmp.append(sql);
            tmp.append(" type=");
            tmp.append(type == PREPARED_STATEMENT ? "PS" : "CS");
            tmp.append(" resultSetType=");
            switch (resultSetType) {
                case ResultSet.TYPE_FORWARD_ONLY: {
                    tmp.append("TYPE_FORWARD_ONLY");
                    break;
                }
                case ResultSet.TYPE_SCROLL_INSENSITIVE: {
                    tmp.append("TYPE_SCROLL_INSENSITIVE");
                    break;
                }
                case ResultSet.TYPE_SCROLL_SENSITIVE: {
                    tmp.append("TYPE_SCROLL_SENSITIVE");
                    break;
                }
                default:
                    tmp.append(resultSetType);
            }
            tmp.append(" resultSetConcurrency=");
            switch (resultSetConcurrency) {
                case ResultSet.CONCUR_READ_ONLY: {
                    tmp.append("CONCUR_READ_ONLY");
                    break;
                }
                case ResultSet.CONCUR_UPDATABLE: {
                    tmp.append("CONCUR_UPDATABLE");
                    break;
                }
                default:
                    tmp.append(resultSetConcurrency);
            }
            tmp.append(']');
            return tmp.toString();
        }
    }

    /**
     * @param max
     */
    public PreparedStatementCache(int max) {
        super(2, max);
        create();
    }

    @Override
    protected void ageOut(LRUCachePolicy.LRUCacheEntry entry) {
        try {
            CachedPreparedStatement ws = (CachedPreparedStatement) entry.m_object;
            ws.agedOut();
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed closing cached statement", e);
            }
        } finally {
            super.ageOut(entry);
        }
    }
}
