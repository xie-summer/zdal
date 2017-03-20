package com.alipay.zdal.test.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Assert;


public class ZdalTestCommon {
    /**
     * mysql获取connection
     * @param url
     * @param psd
     * @param user
     * @return
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
	public static Connection getConnectionFromMysql(String url, String psd,
			String user) throws SQLException, java.lang.ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, user, psd);
		return con;
	}
    /**
     * oracle获取connection
     * @param url
     * @param psd
     * @param user
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
	public static Connection getConnectionFromOracle(String url, String psd,
			String user) throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(url, user, psd);
		return con;

	}

	/**
	 * 通过mysql jdbc连接获取数据
	 * @param querySqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dataCheckFromJDBC(String querySqlJDBC, String url,
			String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromMysql(url, psd, user);

			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(querySqlJDBC);
			result = stateNormal.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("用jdbc查询失败");
		}
		return result;

	}

	/**
	 * 通过mysql jdbc来增修改删除数据
	 * @param updateSqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static int dataUpdateJDBC(String updateSqlJDBC, String url,
			String psd, String user) {

		int rNumber = 0;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromMysql(url, psd, user);
			// 不带参数的处理
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(updateSqlJDBC);
			rNumber = stateNormal.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("用jdbc更新失败");
		}
		return rNumber;

	}

	/**
	 * 通过oracle jdbc连接获取数据
	 * @param querySqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dataCheckFromJDBCOracle(String querySqlJDBC,
			String url, String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			Statement stmt = jdbcCon.createStatement();
			result = stmt.executeQuery(querySqlJDBC);

		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("用jdbc查询失败");
		}
		return result;

	}

	
	/**
	 * 通过oracle jdbc来增修改删除数据
	 * @param updateSqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static int dataUpdateJDBCOracle(String updateSqlJDBC, String url,
			String psd, String user) {

		int rNumber = 0;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			// 不带参数的处理
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(updateSqlJDBC);
			rNumber = stateNormal.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("用jdbc更新失败");
		}
		return rNumber;

	}


	/**
	 * jdbc查询oracle 的dual表
	 * @param url
	 * @param queryNextSql
	 * @param queryCurrSql
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dualCheckJDBC(String url, String queryNextSql,
			String queryCurrSql, String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			// 执行sequence.nextval查询
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(queryNextSql);
			stateNormal.executeQuery();
			// 执行sequence.currval查询
			PreparedStatement stateNormal0 = jdbcCon
					.prepareStatement(queryCurrSql);
			result = stateNormal0.executeQuery();

		} catch (Exception e) {
			Assert.fail("用jdbc查询失败");
		}
		return result;
		// logger.warn(result);

	}

	/**
	 * 为mysql 的zds两个库准备数据
	 */
	public static void dataPrepareForZds() {
		String url1 = ConstantsTest.mysql12UrlZds1;
		String url2 = ConstantsTest.mysql12UrlZds2;
		String psd = ConstantsTest.mysq112Psd;
		String user = ConstantsTest.mysq112User;
		String insertSql1 = "insert into test1(clum,colu2) values (100,'DB_A')";
		String insertSql2 = "insert into test1(clum,colu2) values (100,'DB_B')";
		dataUpdateJDBC(insertSql1, url1, psd, user);
		dataUpdateJDBC(insertSql2, url2, psd, user);
	}
	
	/**
	 * 为mysql的zds两个库删除数据
	 */
	public static void dataDeleteForZds(){
		String url1 = ConstantsTest.mysql12UrlZds1;
		String url2 = ConstantsTest.mysql12UrlZds2;
		String psd = ConstantsTest.mysq112Psd;
		String user = ConstantsTest.mysq112User;
		String delSql1="delete from test1";
		dataUpdateJDBC(delSql1, url1, psd, user);
		dataUpdateJDBC(delSql1, url2, psd, user);
	}
	
	/**
	 * 为mysql的fail_0准备数据
	 */
	public static void dataPrepareForFail0(){
		String insertSqlJDBC = "insert into master_0 (user_id,age,name,content) values (20,10,'a','s')";
		String url=ConstantsTest.mysq112UrlFail0;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd ;
		dataUpdateJDBC(insertSqlJDBC, url, psd, user);
	}
	
	/**
	 * 为mysql的Tddl0,tddl_1,tddl_2准备数据
	 */
	public static void dataPrepareForTddl(){
		String insertSqlJDBC0 ="insert into users(name,address) values ('DB','DB_A')";
		String insertSqlJDBC1 ="insert into users(name,address) values ('DB','DB_B')";
		String insertSqlJDBC2 ="insert into users(name,address) values ('DB','DB_C')";
		String url0=ConstantsTest.mysq112UrlTddl0;
		String url1=ConstantsTest.mysq112UrlTddl1;
		String url2=ConstantsTest.mysq112UrlTddl2;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd;
		dataUpdateJDBC(insertSqlJDBC0, url0, psd, user);
		dataUpdateJDBC(insertSqlJDBC1, url1, psd, user);
		dataUpdateJDBC(insertSqlJDBC2, url2, psd, user);		
	}
	
	/**
	 * 为mysql的tddl_0,tddl_1,tddl_2删除数据
	 */
	public static void dataDeleteForTddl(){
		String delSql="delete from users";
		String url0=ConstantsTest.mysq112UrlTddl0;
		String url1=ConstantsTest.mysq112UrlTddl1;
		String url2=ConstantsTest.mysq112UrlTddl2;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd;
		dataUpdateJDBC(delSql, url0, psd, user);
		dataUpdateJDBC(delSql, url1, psd, user);
		dataUpdateJDBC(delSql, url2, psd, user);	
		
	}

}
