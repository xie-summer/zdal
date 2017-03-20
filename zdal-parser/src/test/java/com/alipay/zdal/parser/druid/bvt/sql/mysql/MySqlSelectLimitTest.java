package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.MysqlTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlUpdateStatementLimitTest.java, v 0.1 2012-5-17 ÉÏÎç10:07:20 xiaoqing.zhouxq Exp $
 */
public class MySqlSelectLimitTest extends MysqlTest {
    public void test_limit() {
        String sql = "select t1.owner_card_no,opposite_card_no,opposite_name,opposite_nick_name,consume_title,consume_fee,consume_type,t1.biz_type,biz_sub_type,biz_orig,t1.biz_in_no,biz_out_no,biz_extra_no,biz_state,consume_status,consume_refund_status,in_out,consume_site,trade_from,flag_locked,flag_refund,gmt_biz_create,delete_type,delete_time,delete_over_time,gmt_modify "
                     + "from ("
                     + "select owner_card_no,biz_in_no,biz_type "
                     + "from ps_consume_delete15 where owner_card_no = ? and delete_type IN (?,?)  ORDER BY gmt_biz_create DESC LIMIT ?, ?"
                     + ") "
                     + " as t1,ps_consume_delete15 t2 where t1.owner_card_no =t2.owner_card_no  and t1.biz_in_no=t2.biz_in_no and t1.biz_type=t2.biz_type";

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
        System.out.println("limit : " + visitor.getLimits());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(26, visitor.getColumns().size());
        Assert.assertEquals(4, visitor.getConditions().size());
    }
}
