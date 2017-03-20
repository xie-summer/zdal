package com.alipay.zdal.test.ut.datasource;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

/**
 * mysql db 基本使用
 * @author sicong.shou
 * @version $Id: ZdsTest1.java, v 0.1 2012-11-21 下午04:53:45 sicong.shou Exp $
 */
public class ZdsTestMysql {
    protected static DataSource   dataSource = null;
    protected static JdbcTemplate jt         = null;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds1");
        dsDo.setUserName("mysql");
        dsDo.setPassWord("mysql");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo.setExceptionSorterClassName(MySQLExceptionSorter.class.getName());
        dsDo.setPreparedStatementCacheSize(0);
        dataSource = new ZDataSource(dsDo);
        jt = new JdbcTemplate(dataSource);
    }

    @Test
    public void test1() {
        System.out.println(jt.update("insert into test1(clum,colu2) values(1,'test001')"));       
        
        System.out.println(jt.queryForInt("select clum from test1 where colu2=?",
                new Object[] { "test001" }));
        
        System.out.println(jt.update("update test1 set colu2=? where clum= ?", new Object[] { "XXX",
                1 }));      
        
        System.out.println(jt.queryForObject("select 1 from test1 where clum=?",
                new Object[] { 1 }, Integer.class));
        
        System.out.print(jt.update("delete from test1 where clum=?", new Object[] { 1 }));
        
    }


}