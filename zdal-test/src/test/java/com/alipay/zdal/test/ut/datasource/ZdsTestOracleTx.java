package com.alipay.zdal.test.ut.datasource;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;


public class ZdsTestOracleTx {
    protected static DataSource         dataSource = null;
    protected static JdbcTemplate       jt         = null;
    static DataSourceTransactionManager tm         = null;
    static TransactionTemplate          tt         = null;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo.setConnectionURL("jdbc:oracle:oci:@perf6.lab.alipay.net:1521:perfdb6");
        dsDo.setUserName("Acm");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("oracle.jdbc.OracleDriver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);
        dataSource = new ZDataSource(dsDo);
        jt = new JdbcTemplate(dataSource);
        tm = new DataSourceTransactionManager(dataSource);
        tt = new TransactionTemplate(tm);

        jt.execute("insert into testcase (id,name) values (1,'sb')");
    }

    @AfterClass
    public static void tearDown() {
        jt.execute("delete from testcase");
    }

    /**
     * 成功的事务 update
     */
    @Test
    public void test1() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                jt.execute("UPDATE testcase SET name = 'diao' WHERE id = 1");
                System.err.print(jt.queryForList("select * from testcase where id =1"));
                return null;
            }
        });
    }

    /**
     * 成功的事务 insert
     */
    @Test
    public void test4() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                jt.execute("insert into testcase (id,name) values(3,'ttttttttt')");

                return null;
            }
        });
        System.err.print(jt.queryForList("select * from testcase"));
    }

    /**
     * 成功的事务 delete
     */
    @Test
    public void test5() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                jt.execute("delete from testcase");

                return null;
            }
        });
        System.err.print(jt.queryForList("select count(*) from testcase"));
    }

    /**
     * 失败的事务，看是否rollback
     */
    @Test
    public void test2() {
        System.out.println(jt.update("UPDATE testcase SET name = 'before' WHERE id = 1"));
        System.out.println(jt.queryForList("select name from testcase where id =1"));
        try {
            tt.execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus status) {
                    jt.execute("UPDATE zdstest SET name = 'after' WHERE id = 1");
                    System.err.print(jt.queryForList("select * from sb where id =1"));//不存在的表
                    return null;
                }
            });
        } catch (Exception e) {

        }
        Assert.assertTrue("[{name=before}]".equalsIgnoreCase(jt.queryForList(
            "select name from testcase where id =1").toString()));

    }
}