package com.alipay.zdal.datasource.resource.adapter.jdbc.vender;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

/**
 * OracleExceptionSorter ≤‚ ‘”√¿˝
 * 
 * @author liangjie.li
 * @version $Id: OracleExceptionSorterTest.java, v 0.1 2012-8-15 œ¬ŒÁ3:05:49 liangjie.li Exp $
 */
public class OracleExceptionSorterTest {

    protected static DataSource         dataSource = null;

    private final OracleExceptionSorter sorter     = new OracleExceptionSorter();

    @Test
    public void testIsExceptionFatal() {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            assertTrue(sorter.isExceptionFatal(e));
        }
    }

    @Test
    public void testErrorCode() {
        int[] errors = new int[] { 28, 600, 1012 };

        SQLException e = null;

        for (int err : errors) {
            e = new SQLException("reason", "01XXX", err);
            Assert.assertTrue(sorter.isExceptionFatal(e));
        }

        e = new SQLException("NO DATASOURCE!", "01XXX", 21001);
        Assert.assertTrue(sorter.isExceptionFatal(e));
        e = new SQLException("NO ALIVE DATASOURCE", "01XXX", 21001);
        Assert.assertTrue(sorter.isExceptionFatal(e));

        e = new SQLException("msg", "01XXX", -1);
        Assert.assertFalse(sorter.isExceptionFatal(e));

        e = new SQLException("msg", "01XXX", -1);

        e.initCause(new SQLException("msg", "01XXX", -1));
        Assert.assertFalse(sorter.isExceptionFatal(e));

        e = new SQLException("msg", "01XXX", -1);
        e.initCause(null);
        Assert.assertFalse(sorter.isExceptionFatal(e));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo.setConnectionURL("jdbc:oracle:oci:@devdb05dg911");
        dsDo.setUserName("diamond");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.client.jdbc.sorter.OracleExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);

        dataSource = new ZDataSource(dsDo);
    }

}
