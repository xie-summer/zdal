/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.config.DataSourceConfigType;
import com.alipay.zdal.client.dispatcher.SqlDispatcher;
import com.alipay.zdal.client.util.ExceptionUtils;

/**
 * 
 * @author 伯牙
 * @version $Id: ZdalConnection.java, v 0.1 2014-1-6 下午05:02:15 Exp $
 */
public class ZdalConnection implements Connection {
    private static final Logger                  logger               = Logger
                                                                          .getLogger(ZdalConnection.class);

    private Map<String, DBSelector>              dbSelectors;

    private SqlDispatcher                        writeDispatcher;
    private SqlDispatcher                        readDispatcher;

    private int                                  retryingTimes;
    private String                               username;
    private String                               password;

    // ZdalConnection对象所持有的真正数据库连接
    private Map<String, ConnectionAndDatasource> actualConnections    = new HashMap<String, ConnectionAndDatasource>();

    private boolean                              autoCommit           = true;

    private int                                  transactionIsolation = -1;

    private boolean                              closed;

    private boolean                              readOnly;

    private boolean                              txStart;
    private String                               txTarget;

    // TConnection对象创建的所有TStatement对象，包括TPreparedStatement对象
    private Set<ZdalStatement>                   openStatements       = new HashSet<ZdalStatement>();

    private DataSourceConfigType                 dbConfigType         = null;

    /**  数据源的名称*/
    protected String                             appDsName            = null;

    public ZdalConnection() {
    }

    public ZdalConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        checkClosed();

