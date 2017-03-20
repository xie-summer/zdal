/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.enumerator;

import com.alipay.zdal.common.sqljep.function.Comparative;

public class EnumerationInterruptException extends RuntimeException {
    /**
     * 
     */
    private static final long     serialVersionUID = 1L;
    private transient Comparative comparative;

    public EnumerationInterruptException(Comparative comparative) {
        this.comparative = comparative;
    }

    public EnumerationInterruptException() {
    }

    public Comparative getComparative() {
        return comparative;
    }

    public void setComparative(Comparative comparative) {
        this.comparative = comparative;
    }

}
