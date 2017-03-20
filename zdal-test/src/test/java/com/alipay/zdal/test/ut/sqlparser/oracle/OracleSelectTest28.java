package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleSelectTest28   {
@Test
    public void test_0() throws Exception {
        String sql = //
        "SELECT /*+ ORDERED USE_NL ( \"A1 \") USE_NL ( \"A2 \") USE_NL ( \"A3 \") */  \"A3 \". \"AP_PAY_TIME \", \"A2 \". \"ORDER_ID \", \"A3 \". \"AP_PAY_AMT \", \"A1 \". \"COUNTRY \" FROM  \"ESCROW \". \"TRADE_PAY \"  \"A3 \", \"ESCROW \". \"ESCROW_TRADE \"  \"A2 \", \"ESCROW \". \"BUSINESS_ORDER \"  \"A1 \" WHERE  \"A3 \". \"TRADE_ID \"= \"A2 \". \"ID \" AND  \"A1 \". \"ID \"(+)=TO_NUMBER( \"A2 \". \"OUT_ORDER_ID \") AND  \"A2 \". \"ORDER_FROM \"='wholesale_order' AND  \"A3 \". \"AP_PAY_TIME \">=:1-.003819444444444444444444444444444444444444 AND  \"A3 \". \"AP_PAY_TIME \">=TRUNC(:2)";

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

        Assert.assertEquals(3, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("ESCROW.TRADE_PAY")));
        Assert.assertTrue(visitor.getTables()
            .containsKey(new TableStat.Name("ESCROW.ESCROW_TRADE")));
        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.BUSINESS_ORDER")));

        Assert.assertEquals(9, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
