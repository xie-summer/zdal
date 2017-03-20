/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.Map;

import com.alipay.zdal.client.RouteCondition;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.result.SqlParserResult;

/**
 * 走规则引擎的条件表达式
 *
 */
public interface RuleRouteCondition extends RouteCondition {
    /**
     * 兼容老实现
     * @return
     */
    public Map<String, Comparative> getParameters();

    public SqlParserResult getSqlParserResult();
}
