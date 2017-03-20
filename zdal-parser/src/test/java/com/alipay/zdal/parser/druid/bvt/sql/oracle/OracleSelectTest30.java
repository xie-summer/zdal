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
 * @version $Id: OracleSelectTest30.java, v 0.1 2012-5-17 ÉÏÎç10:21:09 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest30 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "SELECT "
                + //
                "/* OPT_DYN_SAMP */ "
                + //
                "/*+ ALL_ROWS IGNORE_WHERE_CLAUSE NO_PARALLEL(SAMPLESUB) opt_param('parallel_execution_enabled', 'false') NO_PARALLEL_INDEX(SAMPLESUB) NO_SQL_TUNE */ " //
                + "NVL(SUM(C1),:\"SYS_B_0\"), NVL(SUM(C2),:\"SYS_B_1\") "
                + //
                "FROM (SELECT "
                + //
                "   /*+ NO_PARALLEL(\"IPAY_ACCOUNT_FUND_RCD\") FULL(\"IPAY_ACCOUNT_FUND_RCD\") NO_PARALLEL_INDEX(\"IPAY_ACCOUNT_FUND_RCD\") */ "
                + //
                ":\"SYS_B_2\" AS C1, :\"SYS_B_3\" AS C2 FROM \"ESCROW\".\"IPAY_ACCOUNT_FUND_RCD\" SAMPLE BLOCK (:\"SYS_B_4\" , :\"SYS_B_5\") SEED (:\"SYS_B_6\") \"IPAY_ACCOUNT_FUND_RCD\") SAMPLESUB";

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

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.IPAY_ACCOUNT_FUND_RCD")));

        //        Assert.assertEquals(0, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
