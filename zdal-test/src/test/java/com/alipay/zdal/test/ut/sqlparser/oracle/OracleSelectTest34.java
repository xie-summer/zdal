package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleSelectTest34   {
@Test
    public void test_0() throws Exception {
        String sql = //
        "select t.logistics_no, t.event_date, t.country, t.province"
                + //
                "   , t.city,t.address, t.area_code,t.received_status  "
                + //
                "from wl_tracking t  "
                + //
                "where t.logistics_no in ( "
                + //
                "   select el.logistics_no "
                + //
                "   from escrow_logistics el"
                + //
                "   where rownum <= 20"
                + //
                "       and el.gmt_send between to_date ('2011-9-1', 'yyyy-mm-dd') "
                + //
                "           and to_date ('2011-11-30 23:59:59','yyyy-mm-dd hh24:mi:ss')"
                + //
                "       and el.received_status = 'received'"
                + //
                "       and el.goods_direction = 'send_goods'"
                + //
                "       and el.country = 'US'"
                + //
                "       and el.logistics_company in ('Hongkong Post Air Mail','Hongkong Post Air Parcel','China Post Air Mail','China Post Air Parcel')"
                + "       and el.recv_status_desc is null) and t.event_date is not null order by t.logistics_no, t.event_date"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
       

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

        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("wl_tracking")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("escrow_logistics")));

        Assert.assertEquals(15, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("ESCROW_LOGISTICS", "*")));

        // Assert.assertTrue(visitor.getOrderByColumns().contains(new TableStat.Column("employees", "last_name")));
    }
}
