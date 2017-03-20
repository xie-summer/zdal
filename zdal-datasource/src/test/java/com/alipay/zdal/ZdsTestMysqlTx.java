package com.alipay.zdal;

import javax.sql.DataSource;

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

/**
 * mysql transation test
 * @author sicong.shou
 * @version $Id: ZdsTest1.java, v 0.1 2012-11-21 下午04:53:45 sicong.shou Exp $
 */
public class ZdsTestMysqlTx {
    protected static DataSource         dataSource = null;
    protected static JdbcTemplate       jt         = null;
    static DataSourceTransactionManager tm         = null;
    static TransactionTemplate          tt         = null;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://mypay83307.devdb.alipay.net:3307/zds_switch?useUnicode=true&amp;characterEncoding=gbk");
        dsDo.setUserName("zds_switch");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);
        dataSource = new ZDataSource(dsDo);
        jt = new JdbcTemplate(dataSource);
        tm = new DataSourceTransactionManager(dataSource);
        tt = new TransactionTemplate(tm);

    }

    /**
     * 成功的事务 update
     */
    @Test
    public void test1() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                jt.execute("UPDATE zdstest SET name = 'diao' WHERE id = 52342342352");
                System.err.print(jt.queryForList("select * from zdstest where id =52342342352"));
                return null;
            }
        });
    }

    /**
     * 成功的事务 insert
     */
    @Test
    public void test3() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                jt.update("insert into zdstest (id,name) values (?,?)", new Object[] { 99887766,
                        "qqqqqqq" });

                return null;
            }
        });
        System.err.print(jt.queryForList("select * from zdstest where id =99887766"));
    }

    /**
     * 成功的事务 delete
     */
    @Test
    public void test4() {
        tt.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                System.out.println(jt.update("delete from zdstest"));

                return null;
            }
        });
    }

    /**
     * 失败的事务，看是否rollback
     */
    @Test
    public void test2() {
        System.out.println(jt.update("UPDATE zdstest SET name = 'before' WHERE id = 52342342352"));
        System.out.println(jt.queryForList("select name from zdstest where id =52342342352"));
        try {
            tt.execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus status) {
                    jt.execute("UPDATE zdstest SET name = 'after' WHERE id = 52342342352");
                    System.err.print(jt.queryForList("select * from sb where id =52342342352"));//不存在的表
                    return null;
                }
            });
        } catch (Exception e) {

        }
        Assert.assertTrue("[{name=before}]".equals(jt.queryForList(
            "select name from zdstest where id =52342342352").toString()));

    }
}