        // TODO: DatabaseMetaData目前还不存在元数据的信息
        return new ZDatabaseMetaData();
    }

    public boolean getAutoCommit() throws SQLException {
        checkClosed();

        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();

        this.autoCommit = autoCommit;
        this.txStart = !autoCommit;

    }

    public int getTransactionIsolation() throws SQLException {
        checkClosed();

        return transactionIsolation;
    }

    public void setTransactionIsolation(int transactionIsolation) throws SQLException {
        checkClosed();

        this.transactionIsolation = transactionIsolation;
    }

    public Statement createStatement() throws SQLException {
        checkClosed();

        ZdalStatement stmt = new RetryableTStatement(writeDispatcher, readDispatcher);
        stmt.setDataSourcePool(dbSelectors);
        //        stmt.setHintReplaceSupport(isHintReplaceSupport);
        //stmt.setRuleController(ruleController);
        stmt.setAutoCommit(autoCommit);
        stmt.setReadOnly(readOnly);
        stmt.setConnectionProxy(this);
        stmt.setRetryingTimes(retryingTimes);
        //		stmt.setPoolRandom(poolRandom);
        stmt.setDbConfigType(dbConfigType);
        stmt.setAppDsName(getAppDsName());
        openStatements.add(stmt);

        return stmt;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
                                                                                 throws SQLException {
        ZdalStatement stmt = (ZdalStatement) createStatement();
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        return stmt;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency,
                                     int resultSetHoldability) throws SQLException {
        ZdalStatement stmt = (ZdalStatement) createStatement(resultSetType, resultSetConcurrency);

        stmt.setResultSetHoldability(resultSetHoldability);

        return stmt;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkClosed();

        ZdalPreparedStatement stmt = new RetryableTPreparedStatement(writeDispatcher,
            readDispatcher);
        stmt.setDataSourcePool(dbSelectors);
        stmt.setAutoCommit(autoCommit);
        stmt.setReadOnly(readOnly);
        stmt.setConnectionProxy(this);
        stmt.setSql(sql);
        stmt.setRetryingTimes(retryingTimes);
        stmt.setDbConfigType(dbConfigType);
        stmt.setAppDsName(getAppDsName());
        openStatements.add(stmt);
        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        ZdalPreparedStatement stmt = (ZdalPreparedStatement) prepareStatement(sql);

        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);

        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
                                                                                                 throws SQLException {
        ZdalPreparedStatement stmt = (ZdalPreparedStatement) prepareStatement(sql, resultSetType,
            resultSetConcurrency);

        stmt.setResultSetHoldability(resultSetHoldability);

        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                                                                                throws SQLException {
        ZdalPreparedStatement stmt = (ZdalPreparedStatement) prepareStatement(sql);

        stmt.setAutoGeneratedKeys(autoGeneratedKeys);

        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        ZdalPreparedStatement stmt = (ZdalPreparedStatement) prepareStatement(sql);

        stmt.setColumnIndexes(columnIndexes);

        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        ZdalPreparedStatement stmt = (ZdalPreparedStatement) prepareStatement(sql);

        stmt.setColumnNames(columnNames);

        return stmt;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new UnsupportedOperationException("prepareCall");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                                                                                                 throws SQLException {
        throw new UnsupportedOperationException("prepareCall");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("prepareCall");
    }

    public void commit() throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("invoke commit");
        }

        checkClosed();

        if (autoCommit) {
            return;
        }

        txStart = true;

        List<SQLException> exceptions = null;

        for (Map.Entry<String, ConnectionAndDatasource> entry : actualConnections.entrySet()) {
            try {
                entry.getValue().connection.commit();
            } catch (SQLException e) {
                if (exceptions == null) {
                    exceptions = new ArrayList<SQLException>();
                }
                exceptions.add(e);

                logger.error(new StringBuilder("data source name: ").append(entry.getKey())
                    .toString(), e);
            }
        }

        ExceptionUtils.throwSQLException(exceptions, null, null);

    }

    public void rollback() throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("invoke rollback");
        }

        checkClosed();

        if (autoCommit) {
            return;
        }

        txStart = true;

        List<SQLException> exceptions = null;

        for (Map.Entry<String, ConnectionAndDatasource> entry : actualConnections.entrySet()) {
            try {
                entry.getValue().connection.rollback();
            } catch (SQLException e) {
                if (exceptions == null) {
                    exceptions = new ArrayList<SQLException>();
                }
                exceptions.add(e);

                logger.error(new StringBuilder("data source name: ").append(entry.getKey())
                    .toString(), e);
            }
        }
        ExceptionUtils.throwSQLException(exceptions, null, null);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException("rollback");
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new UnsupportedOperationException("setSavepoint");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new UnsupportedOperationException("setSavepoint");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException("releaseSavepoint");
    }

    protected void checkClosed() throws SQLException {
        if (closed) {
            throw new SQLException("No operations allowed after connection closed.");
        }
    }

    public boolean isClosed() throws SQLException {
        return closed;
    }

    public void close() throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("invoke close");
        }

        if (closed) {
            return;
        }

        List<SQLException> exceptions = null;

        try {
            for (ZdalStatement stmt : openStatements) {
                try {
                    //bug fix by shenxun :这里不允许内部调用remove的方法，而在外部显示的调用该方法
                    //引发bug的主要原因是当set里面的size多于1个，并且调用remove方法时会发生这时候HashSet会检查modification.
                    stmt.closeInternal(false);
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);
                }
            }

            for (Map.Entry<String, ConnectionAndDatasource> entry : actualConnections.entrySet()) {
                try {
                    entry.getValue().connection.close();
                } catch (SQLException e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<SQLException>();
                    }
                    exceptions.add(e);

                    logger.error(new StringBuilder("data source name: ").append(entry.getKey())
                        .toString(), e);
                }
            }
        } finally {
            closed = true;
            openStatements.clear();
            actualConnections.clear();
        }

        ExceptionUtils.throwSQLException(exceptions, null, null);
    }

    public boolean isReadOnly() throws SQLException {
        checkClosed();

        return readOnly;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        checkClosed();

        this.readOnly = readOnly;
    }

    public String getCatalog() throws SQLException {
        throw new UnsupportedOperationException("getCatalog");
    }

    public void setCatalog(String catalog) throws SQLException {
        throw new UnsupportedOperationException("setCatalog");
    }

    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("getHoldability");
    }

    public void setHoldability(int holdability) throws SQLException {
        throw new UnsupportedOperationException("setHoldability");
    }

    public SQLWarning getWarnings() throws SQLException {
        // TODO:
        return null;
    }

    public void clearWarnings() throws SQLException {
        // TODO:
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new UnsupportedOperationException("getTypeMap");
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException("setTypeMap");
    }

    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException("nativeSQL");
    }

    public Map<String, DBSelector> getDataSourcePool() {
        return dbSelectors;
    }

    public void setDataSourcePool(Map<String, DBSelector> dbSelectors) {
        this.dbSelectors = dbSelectors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*	public Map<String, ConnectionAndDatasource> getActualConnections() {
    		return actualConnections;
    	}*/

    public ConnectionAndDatasource getConnectionAndDatasourceByDBSelectorID(String id) {
        //		Map<String, ConnectionAndDatasource> connectionAndDatasources = actualConnections;
        ConnectionAndDatasource connectionAndDatasource = actualConnections.get(id);
        return connectionAndDatasource;
    }

    public void put(String key, ConnectionAndDatasource connectionAndDatasource) {
        actualConnections.put(key, connectionAndDatasource);
    }

    public ConnectionAndDatasource get(String key) {
        return actualConnections.get(key);
    }

    public void removeConnectionAndDatasourceByID(String id) {
        ConnectionAndDatasource connectionAndDatasource = actualConnections.remove(id);
        if (connectionAndDatasource == null) {
            logger.warn("remove by other object?");
        } else {
            if (connectionAndDatasource.connection == null) {
                logger.warn("connection is null");
            } else {
                try {
                    connectionAndDatasource.connection.close();
                } catch (SQLException e) {
                    logger.error("Failed to close connection", e);
                }
            }
        }

    }

    public boolean containsID(String id) {
        return actualConnections.containsKey(id);
    }

    public Set<? extends Statement> getOpenStatements() {
        return openStatements;
    }

    public boolean getTxStart() {
        return txStart;
    }

    public int size() {
        return actualConnections.size();
    }

    public boolean isEmpty() {
        return actualConnections.isEmpty();
    }

    public void setTxStart(boolean txStart) {
        this.txStart = txStart;
    }

    public String getTxTarget() {
        return txTarget;
    }

    public void setTxTarget(String txTarget) {
        this.txTarget = txTarget;
    }

    public void setWriteDispatcher(SqlDispatcher writeDispatcher) {
        this.writeDispatcher = writeDispatcher;
    }

    public void setReadDispatcher(SqlDispatcher readDispatcher) {
        this.readDispatcher = readDispatcher;
    }

    public int getRetryingTimes() {
        return retryingTimes;
    }

    public void setRetryingTimes(int retryingTimes) {
        this.retryingTimes = retryingTimes;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public void setDbConfigType(DataSourceConfigType dbConfigType) {
        this.dbConfigType = dbConfigType;
    }

    public DataSourceConfigType getDbConfigType() {
        return dbConfigType;
    }

    public String getAppDsName() {
        return appDsName;
    }

    public void setAppDsName(String appDsName) {
        this.appDsName = appDsName;
    }

	@Override
	public void abort(Executor arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSchema(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
