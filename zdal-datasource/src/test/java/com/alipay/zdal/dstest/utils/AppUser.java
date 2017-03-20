package com.alipay.zdal.dstest.utils;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

public class AppUser implements Runnable {
	private DataSource dataSource;
	private Connection connection;
	private Statement statement;
	private String sql;
	private boolean isStop = false;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
	
	public void connect() throws Exception{
		connection = dataSource.getConnection();
		statement = connection.createStatement();
	}
	
	public void execSql() throws Exception{
		statement.execute(sql);
//		ResultSet resultSet = statement.getResultSet();
//		System.out.println(Thread.currentThread().getName() + 
//				" " + resultSet.getMetaData().getColumnCount());
	}
	
	public void run(){
		try{
			connect();
//			System.out.println("应用使用者" + Thread.currentThread().getName() + 
//				"建立连接");
			while(!isStop()){
				execSql();
			}
			statement.close();
			connection.close();
//			System.out.println("应用使用者" + Thread.currentThread().getName() + 
//					"关闭连接");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}