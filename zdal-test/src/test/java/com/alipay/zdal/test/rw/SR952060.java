package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal的preparedStatement的executeQuery()与executeUpdate()")
public class SR952060 extends ZdalTestBase {

    private Connection   connection = null;
    public TestAssertion Assert     = new TestAssertion();

    @Before
    public void beforeTestCase() {
        localFile = "./config/rw";
        zdalDataSource.setAppName("zdalPreparedStatement");
        zdalDataSource.setAppDsName("preparedStatementDs1");
        zdalDataSource.setConfigPath(localFile);
        zdalDataSource.init();
        try {
            connection = zdalDataSource.getConnection();
        } catch (Exception ex) {
            Assert.isFalse(true, "取连接异常" + ex);
        }
    }

    @After
    public void afterTestCase() {
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            connection = null;
            e.printStackTrace();
        }

    }

    @Subject("preparedStatement的执行insert和select")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952061() {

        Step("1、验证insert语句");
        String sql = "insert into test1(colu2) values('hello')";
        int result = 0;
        result = testExecuteUpdate(sql);
        Assert.areEqual(1, result, "preparedStatement执行insert sql语句");

        Step("2、 preparedStatement开始执行select语句");
        String sql_2 = "select count(*) from test1 where colu2 ='hello'";
        ResultSet result_2 = null;
        PreparedStatement preparedStatement_2 = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement_2 = connection.prepareStatement(sql_2);
            result_2 = preparedStatement_2.executeQuery();
            connection.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                result_2.close();
                preparedStatement_2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Step("3、 preparedStatement开始执行update语句");
        int result_3 = 0;
        String sql_3 = "update test1 set colu2 = 'world' ";
        result_3 = testExecuteUpdate(sql_3);
        Assert.areEqual(1, result_3, "preparedStatement执行udpate sql语句");

        Step("4、 preparedStatement开始执行delete语句");
        int result_4 = 0;
        String sql_4 = "delete from test1 where colu2 ='world' ";
        result_4 = testExecuteUpdate(sql_4);
        Assert.areEqual(1, result_4, "preparedStatement执行delete sql语句");

    }

    @Subject("preparedStatement的执行select")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952062() {

        Step("1、执行insert操作");
        String sql = "insert into test1(colu2) values('world')";
        int res_1 = 0;
        res_1 = testExecute(sql);
        Assert.areEqual(1, res_1, "验证插入操作," + res_1);

        Step("2、执行select操作");
        sql = "select count(*) from test1 where colu2 = 'world'";
        ResultSet res_2 = null;
        int res_s = 0;
        PreparedStatement preparedStatement_2 = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement_2 = connection.prepareStatement(sql);
            preparedStatement_2.execute();
            connection.commit();
            res_2 = preparedStatement_2.getResultSet();
            res_2.next();
            res_s = res_2.getInt(1);

        } catch (SQLException e) {
            // e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                res_2.close();
                preparedStatement_2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                preparedStatement_2 = null;
            }
        }

        Assert.areEqual(1, res_s, "验证select结果");

        Step("3、测试执行update操作");
        sql = "update test1 set colu2 = 'abc' ";
        int res_4 = 0;
        res_4 = testExecute(sql);
        Assert.areEqual(1, res_4, "验update操作");

        Step("4、测试执行delete操作");
        sql = "delete from test1 where colu2 = 'abc'";
        int res_3 = 0;
        res_3 = testExecute(sql);
        Assert.areEqual(1, res_3, "验证删除操作");

    }

    /**
     * 测试方法executeUpdate
     * 
     * @param sqlStr
     * @return
     */
    private int testExecuteUpdate(String sqlStr) {
        int result = 0;
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlStr);
            result = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                preparedStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 测试方法execute
     * 
     * @param sqlStr
     * @return
     */
    private int testExecute(String sqlStr) {

        int res = 0;
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlStr);
            preparedStatement.execute();
            connection.commit();
            res = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

}
