package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

public class MySqlSelectGroupTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT e.employee_id FROM employees e, orders o   WHERE e.employee_id = o.sales_rep_id   GROUP BY e.employee_id; ";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	        

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
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
	    }

}
