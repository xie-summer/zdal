package com.alipay.zdal.test.shardrw;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.After;
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
@Feature("shard+rw数据源获取dbid：正常场景，有两个group,非事务执行sql操作")
public class SR954080 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
    
	@After
	public void afterTestcase(){
		ThreadLocalMap.reset();
	}
	
	@SuppressWarnings("unchecked")
	@Subject("zoneDs=group_0_r,group_0_w,zoneError=Exception.读库，获取当前dbId")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954081() {
		Step("zoneDs=group_0_r,group_0_w,zoneError=Exception.读库，获取当前dbId");
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwMysql");

		try {
			sqlMap.queryForList("selectShardrwMysql");
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {
				String dbId = entry.getKey();
				Assert.areEqual("MysqlShardrwDs1.group_0_r_1", dbId, "验证dbid");
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(1, 2, "出现不该有的异常");
		}
	}

	@SuppressWarnings("unchecked")
	@Subject("zoneDs=group_0_r,group_0_w,zoneError=Exception.写库，获取当前dbId")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954082() {
		Step("zoneDs=group_0_r,group_0_w,zoneError=Exception.写库，获取当前dbId");
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwMysql");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("user_id", 10);
			params.put("age", 10);
			params.put("name", "testOnly");
			sqlMap.insert("insertShardrwMysql", params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
				.get(ThreadLocalString.GET_ID_AND_DATABASE);
		for (Map.Entry<String, DataSource> entry : map.entrySet()) {

			String dbId = entry.getKey();
			Assert.areEqual("MysqlShardrwDs1.group_0_w_0", dbId, "验证dbid");
		
		}
		String dburl0=ConstantsTest.mysql12UrlTranation0;
		String dbuser=ConstantsTest.mysq112User;
		String dbpsd=ConstantsTest.mysq112Psd;
		String delsqlStr="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delsqlStr, dburl0, dbpsd, dbuser);

	}

}
