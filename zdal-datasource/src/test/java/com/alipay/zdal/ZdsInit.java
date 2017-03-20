package com.alipay.zdal;

import javax.sql.DataSource;

import org.junit.Test;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

public class ZdsInit {
    @Test
    public void right() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://localhost:3306/diamond?useUnicode=true&amp;characterEncoding=UTF-8");
        dsDo.setUserName("root");
        dsDo.setPassWord("123456");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName(MySQLExceptionSorter.class.getName());
        dsDo.setPreparedStatementCacheSize(0);
        DataSource dataSource = new ZDataSource(dsDo);

    }

    @Test(expected = IllegalArgumentException.class)
    public void wrong() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://localhost:3306/diamond?useUnicode=true&amp;characterEncoding=UTF-8");
        dsDo.setUserName("root");
        dsDo.setPassWord("123456");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        //dsDo.setExceptionSorterClassName(MySQLExceptionSorter.class.getName());
        dsDo.setPreparedStatementCacheSize(0);
        DataSource dataSource = new ZDataSource(dsDo);

    }
}
