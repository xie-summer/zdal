package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleCreateTableTest9   {
@Test
    public void test_0() throws Exception {
        String sql = //
        "CREATE GLOBAL TEMPORARY TABLE \"ESCROW\".\"RUPD$_HT_TASK_TRADE_HISTOR\" (" + //
                "\"ID\" NUMBER, " + //
                "dmltype$$ varchar2(1), " + //
                "snapid integer, " + //
                "change_vector$$ raw(255)" + //
                ") ON COMMIT PRESERVE ROWS";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.RUPD$_HT_TASK_TRADE_HISTOR")));

        Assert.assertEquals(4, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ESCROW.RUPD$_HT_TASK_TRADE_HISTOR", "ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ESCROW.RUPD$_HT_TASK_TRADE_HISTOR", "dmltype$$")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ESCROW.RUPD$_HT_TASK_TRADE_HISTOR", "snapid")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ESCROW.RUPD$_HT_TASK_TRADE_HISTOR", "change_vector$$")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
