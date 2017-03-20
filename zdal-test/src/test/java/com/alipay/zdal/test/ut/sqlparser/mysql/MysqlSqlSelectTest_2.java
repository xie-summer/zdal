
package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;


public class MysqlSqlSelectTest_2  {
    public static final String SELECT_DS_VER_BY_APPID_SQL          = "select app_id,node_id,current_version,creator,create_time from app_ds_ver "
                                                                     + "where app_id = ? and current_version = "
                                                                     + "(select max(current_version) from app_ds_ver where app_id=?)";

    public static final String SELECT_CHILD_DS_BY_APPID_NODEID_SQL = "select node_id,app_id,node_template_id,name,description,ds_type,db_type,"
                                                                     + " failover_rule,general_rule,readwrite_rule,"
                                                                     + " creator,create_time "
                                                                     + " from app_ds "
                                                                     + " where  app_id = ? "
                                                                     + " and node_id in ( select child_node_id from  app_ds_ver_tree where node_id=? and app_id=? ) order by name";
@Test
    public void test1() {
        MySqlStatementParser parser = new MySqlStatementParser(SELECT_DS_VER_BY_APPID_SQL);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
       

        Assert.assertEquals(1, statementList.size());

        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
        statemen.accept(visitor);
        System.out.println(visitor);
    }
@Test
    public void test2() {
        MySqlStatementParser parser = new MySqlStatementParser(SELECT_CHILD_DS_BY_APPID_NODEID_SQL);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
  

        Assert.assertEquals(1, statementList.size());

        ZdalMySqlSchemaStatVisitor visitor = new ZdalMySqlSchemaStatVisitor();
        statemen.accept(visitor);
        System.out.println(visitor);
    }
}
