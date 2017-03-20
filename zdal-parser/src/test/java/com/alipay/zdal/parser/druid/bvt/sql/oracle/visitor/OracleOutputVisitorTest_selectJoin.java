package com.alipay.zdal.parser.druid.bvt.sql.oracle.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleOutputVisitorTest_selectJoin.java, v 0.1 2012-5-17 ÉÏÎç10:23:10 xiaoqing.zhouxq Exp $
 */
public class OracleOutputVisitorTest_selectJoin extends TestCase {

    public void test_0() throws Exception {
        String sql = "SELECT e.salary from employee e join department d where e.depId = d.id";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("--------------------------------");

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("employee"));
        Assert.assertEquals(true, visitor.containsTable("department"));

        Assert.assertEquals(3, visitor.getColumns().size());
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("employee", "salary")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("employee", "depId")));
        Assert.assertEquals(true, visitor.getColumns().contains(
            new Column("department", "id")));

        StringBuilder buf = new StringBuilder();
        OracleOutputVisitor outputVisitor = new OracleOutputVisitor(buf);
        stmt.accept(outputVisitor);
        Assert.assertEquals(
            "SELECT e.salary\nFROM employee e\n\tJOIN department d\nWHERE e.depId = d.id;\n", buf
                .toString());

    }
}
