package com.alipay.zdal.parser.druid.bvt.sql.mysql.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlSchemaStatVisitorTest5.java, v 0.1 2012-5-17 ÉÏÎç10:12:25 xiaoqing.zhouxq Exp $
 */
public class MySqlSchemaStatVisitorTest5 extends TestCase {

    public void test_0() throws Exception {
        //        String sql = "SELECT      distinct a.id \"id\",    a.col \"col\",     a.position \"position\",     a.panel_id \"panelId\"    "
        //                     + "FROM     (select * from view_position_info) a LEFT JOIN db1.view_portal b ON a.panel_id = b.panel_id     "
        //                     + "  LEFT JOIN (select * from view_portal_panel) c  ON a.panel_id = c.panel_id   "
        //                     + " WHERE     b.user_id = ? and     ((b.is_grid='y' and c.param_name='is_hidden' and c.param_value='false') or      b.is_grid  != 'y') and b.user_id in (select user_id from table1 where id = 1)    ORDER BY    a.col ASC, a.position ASC";

        String sql = "select columnName from table1 where id in (select id from table3 where name = ?)";
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
        System.out.println("limit : " + visitor.getLimits());
        System.out.println("--------------------------------");

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("table1"));

        Assert.assertEquals(4, visitor.getColumns().size());
        // Assert.assertEquals(true, visitor.getFields().contains(new
        // Column("users", "id")));
        // Assert.assertEquals(true, visitor.getFields().contains(new
        // Column("users", "name")));

    }

}
