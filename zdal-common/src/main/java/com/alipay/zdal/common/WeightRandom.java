/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * 
 * @author 伯牙
 * @version $Id: WeightRandom.java, v 0.1 2014-1-6 下午05:17:44 Exp $
 */
public class WeightRandom {

    private static final Logger               logger                 = Logger
                                                                         .getLogger(WeightRandom.class);

    public static final int                   DEFAULT_WEIGHT_NEW_ADD = 0;
    public static final int                   DEFAULT_WEIGHT_INIT    = 10;

    private Map<String, Integer>              cachedWeightConfig;
    private final RuntimeConfigHolder<Weight> weightHolder           = new RuntimeConfigHolder<Weight>();

    /**
     * 保持不变对象，只能重建，不能修改
     */
    private static class Weight {
        public Weight(int[] weights, String[] weightKeys, int[] weightAreaEnds) {
            this.weightKeys = weightKeys;
            this.weightValues = weights;
            this.weightAreaEnds = weightAreaEnds;
        }

        public final String[] weightKeys;    //调用者保证不能修改其元素
        public final int[]    weightValues;  //调用者保证不能修改其元素
        public final int[]    weightAreaEnds; //调用者保证不能修改其元素
    }

    public WeightRandom(Map<String, Integer> weightConfigs) {
        this.init(weightConfigs);
    }

    public WeightRandom(String[] keys) {
        Map<String, Integer> weightConfigs = new HashMap<String, Integer>(keys.length);
        for (String key : keys) {
            weightConfigs.put(key, DEFAULT_WEIGHT_INIT);
        }
        this.init(weightConfigs);
    }

    private void init(Map<String, Integer> weightConfig) {
        this.cachedWeightConfig = weightConfig;
        String[] weightKeys = weightConfig.keySet().toArray(new String[0]);
        int[] weights = new int[weightConfig.size()];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weightConfig.get(weightKeys[i]);
        }
        int[] weightAreaEnds = genAreaEnds(weights);
        weightHolder.set(new Weight(weights, weightKeys, weightAreaEnds));
    }

    /**
     * 支持动态修改
     */
    public void setWeightConfig(Map<String, Integer> weightConfig) {
        this.init(weightConfig);
    }

    public Map<String, Integer> getWeightConfig() {
        return this.cachedWeightConfig;
    }

    /**
     * 假设三个库权重    10   9   8
     * 那么areaEnds就是  10  19  27
     * 随机数是0~27之间的一个数
     * 
     * 分别去上面areaEnds里的元素比。
     * 
     * 发现随机数小于一个元素了，则表示应该选择这个元素
     * 
     * 注意：该方法不能改变参数数组内容
     */
    private final Random random = new Random();

    private String select(int[] areaEnds, String[] keys) {
        int sum = areaEnds[areaEnds.length - 1];
        if (sum == 0) {
            logger.error("areaEnds: " + intArray2String(areaEnds));
            return null;
        }
        //选择的过
        //findbugs认为这里不是很好(每次都新建一个Random)(guangxia)
        int rand = random.nextInt(sum);
        for (int i = 0; i < areaEnds.length; i++) {
            if (rand < areaEnds[i]) {
                return keys[i];
            }
        }
        return null;
    }

    /**
     * 通过数据源的标识获取对应的权重；
     * @param key
     * @return
     */
    public int getWeightByKey(String key) {
        int weight = 0;
        boolean flag = false;
        final Weight w = weightHolder.get();
        for (int k = 0; k < w.weightKeys.length; k++) {
            if (w.weightKeys[k].equals(key)) {
                flag = true;
                weight = w.weightValues[k];
            }
        }
        if (flag == false) {
            logger.error("数据源的标识不存在，非法的key=" + key);
        }
        return weight;
    }

    /**
     * @param excludeKeys 需要排除的key列表 
     * @return
     */
    public String select(List<String> excludeKeys) {
        final Weight w = weightHolder.get(); //后续实现保证不能改变w中任何数组的内容，否则线程不安全
        if (excludeKeys == null || excludeKeys.isEmpty()) {
            return select(w.weightAreaEnds, w.weightKeys);
        }
        int[] tempWeights = w.weightValues.clone();
        for (int k = 0; k < w.weightKeys.length; k++) {
            if (excludeKeys.contains(w.weightKeys[k])) {
                tempWeights[k] = 0;
            }
        }
        int[] tempAreaEnd = genAreaEnds(tempWeights);
        return select(tempAreaEnd, w.weightKeys);
    }

    public static interface Tryer<T extends Throwable> {
        /**
         * @return null表示成功，否则返回一个异常
         */
        public T tryOne(String name);
    }

    /**
     * @return null表示成功，否则返回一个异常列表
     */
    public <T extends Throwable> List<T> retry(int times, Tryer<T> tryer) {
        List<T> exceptions = new ArrayList<T>(0);
        List<String> excludeKeys = new ArrayList<String>(0);
        for (int i = 0; i < times; i++) {
            String name = this.select(excludeKeys);
            T e = tryer.tryOne(name);
            if (e != null) {
                exceptions.add(e);
                excludeKeys.add(name);
            } else {
                return null;
            }
        }
        return exceptions;
    }

    public <T extends Throwable> List<T> retry(Tryer<T> tryer) {
        return retry(3, tryer);
    }

    private static int[] genAreaEnds(int[] weights) {
        if (weights == null) {
            return null;
        }
        int[] areaEnds = new int[weights.length];
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            areaEnds[i] = sum;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("generate " + intArray2String(areaEnds) + " from "
                         + intArray2String(weights));
        }
        if (sum == 0) {
            logger.warn("generate " + intArray2String(areaEnds) + " from "
                        + intArray2String(weights));
        }
        return areaEnds;
    }

    private static String intArray2String(int[] inta) {
        if (inta == null) {
            return "null";
        } else if (inta.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(inta[0]);
        for (int i = 1; i < inta.length; i++) {
            sb.append(", ").append(inta[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public String getAllDbKeys() {
        StringBuilder sb = new StringBuilder();
        final Weight w = weightHolder.get();
        sb.append("[").append(w.weightKeys[0]);
        for (int i = 1; i < w.weightKeys.length; i++) {
            sb.append(",").append(w.weightKeys[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
