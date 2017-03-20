/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.rule.ruleengine.cartesianproductcalculator.CartesianProductCalculator;
import com.alipay.zdal.rule.ruleengine.cartesianproductcalculator.SamplingField;
import com.alipay.zdal.rule.ruleengine.enumerator.Enumerator;
import com.alipay.zdal.rule.ruleengine.enumerator.EnumeratorImp;
import com.alipay.zdal.rule.ruleengine.util.RuleUtils;

/**
 * 结果集是一列数的规则
 * 
 * 
 */
public abstract class CartesianProductBasedListResultRule extends ListAbstractResultRule {

    private static final Logger log        = Logger
                                               .getLogger(CartesianProductBasedListResultRule.class);
    Enumerator                  enumerator = new EnumeratorImp();

    /**
     * 是否需要对交集内的数据取抽样点
     *
     * @see com.alipay.zdal.rule.ruleengine.rule.ListAbstractResultRule#eval(java.util.Map)
     */
    public Map<String/*结果的值，如db的index或table的index */, Field> eval(

    Map<String, Comparative> argumentsMap) {
        Map<String, Set<Object>> enumeratedMap = prepareEnumeratedMap(argumentsMap);//生成描点集合
        if (log.isDebugEnabled()) {
            log.debug("Sampling filed message : " + enumeratedMap);
        }
        Map<String, Field> map = evalElement(enumeratedMap);
        decideWhetherOrNotToThrowSpecEmptySetRuntimeException(map);//决定是否抛出runtimeException
        return map;
    }

    /**
     * 决定是否抛出runtimeException
     * 
     * @param map
     */
    private void decideWhetherOrNotToThrowSpecEmptySetRuntimeException(Map<String, Field> map) {
        if ((map == null || map.isEmpty()) && ruleRequireThrowRuntimeExceptionWhenSetIsEmpty()) {
            throw new EmptySetRuntimeException();
        }
    }

    /**
     * TODO:这个要提到父类方法中
     * @param argumentsMap
     * @return
     */
    public Map<String, Set<Object>> prepareEnumeratedMap(Map<String, Comparative> argumentsMap) {
        if (log.isDebugEnabled()) {
            log.debug("eval at CartesianProductRule ,param is " + argumentsMap);
        }

        Map<String/* column */, Set<Object>/* 描点 */> enumeratedMap = RuleUtils.getSamplingField(
            argumentsMap, parameters);
        return enumeratedMap;
    }

    /** 
     * @see com.alipay.zdal.rule.ruleengine.rule.ListAbstractResultRule#evalWithoutSourceTrace(java.util.Map, java.lang.String, java.util.Set)
     */
    public Set<String> evalWithoutSourceTrace(Map<String, Set<Object>> enumeratedMap,
                                              String mappingTargetColumn, Set<Object> mappingKeys) {
        //        Set<String> set = null;

        if (enumeratedMap.size() == 0) {
            return evalZeroArgumentExpression();

        } else if (enumeratedMap.size() == 1) {
            return evalOneArgumentExpression(enumeratedMap, mappingTargetColumn, mappingKeys);

        } else {

            return evalMutiargumentsExpression(enumeratedMap, mappingTargetColumn, mappingKeys);
        }
    }

    private Set<String> evalMutiargumentsExpression(Map<String, Set<Object>> enumeratedMap,
                                                    String mappingTargetColumn,
                                                    Set<Object> mappingKeys) {
        Set<String> set;
        if (mappingTargetColumn != null || mappingKeys != null) {
            throw new IllegalArgumentException("多列枚举不支持使用映射规则");
        }

        // TODO:用到多个值共同决定分库或分表的时候需要review
        // 多于一个值，需要进行笛卡尔积
        CartesianProductCalculator cartiesianProductCalculator = new CartesianProductCalculator(
            enumeratedMap);
        /*
         * 确实很难确定set的大小，但一般来说分库是16个，所以这里就定16个暂时。还有一种可能的考虑是将
         * capacity设置为最大可能出现的结果。
         */
        set = new HashSet<String>(16);
        for (SamplingField samplingField : cartiesianProductCalculator) {
            evalOnceAndAddToReturnSet(set, samplingField, 16);
        }

        return set;
    }

