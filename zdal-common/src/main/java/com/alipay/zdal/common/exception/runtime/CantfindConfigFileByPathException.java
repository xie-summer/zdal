/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.runtime;

/**
 * 
 * @author 伯牙
 * @version $Id: CantfindConfigFileByPathException.java, v 0.1 2014-1-6 下午05:18:20 Exp $
 */
public class CantfindConfigFileByPathException extends ZdalRunTimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3338684575935778495L;

    public CantfindConfigFileByPathException(String path) {
        super("无法根据path:" + path + "找到指定的xml文件");
    }
}
