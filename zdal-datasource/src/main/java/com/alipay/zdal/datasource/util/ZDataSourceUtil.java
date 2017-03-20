/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.resource.adapter.jdbc.local.LocalTxDataSource;

/**
 * ZDataSourceDRM util class
 * 
 * @author liangjie
 * @version $Id: ZDataSourceDRMUtil.java, v 0.1 2012-8-15 下午1:32:37 liangjie Exp $
 */
public final class ZDataSourceUtil {

    private static final Logger logger = Logger.getLogger(ZDataSourceUtil.class);

    static void replaceValueFromMap(LocalTxDataSourceDO newDO, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                String key = e.getKey(), value = e.getValue();

                if (StringUtil.equalsIgnoreCase(key, Parameter.BACKGROUND_VALIDATION)) {
                    boolean bkv = Boolean.valueOf(value);
                    newDO.setBackgroundValidation(bkv);
                } else if (StringUtil
                    .equalsIgnoreCase(key, Parameter.BACKGROUND_VALIDATION_MINUTES)) {
                    try {
                        long bvm = Long.valueOf(value);
                        newDO.setBackgroundValidationMinutes(bvm);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 backgroundValidationMinutes的值不是Long类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.BLOCKING_TIMEOUT_MILLIS)) {
                    try {
                        int blk = Integer.valueOf(value);
                        newDO.setBlockingTimeoutMillis(blk);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 blockingTimeoutMillis的值不是Integer类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.CHECK_VALID_CONNECTIONSQL)) {
                    newDO.setCheckValidConnectionSQL(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.JDBC_URL)) {
                    newDO.setConnectionURL(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.CONNECTION_PROPERTY)) {
                    newDO.setConnectionProperties(parseConnectionProperties(value));
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.DRIVER_CLASS)) {
                    newDO.setDriverClass(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.EXCEPTION_SORTER_CLASSNAME)) {
                    newDO.setExceptionSorterClassName(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.IDLE_TIMEOUT_MINUTES)) {
                    try {
                        long itm = Long.valueOf(value);
                        newDO.setIdleTimeoutMinutes(itm);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 idleTimeoutMinutes的值不是Long类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.MAXCONN)) {
                    try {
                        int max = Integer.valueOf(value);
                        newDO.setMaxPoolSize(max);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 maxConn的值不是Integer类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.MINCONN)) {
                    try {
                        int min = Integer.valueOf(value);
                        newDO.setMinPoolSize(min);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 minConn的值不是Integer类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.NEW_CONNECTION_SQL)) {
                    newDO.setNewConnectionSQL(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.NO_TXSEPARATEPOOLS)) {
                    boolean ntp = Boolean.parseBoolean(value);
                    newDO.setNoTxSeparatePools(ntp);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.PREFILL)) {
                    boolean pf = Boolean.parseBoolean(value);
                    newDO.setPrefill(pf);
                } else if (StringUtil
                    .equalsIgnoreCase(key, Parameter.PREPARED_STATEMENT_CACHE_SIZE)) {
                    try {
                        int psc = Integer.valueOf(value);
                        newDO.setPreparedStatementCacheSize(psc);
                    } catch (NumberFormatException ex) {
                        logger.error("配置 preparedStatementCacheSize的值不是Integer类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.QUERY_TIMEOUT)) {
                    try {
                        int qt = Integer.valueOf(value);
                        newDO.setQueryTimeout(qt);
                    } catch (NumberFormatException ex) {
                        logger.error("配置queryTimeout的值不是Integer类型，忽略");
                    }
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.SHARE_PREPARED_STATEMENTS)) {
                    boolean sps = Boolean.parseBoolean(value);
                    newDO.setSharePreparedStatements(sps);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.TX_QUERY_TIMEOUT)) {
                    boolean tqt = Boolean.parseBoolean(value);
                    newDO.setTxQueryTimeout(tqt);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.TRANSACTION_ISOLATION)) {
                    newDO.setTransactionIsolation(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.TRACK_STATEMENTS)) {
                    newDO.setTrackStatements(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.USE_FAST_FAIL)) {
                    boolean uff = Boolean.parseBoolean(value);
                    newDO.setUseFastFail(uff);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.VALIDATE_ONMATCH)) {
                    boolean vom = Boolean.parseBoolean(value);
                    newDO.setValidateOnMatch(vom);
                } else if (StringUtil.equalsIgnoreCase(key,
                    Parameter.VALID_CONNECTION_CHECKER_CLASSNAME)) {
                    newDO.setValidConnectionCheckerClassName(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.USER_NAME)) {
                    newDO.setUserName(value);
                } else if (StringUtil.equalsIgnoreCase(key, Parameter.PASSWORD)) {
                    try {
                        newDO.setEncPassword(value);
                    } catch (Exception e1) {
                        logger.error(e1);
                    }
                }
            }
        }
    }

    static boolean isChanged(Map<String, String> map, String key) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                if (StringUtil.equalsIgnoreCase(e.getKey(), key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * connectionProperties的格式   key1%value1;key2%value2...
     * 主要是防止和整个配置的key=value里面的=冲突
     * 
     * @param strP
     * @return
     */
    static Map<String, String> parseConnectionProperties(String strP) {
        if (strP.indexOf("{") > -1) {
            throw new RuntimeException("not support json");
        } else {
            Map<String, String> p = new HashMap<String, String>();
            String[] properties = strP.split(";");
            for (String str : properties) {
                int pos = str.indexOf("=");
                if (pos == -1) {
                    logger.error("connection property格式不对 " + str);
                    continue;
                }
                String key = str.substring(0, pos), value = str.substring(pos + 1);
                p.put(key, value);
            }
            return p;
        }
    }

    /**
     * 将老的LocalTxDataSource里的所有配置复制到新的LocalTxDataSourceDO 里面
     * 
     * @param ds
     * @param DO
     */
    static void copyDS2DO(LocalTxDataSource ds, LocalTxDataSourceDO DO) {
        DO.setUserName(ds.getUserName());
        try {
            DO.setEncPassword(ds.getEncPassword());
        } catch (Exception e1) {
            logger.error(e1);
        }
        DO.setBackgroundValidation(ds.getBackgroundValidation());
        DO.setBackgroundValidationMinutes(ds.getBackGroundValidationMinutes());
        DO.setBlockingTimeoutMillis(ds.getBlockingTimeoutMillis());
        DO.setCheckValidConnectionSQL(ds.getCheckValidConnectionSQL());
        DO.setConnectionURL(ds.getConnectionURL());
        Set<Entry<Object, Object>> entries = ds.getConnectionProperties().entrySet();
        for (Entry<Object, Object> e : entries) {

            String key = (String) e.getKey();
            String value = (String) e.getValue();
            DO.getConnectionProperties().put(key, value);
        }
        DO.setDriverClass(ds.getDriverClass());
        DO.setExceptionSorterClassName(ds.getExceptionSorterClassName());
        DO.setIdleTimeoutMinutes(ds.getIdleTimeoutMinutes());
        DO.setMaxPoolSize(ds.getMaxSize());
        DO.setMinPoolSize(ds.getMinSize());
        DO.setNewConnectionSQL(ds.getNewConnectionSQL());
        DO.setNoTxSeparatePools(ds.getNoTxSeparatePools());
        DO.setPassWord(ds.getPassword());
        DO.setPrefill(ds.getPrefill());
        DO.setPreparedStatementCacheSize(ds.getPreparedStatementCacheSize());
        DO.setQueryTimeout(ds.getQueryTimeout());
        DO.setSharePreparedStatements(ds.getSharePreparedStatements());
        DO.setTrackStatements(ds.getTrackStatements());
        DO.setTransactionIsolation(ds.getTransactionIsolation());
        DO.setTxQueryTimeout(ds.getTxQueryTimeout());
        DO.setUseFastFail(ds.getUseFastFail());
        DO.setUserName(ds.getUserName());
        DO.setValidConnectionCheckerClassName(ds.getValidConnectionCheckerClassName());
        DO.setValidateOnMatch(ds.getValidateOnMatch());
    }

    /**
     * 将LocalTxDataSourceDO里的所有配置复制到LocalTxDataSource里面，现在暂时没用了
     * 
     * @param DO
     * @param ds
     */
    static void copyDO2DS(LocalTxDataSourceDO DO, LocalTxDataSource ds) {
        ds.setBackgroundValidation(DO.isBackgroundValidation());
        ds.setBackGroundValidationMinutes(DO.getBackgroundValidationMinutes());
        ds.setBlockingTimeoutMillis(DO.getBlockingTimeoutMillis());
        ds.setCheckValidConnectionSQL(DO.getCheckValidConnectionSQL());
        ds.setConnectionURL(DO.getConnectionURL());
        ds.getConnectionProperties().putAll(DO.getConnectionProperties());
        ds.setDriverClass(DO.getDriverClass());
        ds.setExceptionSorterClassName(DO.getExceptionSorterClassName());
        ds.setIdleTimeoutMinutes(ds.getIdleTimeoutMinutes());
        ds.setMaxSize(DO.getMaxPoolSize());
        ds.setMinSize(DO.getMinPoolSize());
        ds.setNewConnectionSQL(DO.getNewConnectionSQL());
        ds.setNoTxSeparatePools(DO.isNoTxSeparatePools());
        ds.setPassword(DO.getPassWord());
        ds.setPrefill(DO.isPrefill());
        ds.setPreparedStatementCacheSize(DO.getPreparedStatementCacheSize());
        ds.setQueryTimeout(DO.getQueryTimeout());
        ds.setSharePreparedStatements(DO.isSharePreparedStatements());
        ds.setTrackStatements(DO.getTrackStatements());
        ds.setTransactionIsolation(DO.getTransactionIsolation());
        ds.setTxQueryTimeout(DO.isTxQueryTimeout());
        ds.setUseFastFail(DO.isUseFastFail());
        ds.setUserName(DO.getUserName());
        ds.setValidConnectionCheckerClassName(DO.getValidConnectionCheckerClassName());
        ds.setValidateOnMatch(DO.isValidateOnMatch());
    }

    /**
     * MinPoolSize  MaxPoolSize  BlockingTimeout  IdleTimeout  PreparedStatementCacheSize
     * 或者oracle的connectionproperties
     * 有变化的时候，连接池要重建,这里为了方便起见，mysql的connectionproperties变化的时候，也重建
     * 
     * @param newDO
     * @param oldDS
     * @param propChange
     * @return
     */
    static boolean isNeedReCreate(LocalTxDataSourceDO newDO, LocalTxDataSource oldDS,
                                  boolean propChange) {
        if (propChange) {
            return true;
        }

        if (newDO.getMinPoolSize() != oldDS.getMinSize()
            || newDO.getMaxPoolSize() != oldDS.getMaxSize()
            || newDO.getBlockingTimeoutMillis() != oldDS.getBlockingTimeoutMillis()
            || !StringUtil.equals(newDO.getConnectionURL(), oldDS.getConnectionURL())
            || newDO.getIdleTimeoutMinutes() != oldDS.getIdleTimeoutMinutes()
            || newDO.getPreparedStatementCacheSize() != oldDS.getPreparedStatementCacheSize()
            || newDO.isBackgroundValidation() != oldDS.getBackgroundValidation()
            || newDO.getBackgroundValidationMinutes() != oldDS.getBackGroundValidationMinutes()
            || !StringUtil.equals(newDO.getCheckValidConnectionSQL(), oldDS
                .getCheckValidConnectionSQL())
            || !StringUtil.equals(newDO.getDriverClass(), oldDS.getDriverClass())
            || !StringUtil.equals(newDO.getExceptionSorterClassName(), oldDS
                .getExceptionSorterClassName())
            || !StringUtil.equals(newDO.getNewConnectionSQL(), oldDS.getNewConnectionSQL())
            || newDO.getPreparedStatementCacheSize() != oldDS.getPreparedStatementCacheSize()
            || newDO.getQueryTimeout() != oldDS.getQueryTimeout()
            || !StringUtil.equals(newDO.getTrackStatements(), oldDS.getTrackStatements())
            || (!StringUtil.equals(
                StringUtil.equals("-1", newDO.getTransactionIsolation()) ? "DEFAULT" : newDO
                    .getTransactionIsolation(), oldDS.getTransactionIsolation()))
            || !StringUtil.equals(newDO.getValidConnectionCheckerClassName(), oldDS
                .getValidConnectionCheckerClassName())
            || newDO.isNoTxSeparatePools() != oldDS.getNoTxSeparatePools()
            || newDO.isSharePreparedStatements() != oldDS.getSharePreparedStatements()
            || newDO.isTxQueryTimeout() != oldDS.getTxQueryTimeout()
            || newDO.isUseFastFail() != oldDS.getUseFastFail()
            || newDO.isValidateOnMatch() != oldDS.getValidateOnMatch()
            || newDO.isPrefill() != oldDS.getPrefill()
            || !StringUtil.equals(newDO.getUserName(), oldDS.getUserName())
            || !StringUtil.equals(newDO.getEncPassword(), oldDS.getEncPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 判断connectionproperties是否有变化
     * 
     * @param connectionProperties
     * @return
     */
    static boolean solveConnectionProperties(Map<String, String> newConnectionProperties,
                                             Properties oldConnectionProperties) {
        boolean changed = false;
        if (newConnectionProperties.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, String> entry : newConnectionProperties.entrySet()) {
            String oldValue = oldConnectionProperties.getProperty(entry.getKey());
            if (oldValue == null || !StringUtil.equalsIgnoreCase(oldValue, entry.getValue())) {
                return true;
            }
        }
        return changed;
    }

    /**
     * 判断配置信息字符串的版本信息
     * 
     * @param configValue
     * @return
     */
    static boolean isConfigValueNew(String configValue) {
        String s = new String(configValue);
        s = s.substring(1, s.length() - 1);
        String[] arrays = s.split(";");
        if (arrays[0].equals("{version=2.1.0}")) {
            return true;
        }
        return false;
    }

    private ZDataSourceUtil() {
    }

}