/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.rule.bean.AdvancedParameter;

/**
 * 规则的总抽象 规则由参数和表达式组成
 *
 */
public abstract class AbstractRule {
    private static final Logger      log    = Logger.getLogger(AbstractRule.class);
    /**
     * 当前规则需要用到的参数
     */
    protected Set<AdvancedParameter> parameters;

    private boolean                  inited = false;

    /**
     * 当前规则需要用到的表达式
     */
    protected String                 expression;

    /*
     * 通过分析库表结构规则智能计算叠加次数，并set到规则中，需要注意的是
     * 如果有多个值参与了一个计算式，这种分析是不准确的，这时候可以通过配置文件
     * 手动的优先设置针对每一个参与运算的参数的叠加次数。
     * 
     * 现在还没启用，因为比较复杂
     * 
     * @param cumulativeTimes
     
    public void setCumulativeTimes(int cumulativeTimes){
    	for(KeyAndAtomIncValue keyAndAtomIncValue :parameters){
    		if(keyAndAtomIncValue.cumulativeTimes == null){
    			keyAndAtomIncValue.cumulativeTimes = cumulativeTimes;
    		}
    	}
    }
    */
    protected abstract void initInternal();

    /**
     * 确保规则只初始化一次
     */
    public void initRule() {
        if (inited) {
            if (log.isDebugEnabled()) {
                log.debug("rule has inited");
            }
        } else {
            initInternal();
            inited = true;
        }
    }

    public Set<AdvancedParameter> getParameters() {
        return parameters;
    }

    /**
     * spring注入带有默认自增字段的值,会将所有值变为小写
     * 
     * @param parameters
     */
    public void setParameters(Set<String> parameters) {
        if (this.parameters == null) {
            this.parameters = new HashSet<AdvancedParameter>();
        }
        for (String str : parameters) {
            AdvancedParameter advancedParameter = getAdvancedParamByParamToken(str);
            this.parameters.add(advancedParameter);
        }
    }

    /**
     * Spring注入多个
     * @param parameters
     */
    public void setAdvancedParameter(Set<AdvancedParameter> parameters) {
        if (this.parameters == null) {
            this.parameters = new HashSet<AdvancedParameter>();
        }
        for (AdvancedParameter keyAndAtomIncValue : parameters) {
            this.parameters.add(keyAndAtomIncValue);
        }

    }

    /**
     * spring注入一个
     * @param parameter
     */
    public void setAdvancedParameter(AdvancedParameter parameter) {
        if (this.parameters == null) {
            this.parameters = new HashSet<AdvancedParameter>();
        }
        this.parameters.add(parameter);
    }

    public String getExpression() {
        return expression;
    }

    /**
     * 解析
     * col,1,7这样的字段
     * col,1_date,7
     * col = 需要分库分表的列名
     * 1，表示原子自增数。
     * 7 表示y的变化范围
     * 具体请看设计文档
     * 
     * @param paramToken
     * @return
     */
    protected AdvancedParameter getAdvancedParamByParamToken(String paramToken) {
        AdvancedParameter param = new AdvancedParameter();
        String[] paramTokens = paramToken.split(",");

        int tokenLength = paramTokens.length;
        switch (tokenLength) {
            case 1:
                param.key = paramTokens[0];
                break;
            case 3:
                param.key = paramTokens[0];
                try {
                    /*
                     * 根据tokens获取自增数据
                     * 主要有两类，第一类是数字_date...
                     * 第二类是 直接为数字的。 
                     */
                    Comparable<?> atomicIncreateValue = getIncreatementValueByString(paramTokens);
                    param.atomicIncreateValue = atomicIncreateValue;
                    param.cumulativeTimes = Integer.valueOf(paramTokens[2]);

                    if (param.atomicIncreateValue != null) {
                        param.needMergeValueInCloseInterval = true;
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("输入的参数不为Integer类型,参数为:" + paramToken, e);
                }
                break;
            default:
                throw new IllegalArgumentException("错误的参数个数，必须为1个或者3个，3个的时候为允许使用" + "枚举时的数据");
        }
        return param;
    }

    /**
     * 解析 1_date。数字_month。数字_year 。以及简单数字的几种情况。
     * 不可能是空
     * @param paramTokens
     * @return
     */
    private Comparable<?> getIncreatementValueByString(String[] paramTokens) {
        Comparable<?> atomicIncreateValue = null;
        String atomicIncreateValueField = paramTokens[1];
        String[] fields = StringUtil.split(atomicIncreateValueField, "_");
        int length = fields.length;
        switch (length) {
            //数字_日期类型
            case 2:
                int calendarFieldType = 0;
                String fieldString = StringUtil.trim(fields[1]);

                if (StringUtil.equalsIgnoreCase("date", fieldString)) {
                    calendarFieldType = Calendar.DATE;
                } else if (StringUtil.equalsIgnoreCase("month", fieldString)) {
                    calendarFieldType = Calendar.MONTH;
                } else if (StringUtil.equalsIgnoreCase("YEAR", fieldString)) {
                    calendarFieldType = Calendar.YEAR;
                }
                DateEnumerationParameter dateEP = new DateEnumerationParameter(Integer
                    .valueOf(fields[0]), calendarFieldType);
                atomicIncreateValue = dateEP;
                break;

            default:
                //默认情况下直接valueOf,走的路径和以前一样，会抛出NumberformatException时 会打异常出去。
                atomicIncreateValue = Integer.valueOf(paramTokens[1]);
                break;
        }
        return atomicIncreateValue;
    }

    public void setExpression(String expression) {
        if (expression != null)
            this.expression = expression;
    }

    /**
     * col,1,7|col1,1,7....
     * @param parameterArray
     */
    public void setParameter(String parameterArray) {
        if (parameterArray != null && parameterArray.length() != 0) {
            String[] paramArray = parameterArray.split("\\|");
            Set<String> paramSet = new HashSet<String>(Arrays.asList(paramArray));
            this.setParameters(paramSet);
        }
    }

    public void setContext(Map<String, Object> context) {
    }

}
