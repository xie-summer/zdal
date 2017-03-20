package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSelectTest14.java, v 0.1 2012-5-17 ÉÏÎç10:20:05 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest14 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "select /* EXEC_FROM_DBMS_XPLAN */ case when upper(sql_text) like '%DBMS_XPLAN%' then 0 else 1 end case, SQL_ID, child_number"
                     + " from v$sql where SQL_ID ='90jx354zd6jx5' and child_number =0"; //

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

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("v$sql")));

        Assert.assertEquals(3, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("v$sql", "sql_text")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("v$sql", "SQL_ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("v$sql", "child_number")));
    }
}
