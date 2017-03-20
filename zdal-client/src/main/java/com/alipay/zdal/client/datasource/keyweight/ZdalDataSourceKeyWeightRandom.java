/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.datasource.keyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.RuntimeConfigHolder;

/**
 * trade-failover方案数据源权重管理的核心类
 * 主要完成Master/Failover数据源权重的管理，根据权重随机产生一个db序号。
 * @author zhaofeng.wang
 * @version $Id: TDataSourceKeyWeightRandom.java,v 0.1 2012-5-2 上午10:29:56 zhaofeng.wang Exp $
 */
public class ZdalDataSourceKeyWeightRandom {

    private static final Logger               logger             = Logger
                                                                     .getLogger(ZdalDataSourceKeyWeightRandom.class);

    /**
     * 每个组内的数据源的个数；
     */
    private final int                         dataSourceNumberInGroup;
    /**
     * 缓存的数据源权重，其中key为数据源标识，value为对应的权重
     */
    private final Map<String, Integer>        cachedWeightConfig = new HashMap<String, Integer>();
    /**
     * 运行时变量，存放权重的容器
     */
    private final RuntimeConfigHolder<Weight> weightHolder       = new RuntimeConfigHolder<Weight>();

    /**
     * 保持不变对象，只能重建，不能修改
     */
    private static class Weight {
        public Weight(int[] weights, String[] weightKeys, int[] weightAreaEnds) {
            this.weightKeys = weightKeys;
            this.weightValues = weights;
            this.weightAreaEnds = weightAreaEnds;
        }

        /**
         * 数据源的标识作为weight的key，调用者保证不能修改其元素
         */
        public final String[] weightKeys;
        /**
         * 数据源的权重，与上面的weightKeys中的值一一对应，调用者保证不能修改其元素
         */
        public final int[]    weightValues;
        /**
         * 计算出来的权重区间段，与上述的key和value对应，调用者保证不能修改其元素
         */
        public final int[]    weightAreaEnds;
    }

    /**
     * 初始化权重缓存，以及计算权重区间段
     * @param weightKeys  数据源key
     * @param weights     与数据源key一一对应的权重值
     */
    public ZdalDataSourceKeyWeightRandom(String[] weightKeys, int[] weights) {

        for (int i = 0; i < weightKeys.length; i++) {
            this.cachedWeightConfig.put(weightKeys[i], weights[i]);
        }
        int[] weightAreaEnds = genAreaEnds(weights);
        this.dataSourceNumberInGroup = weightKeys.length;
        weightHolder.set(new Weight(weights, weightKeys, weightAreaEnds));
    }

    public Map<String, Integer> getWeightConfig() {
        return this.cachedWeightConfig;
    }

    /**
     * 随机器，用于根据权重产生随机数
     */
    private final Random random = new Random();

    /**
     * 
     * 假设三个库权重    10   9   8
     * 那么areaEnds就是  10  19  27
     * 随机数是0~27之间的一个数，依次去和areaEnds里的元素比，若发现随机数小于某元素，则表示应该选择这个元素,即返回该元素的下标号。
     * 
     * 注意：该方法不能改变参数数组内容,后续实现保证不能改变w中任何数组的内容，否则线程不安全
     * @return int 
     */
    public int select() {
        final Weight w = weightHolder.get();
        int[] areaEnds = w.weightAreaEnds;
        int sum = areaEnds[areaEnds.length - 1];
        if (sum == 0) {
            logger.error("该组数据源权重全部为0，areaEnds: " + intArray2String(areaEnds));
            return -1;
        }
        int rand = random.nextInt(sum);
        for (int i = 0; i < areaEnds.length; i++) {
            if (rand < areaEnds[i]) {
                return i;
            }
        }
        logger.error("Choose the dataSource in the areaEnds failed, the rand=" + rand
                     + ", areaEnds:" + intArray2String(areaEnds));
        return -1;
    }

