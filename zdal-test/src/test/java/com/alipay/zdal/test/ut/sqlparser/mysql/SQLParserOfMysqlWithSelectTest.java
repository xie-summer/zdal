package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.common.sqljep.function.ComparativeAND;
import com.alipay.zdal.common.sqljep.function.ComparativeOR;
import com.alipay.zdal.parser.DefaultSQLParser;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.visitor.OrderByEle;

public class SQLParserOfMysqlWithSelectTest {

    private static final String   MYSQL_SELECT       = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
                                                       + " from fin_retrieval_serial"
                                                       + " where (finance_exchange_code = ? or finance_exchange_code = ?) "
                                                       + " and (gmt_biz_create > ?"
                                                       + " and gmt_biz_create < ?)"
                                                       + " and div_db_flag = ?" + " limit ?,?";

    private static final Object[] MYSQL_SELECT_ARGS  = new Object[] { "code1", "code2", "create1",
            "create2", 1, 5, 20                     };

    private static final String   MYSQL_SELECT1      = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
                                                       + " from fin_retrieval_serial"
                                                       + " where finance_exchange_code = ?"
                                                       + " and (gmt_biz_create > ?"
                                                       + " or gmt_biz_create < ?)"
                                                       + " and div_db_flag = ?" + " limit ? , ?";

    private static final Object[] MYSQL_SELECT_ARGS1 = new Object[] { "code1", "create1",
            "create2", 1, 5, 20                     };

    @Test
    public void testOrderBy() {
        String sql = "select owner_card_no,owner_customer_id,opposite_card_no,opposite_name,opposite_nick_name,consume_type,consume_title,"
                     + "consume_fee,consume_refund_fee,consume_site,consume_status,consume_refund_status,in_out,service_fee,currency,biz_type,"
                     + "biz_sub_type,biz_orig,biz_in_no,biz_out_no,biz_extra_no,biz_state,biz_props,biz_memo,biz_actions,gmt_biz_create,"
                     + "gmt_biz_modified,gmt_modified,gmt_create, owner_login_id,opposite_login_id,partner_id, product, pay_channel, "
                     + "access_channel, flag_locked, flag_refund, additional_status,gmt_receive_pay, trade_from, owner_name, owner_nick,"
                     + " var1, var2, var3, var4, var5, var6, big1,big2, big3, date1, date2, date3 "
                     + "from ps_consume_record "
                     + "where owner_card_no = ? "
                     + "and gmt_biz_create > ? "
                     + "and gmt_biz_create <=? "
                     + "ORDER BY gmt_biz_create DESC";
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(sql, DBType.MYSQL);
        System.out.println(parserResult.getGroupFuncType());
        List<OrderByEle> orderByes = parserResult.getOrderByEles();
        for (OrderByEle orderByEle : orderByes) {
            System.out.println(orderByEle.isASC());
        }
        System.out.println(parserResult.getOrderByEles());
    }

    //    @Test
    public void test() {
        //        String sql = "select  p.id, p.channel_system_id, p.out_trans_code_id, p.template_id, p.message_type, p.gmt_create, p.gmt_modified from sgw_trans_assemble_mapping p where (p.channel_system_id IN "
        //                     + "(select distinct channel_system_id from sgw_cluster_mapping where (cluster_id = ?)))";
        //        String sql = "select max(biz_type) from (select biz_type from ps_consume_record  where owner_card_no = ?   and gmt_biz_create >= ?   and gmt_biz_create <= ?   and  biz_sub_type NOT IN ( ?    ,        ?        ,        ?        ,        ?        ,        ?        ,        ?        ,        ?        ,        ?        ,        ?        )   and (var4 is null or var4 = '0'           )  ) b";
        String sql = "select count(*) from (select * from test where id in (?,?,?)) ";
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(sql, DBType.MYSQL);
        System.out.println(parserResult.getGroupFuncType());
        System.out.println(parserResult);
    }

    /**
     * 测试绑定参数时，判断单个字段的拆分规则是否正确.
     */
    //    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(MYSQL_SELECT, DBType.MYSQL);
        Assert.assertEquals("fin_retrieval_serial", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(25, parserResult.getMax(Arrays.asList(MYSQL_SELECT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(5, parserResult.getSkip(Arrays.asList(MYSQL_SELECT_ARGS)));
        Assert.assertEquals(6, parserResult.isRowCountBind());
        Assert.assertEquals(5, parserResult.isSkipBind());

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("gmt_biz_create");
        partinationSet.add("finance_exchange_code");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_SELECT_ARGS), partinationSet);
        Assert.assertEquals(2, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("gmt_biz_create")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeAND.class);
            } else if (entry.getKey().equals("finance_exchange_code")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeOR.class);
            }
        }
    }

    /**
     * 测试绑定参数是，如果拆分字段不在sql语句中，拆分规则会报错.
     */
    //    @Test
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_SELECT1, DBType.MYSQL);
        Assert.assertEquals("fin_retrieval_serial", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(25, parserResult.getMax(Arrays.asList(MYSQL_SELECT_ARGS1)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(5, parserResult.getSkip(Arrays.asList(MYSQL_SELECT_ARGS1)));
        Assert.assertEquals(5, parserResult.isRowCountBind());
        Assert.assertEquals(4, parserResult.isSkipBind());

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("gmt_biz_create");
        partinationSet.add("finance_exchange_code");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_SELECT_ARGS1), partinationSet);
        Assert.assertEquals(2, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("gmt_biz_create")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeOR.class);
            } else if (entry.getKey().equals("finance_exchange_code")) {
                Assert.assertEquals(entry.getValue().getClass(), Comparative.class);
            }
        }
    }

}
