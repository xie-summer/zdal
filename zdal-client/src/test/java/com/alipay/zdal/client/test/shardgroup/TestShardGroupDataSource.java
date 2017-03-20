/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.test.shardgroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.test.BaseTest;
import com.alipay.zdal.client.util.ThreadLocalMap;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: TestShardFailoverDataSource.java, v 0.1 2013-12-27 ÉÏÎç10:20:50 Exp $
 */
public class TestShardGroupDataSource extends BaseTest {
    private static final String   APPNAME    = "ShardGroup";

    private static final String   APPDSNAME  = "ShardGroupDataSource";

    private static final String   DBMODE     = "dev";

    private static final String   CONFIGPATH = "./configs/ShardGroup";

    private static final String[] USER_IDS   = new String[] { "201312268302803810",
            "201312268302803811", "201312268302803812", "201312268302803813", "201312268302803814",
            "201312268302803815", "201312268302803816", "201312268302803817", "201312268302803818",
            "201312268302803819"            };

    @Test
    public void testShardGroupDataSource() throws Throwable {
        ZdalDataSource dataSource = new ZdalDataSource();
        dataSource.setAppName(APPNAME);
        dataSource.setAppDsName(APPDSNAME);
        dataSource.setDbmode(DBMODE);
        dataSource.setConfigPath(CONFIGPATH);
        dataSource.init();
        try {
            operate(dataSource);
        } catch (Exception e) {
            throw e;
        }
    }

    private void operate(ZdalDataSource dataSource) {
        for (String userId : USER_IDS) {
            try {
                String name = "name";
                String address = "address";
                insert(dataSource, userId, name, address);
                select(dataSource, userId, name, address);
                String updateName = "zhouxiaoqing";
                update(dataSource, userId, updateName);
                select(dataSource, userId, updateName, address);
                delete(dataSource, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void insert(ZdalDataSource dataSource, String userId, String name, String address)
                                                                                              throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("insert into user(user_id,name,address) values (?,?,?)");
            pst.setString(1, userId);
            pst.setString(2, name);
            pst.setString(3, address);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(pst, conn);
        }
        System.out.println("excute the dbName = "
                           + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));
    }

    private void select(ZdalDataSource dataSource, String userId, String name, String address)
                                                                                              throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("select user_id,name,address from user where user_id=?");
            pst.setString(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Assert.assertEquals(userId, rs.getString(1));
                Assert.assertEquals(name, rs.getString(2));
                Assert.assertEquals(address, rs.getString(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(rs, pst, conn);
        }
        System.out.println("excute the dbName = "
                           + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));
    }

    private void update(ZdalDataSource dataSource, String userId, String name) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("update  user set name= ?  where user_id=?");
            pst.setString(1, name);
            pst.setString(2, userId);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(pst, conn);
        }
        System.out.println("excute the dbName = "
                           + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));
    }

    private void delete(ZdalDataSource dataSource, String userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("delete from user where user_id=?");
            pst.setString(1, userId);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(pst, conn);
        }
        System.out.println("excute the dbName = "
                           + ThreadLocalMap.get(ThreadLocalString.GET_ID_AND_DATABASE));

    }
}
