/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.dispatcher;

import java.util.List;
import java.util.Map;

import com.alipay.zdal.client.controller.MatcherResult;
import com.alipay.zdal.common.exception.checked.ZdalCheckedExcption;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.rule.LogicTableRule;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;

/**
 * 匹配对象用的借口，会将sql计算出的结果，参数以及规则进行匹配
 *
 */
public interface Matcher {
    /**
     * 这里SqlParserResult pr + List<Object> args还需要抽象出一个更小的对象/接口
     * 方便业务通过ThreadLocal方式绕过解析，直接指定
     */
    MatcherResult match(ComparativeMapChoicer comparativeMapChoicer, List<Object> args,
                        LogicTableRule rule) throws ZdalCheckedExcption;

    MatcherResult buildMatcherResult(Map<String, Comparative> comparativeMapDatabase,
                                     Map<String, Comparative> comparativeTable, List<TargetDB> calc);
}
