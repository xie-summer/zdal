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
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+failover ,zoneError=Log，跨zone访问")
public class SR953070 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dburl;
	private String dbpsd;
	private String dbuser;
	private String dburl2;
	
	


	@Before
	public void beforeTestCase() {
	  
		dburl = ConstantsTest.mysq112UrlTddl0;
		dbpsd = ConstantsTest.mysq112Psd;
		dbuser = ConstantsTest.mysq112User;
		dburl2 = ConstantsTest.mysq112UrlTddl1;
	}

	@After
	public void afterTestCase() {
		String delSql = "delete from users_0";
		String delSql2 = "delete from users_1";
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delSql2, dburl2, dbpsd, dbuser);
	}

	
	@Subject("跨zone访问： zoneDs=master_0,zoneError=LOG.访问 master_0库的users_0表,允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953071() throws SQLException {
		Step("跨zone访问： zoneDs=master_0,zoneError=LOG.访问 master_0库的users_0表,允许访问");
		String querySql = "select user_id,name,address from users_0 where user_id = 10";
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorLog");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		params.put("name", "test_users");
		params.put("address", "test_address");

		try {
			sqlMap.insert("insertZoneDsZoneError", params);

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(1, 2, "insertZoneDsZoneError Exception");
		}
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, dburl, dbpsd,
				dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(10, rs.getInt(1),
				"the zoneDs=master_0,zoneError=LOG.then visit master_0,check user_id");
		Assert.areEqual("test_users", rs.getString(2),
				"the zoneDs=master_0,zoneError=LOG.then visit master_0");
		Assert.areEqual("test_address", rs.getString(3),
				"the zoneDs=master_0,zoneError=LOG.then visit master_0");
	}

	@Subject("跨zone访问： zoneDs=master_0,zoneError=LOG.访问 master_1和users_1表，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953072() throws SQLException {
		Step("跨zone访问： zoneDs=master_0,zoneError=LOG.访问 master_1和users_1表，允许访问");
		String querySql = "select user_id,name,address from users_1 where user_id = 11";
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorLog");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_users");
		params.put("address", "test_address");

		try {
			sqlMap.insert("insertZoneDsZoneError", params);

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(1, 2, "insertZoneDsZoneError Exception");
		}
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, dburl2,
				dbpsd, dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(11, rs.getInt(1),
				"the zoneDs=master_0,zoneError=LOG.then visit master_1,check user_id");
		Assert.areEqual("test_users", rs.getString(2),
				"the zoneDs=master_0,zoneError=LOG.then visit master_1");
		Assert.areEqual("test_address", rs.getString(3),
				"the zoneDs=master_0,zoneError=LOG.then visit master_1");
	}

	@Subject("跨zone访问： zoneDs=abc,zoneError=LOG.访问 master_1的users_1表，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953073() throws SQLException {
		Step("跨zone访问： zoneDs=abc,zoneError=LOG.访问 master_1的users_1表，允许访问");
		String querySql = "select user_id,name,address from users_1 where user_id = 11";
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorLog2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_users");
		params.put("address", "test_address");

		try {
			sqlMap.insert("insertZoneDsZoneError", params);

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(1, 2, "insertZoneDsZoneError Exception");
		}
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, dburl2,
				dbpsd, dbuser);
		Assert.areEqual(true, rs.next(), "the value");
		Assert.areEqual(11, rs.getInt(1),
				"the zoneDs=abc,zoneError=LOG.then visit master_1,check user_id");
		Assert.areEqual("test_users", rs.getString(2),
				"the zoneDs=abc,zoneError=LOG.then visit master_1");
		Assert.areEqual("test_address", rs.getString(3),
				"the zoneDs=abc,zoneError=LOG.then visit master_1");
	}

}
