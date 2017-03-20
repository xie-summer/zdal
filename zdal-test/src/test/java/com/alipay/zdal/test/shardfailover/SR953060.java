package com.alipay.zdal.test.shardfailover;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.alipay.zdal.test.common.ConstantsTest;


@RunWith(ATSJUnitRunner.class)
@Feature("shard+Failover ,zoneError=Exception，跨zone访问")
public class SR953060  {
	public TestAssertion Assert  = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dburl;
	private String dbpsd;
	private String dbuser;
	private String dburl2;
	
	@Before
	public void beforeTestCase(){
		dburl=ConstantsTest.mysq112UrlTddl0;
		dbpsd=ConstantsTest.mysq112Psd;
		dbuser=ConstantsTest.mysq112User;
		dburl2=ConstantsTest.mysq112UrlTddl1;
	}
	
	@After
	public void afterTestCase(){
		String delSql="delete from users_0 where user_id=10";
		String delSql2="delete from users_1 where user_id=11";
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delSql2, dburl2, dbpsd, dbuser);						
	}
	
	@Subject("跨zone访问： zoneDs=master_0,zoneError=Exception.写库 master_0库的users_0表，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953061() throws SQLException{
		Step("跨zone访问： zoneDs=master_0,zoneError=Exception.写库 master_0库的users_0表，允许访问");
		String querySql="select user_id,name,address from users_0 where user_id = 10";
		sqlMap=(SqlMapClient)ZdalShardfailoverSuite.context.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		params.put("name", "test_users");
		params.put("address", "test_address");
		
		try{
		sqlMap.insert("insertZoneDsZoneError", params);
				
		}catch(Exception ex){
			ex.printStackTrace();	
			Assert.areEqual(1,2,"insertZoneDsZoneError Exception");
		}
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBC(querySql, dburl, dbpsd, dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(10, rs.getInt(1), "the zoneDs=master_0,zoneError=Exception.then visit master_0,check user_id");
		Assert.areEqual("test_users", rs.getString(2), "the zoneDs=master_0,zoneError=Exception.then visit master_0");
		Assert.areEqual("test_address", rs.getString(3), "the zoneDs=master_0,zoneError=Exception.then visit master_0");
		
	}
	
	
	@Subject("跨zone访问： zoneDs=master_0,zoneError=Exception.写库 master_1的表 users_1，报异常")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953062() throws SQLException{
		Step("跨zone访问： zoneDs=master_0,zoneError=Exception.写库 master_1的表 users_1，报异常");
		sqlMap=(SqlMapClient)ZdalShardfailoverSuite.context.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_users");
		params.put("address", "test_address");
		
		try{
			sqlMap.insert("insertZoneDsZoneError", params);
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.areEqual(NestedSQLException.class, ex.getClass(), "the zoneDs=master_0,zoneError=Exception.then visit db master_1 table users_1");
		}
		
	}
	
	
	
	@Subject("跨zone访问： zoneDs=master_0,master_1,zoneError=Exception.访问库 master_1的表 users_1，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953063() throws SQLException{
		Step("跨zone访问： zoneDs=master_0,master_1,zoneError=Exception.访问库 master_1的表 users_1，允许访问");
		String querySql="select user_id,name,address from users_1 where user_id = 11";
		sqlMap=(SqlMapClient)ZdalShardfailoverSuite.context.getBean("zdalZoneDsZoneErrorRight2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_users");
		params.put("address", "test_address");
		
		try{
		sqlMap.insert("insertZoneDsZoneError", params);
				
		}catch(Exception ex){
			ex.printStackTrace();	
			Assert.areEqual(1,2,"insertZoneDsZoneError Exception");
		}
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBC(querySql, dburl2, dbpsd, dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(11, rs.getInt(1), "the zoneDs=master_0,zoneError=Exception.then visit master_0,check user_id");
		Assert.areEqual("test_users", rs.getString(2), "the zoneDs=master_0,zoneError=Exception.then visit master_0");
		Assert.areEqual("test_address", rs.getString(3), "the zoneDs=master_0,zoneError=Exception.then visit master_0");
		
	
	}
	
	@Subject("跨zone访问： zoneDs=master_0,abc,zoneError=Exception.访问 master_0的 users_0，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953064() throws SQLException{
		Step("跨zone访问： zoneDs=master_0,abc,zoneError=Exception.访问 master_0的 users_0，允许访问");
		String querySql="select user_id,name,address from users_0 where user_id = 10";
		sqlMap=(SqlMapClient)ZdalShardfailoverSuite.context.getBean("zdalZoneDsZoneErrorRight3");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		params.put("name", "test_users");
		params.put("address", "test_address");
		
		try{
		sqlMap.insert("insertZoneDsZoneError", params);
				
		}catch(Exception ex){
			ex.printStackTrace();	
			Assert.areEqual(1,2,"insertZoneDsZoneError Exception");
		}
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBC(querySql, dburl, dbpsd, dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(10, rs.getInt(1), "the zoneDs=master_0,abc,zoneError=Exception.then visit master_0,check user_id");
		Assert.areEqual("test_users", rs.getString(2), "the zoneDs=master_0,abc,zoneError=Exception.then visit master_0");
		Assert.areEqual("test_address", rs.getString(3), "the zoneDs=master_0,abc,zoneError=Exception.then visit master_0");
		
	
	}

}
