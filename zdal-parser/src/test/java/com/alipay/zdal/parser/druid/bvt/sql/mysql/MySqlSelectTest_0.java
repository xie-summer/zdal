package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.MysqlTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlSelectTest_0.java, v 0.1 2012-5-17 ÉÏÎç10:06:54 xiaoqing.zhouxq Exp $
 */
public class MySqlSelectTest_0 extends MysqlTest {

    public void test_0() throws Exception {
        String sql = "SELECT CONCAT(m.last_name,', ',m.first_name) AS full_name FROM mytable as m  where  first_name between ? and ? and user>? and user in(?,?,?) ORDER BY m.full_name desc;";
        //        String sql = "insert into fin_recon_recovery(id,batch_no,batch_type,batch_index,inst_id,inst_name,finance_exchange_code,settle_serial_no,inst_serial_no,"
        //                     + "inst_date,settle_amount,settle_currency,reference_no,recover_status,recover_src,recover_desc,operator,memo,gmt_create,gmt_modified,div_db_flag)"
        //                     + "values (#id#, #batchNo#, #batchType#, #batchIndex#, #instId#, #instName#, #financeExchangeCode#, #settleSerialNo#, #instSerialNo#, #instDate#, #settleAmount.cent#,"
        //                     + "#settleCurrency#, #referenceNo#, #recoverStatus#, #recoverSrc#, #recoverDesc#, #operator#, #memo#, now(), now(), #divDbFlag#)";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println(visitor.getBindVarConditions());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(3, visitor.getColumns().size());
        Assert.assertEquals(0, visitor.getConditions().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));

        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "last_name")));
        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "first_name")));
        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "full_name")));
    }
}
