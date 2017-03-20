/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

/**
 * 专门用于标识需要在结果值计算为空的情况下，让TStatement里面getExecutionContext直接忽略
 * 未找到库表，返回空结果集的一个标志性异常。
 * 
 * 作用于InterruptedException有点相似。
 * 
 *
 */
public class EmptySetRuntimeException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6084485716630722062L;

    public EmptySetRuntimeException() {
        super();
    }
}
