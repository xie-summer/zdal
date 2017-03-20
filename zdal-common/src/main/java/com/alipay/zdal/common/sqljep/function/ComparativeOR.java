/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.sqljep.function;

/**
 * AND节点
 * 在实际的SQL中，实际上是类似
 * [Comparative]              [comparative]
 * 			\                  /
 * 			  \				  /
 *             [ComparativeOR]
 *             
 * 类似这样的节点出现
 * 
 *
 */
public class ComparativeOR extends ComparativeBaseList {

    public ComparativeOR(int function, Comparable<?> value) {
        super(function, value);
    }

    public ComparativeOR() {
    };

    public ComparativeOR(Comparative item) {
        super(item);
    }

    public ComparativeOR(int capacity) {
        super(capacity);
    }

    @Override
    protected String getRelation() {
        return " or ";
    }
}
