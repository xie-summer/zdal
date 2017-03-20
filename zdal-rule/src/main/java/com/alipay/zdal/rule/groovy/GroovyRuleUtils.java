/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.groovy;

import java.util.Map;
import java.util.Map.Entry;

public class GroovyRuleUtils {
    public static final String RULE_CONTEXT         = "context";
    public static final String IMPORT_STATIC_METHOD = "import static com.alipay.zdal.rule.groovy.staticmethod.GroovyStaticMethod.*;";

    protected static String buildArgumentsOutput(Map<Object, Object> var) {
        StringBuilder sb = new StringBuilder();
        if (var == null) {
            return "do not have variable";
        }
        for (Entry<Object, Object> entry : var.entrySet()) {
            sb.append("[").append(entry.getKey()).append("=").append(entry.getValue()).append(
                "|type:").append(entry.getValue() == null ? null : entry.getValue().getClass())
                .append("]");
        }
        return sb.toString();
    }
}