    /**
     * 没有参数的情况，参数用context应用中写入
     * @param enumeratedMap
     * @param mappingTargetColumn
     * @param mappingKeys
     * @return
     */
    private Set<String> evalZeroArgumentExpression() {
        Set<String> set;
        // 等于一个值不需要进行笛卡尔积
        List<String> columns = new ArrayList<String>(1);

        SamplingField samplingField = new SamplingField(columns, 1);

        // 返回值最多也就是与函数的x的个数相对应
        set = new HashSet<String>();

        /*
         * 如果没有前端传来的已经映射的结果，那么使用sql中获得的结果来进行运算
         */
        evalOnceAndAddToReturnSet(set, samplingField, 0);

        if ((set == null || set.isEmpty()) && ruleRequireThrowRuntimeExceptionWhenSetIsEmpty()) {
            throw new EmptySetRuntimeException();
        }
        return set;
    }

    private Set<String> evalOneArgumentExpression(Map<String, Set<Object>> enumeratedMap,
                                                  String mappingTargetColumn,
                                                  Set<Object> mappingKeys) {
        Set<String> set;
        // 等于一个值不需要进行笛卡尔积
        List<String> columns = new ArrayList<String>(1);
        Set<Object> enumeratedValues = null;
        for (Entry<String, Set<Object>> entry : enumeratedMap.entrySet()) {
            columns.add(entry.getKey());
            enumeratedValues = entry.getValue();
        }

        SamplingField samplingField = new SamplingField(columns, 1);

        // 返回值最多也就是与函数的x的个数相对应
        set = new HashSet<String>(enumeratedValues.size());

        if (mappingKeys == null) {
            /*
             * 如果没有前端传来的已经映射的结果，那么使用sql中获得的结果来进行运算
             */
            evalNormal(set, enumeratedValues, samplingField);
        } else {
            //mappingKeys 不为空，那么证明分库时已经经过了映射规则，那么先判断映射数值是否正确。
            //然后要决定到底是使用映射规则还是使用sql传入的数据，这个工作交给子类去做
            evalWithMappingKey(mappingTargetColumn, mappingKeys, set, enumeratedValues,
                samplingField);
        }

        if ((set == null || set.isEmpty()) && ruleRequireThrowRuntimeExceptionWhenSetIsEmpty()) {
            throw new EmptySetRuntimeException();
        }
        return set;
    }

    /**
     * 方法的主要作用是如果分库时已经查到映射后数据，
     * 则使用映射后数据。
     * 
     * 判别的标志是mappingKeys不为空。
     * 
     * @param mappingTargetColumn
     * @param mappingKeys
     * @param set
     * @param enumeratedValues
     * @param samplingField
     * 
     * 
     */
    private void evalWithMappingKey(String mappingTargetColumn, Set<Object> mappingKeys,
                                    Set<String> set, Set<Object> enumeratedValues,
                                    SamplingField samplingField) {
        //放入targetKey,子类可以用这个targetKey和自己的targetKey比对，得知是否和分库使用了同样的targetKey
        samplingField.setMappingTargetKey(mappingTargetColumn);
        if (mappingKeys.size() == enumeratedValues.size()) {
            Iterator<Object> itr = mappingKeys.iterator();
            for (Object value : enumeratedValues) {
                Object oneTargetKey = itr.next();
                samplingField.clear();
                samplingField.setMappingValue(oneTargetKey);
                samplingField.add(0, value);
                evalOnceAndAddToReturnSet(set, samplingField, enumeratedValues.size());
            }
        } else {
            throw new IllegalArgumentException("mapping映射后的targetKeys和输入的参数个数不等,mapping :"
                                               + mappingKeys + " " + "enumeratedValues is :"
                                               + enumeratedValues);
        }
    }

