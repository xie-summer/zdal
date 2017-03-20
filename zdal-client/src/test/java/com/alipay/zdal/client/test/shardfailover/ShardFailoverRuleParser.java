/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.test.shardfailover;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * 
 * @author 伯牙
 * @version $Id: ShardRuleParser.java, v 0.1 2013-12-26 下午04:10:06 Exp $
 */
public class ShardFailoverRuleParser {

    private static final int USERID_LENGTH = 18;

    /**
     * 根据uid解析分库位.
     * @param userId
     * @return
     */
    public static int parserDbIndex(String userId) {
        //由应用确定何时到主库，何时到failover库，需要与规则中的dbindex中的逻辑数据源进行匹配
        return getId(userId) / 2 * 2;
    }

    /**
     * 根据uid解析分表位.
     * @param userId
     * @return
     */
    public static String parserTbIndex(String userId) {
        return "" + getId(userId);
    }

    private static int getId(String userId) {
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("ERROR ## the userId is null");
        }
        if (userId.length() != USERID_LENGTH) {
            throw new IllegalArgumentException("ERROR ## the userId = " + userId + " must have "
                                               + USERID_LENGTH + " length");
        }
        return Integer.parseInt(userId.substring(userId.length() - 1));
    }

    public static void main(String[] args) {
        String userId = "201312268302803819";
        System.out.println(parserDbIndex(userId));
        System.out.println(parserTbIndex(userId));

        userId = "201312268302803800";
        System.out.println(parserDbIndex(userId));
        System.out.println(parserTbIndex(userId));
    }
}
