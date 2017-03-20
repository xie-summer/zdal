package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSelectTest36.java, v 0.1 2012-5-17 ÉÏÎç10:21:31 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest36 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "select ID,name from druid_test where (name>=? or name is null) and card_id<?"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        {
            SQLSelect select = ((SQLSelectStatement) statemen).getSelect();
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) select.getQuery();
            SQLBinaryOpExpr where = (SQLBinaryOpExpr) queryBlock.getWhere();
            Assert.assertEquals(SQLBinaryOperator.BooleanAnd, where.getOperator());

            SQLBinaryOpExpr left = (SQLBinaryOpExpr) where.getLeft();
            Assert.assertEquals(SQLBinaryOperator.BooleanOr, left.getOperator());

            SQLBinaryOpExpr nameGTEQ = (SQLBinaryOpExpr) left.getLeft();
            Assert.assertEquals(SQLBinaryOperator.GreaterThanOrEqual, nameGTEQ.getOperator());

            SQLBinaryOpExpr nameIS = (SQLBinaryOpExpr) left.getRight();
            Assert.assertEquals(SQLBinaryOperator.Is, nameIS.getOperator());
        }

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

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("druid_test")));

        Assert.assertEquals(3, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("druid_test", "ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("druid_test", "name")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("druid_test", "card_id")));

        // Assert.assertTrue(visitor.getOrderByColumns().contains(new TableStat.Column("employees", "last_name")));
    }
}
