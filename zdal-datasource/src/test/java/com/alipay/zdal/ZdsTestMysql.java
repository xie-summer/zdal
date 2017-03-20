package com.alipay.zdal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;



/**
 * mysql db 锟斤拷锟斤拷使锟斤拷
 * @author sicong.shou
 * @version $Id: ZdsTest1.java, v 0.1 2012-11-21 锟斤拷锟斤拷04:53:45 sicong.shou Exp $
 */
public class ZdsTestMysql {
    protected static DataSource   dataSource = null;
    protected static JdbcTemplate jt         = null;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://10.209.104.188:9999/cmsp?useUnicode=true&amp;characterEncoding=UTF-8");
        dsDo.setUserName("garuda");
        dsDo.setPassWord("ali88");
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
        //        jt.execute("select * from cmsp.chg_app_user_coords_dd ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("select * from cmsp.chg_app_user_coords_dd ");
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        System.out.println(jt.update("insert into test1(id,name) values(1,'zhouxiaoqing')"));
        //        System.out.println(jt.queryForInt("select id from zdstest where name=?",
        //            new Object[] { "sb" }));
        //        System.out.println(jt.queryForList("select * from zdstest where name=?",
        //            new Object[] { "nb" }));
    }

    /**
     * delete
     */
    //    @Test
    public void test2() {
        System.out.print(jt.update("delete from zdstest where name=?", new Object[] { "hello" }));
    }

    /**
     * insert
     */
    //    @Test
    public void test3() {
        System.out.println(jt.update("insert into zdstest (id,name) values (?,?)", new Object[] {
                998877, "YYY" }));
        /*System.out.println(jt.queryForInt("select id from zdstest where name=?",
            new Object[] { "YYY" }));*/
    }

    /**
     * update
     */
    //    @Test
    public void test4() {
        System.out.println(jt.update("update zdstest set name=? where id= ?", new Object[] { "XXX",
                998877 }));
        System.out.println(jt.queryForObject("select name from zdstest where id=?",
            new Object[] { "998877" }, String.class));
    }
}