    private void evalNormal(Set<String> set, Set<Object> enumeratedValues,
                            SamplingField samplingField) {
        for (Object value : enumeratedValues) {
            samplingField.clear();
            samplingField.add(0, value);
            evalOnceAndAddToReturnSet(set, samplingField, enumeratedValues.size());
        }
    }

    /** 
     * 真正的计算过程，将列->描点带入规则引擎进行计算，获取最终结果。
     * 
     * @param enumeratedMap
     * @return 返回的map不会为null,但有可能为空的map，如果map不为空，则内部的子map必定不为空。最少会有一个值
     */
    public Map<String/* 结果的值 */, Field> evalElement(Map<String, Set<Object>> enumeratedMap) {
        Map<String/* 结果的值 */, Field> map;
        if (enumeratedMap.size() == 1) {
            // 列个数等于一个值不需要进行笛卡尔积
            List<String> columns = new ArrayList<String>(1);
            Set<Object> enumeratedValues = null;
            for (Entry<String, Set<Object>> entry : enumeratedMap.entrySet()) {
                columns.add(entry.getKey());
                enumeratedValues = entry.getValue();
            }

            SamplingField samplingField = new SamplingField(columns, 1);
            // 返回值最多也就是与函数的x的个数相对应
            map = new HashMap<String, Field>(enumeratedValues.size());
            // 为计算列赋予列名字段
            for (Object value : enumeratedValues) {
                samplingField.clear();
                samplingField.add(0, value);
                evalOnceAndAddToReturnMap(map, samplingField, enumeratedValues.size());
            }

            return map;

        }
        /**
         * 当使用GroovyThreadLocal 方式注入参数时，因为sql里没有对应的列名，而走到这里。
         * 到里面，直接根据用户在GroovyThreadLocal里的参数结合配置的规则进行计算而返回分表或分库結果
         */
        else if (enumeratedMap.size() == 0) {
            List<String> columns = new ArrayList<String>(1);
            SamplingField samplingField = new SamplingField(columns, 1);
            map = new HashMap<String, Field>(1);
            evalOnceAndAddToReturnMap(map, samplingField, 1);
            return map;

        } else {
            //TODO:默认关闭掉
            // TODO:用到多个值共同决定分库或分表的时候需要review
            // 多于一个值，需要进行笛卡尔积
            CartesianProductCalculator cartiesianProductCalculator = new CartesianProductCalculator(
                enumeratedMap);
            /*
             * 确实很难确定set的大小，但一般来说分库是16个，所以这里就定16个暂时。还有一种可能的考虑是将
             * capacity设置为最大可能出现的结果。
             */
            map = new HashMap<String, Field>(16);
            for (SamplingField samplingField : cartiesianProductCalculator) {
                evalOnceAndAddToReturnMap(map, samplingField, 16);
            }

            return map;
        }
    }

    /**
     * 如果子规则需要在返回值为null或为空collections时抛出异常，则继承此类后将false变为true即可
     * 
     * @return
     */
    protected boolean ruleRequireThrowRuntimeExceptionWhenSetIsEmpty() {
        return false;
    }

    void evalOnceAndAddToReturnSet(Set<String> set, SamplingField samplingField, int valueSetSize) {
        ResultAndMappingKey resultAndMappingKey = evalueateSamplingField(samplingField);
        String targetIndex = resultAndMappingKey.result;
        //ODOT:重复判断
        if (targetIndex != null) {
            set.add(targetIndex);
        } else {
            throw new IllegalArgumentException("规则引擎的结果不能为null");
        }
    }

