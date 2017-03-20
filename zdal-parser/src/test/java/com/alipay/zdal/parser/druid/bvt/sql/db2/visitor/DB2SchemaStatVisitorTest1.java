package com.alipay.zdal.parser.druid.bvt.sql.db2.visitor;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.visitor.ZdalDB2SchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSchemaStatVisitorTest1.java, v 0.1 2012-5-17 ÉÏÎç10:23:31 xiaoqing.zhouxq Exp $
 */
public class DB2SchemaStatVisitorTest1 {

    @Test
    public void test_0() throws Exception {
        String sql = "select t.id from users t "
                     + " where t.funds_type='point' and t.gmt_set_type='R' and t.total_amount-published_amount > 500 "
                     + " and t.sysdate > nvl(t.gmt_start,t.gmt_create) and t.gmt_end  > sysdate and t.status='USE' "
                     + " order by t.gmt_create desc";

        //        String sql = "select count(*) from user;";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalDB2SchemaStatVisitor visitor = new ZdalDB2SchemaStatVisitor();
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
        System.out.println("bindConditions : " + visitor.getBindVarConditions());
        System.out.println("noBindConditions : " + visitor.getNoBindVarConditions());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("users"));

        Assert.assertEquals(9, visitor.getColumns().size());

    }

    @Test
    public void test_1() throws Exception {
        String sql = "select a.name, b.name FROM users a, usergroups b on a.groupId = b.id where a.groupID = ?";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalDB2SchemaStatVisitor visitor = new ZdalDB2SchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("users"));
        Assert.assertEquals(true, visitor.containsTable("usergroups"));

        Assert.assertEquals(4, visitor.getColumns().size());
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "groupId")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "name")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("usergroups", "id")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("usergroups", "name")));

    }
}
