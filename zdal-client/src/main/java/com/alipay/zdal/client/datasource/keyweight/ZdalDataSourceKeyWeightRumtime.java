/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.alipay.zdal.client.datasource.keyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * 
 * @author zhaofeng.wang
 * @version $Id: KeyWeightRumtime.java, v 0.1 2011-9-23 下午04:12:46 zhaofeng.wang Exp $
 */
public class ZdalDataSourceKeyWeightRumtime {

    private static Logger                              logger                    = Logger
                                                                                     .getLogger(ZdalDataSourceKeyWeightRumtime.class);
    private static final int                           DEFAULT_DATASOURCE_WEIGHT = 10;
    private Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder;

    public ZdalDataSourceKeyWeightRumtime(
                                          Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder) {
        this.keyWeightMapHolder = keyWeightMapHolder;
    }

    /**
     * 处理数据源的keyWeight的权重标识
     * 
     * 参数p格式如下
     * group_0=TradeCore00:10,TradeCore00_0:0
     * group_1=TradeCore01:10,TradeCore01_0:0
     * group_2=TradeCore02:0,TradeCore02_0:10
     * 放在运行时变量里keyWeightRuntimeConfigHoder里
     * 
     * @param keyWeightConfig
     * @return
     * @throws Exception 
     */
    public static Map<String, ZdalDataSourceKeyWeightRandom> buildKeyWeightConfig(
                                                                                  Map<String, String> keyWeightConfig,
                                                                                  Map<String, ? extends Object> dataSourceKeyConfig) {
        Map<String, ZdalDataSourceKeyWeightRandom> map = new ConcurrentHashMap<String, ZdalDataSourceKeyWeightRandom>(
            keyWeightConfig.size());
        for (Map.Entry<String, String> entry : keyWeightConfig.entrySet()) {
            String groupKey = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (StringUtil.isBlank(groupKey) || StringUtil.isBlank(value)) {
                logger.error("数据源key=" + groupKey + "分组权重配置信息不能为空,value=" + value);
                return null;
            }
            String[] keyWeightStr = value.split(",");
            String[] weightKeys = new String[keyWeightStr.length];
            int[] weights = new int[keyWeightStr.length];
            //case:单数据源key的时候 ，示例：group_0=TradeCore00或者group_0=TradeCore00:10
            if (keyWeightStr.length == 1) {
                if (StringUtil.isBlank(keyWeightStr[0])) {
                    logger.error("单数据源keyWeightStr[0]分组权重配置信息为空.");
                    return null;
                }
                String[] keyAndWeight = keyWeightStr[0].split(":");
                if (dataSourceKeyConfig.keySet() == null
                    || !dataSourceKeyConfig.keySet().contains(keyAndWeight[0].trim())) {
                    logger.error("数据源key=" + keyAndWeight[0] + "在数据源配置中不存在.");
                    return null;
                }
                weightKeys[0] = keyAndWeight[0].trim();//数据源名称key
                weights[0] = DEFAULT_DATASOURCE_WEIGHT;//默认权重10
            } else if (keyWeightStr.length > 1) {
                //case: 多个数据源的时候
                // 示例:group_0=TradeCore00:10,TradeCore00_0:0
                for (int i = 0; i < keyWeightStr.length; i++) {
                    if (StringUtil.isBlank(keyWeightStr[i])) {
                        logger.error("多数据源keyWeightStr[" + i + "]分组权重配置信息为空.");
                        return null;
                    }
                    String[] keyAndWeight = keyWeightStr[i].split(":");
                    if (keyAndWeight.length != 2) {
                        logger.error("数据源key按组配置权重错误,keyWeightStr[" + i + "]=" + keyWeightStr[i]
                                     + ".");
                        return null;
                    }
                    String key = keyAndWeight[0];
                    if (dataSourceKeyConfig.keySet() == null
                        || !dataSourceKeyConfig.keySet().contains(key)) {
                        logger.error("数据源key=" + key + "在数据源dataSourcePool配置中不存在.");
                        return null;
                    }
                    String weightStr = keyAndWeight[1];
                    if (StringUtil.isBlank(key) || StringUtil.isBlank(weightStr)) {
                        logger.error("数据源key=" + key + "或其权重配置weightStr=" + weightStr + "不能为空.");
                        return null;
                    }
                    weightKeys[i] = key.trim();
                    weights[i] = Integer.parseInt(weightStr.trim());
                }
            } else {
                logger.error("该分组groupKey=" + groupKey + "中数据源的个数不对，length=" + keyWeightStr.length);
                return null;
            }
            //根据该组的groupKey以及对应的keyAndWeightMap生成TDataSourceKeyWeightRandom
            ZdalDataSourceKeyWeightRandom TDataSourceKeyWeightRandom = new ZdalDataSourceKeyWeightRandom(
                weightKeys, weights);
            map.put(groupKey, TDataSourceKeyWeightRandom);
        }
        return map;

    }

    /**
     * Setter method for property <tt>keyWeightMapHolder</tt>.
     * 
     * @param keyWeightMapHolder value to be assigned to property keyWeightMapHolder
     */
    public void setKeyWeightMapHolder(Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder) {
        this.keyWeightMapHolder = keyWeightMapHolder;
    }

    /**
     * Getter method for property <tt>keyWeightMapHolder</tt>.
     * 
     * @return property value of keyWeightMapHolder
     */
    public Map<String, ZdalDataSourceKeyWeightRandom> getKeyWeightMapHolder() {
        return keyWeightMapHolder;
    }

}
