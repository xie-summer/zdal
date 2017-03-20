package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;


public class TestSqlParser {
        @Test
    public void test_1() throws Exception {
        String sql = "update trade_message_board set IS_READ='1' where (TRADE_NO = ?) AND (POSTER_ROLE <> ?) AND(IS_READ = '0')";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

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
        System.out.println("--------------------------------");

        Assert.assertEquals(2, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_2() {
        String sql = "update  sub_trade_base  set SUB_TRADE_STATUS=?, REFUND_FEE=?, GMT_MODIFIED=? where (trade_no = ? and sub_trade_no = ?)";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(5, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_3() {
        String sql = "update timeout set end_time= end_time+?/86400 where (job_id = ?) AND (trade_no = ?)";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(3, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_4() {
        String sql = "update timeout set prior_Level= prior_Level + ?, gmt_modified=? where ((job_id = ?) AND (trade_no = ?))";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(4, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_5() {
        String sql = "select /*+ index(t,TIMEOUT_TNO_IND)*/ * from timeout t where ((Trade_No = ?) AND (user_id = ?) AND (end_time > SYSDATE) AND (status = 'W')) and id in(?,?,?,?,?) and rownum=?";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(8, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_6() {
        String sql = "update timeout_remind set end_time= end_time+?/86400 where (job_id = ?) AND (trade_no = ?)";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(3, visitor.getBindVarConditions().size());
    }

        @Test
    public void test_7() {
        String sql = "select  * from trade_base a where (trade_no = ?) and gmt_create < to_date(substr(?,1,8),'yyyyMMdd')+2 and gmt_create > to_date(substr(?,1,8),'yyyyMMdd')-1";
        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);
        Assert.assertEquals(1, visitor.getBindVarConditions().size());
    }

    @Test
    public void test_8() {
        String sql = "select count(*) from ( select biz_type from ps_consume_delete  where owner_card_no = ?  and delete_type IN (?,?,?) LIMIT 200) b";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statements = parser.parseStatementList();
        SQLStatement statement = statements.get(0);
        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
        statement.accept(visitor);
        System.out.println(visitor);
    }
}

