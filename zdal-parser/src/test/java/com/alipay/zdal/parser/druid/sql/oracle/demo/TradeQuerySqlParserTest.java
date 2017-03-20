/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.parser.druid.sql.oracle.demo;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.visitor.BindVarCondition;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: TradeQuerySqlParserTest.java, v 0.1 2013-3-11 ÉÏÎç09:48:35 Exp $
 */
public class TradeQuerySqlParserTest extends OracleTest {

    public void test_0() throws Exception {
        String sql = "select TRADE_NO,EXT_INFO,TRADE_AGENT,gmt_modified,fp_time_out_rule,forex_rate_id,forex_currency,forex_total_fee,goods_inner_sell_rate,prepare_coupon_fee,prepare_coupon_list,forex_cert_no,gmt_create,auth_debit_type,support_credit_card,specified_pay_channel,pay_channels from trade_ext where trade_no = ? and gmt_create < to_date(substr(?,1,8),'yyyyMMdd')+2 and gmt_create > to_date(substr(?,1,8),'yyyyMMdd')-1";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("trade_ext"));

    }

    public void test_1() throws Exception {
        String sql = "select /*+ index(a,BYD_TRADE_FUND_BILL_TRDNO_IND) */ BILL_NO,TRADE_NO,PAY_ACCOUNT_NO,RECEIVE_ACCOUNT_NO,CURRENCY,AMOUNT,TYPE,STATUS,CHANNEL,GMT_PAY,IW_TRANS_LOG_ID,GMT_TRADE_CREATE,GMT_CREATE,GMT_MODIFIED,MEMO,BANK_PAY_ONLINE_ID,BANK_AMOUNT,BANK_TYPE,FREEZE_TYPE,PARTNER_ID,OUT_ORDER_NO,DEPOSIT_ID,BILL_EXT,PAY_ORDER_NO from trade_fund_bill a where trade_no = ? and gmt_trade_create < to_date(substr(?,1,8),'yyyyMMdd')+2 and gmt_trade_create > to_date(substr(?,1,8),'yyyyMMdd')-1  and status = ? and receive_account_no = ? and type in ('10','11','12')";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("trade_fund_bill"));
    }

    public void test_2() {
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

        OracleStatementParser parser = new OracleStatementParser(
            SELECT_DATASOURCETEMPLATE_BY_DBMODE_IDCNAME_DSTYPE_SQL);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println(SELECT_DATASOURCETEMPLATE_BY_DBMODE_IDCNAME_DSTYPE_SQL);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
    }

    public void test_3() {
        String sql = "select *  from (select * from trade_voucher where trade_no =? and refund_id = ? and UPLOAD_FLAG = 7 order by id desc )  where rownum<2";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");
    }

    public void test_4() {
        String sql = "select ID,TRADE_NO,REFUND_ID,SELLER_OR_BUYER,SELLER_UPLOAD_VOUCHER,BUYER_UPLOAD_VOUCHER,UPLOAD_FLAG,GMT_CREATE,GMT_MODIFIED,GMT_CHECK,LAST_OPERATOR,CHECK_MEMO,MEMEO,GMT_TIMEOUT_START   from (select * from trade_voucher where trade_no = ? and UPLOAD_FLAG <> 4 and UPLOAD_FLAG <> 7 order by id desc ) where rownum<2";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");
    }

    public void test_5() {
        String sql = "update timeout set user_id=?, paytype=?, end_time=?, action=?, parameter=?, status=?, action_type=?, PRIOR_LEVEL=?, gmt_modified=sysdate where ((trade_no = ?) AND (paytype = ?))     ";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");
    }

    public void test_6() {
        String sql = "select * from (select /*+ index(a)*/ ID,OUT_TRADE_NO,TRADE_NO,SELLER_ACCOUNT,SELLER_LOGIN_EMAIL,BUYER_ACCOUNT,BUYER_LOGIN_EMAIL,SELLER_TYPE,BUYER_TYPE,TRADE_FROM,TRADE_EMAIL,OPERATOR_ROLE,TRADE_STATUS,TOTAL_FEE,SERVICE_FEE_RATIO,SERVICE_FEE,CURRENCY,SELLER_ACTION,BUYER_ACTION,GMT_CREATE,SELLER_USER_ID,BUYER_USER_ID,ADDITIONAL_TRD_STATUS,TRADE_TYPE,SELLER_FULLNAME,BUYER_FULLNAME,SELLER_NICK,BUYER_NICK,GOODS_TITLE,GMT_LAST_MODIFIED_DT,STOP_TIMEOUT,GATHERING_TYPE,BUYER_MARKER,SELLER_MARKER,BUYER_MARKER_MEMO,SELLER_MARKER_MEMO,CHANNEL,PRODUCT,PAY_CHANNEL,RELATION_PRO from trade_base a where  (BUYER_ACCOUNT =?) and GMT_CREATE >= ? and GMT_CREATE <= ? union all select /*+ index(b)*/ ID,OUT_TRADE_NO,TRADE_NO,SELLER_ACCOUNT,SELLER_LOGIN_EMAIL,BUYER_ACCOUNT,BUYER_LOGIN_EMAIL,SELLER_TYPE,BUYER_TYPE,TRADE_FROM,TRADE_EMAIL,OPERATOR_ROLE,TRADE_STATUS,TOTAL_FEE,SERVICE_FEE_RATIO,SERVICE_FEE,CURRENCY,SELLER_ACTION,BUYER_ACTION,GMT_CREATE,SELLER_USER_ID,BUYER_USER_ID,ADDITIONAL_TRD_STATUS,TRADE_TYPE,SELLER_FULLNAME,BUYER_FULLNAME,SELLER_NICK,BUYER_NICK,GOODS_TITLE,GMT_LAST_MODIFIED_DT,STOP_TIMEOUT,GATHERING_TYPE,BUYER_MARKER,SELLER_MARKER,BUYER_MARKER_MEMO,SELLER_MARKER_MEMO,CHANNEL,PRODUCT,PAY_CHANNEL,RELATION_PRO from trade_base b where  (SELLER_ACCOUNT  = ?) and GMT_CREATE >= ? and GMT_CREATE <= ?)   where rownum<=?";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        Set<BindVarCondition> rownums = visitor.getRownums();
        for (BindVarCondition bindVarCondition : rownums) {
            System.out.println(bindVarCondition.getColumnName());
        }
        System.out.println("--------------------------------");
    }
}
