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
import com.alipay.zdal.common.sqljep.function.ComparativeOR;
import com.alipay.zdal.parser.DefaultSQLParser;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.result.SqlParserResult;

public class SQLParserWithSelectInTest {

    private static final String   MYSQL_SELECT      = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
                                                      + " from fin_retrieval_serial"
                                                      + " where inst_id in (?,?,?,?)";

    private static final Object[] MYSQL_SELECT_ARGS = new Object[] { "code1", "code2", "create1",
            "create2"                              };

    private static final String   MYSQL_SELECT1     = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
                                                      + " from fin_retrieval_serial"
                                                      + " where inst_id in ('code1','code2','code3','code4')";

    /**
     * 测试绑定参数时,in里面带多个参数.
     */
    @Test
    public void test1() {
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(MYSQL_SELECT, DBType.MYSQL);
        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("inst_id");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_SELECT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("inst_id")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeOR.class);
            }
        }
    }

    /**
     * 测试非绑定参数，in里面带多个参数.
     */
    @Test
    public void test2() {
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(MYSQL_SELECT1, DBType.MYSQL);
        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("inst_id");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_SELECT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("inst_id")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeOR.class);
            }
        }
    }

}
