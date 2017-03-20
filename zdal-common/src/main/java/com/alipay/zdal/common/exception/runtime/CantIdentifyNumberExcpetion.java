/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.runtime;

/**
 * 
 * @author 伯牙
 * @version $Id: CantIdentifyNumberExcpetion.java, v 0.1 2014-1-6 下午05:18:39 Exp $
 */
public class CantIdentifyNumberExcpetion extends ZdalRunTimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7861250013675710468L;

    public CantIdentifyNumberExcpetion(String input, String input1, Throwable e) {
        super("关键字：" + input + "或：" + input1 + "不能识别为一个数，请重新设定", e);
    }
}
