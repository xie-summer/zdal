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
import com.alipay.zdal.common.sqljep.function.ComparativeAND;
import com.alipay.zdal.common.sqljep.function.ComparativeOR;
import com.alipay.zdal.parser.DefaultSQLParser;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.result.SqlParserResult;

public class SQLParserOfOracleWithSelectTest {
    //    private static final String   ORACLE_SELECT       = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
    //                                                        + " from fin_retrieval_serial"
    //                                                        + " where (finance_exchange_code = ? or finance_exchange_code = ?)"
    //                                                        + " and (gmt_biz_create > ?"
    //                                                        + " and gmt_biz_create < ?)"
    //                                                        + " and div_db_flag = ?"
    //                                                        + " and rownum>? and rownum<?";

    private static final String   ORACLE_SELECT       = "   select  * from trade_base a where (trade_no = ?)          and gmt_create < to_date(substr(?,1,8),'yyyyMMdd')+2           and gmt_create > to_date(substr(?,1,8),'yyyyMMdd')-1  ";

    private static final Object[] ORACLE_SELECT_ARGS  = new Object[] { "code1", "code2", "create1",
            "create2", 1, 5, 20                      };

    private static final String   ORACLE_SELECT1      = "select inst_id, finance_exchange_code, reference_no, div_db_flag, gmt_biz_create, gmt_biz_modified, gmt_create, gmt_modified"
                                                        + " from fin_retrieval_serial"
                                                        + " where finance_exchange_code = ? "
                                                        + " and (gmt_biz_create > ?"
                                                        + " and gmt_biz_create < ?)"
                                                        + " and div_db_flag = ?"
                                                        + " and rownum>? and rownum<?";

    private static final Object[] ORACLE_SELECT_ARGS1 = new Object[] { "code1", "create1",
            "create2", 1, 5, 20                      };

