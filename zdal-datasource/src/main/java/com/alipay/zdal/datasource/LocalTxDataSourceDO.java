/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;
import com.alipay.zdal.datasource.resource.security.SecureIdentityLoginModule;

/**
 * 
 * @author sicong.shou
 * @version $Id: LocalTxDataSourceDO.java, v 0.1 2012-11-22 上午10:04:48 sicong.shou Exp $
 */
public class LocalTxDataSourceDO implements Cloneable {

    /** 数据源名称,必填 */
    private String              dsName                          = null;

    /** 数据库连接的url,必填，由统一数据源提供. */
    private String              connectionURL                   = null;
    /**  连接数据库的driver类名，必填，目前只支持mysql和oracle 
     * mysql：com.mysql.jdbc.Driver
     * oracle:oracle.jdbc.OracleDriver*/
    private String              driverClass                     = null;
    /** 连接数据库的用户名，必填，由统一数据眼提供. */
    private String              userName                        = null;
    /** 连接数据库的明文密码，password与encPassword两个必须有一个. */
    private String              passWord                        = null;
    /** 连接数据库的密文密码，password与encPassword两个必须有一个，由统一数据源提供. */
    private String              encPassword                     = null;
    /** 最小连接数  ，必须 >=0 */
    private int                 minPoolSize                     = 0;
    /** 最大连接数，必须 >= 0 */
    private int                 maxPoolSize                     = 0;
    /** 用于数据源内部连接是否需要自动剔出的异常检测类，必填，目前只支持mysql，oracle
     *  mysql:com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter.class.getName()
     *  oracle:com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter.class.getName()
     *  与driverClass对应.
     *  */
    private String              exceptionSorterClassName        = null;
    /** 连接空闲时间的长度，确定需要被剔出连接池，原则如下：
     * (idleTimeoutMinutes*60*1000)/2(ms)检测一次，超过idleTimeoutMinutes*60*1000(ms)没有使用的连接就是空闲连接，会自动剔出，
     * 单位：分  必须 > 0 */
    private long                idleTimeoutMinutes              = 180;
    /** 获取连接的最大超时时间，在指定时间内获取不到连接，第一种情况是，连接池的连接全部在使用，第二种情况是数据库繁忙或者有异常,
     * 单位：毫秒  必须 > 0 */
    private int                 blockingTimeoutMillis           = 180;
    /** 每个connection缓存的preparedStatement的大小，
     * 1，oracle11g-thin模式，请设置成0，采用oracle-driver提供的cache 
     * 2，oracle11g-oci模式，根据业务情况设置成一定的>0的值
     * 3，mysql目前不支持，直接设置成0就可以
     * 必须>=0*/
    private int                 preparedStatementCacheSize      = 0;
    /** 执行execute，executeQuery，executeUpdate的最大超时时间,
     * 单位：秒  必须>0 */
    private int                 queryTimeout                    = 180;
    /** 事务隔离级别. */
    private String              transactionIsolation            = "-1";
    /** mysql-driver,oracle-driver所提供的连接参数
     * mysql会默认加上useUnicode=true；characterEncoding=gbk这两个Key-Value
     * oracle会默认加上SetBigStringTryClob=true；defaultRowPrefetch=50这两个Key-Value. */
    private Map<String, String> connectionProperties            = new HashMap<String, String>();
    /** 数据源连接池在初始化成功以后，是否建立最小连接数. */
    private boolean             prefill                         = false;

    @Deprecated
    private boolean             backgroundValidation            = false;
    @Deprecated
    private boolean             validateOnMatch                 = false;
    @Deprecated
    private String              checkValidConnectionSQL         = null;
    @Deprecated
    private String              validConnectionCheckerClassName = null;
    @Deprecated
    private String              trackStatements                 = "nowarn";
    @Deprecated
    private boolean             useFastFail                     = false;
    @Deprecated
    private boolean             sharePreparedStatements         = false;
    @Deprecated
    private String              newConnectionSQL                = null;
    @Deprecated
    private boolean             noTxSeparatePools               = false;
    @Deprecated
    private boolean             txQueryTimeout                  = false;
    @Deprecated
    private long                backgroundValidationMinutes     = 0;

    /**  */
    public static final int     UNSET                           = -1;

