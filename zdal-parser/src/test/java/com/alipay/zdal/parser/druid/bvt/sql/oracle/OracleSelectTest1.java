package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSelectTest1.java, v 0.1 2012-5-17 ÉÏÎç10:19:47 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest1 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "insert into iw_account_log   (iw_account_log_id, trans_log_id,   trans_date, out_date, trans_dt,   trans_code, trans_amount, balance, trans_currency, account_type,   trans_account,   other_account_type, other_account,   trans_institution, trans_out_order_no, bank_name, trans_memo,   sub_trans_code,   out_biz_no, model, suite_id, is_write_off,   is_red, other_account_log, delete_flag, voucher_id,   orgi_voucher_id,   trans_operator,inst_channel_api,gmt_create,gmt_modified)   values   (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("--------------------------------");

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("orders")));

        //        Assert.assertEquals(0, visitor.getTables().size());
        //        Assert.assertEquals(1, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "employee_id")));
    }

}
