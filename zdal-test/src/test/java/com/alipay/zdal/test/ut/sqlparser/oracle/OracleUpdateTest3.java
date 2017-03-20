package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleUpdateTest3  {
@Test
    public void test_0() throws Exception {
        String sql = "update sys.col_usage$ " //
                     + "set equality_preds = equality_preds + decode(bitand(:flag,1),0,0,1)" //
                     + "    , equijoin_preds = equijoin_preds + decode(bitand(:flag,2),0,0,1)" //
                     + "    , nonequijoin_preds = nonequijoin_preds + decode(bitand(:flag,4),0,0,1)" //
                     + "    , range_preds = range_preds + decode(bitand(:flag,8),0,0,1)" //
                     + "    , like_preds = like_preds + decode(bitand(:flag,16),0,0,1)" //
                     + "    , null_preds = null_preds + decode(bitand(:flag,32),0,0,1), timestamp = :time "
                     + //
                     "where obj# = :objn and intcol# = :coln"; //

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
        System.out.println("--------------------------------");

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sys.col_usage$")));

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(9, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sys.col_usage$", "obj#")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sys.col_usage$", "intcol#")));
    }

}
