/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.test.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.test.BaseTest;
import com.alipay.zdal.client.util.ThreadLocalMap;

/**
 * 
 * @author 伯牙
 * @version $Id: TestGroupDataSource.java, v 0.1 2013-12-25 上午09:44:45 Exp $
 */
public class TestGroupDataSource extends BaseTest {

    private static final String APPNAME    = "Group";

    private static final String APPDSNAME  = "groupDataSource";

    private static final String DBMODE     = "dev";

    private static final String CONFIGPATH = "./configs/Group";

    @Test
    public void testGroupDataSource() {
        ZdalDataSource dataSource = new ZdalDataSource();
        dataSource.setAppName(APPNAME);
        dataSource.setAppDsName(APPDSNAME);
        dataSource.setDbmode(DBMODE);
        dataSource.setConfigPath(CONFIGPATH);
        dataSource.init();
        try {
            deleteData(dataSource);
            operateGroupDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataSource.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除历史数据.
     * @param dataSource
     */
    private void deleteData(ZdalDataSource dataSource) {
        String deleteSql = "delete from test";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(deleteSql);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeResource(pst, conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行sql语句操作.
     * @param dataSource
     */
    private void operateGroupDataSource(ZdalDataSource dataSource) {
        String insertSql = "insert into test(id,name,address) values(?,?,?)";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(insertSql);
            pst.setString(1, "id");
            pst.setString(2, "name");
            pst.setString(3, "address");
            pst.execute();
            System.out.println("execute dbIndex = "
                               + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeResource(pst, conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String selectSql = "select id,name,address from test where id=?";
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(selectSql);
            pst.setString(1, "id");
            ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 0);//指定写库读.
            rs = pst.executeQuery();
            while (rs.next()) {
                Assert.assertEquals("name", rs.getString("name"));
                Assert.assertEquals("address", rs.getString("address"));
            }
            System.out.println("execute dbIndex = "
                               + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeResource(rs, pst, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
