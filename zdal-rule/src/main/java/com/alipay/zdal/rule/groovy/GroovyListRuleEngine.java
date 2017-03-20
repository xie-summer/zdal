/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.groovy;

import groovy.lang.GroovyClassLoader;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alipay.zdal.rule.bean.AdvancedParameter;
import com.alipay.zdal.rule.ruleengine.cartesianproductcalculator.SamplingField;
import com.alipay.zdal.rule.ruleengine.exception.ZdalRuleCalculateException;
import com.alipay.zdal.rule.ruleengine.rule.CartesianProductBasedListResultRule;
import com.alipay.zdal.rule.ruleengine.rule.ResultAndMappingKey;

public class GroovyListRuleEngine extends CartesianProductBasedListResultRule {
    private static final Logger logger               = Logger.getLogger(GroovyListRuleEngine.class);
    private Object              ruleObj;
    private Method              m_routingRuleMap;
    private static final String IMPORT_STATIC_METHOD = "import static com.alipay.zdal.rule.groovy.staticmethod.GroovyStaticMethod.*;";

    protected void initInternal() {
        if (expression == null) {
            throw new IllegalArgumentException("未指定 expression");
        }
        GroovyClassLoader loader = AccessController
            .doPrivileged(new PrivilegedAction<GroovyClassLoader>() {
                public GroovyClassLoader run() {
                    return new GroovyClassLoader(GroovyListRuleEngine.class.getClassLoader());
                }
            });
        String groovyRule = getGroovyRule(expression);
        Class<?> c_groovy = loader.parseClass(groovyRule);

        try {
            // 新建类实例
            ruleObj = c_groovy.newInstance();
            // 获取方法
            m_routingRuleMap = getMethod(c_groovy, "eval", Map.class);
            if (m_routingRuleMap == null) {
                throw new IllegalArgumentException("规则方法没找到");
            }
            m_routingRuleMap.setAccessible(true);

        } catch (Throwable t) {
            throw new IllegalArgumentException("实例化规则对象失败", t);
        }
    }

    private static final Pattern RETURN_WHOLE_WORD_PATTERN = Pattern.compile("\\breturn\\b",
                                                               Pattern.CASE_INSENSITIVE); // 全字匹配
    private static final Pattern DOLLER_PATTERN            = Pattern.compile("#.*?#");

    // Integer.valueOf(#userIdStr#.substring(0,1),16).intdiv(8)
    protected String getGroovyRule(String expression) {
        StringBuffer sb = new StringBuffer();
        sb.append(IMPORT_STATIC_METHOD);
        Set<AdvancedParameter> params = new HashSet<AdvancedParameter>();
        Matcher matcher = DOLLER_PATTERN.matcher(expression);
        sb.append("public class RULE ").append("{");
        sb.append("public Object eval(Map map){");
        // StringBuffer sb = new StringBuffer();
        // 替换并组装advancedParameter
        int start = 0;

        Matcher returnMarcher = RETURN_WHOLE_WORD_PATTERN.matcher(expression);
        if (!returnMarcher.find()) {
            sb.append("return ");
        }

        while (matcher.find(start)) {
            String realParam = matcher.group();
            realParam = realParam.substring(1, realParam.length() - 1);
            AdvancedParameter advancedParameter = getAdvancedParamByParamToken(realParam);
            params.add(advancedParameter);
            sb.append(expression.substring(start, matcher.start()));
            sb.append("(map.get(\"");
            // 替换成(map.get("key"));
            sb.append(advancedParameter.key);
            sb.append("\"))");

            start = matcher.end();
        }
        // 设置需要用到的参数
        setAdvancedParameter(params);
        sb.append(expression.substring(start));
        sb.append(";");
        sb.append("}");
        sb.append("}");
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString());
        }
        return sb.toString();
    }

    public ResultAndMappingKey evalueateSamplingField(SamplingField samplingField) {
        List<String> columns = samplingField.getColumns();
        List<Object> values = samplingField.getEnumFields();

        int size = columns.size();
        Map<String, Object> argumentMap = new HashMap<String, Object>(size);
        for (int i = 0; i < size; i++) {
            argumentMap.put(columns.get(i), values.get(i));
        }
        // 放入应用自定义字段
        if (GroovyContextHelper.getContext() != null) {
            for (Map.Entry<String, Object> entry : GroovyContextHelper.getContext().entrySet()) {
                argumentMap.put(entry.getKey(), entry.getValue());
            }
        }
        // 放入应用threadLocal自定义字段
        if (GroovyThreadLocalContext.getContext() != null) {
            for (Map.Entry<String, Object> entry : GroovyThreadLocalContext.getContext().entrySet()) {
                argumentMap.put(entry.getKey(), entry.getValue());
            }
        }

        Object[] args = new Object[] { argumentMap };
        try {
            String result = imvokeMethod(args);
            if (result != null) {
                return new ResultAndMappingKey(result);
            } else {
                throw new IllegalArgumentException("规则引擎的结果不能为null");
            }
        } catch (Exception e) {
            throw new ZdalRuleCalculateException("规则引擎计算出错,拆分值=" + argumentMap, e);
        }
    }

    /**
     * 调用目标方法
     * 
     * @param args
     * @return
     */
    public String imvokeMethod(Object[] args) {
        Object value = invoke(ruleObj, m_routingRuleMap, args);
        String retString = null;
        if (value == null) {
            return null;
        } else {
            retString = String.valueOf(value);
            return retString;
        }
    }

    private static Method getMethod(Class<?> c, String name, Class<?>... parameterTypes) {
        try {
            return c.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            throw new IllegalArgumentException("实例化规则对象失败", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("没有这个方法" + name, e);
        }
    }

    private static Object invoke(Object obj, Method m, Object... args) {
        try {
            return m.invoke(obj, args);
        } catch (Throwable t) {
            // logger.warn("调用方法：" + m + "失败", t);
            // return null;
            throw new IllegalArgumentException("调用方法失败: " + m, t);
        }
    }

    @Override
    public String toString() {
        return "GroovyListRuleEngine [expression=" + expression + ", parameters=" + parameters
               + "]";
    }

}
