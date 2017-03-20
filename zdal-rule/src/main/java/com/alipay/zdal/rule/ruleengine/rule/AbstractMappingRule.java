/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alipay.zdal.rule.bean.AdvancedParameter;
import com.alipay.zdal.rule.groovy.GroovyListRuleEngine;
import com.alipay.zdal.rule.ruleengine.cartesianproductcalculator.SamplingField;

/**
 * 抽象方法，用于对对mapping的抽象。
 * 会先进行枚举，然后笛卡尔积，拿到里面的
 * 需要做映射的对象以后，调用get方法进行映射。
 * 然后将映射的结果带入targetRule进行运算
 * 
 *
 */
public abstract class AbstractMappingRule extends CartesianProductBasedListResultRule {
    //	protected CartesianProductBasedListResultRule targetRule;
    private static final Logger    logger     = Logger.getLogger(AbstractMappingRule.class);
    /**
     * 转意以后的目标规则
     */
    protected GroovyListRuleEngine targetRule = new GroovyListRuleEngine();
    /**
     * 转换后的目标列名
     */
    private String                 targetKey  = null;

    /** 
     * 测试了通过映射规则，正常返回结果和映射后数据这个testCase,对应在分库时取数据这个逻辑。
     * @see com.alipay.zdal.rule.ruleengine.rule.CartesianProductBasedListResultRule#evalueateSamplingField(com.alipay.zdal.rule.ruleengine.cartesianproductcalculator.SamplingField)
     */
    @Override
    public ResultAndMappingKey evalueateSamplingField(SamplingField samplingField) {

        List<String> columns = samplingField.getColumns();
        List<Object> enumFields = samplingField.getEnumFields();
        if (columns != null && columns.size() == 1) {
            //映射以后的数据

            Object target = null;
            if (samplingField.getMappingValue() != null
                && samplingField.getMappingTargetKey().equals(targetKey)) {
                //获取的映射值不为空，并且targetKey = targetKey.则表示数据已经被分库取出，并且正好可以被分表所使用。
                target = samplingField.getMappingValue();
            } else {
                target = get(targetKey, columns.get(0), enumFields.get(0));
            }
            if (target == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("target value is null");
                }
                return null;
            }
            Map<String/*target column*/, Object/*target value*/> argumentMap = new HashMap<String, Object>(
                1);

            argumentMap.put(targetKey, target);
            if (logger.isDebugEnabled()) {
                logger.debug("invoke target rule ,value is " + target);
            }
            //构造参数加值 ，进行查询
            String resultString = targetRule.imvokeMethod(new Object[] { argumentMap });
            ResultAndMappingKey result = null;
            if (resultString != null) {
                //返回规则结果和其对应的mapping key
                result = new ResultAndMappingKey(resultString);
                result.mappingKey = target;
                result.mappingTargetColumn = targetKey;
            } else {
                //结果为空则抛出异常，这和映射没有取到targetValue是两个层面的事情。
                throw new IllegalArgumentException("规则引擎的结果不能为null");
            }
            return result;
        } else {
            throw new IllegalStateException("列名不符要求:columns:" + columns);
        }
    }

    @Override
    protected boolean ruleRequireThrowRuntimeExceptionWhenSetIsEmpty() {
        //在mapping rule中，需要在为空串的时候抛出异常
        return true;
    }

    /**
     * 根据sourceKey和sourceValue获取 用于targerRule里的参数的targetValue
     * 
     * @param sourceKey
     * @param sourceValue
     * @return
     */
    protected abstract Object get(String targetKey, String sourceKey, Object sourceValue);

    public CartesianProductBasedListResultRule getTargetRule() {
        return targetRule;
    }

    protected void initInternal() {
        if (targetRule == null) {
            throw new IllegalArgumentException("target rule is null");
        }
        // 解析目标规则。
        targetRule.initRule();
        // 从解析后的目标规则里拿到当前参数
        Set<AdvancedParameter> advancedParameters = targetRule.getParameters();
        if (advancedParameters.size() != 1) {
            throw new IllegalArgumentException("目标规则的参数必须为1个，才能使用" + "映射规则");
        }
        // 确认参数唯一以后，取出该参数
        AdvancedParameter advancedParameter = advancedParameters.iterator().next();
        targetKey = advancedParameter.key;
        if (targetKey == null || targetKey.length() == 0) {
            throw new IllegalArgumentException("target key is null .");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parse mapping rule , target rule is ").append(targetRule).append(
            "target target key is ").append(targetKey);
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString());
        }
    }

    @Override
    /**
     * 必填项 ，映射后应该走的规则是啥
     */
    public void setExpression(String expression) {
        targetRule.setExpression(expression);
    }

}
