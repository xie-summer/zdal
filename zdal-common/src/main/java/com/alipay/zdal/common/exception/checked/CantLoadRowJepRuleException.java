/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.checked;

/**
 * 
 * @author 伯牙
 * @version $Id: CantLoadRowJepRuleException.java, v 0.1 2014-1-6 下午05:17:59 Exp $
 */
public class CantLoadRowJepRuleException extends ZdalCheckedExcption {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1765363763147779906L;

    public CantLoadRowJepRuleException(String expression, String vtable, String parameter) {
        super("无法通过param:" + parameter + "|virtualTableName:" + vtable + "|expression:"
              + expression + "找到指定的规则判断引擎");
    }

}