    /**
     * 测试绑定参数时，判断单个字段的拆分规则是否正确.
     */
    @Test
    public void testParseWithTwoPartination() {
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(ORACLE_SELECT, DBType.ORACLE);
        Assert.assertEquals("trade_base", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        //Assert.assertEquals(19, parserResult.getMax(Arrays.asList(ORACLE_SELECT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        //Assert.assertEquals(5, parserResult.getSkip(Arrays.asList(ORACLE_SELECT_ARGS)));
        // Assert.assertEquals(6, parserResult.isRowCountBind());
        //Assert.assertEquals(5, parserResult.isSkipBind());

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("gmt_biz_create");
        partinationSet.add("finance_exchange_code");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_SELECT_ARGS), partinationSet);
        Assert.assertEquals(0, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("gmt_biz_create")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeAND.class);
            } else if (entry.getKey().equals("finance_exchange_code")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeOR.class);
            }
        }
    }

    @Test
    public void testParseWithOnePartination() {
        SQLParser sqlParser = new DefaultSQLParser();

        SqlParserResult parserResult = sqlParser.parse(ORACLE_SELECT1, DBType.ORACLE);
        Assert.assertEquals("fin_retrieval_serial", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(19, parserResult.getMax(Arrays.asList(ORACLE_SELECT_ARGS1)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(5, parserResult.getSkip(Arrays.asList(ORACLE_SELECT_ARGS1)));
        Assert.assertEquals(5, parserResult.isRowCountBind());
        Assert.assertEquals(4, parserResult.isSkipBind());

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("gmt_biz_create");
        partinationSet.add("finance_exchange_code");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(ORACLE_SELECT_ARGS1), partinationSet);
        Assert.assertEquals(2, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            if (entry.getKey().equals("gmt_biz_create")) {
                Assert.assertEquals(entry.getValue().getClass(), ComparativeAND.class);
            } else if (entry.getKey().equals("finance_exchange_code")) {
                Assert.assertEquals(entry.getValue().getClass(), Comparative.class);
            }
        }
    }

    @Test
    public void test1() {
        //        String sql = "DELETE FROM GP_CONSULT_EVENT WHERE CONSULT_NO = '2013050900013001400410008122'";
        String sql = "SELECT "
                     + "a.ID, a.TRADE_NO, a.LOGISTICS_ID, a.TRANSPORT_TYPE, a.TRANSPORT_FEE, a.LOGISTICS_STATUS, a.LOGISTICS_MEMO,"
                     + "a.TRADE_OR_REFUND_FLAG, a.LOGISTICS_NAME, a.INVOICE_NO, a.SIGN_VOUCHER_NO, a.TRANSPORT_PAYMENT,"
                     + "a.TRANSPORT_TYPE_SEND, a.LOGISTICS_NO, a.TRANS_GOODS_INFO, a.REC_ADDRESS_NO, a.FLAG_MAIN,"
                     + " a.GMT_CREATE, a.GMT_TRANSPORT, a.GMT_SIGN, a.GMT_MODIFIED,"
                     + " b.RECEIVE_NAME as RECEIVE_FULLNAME, b.PHONE as RECEIVE_PHONE, b.MOBILE_PHONE as RECEIVE_MOBILE_PHONE,"
                     + " b.ADDRESS as RECEIVE_ADDRESS, b.RECEIVE_POST, b.ADDRESS_CODE as RECEIVE_ADDRESS_CODE "
                     + " FROM    trade_logistics a, " + " trade_receive_address b "
                     + " WHERE   b.trade_no = a.trade_no "
                     + " AND b.address_no = a.rec_address_no "
                     + " AND (a.trade_or_refund_flag = 1) " + " AND (a.flag_main = 1) "
                     + " AND a.trade_no = ?";
        Object[] objects = new Object[] { 1, 2, 3, 4 };
        SQLParser parser = new DefaultSQLParser();
        SqlParserResult parserResult = parser.parse(sql, DBType.ORACLE);
        // parserResult.getTableName();
        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("trade_no");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(objects), partinationSet);
        System.out.println();
    }

    @Test
    public void test2() {
        Object[] args = new Object[] { 1, 2, 2 };
        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("trade_no");
        SQLParser sqlParser = new DefaultSQLParser();
        String sql = " select /*+ index(t, TRADE_GOODS_INFO_TNO_IND) */ ID,TRADE_NO,GOODS_ID,GOODS_TITLE,GOODS_BID,GOODS_QUANTITY,DETAIL_URL,GOODS_MEMO,REFER_URL,PARTNER,GMT_CREATE,OTHER_FEE,CATEGORY,PROMOTION_DESC,PIC_URL from trade_goods_info t where  (trade_no=? or trade_no=?) and rownum <= ?    ";
        SqlParserResult parserResult = sqlParser.parse(sql, DBType.ORACLE);
        Assert.assertEquals("trade_goods_info", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(2, parserResult.getMax(Arrays.asList(args)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(2, parserResult.isRowCountBind());

        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(args), partinationSet);
        System.out.println(patitions);
    }

    @Test
    public void test3() {
        Object[] args = new Object[] { 1, 2, 3, 4, 5 };
        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("dbmode");
        SQLParser sqlParser = new DefaultSQLParser();
        String SELECT_DATASOURCETEMPLATE_BY_DBMODE_IDCNAME_DSTYPE_SQL = "select * from "
                                                                        + "("
                                                                        + " select a.*,rownum row_num from"
                                                                        + "("
                                                                        + "select id,name,ds_type,db_type,jdbcurl,username,password,conn_min,conn_max,"
                                                                        + "driver_class, blockingTimeoutMillis,idleTimeoutMinutes,"
                                                                        + "preStatCacheSize,queryTimeout, sqlValve,transactionValve,"
                                                                        + "tableValve,creator,create_time,dbmode,idcName from app_ds_template "
                                                                        + "where id in"
                                                                        + "("
                                                                        + "select max(id) from app_ds_template "
                                                                        + "where dbmode=? and idcName = ? and db_type = ? "
                                                                        + "group by(dbmode,idcName,db_type,name)"
                                                                        + ")" + ")"
                                                                        + "a where rownum<=?" + ")"
                                                                        + "b where b.row_num >?";

        SqlParserResult parserResult = sqlParser.parse(
            SELECT_DATASOURCETEMPLATE_BY_DBMODE_IDCNAME_DSTYPE_SQL, DBType.ORACLE);
        Assert.assertEquals("app_ds_template", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        // Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(4, parserResult.getMax(Arrays.asList(args)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(-1000, parserResult.getSkip(Arrays.asList(args)));
        Assert.assertEquals(3, parserResult.isRowCountBind());
        Assert.assertEquals(-1, parserResult.isSkipBind());

        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(args), partinationSet);
        System.out.println(patitions);
    }
}
