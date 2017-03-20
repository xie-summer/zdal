/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource;

import java.util.Map;

/**
 *   数据源刷新器，通过传入新的数据源属性来重新载入
 * 
 * @author liangjie
 * @version $Id: Flusher.java, v 0.1 2012-7-18 上午10:21:19 liangjie Exp $
 */
public interface Flusher {

    /**
     *  根据具体配置实体重新刷新数据源
     * 
     * @param localTxDataSourceDO
     * @return
     */
    //    public boolean flush(LocalTxDataSourceDO localTxDataSourceDO);

    /**
     * 根据配置map刷新数据源
     * 
     * @param map
     * @return
     */
    public boolean flush(Map<String, String> map);

}
