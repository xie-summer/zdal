package com.alipay.zdal.parser.druid.bvt.sql.mysql.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlSchemaStatVisitorTest3.java, v 0.1 2012-5-17 ÉÏÎç10:12:18 xiaoqing.zhouxq Exp $
 */
public class MySqlSchemaStatVisitorTest3 extends TestCase {

    public void test_0() throws Exception {
        String sql = "insert into users2 (id2, name2) select id, name FROM users where loginCount > 1";

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
        System.out.println("--------------------------------");

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("users"));
        Assert.assertEquals(true, visitor.containsTable("users2"));

        Assert.assertEquals(5, visitor.getColumns().size());
        Assert
            .assertEquals(true, visitor.getColumns().contains(new Column("users", "id")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("users", "name")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("users", "loginCount")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("users2", "name2")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("users2", "id2")));

    }

}
