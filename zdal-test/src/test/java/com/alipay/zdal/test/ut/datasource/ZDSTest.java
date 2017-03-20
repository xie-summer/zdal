package com.alipay.zdal.test.ut.datasource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.resource.security.SecureIdentityLoginModule;

public class ZDSTest {
    private int                                      arrayLength               = 10;                                               //数组的长度
    protected ZDSFactory                             zdsFactory                = new ZDSFactory();
    protected DataSource[]                           dataSources               = new DataSource[arrayLength];
    protected ZDataSource[]                          zDataSources              = new ZDataSource[arrayLength];
    protected Connection[]                           connections               = new Connection[arrayLength * 2];
    protected Statement[]                            statements                = new Statement[arrayLength * 2];
    protected Connection[]                           groupconnections          = new Connection[arrayLength * 2];
    protected Statement[]                            groupstatements           = new Statement[arrayLength * 2];
    protected String[]                               dataSourceBeans           = new String[arrayLength];
    protected int[]                                  groupdataSourceBeans      = new int[arrayLength];
    protected String                                 key_configValue           = "configValue";
    protected String                                 key_isLogger              = "isLogger";
    protected String                                 key_status                = "status";
    protected String                                 host                      = "10.32.193.93";
    protected String                                 DATASOURCE_TYPE           = "ds";
    protected LocalTxDataSourceDO                    dsConfigExpectMysql       = new LocalTxDataSourceDO();
    protected LocalTxDataSourceDO                    dsConfigExpectOracle      = new LocalTxDataSourceDO();
    protected Map<String, SecureIdentityLoginModule> secureIdentityLoginModule = new HashMap<String, SecureIdentityLoginModule>();
    protected String[]                               jndiName                  = {
            "DataSource-Oracle", "DataSource-Mysql", "DataSource-MysqlChange",
            "DataSource-MysqlAPP"                                             };

    //TODO assert的处理
    //    public TestAssertion Assert = new TestAssertion();

    public final static Log                          Logger                    = LogFactory
                                                                                   .getLog(ZDSTest.class);

    /**
     * 
     * @param i= <br>1-DataSource-Oracle
     * <br>2-DataSource-Mysql
     * <br>3-DataSource-MysqlChange
     * <br>4-DataSource-MysqlAPP
     * <br>5-DataSource-invalid
     * <br>6-DataSource-OracleChange<br>
     * @return 数据源id
     */
    protected String getDataSourceBean(int i) {
        String bean = "";
        switch (i) {
            case 1:
                bean = "DataSource-Oracle";
                break;
            case 2:
                bean = "DataSource-Mysql";
                break;
            case 3:
                bean = "DataSource-MysqlChange";
                break;
            case 4:
                bean = "DataSource-MysqlAPP";
                break;
            case 5:
                bean = "DataSource-invalid";
                break;
            case 6:
                bean = "DataSource-OracleChange";
            default:
        }
        return bean;
    }

    protected String getSql(int i) {
        String sql = "";
        if (i == 1) {
            sql = "select * from test1";
        }
        if (i == 2) {
            sql = "insert into test1 value(1,'hello')";
        }
        if (i == 3) {
            sql = "select * from flag";
        }
        if (i == 4) {
            sql = "insert into test1 values(99,'hello')";
        }
        if (i == 5) {
            sql = "select colu2 from test1 where clum = 99";
        }
        if (i == 6) {
            sql = "delete from test1 where clum = 99";
        }
        if (i == 7) {
            sql = "insert into ACM_TARGET_RECORD (id,test_varchar,test_date,int_field_1,int_field_2,var_field_1,var_field_2) values (99,'hello',to_date('2012-06-15 20:46:34','YYYY-MM-DD-HH24:MI:SS'),1,1,'a','b')";
        }
        if (i == 8) {
            sql = "select test_varchar from ACM_TARGET_RECORD where id = 99";
        }
        if (i == 9) {
            sql = "delete from ACM_TARGET_RECORD where id = 99";
        }
        return sql;
    }

