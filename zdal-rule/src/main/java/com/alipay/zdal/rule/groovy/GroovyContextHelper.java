/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.groovy;

import java.util.Map;

/**
 * groovy 规则上下文,可以支持自定义字段
 * 具体的字段可以由应用的spring注入
 * 
 * @author liang.chenl
 *
 */
public class GroovyContextHelper {
    static private Map<String, Object> context;

    static public Map<String, Object> getContext() {
        return context;
    }

    static public void setContext(Map<String, Object> context) {
        GroovyContextHelper.context = context;
    }
}
