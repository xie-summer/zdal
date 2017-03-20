/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher;

/**
 * 执行计划抽象。
 * 
 * 专门为了判断这条sql的执行属性而设计。
 *
 */
public enum EXECUTE_PLAN {
    SINGLE(1), MULTIPLE(2), NONE(0);
    private int i;

    private EXECUTE_PLAN(int i) {
        this.i = i;
    }

    public int value() {
        return this.i;
    }

    public static EXECUTE_PLAN valueOf(int i) {
        for (EXECUTE_PLAN p : values()) {
            if (p.value() == i) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid Execute_plan" + i);
    }
}
