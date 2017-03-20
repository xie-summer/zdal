package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleCreateTableTest8   {
@Test
    public void test_0() throws Exception {
        String sql = //
        "CREATE TABLE \"PRODUCT_NEW_CLUSTER_YZS_0210\" (" + //
                "\"PRODUCT_ID\" NUMBER NOT NULL ENABLE, " + //
                "\"NEW_CLUSTER_ID\" NUMBER NOT NULL ENABLE, " + //
                "\"STATUS\" VARCHAR2(1) NOT NULL ENABLE" + //
                ")  " + //
                "PCTFREE 10 " + //
                "PCTUSED 40 " + //
                "INITRANS 1 " + //
                "MAXTRANS 255 " + //
                "STORAGE(INITIAL 2624585728 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) " + //
                "TABLESPACE \"MCSHADOWTS\" LOGGING NOCOMPRESS";

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
            new TableStat.Name("PRODUCT_NEW_CLUSTER_YZS_0210")));

        Assert.assertEquals(3, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("PRODUCT_NEW_CLUSTER_YZS_0210", "PRODUCT_ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("PRODUCT_NEW_CLUSTER_YZS_0210", "NEW_CLUSTER_ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("PRODUCT_NEW_CLUSTER_YZS_0210", "STATUS")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
