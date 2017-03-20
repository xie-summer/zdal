package com.alipay.zdal.datasource.resource.adapter.jdbc.vender;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

/**
 * MySQLExceptionSorter ��������
 * 
 * @author liangjie.li
 * @version $Id: MySQLExceptionSorterTest.java, v 0.1 2012-8-15 ����3:06:02 liangjie.li Exp $
 */
public class MySQLExceptionSorterTest {

    protected static DataSource        dataSource = null;

    private final MySQLExceptionSorter sorter     = new MySQLExceptionSorter();

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
        int[] errors = new int[] { 1040, 1042, 1043, 1047, 1081, 1129, 1130, 1045, 1004, 1005,
                1021, 1041, 1037, 1038 };

        SQLException e = new SQLException("reason", "08S01");

        Assert.assertTrue(sorter.isExceptionFatal(e));

        for (int err : errors) {
            e = new SQLException("reason", "01XXX", err);
            Assert.assertTrue(sorter.isExceptionFatal(e));
        }

        e = new SQLException("no datasource!", "01XXX");
        Assert.assertTrue(sorter.isExceptionFatal(e));

        e = new SQLException("no alive datasource", "01XXX");
        Assert.assertTrue(sorter.isExceptionFatal(e));

    }

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo
            .setConnectionURL("jdbc:mysql://localhost:3306/diamond?useUnicode=true&amp;characterEncoding=UTF-8");
        dsDo.setUserName("root");
        dsDo.setPassWord("123456");
        dsDo.setDriverClass("com.mysql.jdbc.Driver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo.setExceptionSorterClassName("com.alipay.zdal.client.jdbc.sorter.MySQLExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);

        dataSource = new ZDataSource(dsDo);
    }
}
