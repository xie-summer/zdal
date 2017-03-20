/**
 * 
 */
package com.alipay.zdal.test.ut.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

/**
 * @author Administrator
 *
 */
public class PoolMinSizeResetTests {

	
	@Test
	public void test() throws Exception {
		ZDataSource dataSource = null;
		try {
			dataSource = new ZDataSource(dataSourceDO);
			System.out.println("------------------Before execution ---------------------");
			System.out.println(dataSource.getLocalTxDataSource().getPoolCondition());
			Connection conn = null;
            Statement statement = null;
            ResultSet rs = null;
            try {
                conn = dataSource.getConnection();
                System.out.println("------------------Afer get connection ---------------------");
                System.out.println(dataSource.getLocalTxDataSource().getPoolCondition());
                /*statement = conn.createStatement();
                if (dbType.isMysql()) {
                    rs = statement.executeQuery(MYSQL_PREFILL_SQL);
                } else {
                    rs = statement.executeQuery(ORACLE_PREFILL_SQL);
                }*/
                Thread.currentThread().sleep(12000);
            } catch (Exception e) {
            	e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                    if (statement != null) {
                        statement.close();
                        statement = null;
                    }
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
            System.out.println("------------------After execution ---------------------");
            System.out.println(dataSource.getLocalTxDataSource().getPoolCondition());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( null != dataSource ){
				dataSource.destroy();
			}
		}
	}

	 /** mysql数据库预热连接的sql语句 */
    private static final String MYSQL_PREFILL_SQL  = "select 1";

    /** oracle数据库预热连接的sql语句 */
    private static final String ORACLE_PREFILL_SQL = "select sysdate from dual";
    
	final static String name = "ds1" ;
	final static String jdbcUrl = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds2" ;
    final static String userName = "mysql" ;
    final static String password = "-76079f94c1e11c89" ;
    final static int minConn = 4 ;
    final static int maxConn = 20 ;
    final static int blockingTimeoutMillis = 180 ;
    final static int idleTimeoutMinutes = 1 ;
    final static int preparedStatementCacheSize = 100 ;
    final static int queryTimeout = 180 ;
    final static int maxReadThreshold = 100 ;
    final static int maxWriteThreshold = 100;
	final static String failoverRule = "master" ;
	final static DBType dbType = DBType.MYSQL;
	final static String driverClass = "com.mysql.jdbc.Driver";
	final static boolean prefill = true;
	static LocalTxDataSourceDO dataSourceDO ;
	static{
		dataSourceDO = new LocalTxDataSourceDO();
        dataSourceDO.setDsName("PoolMinSizeResetTests");
        dataSourceDO.setConnectionURL(jdbcUrl);
        dataSourceDO.setUserName(userName);
        try {
			dataSourceDO.setEncPassword(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dataSourceDO.setMinPoolSize(minConn);
        dataSourceDO.setMaxPoolSize(maxConn);
        dataSourceDO.setDriverClass(driverClass);
        dataSourceDO.setBlockingTimeoutMillis(blockingTimeoutMillis);
        dataSourceDO.setIdleTimeoutMinutes(idleTimeoutMinutes);
        dataSourceDO.setPreparedStatementCacheSize(preparedStatementCacheSize);
        dataSourceDO.setQueryTimeout(queryTimeout);
        //        dataSourceDO.setPrefill(isPrefill());
        if (dbType.isMysql()) {
            dataSourceDO.setExceptionSorterClassName(MySQLExceptionSorter.class.getName());
        } else if (dbType.isOracle()) {
            dataSourceDO.setExceptionSorterClassName(OracleExceptionSorter.class.getName());
        } 
        dataSourceDO.setPrefill(prefill);
	}
}
