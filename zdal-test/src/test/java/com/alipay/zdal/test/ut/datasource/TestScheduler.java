package com.alipay.zdal.test.ut.datasource;

import org.junit.Test;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

public class TestScheduler {
    protected  ZDataSource dataSource = null;

    @Test
    public void oracle() throws Exception {
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
        

    }
}
