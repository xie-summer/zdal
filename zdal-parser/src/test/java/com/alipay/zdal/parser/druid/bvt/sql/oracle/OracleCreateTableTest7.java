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
 * @version $Id: OracleCreateTableTest7.java, v 0.1 2012-5-17 ÉÏÎç10:16:20 xiaoqing.zhouxq Exp $
 */
public class OracleCreateTableTest7 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "create table \"ALIBABA1949\".\"SYS_JOURNAL_209051\" ("
                + //
                "C0 VARCHAR2(256),  opcode char(1), partno number,  rid rowid, primary key( C0 , rid )"
                + //
                ") organization index TABLESPACE \"APPINDX1M\"";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

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
            new TableStat.Name("ALIBABA1949.SYS_JOURNAL_209051")));

        Assert.assertEquals(4, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ALIBABA1949.SYS_JOURNAL_209051", "C0")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ALIBABA1949.SYS_JOURNAL_209051", "opcode")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ALIBABA1949.SYS_JOURNAL_209051", "partno")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ALIBABA1949.SYS_JOURNAL_209051", "rid")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
