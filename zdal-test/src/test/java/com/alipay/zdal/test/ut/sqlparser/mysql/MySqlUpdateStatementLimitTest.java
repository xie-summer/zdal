package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;

public class MySqlUpdateStatementLimitTest {
	@Test
	 public void test_limit() {
	        //        String sql = "update t set name = 'x' where id < 100 limit 10,5";
	        String sql = "update timeout set prior_Level= prior_Level + ?, gmt_modified=? where ((job_id = ?) AND (trade_no = ?))";
	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	       

	        Assert.assertEquals(1, statementList.size());

	        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("alias : " + visitor.getAliasMap());
	        System.out.println("conditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());
	        System.out.println("groupBy : " + visitor.getGroupByColumns());
	        System.out.println("variant : " + visitor.getVariants());
	        System.out.println("relationShip : " + visitor.getRelationships());
	        System.out.println("limit : " + visitor.getLimits());
	        System.out.println("bindConditions : " + visitor.getBindVarConditions());

	        Assert.assertEquals(1, visitor.getTables().size());
	        Assert.assertEquals(4, visitor.getColumns().size());
	        Assert.assertEquals(2, visitor.getConditions().size());
	    }

}
