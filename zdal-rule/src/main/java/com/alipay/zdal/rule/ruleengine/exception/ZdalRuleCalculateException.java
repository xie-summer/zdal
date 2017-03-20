/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.exception;

public class ZdalRuleCalculateException extends RuntimeException {

    private static final long serialVersionUID = -1120481970898678244L;

    public ZdalRuleCalculateException() {
        super();
    }

    public ZdalRuleCalculateException(String message) {
        super(message);
    }

    public ZdalRuleCalculateException(String message, Throwable cause) {
        super(message, cause);
    }
}
