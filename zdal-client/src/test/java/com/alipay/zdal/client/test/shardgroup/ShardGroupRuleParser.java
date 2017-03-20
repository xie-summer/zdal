/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.zdal.client.test.shardgroup;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ShardGroupRuleParser.java, v 0.1 2014-1-2 ÉÏÎç09:46:46 Exp $
 */
public class ShardGroupRuleParser {

    private static final int USERID_LENGTH = 18;

    public static int parserDbIndex(String userId) {
        return getId(userId) / 2;
    }

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
}
