/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import com.alipay.zdal.common.sqljep.function.Comparative;

public class ColumnMetaData {
    /**
     * 指定的列名字段
     */
    public final String      key;
    /**
     * 该列名字段的对应Comparative
     */
    public final Comparative value;

    public ColumnMetaData(String key, Comparative value) {
        this.key = key;
        this.value = value;
    }
}
