package com.alipay.zdal.test.ut.datasource;

import javax.sql.DataSource;

import org.junit.Test;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

public class ZdsInit {
    @Test
    public void test001()  {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://mypay83307.devdb.alipay.net:3307/zds_switch?useUnicode=true&amp;characterEncoding=gbk");
        dsDo.setUserName("zds_switch");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
       // dsDo.setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);
        try {
			DataSource dataSource = new ZDataSource(dsDo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @Test
    public void test()  {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://mypay83307.devdb.alipay.net:3307/zds_switch?useUnicode=true&amp;characterEncoding=gbk");
        dsDo.setUserName("zds_switch");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        //        dsDo
        //            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);
        try {
			DataSource dataSource = new ZDataSource(dsDo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
