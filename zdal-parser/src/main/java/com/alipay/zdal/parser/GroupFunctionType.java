/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

/**
 * 所支持的sql语句方法.
 * @author xiaoqing.zhouxq
 * @version $Id: GroupFunctionType.java, v 0.1 2012-5-21 下午01:59:55 xiaoqing.zhouxq Exp $
 */
public enum GroupFunctionType {
    MIN, MAX, COUNT, AVG, SUM,
    /**
     * 非以上的任何一种情况 
     */
    NORMAL
}
