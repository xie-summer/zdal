/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

/**
 * 因为重试以datasource为key,因此需要将parent datasource引用也带入到临时缓存的可重用连接中。
 * 
 * 这3个属性是与直接通过sql计算出来的分库向对应的，通过这3个对象
 * 
 * 可以做到：1，重新根据dbSelector选择datasource.
 *         2，根据parentDataSource 从dbSelector中移除当前有问题的parentDataSource
 *         3，获取真正的链接。
 * 
 * 
 *
 */
public class ConnectionAndDatasource {
    /**
     * 随着读重试 connection也会不断发生变化。
     */
    Connection connection;
    DataSource parentDataSource;
    /**
     * dbselector对象，在正常情况下是TDatasource中持有的dbSelector.
     * 但如果发生读重试，则持有的是经过copy，并去掉了有问题datasource对象的dbSelector
     */
    DBSelector dbSelector;
}
