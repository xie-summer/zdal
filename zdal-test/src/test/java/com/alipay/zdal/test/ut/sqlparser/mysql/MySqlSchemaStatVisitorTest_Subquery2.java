package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;


public class MySqlSchemaStatVisitorTest_Subquery2   {
@Test
    public void test_0() throws Exception {
        String sql = "select "
                     + "t1.owner_card_no,opposite_card_no,opposite_name,opposite_nick_name,consume_title,"
                     + "consume_fee,consume_type,t1.biz_type,biz_sub_type,biz_orig,t1.biz_in_no,biz_out_no,"
                     + "biz_extra_no,biz_state,consume_status,consume_refund_status,in_out,consume_site,"
                     + "trade_from,flag_locked,flag_refund,gmt_biz_create,delete_type,delete_time,"
                     + "delete_over_time,gmt_modify from "
                     + "(select owner_card_no,biz_in_no,biz_type from ps_consume_delete15 where owner_card_no = ?  and delete_type IN ( ?,?) ORDER BY gmt_biz_create DESC LIMIT ?, ?) "
                     + " as t1,ps_consume_delete15 t2    "
                     + " where t1.owner_card_no =t2.owner_card_no  and t1.biz_in_no=t2.biz_in_no and  t1.biz_type=t2.biz_type order by biz_type";

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());

        //        Assert.assertEquals(1, visitor.getTables().size());
        //        Assert.assertEquals(true, visitor.containsTable("users"));
        //        Assert.assertEquals(true, visitor.containsTable("groups"));

        //        Assert.assertEquals(5, visitor.getColumns().size());
        //        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "id")));
        //        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "groupId")));
        //        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "name")));
        //        Assert.assertEquals(true, visitor.getColumns().contains(new Column("groups", "id")));
        //        Assert.assertEquals(true, visitor.getColumns().contains(new Column("groups", "name")));

    }
}
