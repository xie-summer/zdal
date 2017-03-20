package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSelectTest18.java, v 0.1 2012-5-17 ÉÏÎç10:20:20 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest18 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "begin "
                + //
                "   select min(snap_id), max(snap_id), "
                + //
                "       to_char(max(begin_interval_time + 16/24), 'yyyy-mm-dd hh24:mi:ss'),"
                + //
                "       to_char(max(end_interval_time + 16/24), 'yyyy-mm-dd hh24:mi:ss') "
                + //
                "   into :bid, :eid, :bst, :est "
                + //
                "   from (select snap_id, begin_interval_time, end_interval_time"
                + //
                "           from v$instance a,dba_hist_snapshot b"
                + //
                "           where a.INSTANCE_NUMBER=b.INSTANCE_NUMBER and begin_interval_time >= sysdate - 150/1440"
                + //
                "           order by snap_id desc" + //
                "           )" + //
                "   where rownum < 3; " + //
                "   select 'awr_'||:bid||'_'||:eid||'.html' into :awr from dual; " + //
                "end;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("rownums : " + visitor.getRownums());

        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("v$instance")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("dba_hist_snapshot")));

        Assert.assertEquals(5, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