    /**
     * 对一个数据进行计算
     * 走一次规则，有可能返回多个库，利用for循环遍历
     * 只有在数据计算获取了值的时候才会将对应该值获取的列和定义域内的值放入map中。
    * @param map
    * @param samplingField
    * @param valueSetSize
    * @Test 这个方法在TairBasedMappingRule的集成测试和单元测试里都有
    */
    void evalOnceAndAddToReturnMap(Map<String/* 结果的值 */, Field> map, SamplingField samplingField,
                                   int valueSetSize) {
        ResultAndMappingKey returnAndMappingKey = evalueateSamplingField(samplingField);
        if (returnAndMappingKey != null) {
            String dbIndexStr = returnAndMappingKey.result;
            if (StringUtil.isBlank(dbIndexStr)) {
                throw new IllegalArgumentException("根据dbRule计算出的结果不能为null");
            }
            String[] dbIndexes = dbIndexStr.split(",");

            for (String dbIndex : dbIndexes) {
                List<String> lists = samplingField.getColumns();
                List<Object> values = samplingField.getEnumFields();

                Field colMap = prepareColumnMap(map, samplingField, dbIndex,
                    returnAndMappingKey.mappingTargetColumn, returnAndMappingKey.mappingKey);
                int index = 0;
                for (String column : lists) {
                    Object value = values.get(index);
                    Set<Object> set = prepareEnumeratedSet(valueSetSize, colMap, column);
                    set.add(value);
                    index++;
                }
            }
        }
    }

    private Set<Object> prepareEnumeratedSet(int valueSetSize, Field colMap, String column) {
        //sourcekey 初始化以后就内部的set就一直存在
        Set<Object> set = colMap.sourceKeys.get(column);
        if (set == null) {
            set = new HashSet<Object>(valueSetSize);
            colMap.sourceKeys.put(column, set);
        }
        return set;
    }

    private Field prepareColumnMap(Map<String, Field> map, SamplingField samplingField,
                                   String targetIndex, String mappngTargetColumn,
                                   Object mappingValue) {
        Field colMap = map.get(targetIndex);
        if (colMap == null) {
            int size = samplingField.getColumns().size();
            colMap = new Field(size);
            map.put(targetIndex, colMap);
        }

        if (mappngTargetColumn != null && colMap.mappingTargetColumn == null) {
            colMap.mappingTargetColumn = mappngTargetColumn;
        }
        if (mappingValue != null) {
            if (colMap.mappingKeys == null) {
                colMap.mappingKeys = new HashSet<Object>();
            }
            colMap.mappingKeys.add(mappingValue);
        }

        return colMap;
    }

    //	public Map<String, Set<Object>/* 抽样后描点的key和值的pair */> getSamplingField(
    //			Map<String, SharedValueElement> sharedValueElementMap) {
    //		// TODO:详细注释,计算笛卡尔积
    //		// 枚举以后的columns与他们的描点之间的对应关系
    //		Map<String, Set<Object>> enumeratedMap = new HashMap<String, Set<Object>>(
    //				sharedValueElementMap.size());
    //		for (Entry<String, SharedValueElement> entry : sharedValueElementMap
    //				.entrySet()) {
    //			SharedValueElement sharedValueElement = entry.getValue();
    //			String key = entry.getKey();
    //			// 当前enumerator中指定当前规则是否需要处理交集问题。
    //			// enumerator.setNeedMergeValueInCloseInterval();
    //
    //			try {
    //				Set<Object> samplingField = enumerator.getEnumeratedValue(
    //						sharedValueElement.comp,
    //						sharedValueElement.cumulativeTimes,
    //						sharedValueElement.atomicIncreaseValue,
    //						sharedValueElement.needMergeValueInCloseInterval);
    //				enumeratedMap.put(key, samplingField);
    //			} catch (UnsupportedOperationException e) {
    //				throw new UnsupportedOperationException("当前列分库分表出现错误，出现错误的列名是:"
    //						+ entry.getKey(), e);
    //			}
    //
    //		}
    //		return enumeratedMap;
    //	}

    /**
     * 根据一组参数，计算出一个结果
     * 
     * @return 通过规则的结果可能在以下情况下为null:
     * 			映射规则原规则存在，但映射后的目标不存在，会返回null。
     *          其余时刻，会抛异常
     * 
     */
    public abstract ResultAndMappingKey evalueateSamplingField(SamplingField samplingField);

}
