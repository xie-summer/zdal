package com.alipay.zdal.test.rw;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw数据源获取dbid：正常场景，group中两个db,非事务执行sql操作")
public class SR952170 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
    
	@Before
	public void beforeTestCase(){
		sqlMap = (SqlMapClient) ZdalRwSuite.context
		.getBean("zdalGetDbIndexByNow");
	}
	
	@After
	public void afterTestCase(){
		ZdalTestCommon.dataDeleteForZds();
	}
	
	
	@SuppressWarnings("unchecked")
	@Subject("非事务进行读操作，获取dbId")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952171() {		
		try {
			sqlMap.queryForList("queryRwSql");
            Step("非事务进行读操作，获取dbId");
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {
				String dbId = entry.getKey();
				Assert.isTrue("getDbDsIndex1.group0_r_0".equalsIgnoreCase(dbId)
						|| "getDbDsIndex1.group0_r_1".equalsIgnoreCase(dbId),
						"验证dbid"+dbId);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Subject("非事务进行写操作，获取dbId")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952172() {

		Step("非事务进行写操作，获取dbId");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", "111");
			sqlMap.insert("insertRwSql", params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
				.get(ThreadLocalString.GET_ID_AND_DATABASE);
		for (Map.Entry<String, DataSource> entry : map.entrySet()) {
			String dbId = entry.getKey();
			Assert.areEqual("getDbDsIndex1.group0_w_0", dbId, "验证dbid");
	
		}
	}

}
