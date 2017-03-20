/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.groovy;

import java.util.Map;

/**
 * groovy threadlocal context
 * 应用设置必须要清除
 * @author zuomou
 *
 */
public class GroovyThreadLocalContext {

    static private ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>();

    //TODO  增加变量的作用域

    static public Map<String, Object> getContext() {
        return context.get();
    }

    static public void setContext(Map<String, Object> context) {
        GroovyThreadLocalContext.context.set(context);
    }

}
