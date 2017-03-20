package com.alipay.zdal.parser.druid.bvt.sql.mysql.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

/**
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlSchemaStatVisitorTest4.java, v 0.1 2012-5-17 ÉÏÎç10:12:22 xiaoqing.zhouxq Exp $
 */
public class MySqlSchemaStatVisitorTest_Insert extends TestCase {

    public void test_0() throws Exception {
        // String sql = "insert into users (id,gmt_create, name) values(100,now(), 'xiaoqing.zhouxq')";
        String sql = " insert /*MS-CS-USER-GP-IDEM-INSERT-GP-LOG-IDEM*/ into cs_user_gp_idem (card_no, out_biz_no, biz_type, gmt_create) "
                     + "values (?,?,?,now())";

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
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("noBindCoumns : " + visitor.getNoBindVarConditions());
        System.out.println("limits : " + visitor.getLimits());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("cs_user_gp_idem"));

        Assert.assertEquals(4, visitor.getColumns().size());
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("cs_user_gp_idem", "out_biz_no")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("cs_user_gp_idem", "biz_type")));

    }

}
