package com.alipay.zdal.test.rw;

import java.sql.SQLException;
import java.util.HashMap;

import org.junit.After;
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
import static com.alipay.ats.internal.domain.ATS.Step;


@RunWith(ATSJUnitRunner.class)
@Feature("rw ,跨zone访问")
public class SR952190 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	
	@After
	public void afterTestCase(){
		ZdalTestCommon.dataDeleteForZds();
	}
	
	
	@Subject("跨zone访问： zoneDs=ds0,zoneError=Exception.写库ds1的表，不允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952191(){
		Step("跨zone访问： zoneDs=ds0,zoneError=Exception.写库ds1的表，不允许访问");
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwVisitOtherZone");
		HashMap<String ,Object> params = new HashMap<String, Object>();	
		Step("插入数据");
		try {
			params.put("num", 123);
			sqlMap.insert("insertRwSql", params);
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");
	
		}	
	}
	
	
	@Subject("跨zone访问： zoneDs=ds0,zoneError=Exception.读库ds1的表，不允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952192(){
	    Step("跨zone访问： zoneDs=ds0,zoneError=Exception.读库ds1的表，不允许访问");
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwVisitOtherZone");
	
		try {
			sqlMap.queryForList("queryRwSql");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");
	
		}	
		
	}
	
}
