/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

/***
 * Unified all of Zdal DataSource Configuration Type in this enumeration
 */
public enum DataSourceConfigType {
    GROUP, SHARD, SHARD_GROUP, SHARD_FAILOVER;

    public static DataSourceConfigType typeOf(String type) {
        if (null == type || "".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("The DataSourceConfigType can not be null or empty.");
        } else if (type.equalsIgnoreCase("GROUP")) {
            return GROUP;
        } else if (type.equalsIgnoreCase("SHARD")) {
            return SHARD;
        } else if (type.equalsIgnoreCase("SHARD_GROUP")) {
            return SHARD_GROUP;
        } else if (type.equalsIgnoreCase("SHARD_FAILOVER")) {
            return SHARD_FAILOVER;
        } else {
            throw new IllegalArgumentException(
                "The DataSourceConfigType "
                        + type
                        + " has not been supported yet,must to be [group,shard,shard_group,shard_failover].");
        }
    }

    public boolean isGroup() {
        return this.equals(GROUP);
    }

    public boolean isShard() {
        return this.equals(SHARD);
    }

    public boolean isShardGroup() {
        return this.equals(SHARD_GROUP);
    }

    public boolean isShardFailover() {
        return this.equals(SHARD_FAILOVER);
    }
}