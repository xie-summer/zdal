/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.util;

/**
 * 
 * @author sicong.shou
 * @version $Id: PoolCondition.java, v 0.1 2012-11-19 下午08:19:23 sicong.shou Exp $
 */
public class PoolCondition {
    /** 数据源名字 */
    String dsName                   = "";
    /**最小连接数  */
    int    minSize                  = 0;
    /** 最大连接数 */
    int    maxSize                  = 0;
    /** 可用的连接数 */
    long   availableConnectionCount = 0;
    /**  当前数据源管理的连接数*/
    int    connectionCount          = 0;
    /**  当前在使用中的连接数*/
    long   inUseConnectionCount     = 0;
    /**  被使用过的最大的连接数*/
    long   maxConnectionsInUseCount = 0;
    /** 创建的连接数 */
    int    connectionCreatedCount   = 0;
    /** 销毁的连接数 */
    int    connectionDestroyedCount = 0;

    public PoolCondition(String dsName, int min, int max, long avl, int con, long inUse,
                         long maxInUse, int createCnt, int destroyCnt) {
        this.dsName = dsName;
        minSize = min;
        maxSize = max;
        availableConnectionCount = avl;
        connectionCount = con;
        inUseConnectionCount = inUse;
        maxConnectionsInUseCount = maxInUse;
        connectionCreatedCount = createCnt;
        connectionDestroyedCount = destroyCnt;
    }

    @Override
    public String toString() {
        //        return dsName + "\t最小连接数:" + minSize + "\t最大连接数:" + maxSize + "\t可用的连接数:"
        //               + availableConnectionCount + "\t当前数据源管理的连接数:" + connectionCount + "\t当前在使用中的连接数:"
        //               + inUseConnectionCount + "\t被使用过的最大的连接数:" + maxConnectionsInUseCount + "\t总共创建的连接数:"
        //               + connectionCreatedCount + "\t总共销毁的连接数:" + connectionDestroyedCount;
        return dsName + "[min:" + minSize + "-max:" + maxSize + "-canUse:"
               + availableConnectionCount + "-managed:" + connectionCount + "-using:"
               + inUseConnectionCount + "-maxUsed:" + maxConnectionsInUseCount + "-createCount:"
               + connectionCreatedCount + "-destroyCount:" + connectionDestroyedCount + "]";
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getAvailableConnectionCount() {
        return availableConnectionCount;
    }

    public void setAvailableConnectionCount(long availableConnectionCount) {
        this.availableConnectionCount = availableConnectionCount;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public long getInUseConnectionCount() {
        return inUseConnectionCount;
    }

    public void setInUseConnectionCount(long inUseConnectionCount) {
        this.inUseConnectionCount = inUseConnectionCount;
    }

    public long getMaxConnectionsInUseCount() {
        return maxConnectionsInUseCount;
    }

    public void setMaxConnectionsInUseCount(long maxConnectionsInUseCount) {
        this.maxConnectionsInUseCount = maxConnectionsInUseCount;
    }

    public int getConnectionCreatedCount() {
        return connectionCreatedCount;
    }

    public void setConnectionCreatedCount(int connectionCreatedCount) {
        this.connectionCreatedCount = connectionCreatedCount;
    }

    public int getConnectionDestroyedCount() {
        return connectionDestroyedCount;
    }

    public void setConnectionDestroyedCount(int connectionDestroyedCount) {
        this.connectionDestroyedCount = connectionDestroyedCount;
    }
}
