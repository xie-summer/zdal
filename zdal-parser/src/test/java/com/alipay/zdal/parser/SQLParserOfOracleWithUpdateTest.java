/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserOfOracleWithUpdateTest.java, v 0.1 2012-5-29 上午10:44:19 xiaoqing.zhouxq Exp $
 */
public class SQLParserOfOracleWithUpdateTest {
    private static final String   ORACLE_UPDATE        = "update users set c1 = ?, c2 = 'nihao' where c3 = ? and  c4=? AND ROWNUM<=?";

    private static final Object[] ORACLE_UPDATE_ARGS   = new Object[] { 100, "zhouxiaoqing",
            "xuanxuan", 13                            };

    private static final String   ORACLE_UPDATE_NOBIND = "update users set c1 = 100, c2 = 'nihao' where c3 = 'zhouxiaoqing' and  c4='xuanxuan' AND ROWNUM<=13";

    private static final String   PATITION_NAME        = "c3";

    /**
     * 测试绑定参数时，判断拆分规则是否正确.
     */
    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_UPDATE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(13, parserResult.getMax(Arrays.asList(ORACLE_UPDATE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_UPDATE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_UPDATE_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数是，如果拆分字段不在sql语句中，拆分规则会报错.
     */
    @Test(expected = SqlParserException.class)
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_UPDATE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(13, parserResult.getMax(Arrays.asList(ORACLE_UPDATE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_UPDATE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME + 1);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_UPDATE_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数时，判断多个字段的拆分规则是否正确.
     */
    @Test
    public void testParserWithMultiPartinations() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_UPDATE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(13, parserResult.getMax(Arrays.asList(ORACLE_UPDATE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_UPDATE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("c3");
        partinationSet.add("c4");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_UPDATE_ARGS), partinationSet);

        Assert.assertEquals(2, patitions.size());

        Comparative idCompa = patitions.get("c3");
        Assert.assertEquals(Comparative.Equivalent, idCompa.getComparison());
        Assert.assertEquals("zhouxiaoqing", idCompa.getValue());

        Comparative nameCompa = patitions.get("c4");
        Assert.assertEquals(Comparative.Equivalent, nameCompa.getComparison());
        Assert.assertEquals("xuanxuan", nameCompa.getValue());
    }

    /**
     * 测试非绑定参数时，判断单个字段的拆分规则是否正确.
     */
    @Test
    public void testParserWithNoBindPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_UPDATE_NOBIND, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(13, parserResult.getMax(null));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }
}
