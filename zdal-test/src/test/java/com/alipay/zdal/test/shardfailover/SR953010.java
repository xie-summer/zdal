package com.alipay.zdal.test.shardfailover;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+failover数据源获取dbid：正常场景，有两个group,非事务执行sql操作")
public class SR953010 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;

	
	@SuppressWarnings("unchecked")
	@Subject("获取dbId: zoneDs=master_0,zoneError=Exception.写库 master_0库的users_0表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953011() {
		Step("获取dbId: zoneDs=master_0,zoneError=Exception.写库 master_0库的users_0表");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		params.put("name", "test_users");
		params.put("address", "test_address");

		try {
			sqlMap.insert("insertZoneDsZoneError", params);
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {

				String dbId = entry.getKey();
				Assert.areEqual("zdalZoneDsZoneErrorRight.master_0", dbId, "验证dbid");

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(1, 2, "insertZoneDsZoneError Exception");
		}
		Step("删除数据");
		String delsqlStr = "delete from users_0";
		String dburl = ConstantsTest.mysq112UrlTddl0;
		String dbpsd = ConstantsTest.mysq112Psd;
		String dbuser = ConstantsTest.mysq112User;
		ZdalTestCommon.dataUpdateJDBC(delsqlStr, dburl, dbpsd, dbuser);
	}

	
	@SuppressWarnings("unchecked")
	@Subject("获取dbId: zoneDs=master_0,zoneError=Exception.读库 master_0库的users_0表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953012() {
		Step("获取dbId: zoneDs=master_0,zoneError=Exception.读库 master_0库的users_0表");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		try {
			sqlMap.queryForList("selectZoneDsZoneError", params);
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {

				String dbId = entry.getKey();
				Assert.areEqual("zdalZoneDsZoneErrorRight.master_0", dbId, "验证dbid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
