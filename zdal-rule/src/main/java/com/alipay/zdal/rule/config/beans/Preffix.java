/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

/**
 * 一个前辍
 *
 */
public class Preffix {

    private String tbPreffix = "_";
    private String tbType;

    public Preffix() {
    }

    public Preffix(String pre) {
        tbPreffix = pre;
    }

    public String getTbPreffix() {
        return tbPreffix;
    }

    public void setTbPreffix(String tbPreffix) {
        this.tbPreffix = tbPreffix;
    }

    public String getTbType() {
        return tbType;
    }

    public void setTbType(String tbType) {
        this.tbType = tbType;
    }

}
