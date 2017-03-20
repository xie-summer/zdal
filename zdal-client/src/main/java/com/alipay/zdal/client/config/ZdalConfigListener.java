/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

import java.util.Map;

/**
 * 动态调整failover的状态，借助于分布式环境下的动态推送.
 * @author 伯牙
 * @version $Id: ZdalConfigListener.java, v 0.1 2012-11-17 下午4:29:22 Exp $
 */
public interface ZdalConfigListener {

    /**
     * 通过drm推送切换信息.
     * @param keyWeights 推送的值.
     */
    void resetWeight(Map<String, String> keyWeights);

}
