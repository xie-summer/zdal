package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.DefaultSQLParser;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;

public class SQLParserOfOracleWithDeleteTest {
    private static final String   ORACLE_DELETE        = "delete from  users  where c3 = ? and  c4='xiaoqing.zhouxq'";

    private static final Object[] ORACLE_DELETE_ARGS   = new Object[] { 100 };

    private static final String   ORACLE_DELETE_NOBIND = "delete from  users  where c3 = 300 and  c4='xiaoqing.zhouxq'";

    private static final String   PATITION_NAME        = "c3";

    /**
     * 测试绑定参数时，判断拆分规则是否正确.
     */
    //    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_DELETE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_DELETE_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals(100, entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数是，如果拆分字段不在sql语句中，拆分规则会报错.
     */
    //    @Test(expected = SqlParserException.class)
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_DELETE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME + 1);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_DELETE_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals(100, entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数时，判断多个字段的拆分规则是否正确.
     */
    //    @Test
    public void testParserWithMultiPartinations() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_DELETE, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(ORACLE_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(ORACLE_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("c3");
        partinationSet.add("c4");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_DELETE_ARGS), partinationSet);

        Assert.assertEquals(2, patitions.size());

        Comparative idCompa = patitions.get("c3");
        Assert.assertEquals(Comparative.Equivalent, idCompa.getComparison());
        Assert.assertEquals(100, idCompa.getValue());

        Comparative nameCompa = patitions.get("c4");
        Assert.assertEquals(Comparative.Equivalent, nameCompa.getComparison());
        Assert.assertEquals("xiaoqing.zhouxq", nameCompa.getValue());
    }

    /**
     * 测试非绑定参数时，判断单个字段的拆分规则是否正确.
     */
    //    @Test
    public void testParserWithNoBindPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(ORACLE_DELETE_NOBIND, DBType.ORACLE);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(null));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(300, entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }

    /**
     * 测试非绑定参数时，判断单个字段的拆分规则是否正确.
     */
    @Test
    public void testParserWithNoBindPartination1() {
        SQLParser sqlParser = new DefaultSQLParser();
        String sql = "delete from GP_CLIENTBILL_00_00 "
                     + "where bill_no in ('2012010100013000000000000001','2012010100013000000000000002','2012010100013000000000000003','2012010100013000000000000004') "
                     + "or event_no in ('2012010100013000000000000001','2012010100013000000000000002','2012010100013000000000000003') "
                     + "or relative_no in ('2012010100013000000000000001') "
                     + "or (service_type='TRADE' and service_target in ('2012010100013000000000000001','r2','r3','r4')) "
                     + "or rate_id='3255691'";
        SqlParserResult parserResult = sqlParser.parse(sql, DBType.ORACLE);
        //        Assert.assertEquals("users", parserResult.getTableName());
        //        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        //        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        //        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult
        //            .getMax(null));
        //        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        //        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult
        //            .getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("EVENT_NO");
        //        partinationSet.add("MAPPING_NO");
        //        partinationSet.add("no");
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        //        Assert.assertEquals(1, partitions.size());
        //        for (Entry<String, Comparative> entry : partitions.entrySet()) {
        //            Assert.assertEquals(PATITION_NAME, entry.getKey());
        //            Assert.assertEquals(300, entry.getValue().getValue());
        //            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        //        }
    }
}