    /**
     * 计算权重区间
     * 
     * @param weights  数据源的权重数组
     * @return   权重区间
     */
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
            logger.debug("generate areaEnds" + intArray2String(areaEnds) + " from weights"
                         + intArray2String(weights));
        }
        if (sum == 0) {
            logger.warn("generate areaEnds" + intArray2String(areaEnds) + " from weights"
                        + intArray2String(weights));
        }
        return areaEnds;
    }

    /**
     * 权重数组日志输出转义
     * @param inta 权重数组
     * @return  格式化后的日志字符串
     */
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

    /**
     * 获取所有的db的标识合并后的字符串
     *  
     * @return db标识集合字符串
     */
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

    /**
     * 根据权重随机获取db的序号
     * @param excludeNums  不可用db序号的集合
     * @return   db的序号，不一定真正可用，需要外围做校验
     */
    public int getRandomDBIndexByWeight(List<Integer> excludeNums) {
        final Weight w = weightHolder.get();
        int weights[] = w.weightValues;
        List<Integer> dbIndexes = new ArrayList<Integer>();
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > 0 && !(excludeNums.contains(i))) {
                dbIndexes.add(i);
            }
        }
        int size = dbIndexes.size();
        if (size <= 0) {
            throw new IllegalArgumentException("没有可用的数据源了，权重全部为0！");
        }
        int rand = random.nextInt(size);
        return dbIndexes.get(rand);
    }

    /**
      * 根据传入的db序列号，判定db是否可用
      * 
      * @param dbNumber  db序列号
      * @return          当前db是否可用
      */
    public boolean isDataBaseAvailable(int dbNumber) {
        final Weight w = weightHolder.get();
        int weights[] = w.weightValues;
        if (weights[dbNumber] > 0) {
            return true;
        }
        return false;
    }

    /**
     * 返回不可用的数据库序列集合
     * 
     * @return
     */
    public List<Integer> getNotAvailableDBIndexes() {
        final Weight w = weightHolder.get();
        int weights[] = w.weightValues;
        List<Integer> dbIndexes = new ArrayList<Integer>();
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= 0) {
                dbIndexes.add(i);
            }
        }
        return dbIndexes;
    }

    /**
     * 返回可用的数据库序列集合
     * 
     * @return
     */
    public List<Integer> getAvailableDBIndexes() {
        final Weight w = weightHolder.get();
        int weights[] = w.weightValues;
        List<Integer> dbIndexes = new ArrayList<Integer>();
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > 0) {
                dbIndexes.add(i);
            }
        }
        return dbIndexes;
    }

    /**
     * 根据db序号获取db的key标识
     * @param number  db序号
     * @return   db标识
     */
    public String getDBKeyByNumber(int number) {
        final Weight w = weightHolder.get();
        if (number >= w.weightKeys.length) {
            throw new IllegalArgumentException("The db number is out of scope, number= " + number
                                               + ",the largest is " + w.weightKeys.length);
        }
        return w.weightKeys[number];
    }

    public String[] getDBWeightKeys() {
        final Weight w = weightHolder.get();
        return w.weightKeys;
    }

    public int[] getDBWeightValues() {
        final Weight w = weightHolder.get();
        return w.weightValues;
    }

    /**
     * 根据db序号获取db的权重
     * @param number  db序号
     * @return   db 权重
     */
    public int getDBWeightByNumber(int number) {
        final Weight w = weightHolder.get();
        if (number >= w.weightKeys.length) {
            throw new IllegalArgumentException("The db number is out of scope, number= " + number
                                               + ",the largest is " + w.weightKeys.length);
        }
        return w.weightValues[number];
    }

    /**
     * 获取所有的数据源标识
     * 
     * @return 数据源标识集合数组
     */
    public String[] getDBKeysArray() {
        String keys[] = weightHolder.get().weightKeys;
        return keys;
    }

    public int getDataSourceNumberInGroup() {
        return dataSourceNumberInGroup;
    }
}
