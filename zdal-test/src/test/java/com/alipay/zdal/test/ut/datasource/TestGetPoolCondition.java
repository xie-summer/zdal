package com.alipay.zdal.test.ut.datasource;

import java.sql.Connection;

import org.junit.Test;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.util.PoolCondition;

public class TestGetPoolCondition {
    protected  ZDataSource dataSource = null;

    public  void outCondition(PoolCondition con) {
        System.out.println(" min : " + con.getMinSize() + " \n max : " + con.getMaxSize()
                           + "\n AvailableConnectionCount : " + con.getAvailableConnectionCount()
                           + "\n ConnectionCount : " + con.getConnectionCount()
                           + "\n ConnectionCreatedCount : " + con.getConnectionCreatedCount()
                           + "\n ConnectionDestroyedCount : " + con.getConnectionDestroyedCount()
                           + "\n InUseConnectionCount : " + con.getInUseConnectionCount()
                           + "\n MaxConnectionsInUseCoun : " + con.getMaxConnectionsInUseCount());
    }

    @Test
    public void mysql() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds1");
        dsDo.setUserName("mysql");
        dsDo.setPassWord("mysql");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setPrefill(true);
        dsDo.setMinPoolSize(3);
        dsDo.setMaxPoolSize(5);
        dsDo.setIdleTimeoutMinutes(1);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);

        dataSource = new ZDataSource(dsDo);
        PoolCondition con = dataSource.getPoolCondition();
        outCondition(con);
        Connection cc = dataSource.getConnection();
        con = dataSource.getPoolCondition();
        outCondition(con);
        //        cc.close();
        Thread.sleep(10000);
        System.out
            .println("destroy " + dataSource.getPoolCondition().getConnectionDestroyedCount());

    }

    @Test
    public void oracle() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo.setConnectionURL("jdbc:oracle:thin:@perf6.lab.alipay.net:1521:perfdb6");
        dsDo.setUserName("Acm");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("oracle.jdbc.OracleDriver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);

        dataSource = new ZDataSource(dsDo);
        PoolCondition con = dataSource.getPoolCondition();
        outCondition(con);
        dataSource.getConnection();
        con = dataSource.getPoolCondition();
        outCondition(con);

    }
}
