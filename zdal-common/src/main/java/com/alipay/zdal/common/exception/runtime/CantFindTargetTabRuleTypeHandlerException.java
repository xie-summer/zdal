/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.runtime;

/**
 * 
 * @author 伯牙
 * @version $Id: CantFindTargetTabRuleTypeHandlerException.java, v 0.1 2014-1-6 下午05:18:30 Exp $
 */
public class CantFindTargetTabRuleTypeHandlerException extends ZdalRunTimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4073830327289870566L;

    public CantFindTargetTabRuleTypeHandlerException(String msg) {
        super("无法找到" + msg + "对应的处理器");
    }
}