    protected void recoverEvn() {
        System.out.println("恢复测试用例通用环境");
        try {
            for (int i = 0; i < statements.length; i++) {
                if (statements[i] != null) {
                    if (!statements[i].isClosed()) {
                        statements[i].close();
                    }
                }
            }
            for (int i = 0; i < connections.length; i++) {
                if (connections[i] != null) {
                    if (!connections[i].isClosed()) {
                        connections[i].close();
                    }
                }
            }

            for (int i = 0; i < groupconnections.length; i++) {
                if (groupconnections[i] != null) {
                    if (!groupconnections[i].isClosed()) {
                        groupconnections[i].close();
                    }
                }
            }

            for (int i = 0; i < zDataSources.length; i++) {
                if (zDataSources[i] != null) {
                    zDataSources[i].destroy();
                }
            }
        } catch (Exception e) {
            System.out.println("*************销毁连接异常******************");
            e.printStackTrace();
        }
    }

    protected void initEvn() {
        for (int i = 0; i < dataSourceBeans.length; i++) {
            if (dataSourceBeans[i] != null) {
                dataSources[i] = zdsFactory.getZdataSouceByJndiname(dataSourceBeans[i]);
                if (dataSources[i].getClass().equals(ZDataSource.class)) {
                    zDataSources[i] = (ZDataSource) dataSources[i];
                }
                try {
                    connections[i] = dataSources[i].getConnection();
                    statements[i] = connections[i].createStatement();
                } catch (Exception e) {
                    //					logger.error(e);
                    //					fail();
                }
            }
        }

        System.out.println("获取数据源、连接、statement、获取DRM");
        //等待初始化
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void resetStatement(int index) throws Exception {
        connections[index] = dataSources[index].getConnection();
        statements[index] = connections[index].createStatement();
    }

    @Before
    public void onSetUp() {
        try {
            zdsSetUp();
            initEvn();
        } catch (Exception e) {
            System.err.println("setup" + e.getClass() + e.getMessage());
        }
    }

    protected void zdsSetUp() throws Exception {
        //子类实现
    }

    @After
    public void onTearDown() {
        try {
            zdsTearDown();
            recoverEvn();
        } catch (Exception e) {
            e.printStackTrace();
            //			System.err.println(e.getMessage());
        }
    }

    protected void zdsTearDown() throws Exception {
        //子类实现
    }

    protected void sleep(long mill) {
        try {
            long basicSec = 1000l;
            long sec = mill / basicSec;
            long mil = mill % basicSec;
            for (long i = 0; i < sec; i++) {
                Thread.sleep(basicSec);
                System.out.println("已经等待" + i + "秒");
            }
            Thread.sleep(mil);
            System.out.println("已经等待" + sec + "秒" + mil + "毫秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected AppUser[] lunchAppUser(int count, int dsID) {
        AppUser[] appUsers = new AppUser[count];
        for (int i = 0; i < count; i++) {
            appUsers[i] = new AppUser();
            appUsers[i].setDataSource(dataSources[dsID]);
            appUsers[i].setSql(getSql(1));
            new Thread(appUsers[i]).start();
        }
        sleep(2000 + 250 * count);//等待所有用户都启动成功
        return appUsers;
    }

    protected void stopAppUser(AppUser[] appUsers) {
        for (int i = 0; i < appUsers.length; i++) {
            appUsers[i].setStop(true);
        }
        sleep(2000 + 250 * appUsers.length);//等待所有用户都关闭成功
    }

    public InputStream getResourceInputStream(String resource) throws MalformedURLException,
                                                              Exception {
        //		ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //		return loader.getResourceAsStream(resource);
        sleep(1000);
        FileInputStream fileInputStream = new FileInputStream(resource);
        return fileInputStream;
    }

    public static void main(String[] args) {
        String root = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(root);
    }
}
