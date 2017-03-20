/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.runtime;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: CantFindTargetVirtualNameException.java, v 0.1 2014-1-6 ÏÂÎç05:18:35 Exp $
 */
public class CantFindTargetVirtualNameException extends ZdalRunTimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5542737179921749267L;

    public CantFindTargetVirtualNameException(String virtualTabName) {
        super("can't find virtualTabName:" + virtualTabName);
    }
}