    /**
     * 构造方法，默认set进两个conProp，站内的连接都需要
     */
    public LocalTxDataSourceDO() {
        //
        //        connectionProperties.put("useUnicode", "true");
        //        connectionProperties.put("characterEncoding", "gbk");
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public void setBackgroundValidation(boolean backgroundValidation) {
        this.backgroundValidation = backgroundValidation;
    }

    public void setBackgroundValidationMinutes(long backgroundValidationMinutes) {
        this.backgroundValidationMinutes = backgroundValidationMinutes;
    }

    public void setBlockingTimeoutMillis(int blockingTimeoutMillis) {
        this.blockingTimeoutMillis = blockingTimeoutMillis;
    }

    public void setCheckValidConnectionSQL(String checkValidConnectionSQL) {
        this.checkValidConnectionSQL = checkValidConnectionSQL;
    }

    public void addConnectionProperty(String name, String value) {
        this.connectionProperties.put(name, value);
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setExceptionSorterClassName(String exceptionSorterClassName) {
        if (exceptionSorterClassName.equals(MySQLExceptionSorter.class.getName())) {
            connectionProperties.put("useUnicode", "true");
            connectionProperties.put("characterEncoding", "gbk");
        } else if (exceptionSorterClassName.equals(OracleExceptionSorter.class.getName())) {
            connectionProperties.put("SetBigStringTryClob", "true");
            connectionProperties.put("defaultRowPrefetch", "50");
        }
        this.exceptionSorterClassName = exceptionSorterClassName;
    }

    public void setIdleTimeoutMinutes(long idleTimeoutMinutes) {
        this.idleTimeoutMinutes = idleTimeoutMinutes;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public void setNewConnectionSQL(String newConnectionSQL) {
        this.newConnectionSQL = newConnectionSQL;
    }

    public void setNoTxSeparatePools(boolean noTxSeparatePools) {
        this.noTxSeparatePools = noTxSeparatePools;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setPrefill(boolean prefill) {
        this.prefill = prefill;
    }

    public void setPreparedStatementCacheSize(int preparedStatementCacheSize) {
        this.preparedStatementCacheSize = preparedStatementCacheSize;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public void setSharePreparedStatements(boolean sharePreparedStatements) {
        this.sharePreparedStatements = sharePreparedStatements;
    }

    public void setTrackStatements(String trackStatements) {
        this.trackStatements = trackStatements;
    }

    public void setTransactionIsolation(String transactionIsolation) {
        if (StringUtils.equalsIgnoreCase("default", transactionIsolation)) {
            this.transactionIsolation = "-1";
        } else {
            this.transactionIsolation = transactionIsolation;
        }
    }

    public void setTxQueryTimeout(boolean txQueryTimeout) {
        this.txQueryTimeout = txQueryTimeout;
    }

    public void setUseFastFail(boolean useFastFail) {
        this.useFastFail = useFastFail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setValidateOnMatch(boolean validateOnMatch) {
        this.validateOnMatch = validateOnMatch;
    }

    public void setValidConnectionCheckerClassName(String validConnectionCheckerClassName) {
        this.validConnectionCheckerClassName = validConnectionCheckerClassName;
    }

    public String getDsName() {
        return dsName;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getTransactionIsolation() {

        return transactionIsolation;
    }

    public Map<String, String> getConnectionProperties() {
        return connectionProperties;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getBlockingTimeoutMillis() {
        return blockingTimeoutMillis;
    }

    public boolean isBackgroundValidation() {
        return backgroundValidation;
    }

    public long getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    public boolean isValidateOnMatch() {
        return validateOnMatch;
    }

    public String getCheckValidConnectionSQL() {
        return checkValidConnectionSQL;
    }

    public String getValidConnectionCheckerClassName() {
        return validConnectionCheckerClassName;
    }

    public String getExceptionSorterClassName() {
        return exceptionSorterClassName;
    }

    public String getTrackStatements() {
        return trackStatements;
    }

    public boolean isPrefill() {
        return prefill;
    }

    public boolean isUseFastFail() {
        return useFastFail;
    }

    public int getPreparedStatementCacheSize() {
        return preparedStatementCacheSize;
    }

    public boolean isSharePreparedStatements() {
        return sharePreparedStatements;
    }

    public String getNewConnectionSQL() {
        return newConnectionSQL;
    }

    public boolean isNoTxSeparatePools() {
        return noTxSeparatePools;
    }

    public boolean isTxQueryTimeout() {
        return txQueryTimeout;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public String getCriteria() {
        return "ByNothing";
    }

    public long getBackgroundValidationMinutes() {
        return backgroundValidationMinutes;
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (backgroundValidation ? 1231 : 1237);
        result = prime * result
                 + (int) (backgroundValidationMinutes ^ (backgroundValidationMinutes >>> 32));
        result = prime * result + blockingTimeoutMillis;
        result = prime * result
                 + ((checkValidConnectionSQL == null) ? 0 : checkValidConnectionSQL.hashCode());
        result = prime * result
                 + ((connectionProperties == null) ? 0 : connectionProperties.hashCode());
        result = prime * result + ((connectionURL == null) ? 0 : connectionURL.hashCode());
        result = prime * result + ((driverClass == null) ? 0 : driverClass.hashCode());
        result = prime * result
                 + ((exceptionSorterClassName == null) ? 0 : exceptionSorterClassName.hashCode());
        result = prime * result + (int) (idleTimeoutMinutes ^ (idleTimeoutMinutes >>> 32));
        result = prime * result + ((dsName == null) ? 0 : dsName.hashCode());
        result = prime * result + maxPoolSize;
        result = prime * result + minPoolSize;
        result = prime * result + ((newConnectionSQL == null) ? 0 : newConnectionSQL.hashCode());
        result = prime * result + (noTxSeparatePools ? 1231 : 1237);
        result = prime * result + ((passWord == null) ? 0 : passWord.hashCode());
        result = prime * result + (prefill ? 1231 : 1237);
        result = prime * result + preparedStatementCacheSize;
        result = prime * result + queryTimeout;
        result = prime * result + (sharePreparedStatements ? 1231 : 1237);
        result = prime * result + ((trackStatements == null) ? 0 : trackStatements.hashCode());
        result = prime * result
                 + ((transactionIsolation == null) ? 0 : transactionIsolation.hashCode());
        result = prime * result + (txQueryTimeout ? 1231 : 1237);
        result = prime * result + (useFastFail ? 1231 : 1237);
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime
                 * result
                 + ((validConnectionCheckerClassName == null) ? 0 : validConnectionCheckerClassName
                     .hashCode());
        result = prime * result + (validateOnMatch ? 1231 : 1237);
        return result;
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LocalTxDataSourceDO other = (LocalTxDataSourceDO) obj;
        if (backgroundValidation != other.backgroundValidation) {
            return false;
        }
        if (backgroundValidationMinutes != other.backgroundValidationMinutes) {
            return false;
        }
        if (blockingTimeoutMillis != other.blockingTimeoutMillis) {
            return false;
        }
        if (checkValidConnectionSQL == null) {
            if (other.checkValidConnectionSQL != null) {
                return false;
            }
        } else if (!checkValidConnectionSQL.equals(other.checkValidConnectionSQL)) {
            return false;
        }
        if (connectionProperties == null) {
            if (other.connectionProperties != null) {
                return false;
            }
        } else if (!connectionProperties.equals(other.connectionProperties)) {
            return false;
        }
        if (connectionURL == null) {
            if (other.connectionURL != null) {
                return false;
            }
        } else if (!connectionURL.equals(other.connectionURL)) {
            return false;
        }
        if (driverClass == null) {
            if (other.driverClass != null) {
                return false;
            }
        } else if (!driverClass.equals(other.driverClass)) {
            return false;
        }
        if (exceptionSorterClassName == null) {
            if (other.exceptionSorterClassName != null) {
                return false;
            }
        } else if (!exceptionSorterClassName.equals(other.exceptionSorterClassName)) {
            return false;
        }
        if (idleTimeoutMinutes != other.idleTimeoutMinutes) {
            return false;
        }
        if (dsName == null) {
            if (other.dsName != null) {
                return false;
            }
        } else if (!dsName.equals(other.dsName)) {
            return false;
        }
        if (maxPoolSize != other.maxPoolSize) {
            return false;
        }
        if (minPoolSize != other.minPoolSize) {
            return false;
        }
        if (newConnectionSQL == null) {
            if (other.newConnectionSQL != null) {
                return false;
            }
        } else if (!newConnectionSQL.equals(other.newConnectionSQL)) {
            return false;
        }
        if (noTxSeparatePools != other.noTxSeparatePools) {
            return false;
        }

        if (passWord == null) {
            if (other.passWord != null) {
                return false;
            }
        } else if (!passWord.equals(other.passWord)) {
            return false;
        }
        if (prefill != other.prefill) {
            return false;
        }
        if (preparedStatementCacheSize != other.preparedStatementCacheSize) {
            return false;
        }
        if (queryTimeout != other.queryTimeout) {
            return false;
        }
        if (sharePreparedStatements != other.sharePreparedStatements) {
            return false;
        }
        if (trackStatements == null) {
            if (other.trackStatements != null) {
                return false;
            }
        } else if (!trackStatements.equals(other.trackStatements)) {
            return false;
        }
        if (transactionIsolation == null) {
            if (other.transactionIsolation != null) {
                return false;
            }
        } else if (!transactionIsolation.equals(other.transactionIsolation)) {
            return false;
        }
        if (txQueryTimeout != other.txQueryTimeout) {
            return false;
        }
        if (useFastFail != other.useFastFail) {
            return false;
        }

        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        if (validConnectionCheckerClassName == null) {
            if (other.validConnectionCheckerClassName != null) {
                return false;
            }
        } else if (!validConnectionCheckerClassName.equals(other.validConnectionCheckerClassName)) {
            return false;
        }
        if (validateOnMatch != other.validateOnMatch) {
            return false;
        }

        return true;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) throws Exception {
        if (StringUtils.isNotBlank(encPassword)) {
            this.passWord = new String(SecureIdentityLoginModule.decode(encPassword));
            this.encPassword = encPassword;
        }
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     *
     * @return a <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        final String TAB = ", ";
        StringBuilder sb = new StringBuilder();
        sb.append("LocalTxDataSourceDO(").append(super.toString()).append(TAB).append("dsName = ")
            .append(this.dsName).append(TAB).append("connectionURL = ").append(this.connectionURL)
            .append(TAB).append("driverClass = ").append(this.driverClass).append(TAB).append(
                "userName = ").append(this.userName).append(TAB).append("encPassword = ").append(
                this.encPassword).append(TAB).append("maxPoolSize = ").append(this.maxPoolSize)
            .append(TAB).append("minPoolSize = ").append(this.minPoolSize).append(TAB).append(
                "preparedStatementCacheSize = ").append(this.preparedStatementCacheSize)
            .append(TAB).append("transactionIsolation = ").append(this.transactionIsolation)
            .append(TAB).append("connectionProperties = ").append(this.connectionProperties)
            .append(TAB).append("blockingTimeoutMillis = ").append(this.blockingTimeoutMillis)
            .append(TAB).append("backgroundValidation = ").append(this.backgroundValidation)
            .append(TAB).append("idleTimeoutMinutes = ").append(this.idleTimeoutMinutes)
            .append(TAB).append("validateOnMatch = ").append(this.validateOnMatch).append(TAB)
            .append("checkValidConnectionSQL = ").append(this.checkValidConnectionSQL).append(TAB)
            .append("validConnectionCheckerClassName = ").append(
                this.validConnectionCheckerClassName).append(TAB).append(
                "exceptionSorterClassName = ").append(this.exceptionSorterClassName).append(TAB)
            .append("trackStatements = ").append(this.trackStatements).append(TAB).append(
                "prefill = ").append(this.prefill).append(TAB).append("useFastFail = ").append(
                this.useFastFail).append(TAB).append("sharePreparedStatements = ").append(
                this.sharePreparedStatements).append(TAB).append("newConnectionSQL = ").append(
                this.newConnectionSQL).append(TAB).append("noTxSeparatePools = ").append(
                this.noTxSeparatePools).append(TAB).append("txQueryTimeout = ").append(
                this.txQueryTimeout).append(TAB).append("queryTimeout = ")
            .append(this.queryTimeout).append(TAB).append("backgroundValidationMinutes = ").append(
                this.backgroundValidationMinutes);
        return sb.toString();
    }

    /** 
     * @see java.lang.Object#clone()
     */
    @Override
    public LocalTxDataSourceDO clone() {
        LocalTxDataSourceDO LocalTxDataSourceDO = null;
        try {
            LocalTxDataSourceDO = (LocalTxDataSourceDO) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return LocalTxDataSourceDO;
    }

    public void setConnectionProperties(Map<String, String> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}
