package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Assert;

import com.alipay.zdal.test.ut.sqlparser.mysql.MysqlTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;;
public class MySqlSelectTest_0 extends MysqlTest {
	
	 public void test_0() throws Exception {
	        String sql = "SELECT CONCAT(last_name,', ',first_name) AS full_name FROM mytable ORDER BY full_name;";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	      

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("coditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());
	        
	        Assert.assertEquals(1, visitor.getTables().size());
	        Assert.assertEquals(3, visitor.getColumns().size());
	        Assert.assertEquals(0, visitor.getConditions().size());

	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));

	        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "last_name")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "first_name")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "full_name")));
	    }
	 
	  public void test_1() throws Exception {
	        String sql = "SELECT t1.name, t2.salary FROM employee t1, info t2  WHERE t1.name = t2.name;";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	        print(statementList);

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("coditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());

	        Assert.assertEquals(2, visitor.getTables().size());
	        Assert.assertEquals(3, visitor.getColumns().size());
	        Assert.assertEquals(2, visitor.getConditions().size());

	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employee")));
	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("info")));

	        Assert.assertTrue(visitor.getColumns().contains(new Column("employee", "name")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("info", "name")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("info", "salary")));
	    }
	  
	   public void test_2() throws Exception {
	        String sql = "SELECT college, region, seed FROM tournament ORDER BY 2, 3;";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	        print(statementList);

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("coditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());
	        
	        Assert.assertEquals(1, visitor.getTables().size());
	        Assert.assertEquals(3, visitor.getColumns().size());
	        Assert.assertEquals(0, visitor.getConditions().size());

	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("tournament")));

	        Assert.assertTrue(visitor.getColumns().contains(new Column("tournament", "college")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("tournament", "region")));
	        Assert.assertTrue(visitor.getColumns().contains(new Column("tournament", "seed")));
	    }
	   
	   public void test_3() throws Exception {
	        String sql = "SELECT 'Monty!' REGEXP '.*'";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	        print(statementList);

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("coditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());
	        
	        Assert.assertEquals(0, visitor.getTables().size());
	        Assert.assertEquals(0, visitor.getColumns().size());
	        Assert.assertEquals(0, visitor.getConditions().size());

//	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));
	    }
	   
	   public void test_4() throws Exception {
	        String sql = "SELECT 'Monty!' NOT REGEXP '.*'";

	        MySqlStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> statementList = parser.parseStatementList();
	        SQLStatement statemen = statementList.get(0);
	        print(statementList);

	        Assert.assertEquals(1, statementList.size());

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        statemen.accept(visitor);

	        System.out.println("Tables : " + visitor.getTables());
	        System.out.println("fields : " + visitor.getColumns());
	        System.out.println("coditions : " + visitor.getConditions());
	        System.out.println("orderBy : " + visitor.getOrderByColumns());
	        
	        Assert.assertEquals(0, visitor.getTables().size());
	        Assert.assertEquals(0, visitor.getColumns().size());
	        Assert.assertEquals(0, visitor.getConditions().size());

//	        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));
	    }
	   
	

		    public void test_5() throws Exception {
		        String sql = "SELECT 'Monty!' RLIKE '.*'";

		        MySqlStatementParser parser = new MySqlStatementParser(sql);
		        List<SQLStatement> statementList = parser.parseStatementList();
		        SQLStatement statemen = statementList.get(0);
		        print(statementList);

		        Assert.assertEquals(1, statementList.size());

		        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		        statemen.accept(visitor);

		        System.out.println("Tables : " + visitor.getTables());
		        System.out.println("fields : " + visitor.getColumns());
		        System.out.println("coditions : " + visitor.getConditions());
		        System.out.println("orderBy : " + visitor.getOrderByColumns());
		        
		        Assert.assertEquals(0, visitor.getTables().size());
		        Assert.assertEquals(0, visitor.getColumns().size());
		        Assert.assertEquals(0, visitor.getConditions().size());

//		        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));
		    }
		    
		    public void test_6() throws Exception {
		        String sql = "SELECT 'Monty!' NOT RLIKE '.*'";

		        MySqlStatementParser parser = new MySqlStatementParser(sql);
		        List<SQLStatement> statementList = parser.parseStatementList();
		        SQLStatement statemen = statementList.get(0);
		        print(statementList);

		        Assert.assertEquals(1, statementList.size());

		        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		        statemen.accept(visitor);

		        System.out.println("Tables : " + visitor.getTables());
		        System.out.println("fields : " + visitor.getColumns());
		        System.out.println("coditions : " + visitor.getConditions());
		        System.out.println("orderBy : " + visitor.getOrderByColumns());
		        
		        Assert.assertEquals(0, visitor.getTables().size());
		        Assert.assertEquals(0, visitor.getColumns().size());
		        Assert.assertEquals(0, visitor.getConditions().size());

//		        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("mytable")));
		    }

}
