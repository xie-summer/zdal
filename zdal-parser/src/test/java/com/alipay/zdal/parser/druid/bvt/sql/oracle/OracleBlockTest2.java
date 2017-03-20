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
 * @version $Id: OracleBlockTest2.java, v 0.1 2012-5-17 ÉÏÎç10:14:53 xiaoqing.zhouxq Exp $
 */
public class OracleBlockTest2 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "declare   i integer := 0; " //
                     + "begin   "
                     + //
                     "  for c in ("
                     + //
                     "      select id "
                     + //
                     "      from wl_ship_order"
                     + //
                     "      where forwarder_service is null or status is null) "
                     + //
                     "  loop"
                     + //
                     "      update wl_ship_order"
                     + //
                     "          set forwarder_service = nvl(forwarder_service, 'UPS'), status = nvl(status, 500)"
                     + //
                     "      where id = c.id;" + //
                     "      i := i + 1;" + //
                     "      if mod(i, 100) = 0 then" + //
                     "          commit;" + //
                     "      end if;" + //
                     "  end loop;" + //
                     "  commit; " + //
                     "end;";

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

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("wl_ship_order")));

        Assert.assertEquals(4, visitor.getColumns().size());
        Assert.assertEquals(3, visitor.getConditions().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("wl_ship_order", "id")));
    }
}
