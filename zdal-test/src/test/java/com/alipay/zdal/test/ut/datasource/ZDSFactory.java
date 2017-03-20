package com.alipay.zdal.test.ut.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;

public class ZDSFactory {
	private Logger logger = Logger.getLogger(ZDSFactory.class.getName());
	private int length = 20;
	private LocalTxDataSourceDO localTxDataSourceDO_0 = new LocalTxDataSourceDO();
	private LocalTxDataSourceDO localTxDataSourceDO_1 = new LocalTxDataSourceDO();
	private LocalTxDataSourceDO localTxDataSourceDO_2 = new LocalTxDataSourceDO();
	private LocalTxDataSourceDO localTxDataSourceDO_3 = new LocalTxDataSourceDO();
	private LocalTxDataSourceDO localTxDataSourceDO_4 = new LocalTxDataSourceDO();
	private LocalTxDataSourceDO localTxDataSourceDO_5 = new LocalTxDataSourceDO();
	private String[] jndinames = {"DataSource-Oracle","DataSource-Mysql","DataSource-MysqlChange","DataSource-MysqlAPP","DataSource-invalid","DataSource-OracleChange"};
	
	private ZDataSource[] zDataSources = new ZDataSource[length];
	
	public ZDSFactory(){	
        localTxDataSourceDO_0.setBackgroundValidation(false);
        localTxDataSourceDO_0.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_0.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_0.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_0.setConnectionURL("jdbc:oracle:thin:@perf6.lab.alipay.net:1521:perfdb6");
        localTxDataSourceDO_0.setDriverClass("oracle.jdbc.OracleDriver");
        try {
			localTxDataSourceDO_0.setEncPassword("-7cda29b2eef25d0e");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_0.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        localTxDataSourceDO_0.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_0.setMaxPoolSize(12);
        localTxDataSourceDO_0.setMinPoolSize(6);
        localTxDataSourceDO_0.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_0.setPrefill(false);
        localTxDataSourceDO_0.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_0.setQueryTimeout(180);
        localTxDataSourceDO_0.setSharePreparedStatements(false);
        localTxDataSourceDO_0.setTxQueryTimeout(false);
        localTxDataSourceDO_0.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_0.setUserName("ACM");
        localTxDataSourceDO_0.setUseFastFail(false);
        localTxDataSourceDO_0.setValidateOnMatch(false);
        localTxDataSourceDO_0.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleValidConnectionChecker");
        localTxDataSourceDO_0.setDsName("ds");
        try {
			zDataSources[0] = new ZDataSource(localTxDataSourceDO_0);
		} catch (Exception e) {
			logger.error(e);
		}
        
        
        localTxDataSourceDO_1.setBackgroundValidation(false);
        localTxDataSourceDO_1.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_1.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_1.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_1.setConnectionURL("jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds1");
        localTxDataSourceDO_1.setDriverClass("com.mysql.jdbc.Driver");
        try {
			localTxDataSourceDO_1.setEncPassword("-76079f94c1e11c89");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_1.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        localTxDataSourceDO_1.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_1.setMaxPoolSize(12);
        localTxDataSourceDO_1.setMinPoolSize(6);
        localTxDataSourceDO_1.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_1.setPrefill(false);
        localTxDataSourceDO_1.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_1.setQueryTimeout(180);
        localTxDataSourceDO_1.setSharePreparedStatements(false);
        localTxDataSourceDO_1.setTxQueryTimeout(false);
        localTxDataSourceDO_1.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_1.setUserName("mysql");
        localTxDataSourceDO_1.setUseFastFail(false);
        localTxDataSourceDO_1.setValidateOnMatch(false);
        localTxDataSourceDO_1.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLValidConnectionChecker");
        localTxDataSourceDO_1.setDsName("ds");
try {
			zDataSources[1] = new ZDataSource(localTxDataSourceDO_1);
		} catch (Exception e) {
			logger.error(e);
		}
        
        localTxDataSourceDO_2.setBackgroundValidation(false);
        localTxDataSourceDO_2.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_2.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_2.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_2.setConnectionURL("jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds2");
        localTxDataSourceDO_2.setDriverClass("com.mysql.jdbc.Driver");
        try {
			localTxDataSourceDO_2.setEncPassword("-76079f94c1e11c89");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_2.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        localTxDataSourceDO_2.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_2.setMaxPoolSize(12);
        localTxDataSourceDO_2.setMinPoolSize(6);
        localTxDataSourceDO_2.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_2.setPrefill(false);
        localTxDataSourceDO_2.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_2.setQueryTimeout(180);
        localTxDataSourceDO_2.setSharePreparedStatements(false);
        localTxDataSourceDO_2.setTxQueryTimeout(false);
        localTxDataSourceDO_2.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_2.setUserName("mysql");
        localTxDataSourceDO_2.setUseFastFail(false);
        localTxDataSourceDO_2.setValidateOnMatch(false);
        localTxDataSourceDO_2.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLValidConnectionChecker");
        localTxDataSourceDO_2.setDsName("ds");
        try {
			zDataSources[2] = new ZDataSource(localTxDataSourceDO_2);
		} catch (Exception e){
			logger.error(e);
		}
        
        localTxDataSourceDO_3.setBackgroundValidation(false);
        localTxDataSourceDO_3.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_3.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_3.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_3.setConnectionURL("jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/zds1");
        localTxDataSourceDO_3.setDriverClass("com.mysql.jdbc.Driver");
        try {
			localTxDataSourceDO_3.setEncPassword("-76079f94c1e11c89");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_3.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        localTxDataSourceDO_3.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_3.setMaxPoolSize(12);
        localTxDataSourceDO_3.setMinPoolSize(6);
        localTxDataSourceDO_3.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_3.setPrefill(false);
        localTxDataSourceDO_3.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_3.setQueryTimeout(180);
        localTxDataSourceDO_3.setSharePreparedStatements(false);
        localTxDataSourceDO_3.setTxQueryTimeout(false);
        localTxDataSourceDO_3.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_3.setUserName("mysql");
        localTxDataSourceDO_3.setUseFastFail(false);
        localTxDataSourceDO_3.setValidateOnMatch(false);
        localTxDataSourceDO_3.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLValidConnectionChecker");    
        localTxDataSourceDO_3.setDsName("ds");
        try {
			zDataSources[3] = new ZDataSource(localTxDataSourceDO_3);
		} catch (Exception e) {
			logger.error(e);			
		}
        
        localTxDataSourceDO_4.setBackgroundValidation(false);
        localTxDataSourceDO_4.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_4.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_4.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_4.setConnectionURL("jdbc:mysql:///mysql-1-2.bjl.alipay.net:3306/invalid");
        localTxDataSourceDO_4.setDriverClass("com.mysql.jdbc.Driver");
        try {
			localTxDataSourceDO_4.setEncPassword("-76079f94c1e11c89");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_4.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLExceptionSorter");
        localTxDataSourceDO_4.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_4.setMaxPoolSize(12);
        localTxDataSourceDO_4.setMinPoolSize(6);
        localTxDataSourceDO_4.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_4.setPrefill(false);
        localTxDataSourceDO_4.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_4.setQueryTimeout(180);
        localTxDataSourceDO_4.setSharePreparedStatements(false);
        localTxDataSourceDO_4.setTxQueryTimeout(false);
        localTxDataSourceDO_4.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_4.setUserName("mysql");
        localTxDataSourceDO_4.setUseFastFail(false);
        localTxDataSourceDO_4.setValidateOnMatch(false);
        localTxDataSourceDO_4.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.MySQLValidConnectionChecker");    
        localTxDataSourceDO_4.setDsName("ds");
        try {
			zDataSources[4] = new ZDataSource(localTxDataSourceDO_4);
		} catch (Exception e) {
			logger.error(e);			
		}
        
        localTxDataSourceDO_5.setBackgroundValidation(false);
        localTxDataSourceDO_5.setBackgroundValidationMinutes(10);
        localTxDataSourceDO_5.setBlockingTimeoutMillis(2000);
        localTxDataSourceDO_5.setCheckValidConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_5.setConnectionURL("jdbc:oracle:thin:@perf6.lab.alipay.net:1521:perfdb6");
        localTxDataSourceDO_5.setDriverClass("oracle.jdbc.OracleDriver");
        try {
			localTxDataSourceDO_5.setEncPassword("-7cda29b2eef25d0e");
		} catch (Exception e) {
			logger.error(e);
		}
        localTxDataSourceDO_5.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        localTxDataSourceDO_5.setIdleTimeoutMinutes(30);
        localTxDataSourceDO_5.setMaxPoolSize(12);
        localTxDataSourceDO_5.setMinPoolSize(6);
        localTxDataSourceDO_5.setNewConnectionSQL("SELECT 1 from dual");
        localTxDataSourceDO_5.setPrefill(false);
        localTxDataSourceDO_5.setPreparedStatementCacheSize(100);
        localTxDataSourceDO_5.setQueryTimeout(180);
        localTxDataSourceDO_5.setSharePreparedStatements(false);
        localTxDataSourceDO_5.setTxQueryTimeout(false);
        localTxDataSourceDO_5.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        localTxDataSourceDO_5.setUserName("ACM1");
        localTxDataSourceDO_5.setUseFastFail(false);
        localTxDataSourceDO_5.setValidateOnMatch(false);
        localTxDataSourceDO_5.setValidConnectionCheckerClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleValidConnectionChecker");
        localTxDataSourceDO_5.setDsName("ds");
        try {
			zDataSources[5] = new ZDataSource(localTxDataSourceDO_5);
		} catch (Exception e) {
			logger.error(e);
		}		
	}
	
	/**
	 * 
	 * @param index 对应zDataSources[]的下标
	 * @return
	 */
	public ZDataSource getZdataSouceByIndex(int index){
		return zDataSources[index];
	}
	
	public ZDataSource getZdataSouceByJndiname(String jndiname){
		int index = 99;
		boolean flag = true;
	
		for(int i=0;i<length && flag;i++){
			if( jndiname.equalsIgnoreCase(jndinames[i]) ){
				index = i;
				flag = false;				
			}
		}
		
		if(index == 99){
			return null;
		}
		return zDataSources[index];
	}
	
	
	public static void main(String[] args) {
		ZDSFactory zdsFactory = null;
		try {
			zdsFactory = new ZDSFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=1;i<4;i++){
			ZDataSource zds = zdsFactory.getZdataSouceByJndiname("DataSource-Mysql1");
			Connection connection = null;
			
			try {
				connection = zds.getConnection();
				System.err.println(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}


	}
}
