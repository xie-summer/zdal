/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

/**
 * 一个suffix 
 * @author liang.chenl
 *
 */
public class Suffix {

    /**
     * 以下值从tbSuffix推算出来，不需要显式配置
     */
    private int    tbNumForEachDb;       //从tbsuffix和dbIndexes个数推算出来
    private int    tbSuffixFrom    = 0;
    private int    tbSuffixTo      = -1;
    private int    tbSuffixWidth   = 4;
    private String tbSuffixPadding = "_";
    private String tbType;

    public int getTbNumForEachDb() {
        return tbNumForEachDb;
    }

    public void setTbNumForEachDb(int tbNumForEachDb) {
        this.tbNumForEachDb = tbNumForEachDb;
    }

    public int getTbSuffixFrom() {
        return tbSuffixFrom;
    }

    public void setTbSuffixFrom(int tbSuffixFrom) {
        this.tbSuffixFrom = tbSuffixFrom;
    }

    public int getTbSuffixTo() {
        return tbSuffixTo;
    }

    public void setTbSuffixTo(int tbSuffixTo) {
        this.tbSuffixTo = tbSuffixTo;
    }

    public int getTbSuffixWidth() {
        return tbSuffixWidth;
    }

    public void setTbSuffixWidth(int tbSuffixWidth) {
        this.tbSuffixWidth = tbSuffixWidth;
    }

    public String getTbSuffixPadding() {
        return tbSuffixPadding;
    }

    public void setTbSuffixPadding(String tbSuffixPadding) {
        this.tbSuffixPadding = tbSuffixPadding;
    }

    public void setTbSuffixTo(String[] dbIndexes) {
        this.tbSuffixTo = dbIndexes.length - 1 + this.tbSuffixFrom;
    }

    public String getTbType() {
        return tbType;
    }

    public void setTbType(String tbType) {
        this.tbType = tbType;
    }

}
