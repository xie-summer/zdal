/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.config.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.config.ZdalConfigListener;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.lang.StringUtil;

/**
 * zdal 信号资源类, 主要为了响应zdal管控平台管控动作,用于动态推送failover，markdown，markup等.
 * 需要改造成借助于zookeeper进行动态推送.
 * @author 伯牙
 * @version $Id: ZdalSignalResource.java, v 0.1 2012-11-17 下午4:05:12 Exp $
 */
public class ZdalSignalResource {

    private static final String DRM_ATT_KEY_WEIGHT = "keyWeight";

    /** 专门打印推送结果的log信息. */
    private static final Logger log                = Logger
                                                       .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    private ZdalConfigListener  configListener;

    private Lock                lock               = new ReentrantLock();

    public ZdalSignalResource(ZdalConfigListener configListener) {
        this.configListener = configListener;
        registerZk();
    }

    /**
     * 注册zoonkeeper的客户端，进行动态管控.
     */
    private void registerZk() {

    }

    /**
     * 
     * @param key
     * @param value
     */
    public void updateResource(String key, Object value) {
        lock.lock();
        try {
            if (key.equals(DRM_ATT_KEY_WEIGHT)) {
                if (value == null || StringUtil.isBlank(value.toString())) {
                    log.warn("WARN ## the keyWeight is null,will ignore this drm pull");
                    return;
                }
                Map<String, String> groupInfos = convertKeyWeights(value.toString());
                if (groupInfos == null || groupInfos.isEmpty()) {
                    log.warn("WARN ## the pull keyWeights = " + value + " is invalidate");
                    return;
                }
                configListener.resetWeight(groupInfos);
            }
        } catch (Exception e) {
            log.error("ERROR ## ", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 销毁zoonkeeper客户端.
     */
    public void close() {
        //TODO 
    }

    /**
     * failover:
     * group_00=10,0;group_01=10,0
     * @param keyWeight
     * @return
     */
    private Map<String, String> convertKeyWeights(String keyWeight) {
        String[] splits = keyWeight.split(";");
        Map<String, String> results = new HashMap<String, String>();
        for (int i = 0; i < splits.length; i++) {
            String tmp = splits[i];
            String[] groupInfos = tmp.split("=");
            results.put(groupInfos[0], groupInfos[1]);
        }
        return results;
    }

}
