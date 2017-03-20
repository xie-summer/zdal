package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.sqlmap.client.SqlMapClient;



@RunWith(ATSJUnitRunner.class)
@Feature("shard+rw ,跨zone访问")
public class SR954140 {
	 public TestAssertion Assert = new TestAssertion();
	  private SqlMapClient sqlMap;
	  private String dburl0;
	  private String dbuser;
	  private String dbpsd;
    
	  @Before
	  public void beforeTestCase(){
		   dburl0=ConstantsTest.mysql12UrlTranation0;
		   dbuser=ConstantsTest.mysq112User;
		   dbpsd=ConstantsTest.mysq112Psd;
	  }
	
	  
	@Subject("跨zone访问： zoneDs=group_0_r,group_0_w,zoneError=Exception.写库group_0的表，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954141(){
		Step("跨zone访问： zoneDs=group_0_r,group_0_w,zoneError=Exception.写库group_0的表，允许访问");
		 sqlMap=(SqlMapClient)ZdalShardrwSuite.context.getBean("zdalShardrwMysql");
		Map<String, Object> params = new HashMap<String, Object>();	
		try {
			params.put("user_id", 10);
			params.put("age", 10);
			params.put("name", "testOnly");
			sqlMap.insert("insertShardrwMysql", params);
		} catch (SQLException e) {
			e.printStackTrace();	
		}	
		Step("检查数据");
		String sqlStr="select count(*) from user_0 where user_id=10";
		ResultSet se=ZdalTestCommon.dataCheckFromJDBC(sqlStr, dburl0, dbpsd,dbuser);
		try {
			se.next();
			Assert.areEqual(1, se.getInt(1), "数据验证");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delsqlStr="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delsqlStr, dburl0, dbpsd, dbuser);
	}

	
	@Subject("跨zone访问： zoneDs=group_0_r,group_0_w,zoneError=Exception.读库group_0的表，允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954142(){
		Step("跨zone访问： zoneDs=group_0_r,group_0_w,zoneError=Exception.读库group_0的表，允许访问");
		 sqlMap=(SqlMapClient)ZdalShardrwSuite.context.getBean("zdalShardrwMysql");

		try {		
			sqlMap.queryForList("selectShardrwMysql");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(1, 2, "发生了不该有的异常");
		}		
	}
	
		
	
	@Subject("跨zone访问： zoneDs=group_1_r,group_1_w,zoneError=Exception.读库group_0的表，不允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954143(){
		 sqlMap=(SqlMapClient)ZdalShardrwSuite.context.getBean("zdalShardrwMysql2");
         int i=0;
		try {		
			sqlMap.queryForList("selectShardrwMysql");
			
		} catch (SQLException e) {
			 i=1;
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");			
		}
		Assert.areEqual(1, i, "验证无异常");
	}
	
	
	@Subject("跨zone访问： zoneDs=group_0_w,zoneError=Exception.读库group_0的表，不允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954144(){
		Step("跨zone访问： zoneDs=group_0_w,zoneError=Exception.读库group_0的表，不允许访问");
		 sqlMap=(SqlMapClient)ZdalShardrwSuite.context.getBean("zdalShardrwMysql3");
         int i=0;
		try {		
			sqlMap.queryForList("selectShardrwMysql");			
		} catch (SQLException e) {
			 i=1;
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");			
		}
		Assert.areEqual(1, i, "验证无异常");
	}
	
	
	@Subject("跨zone访问： zoneDs=group_0_r,zoneError=Exception.写库group_0的表，不允许访问")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954145(){
		Step("跨zone访问： zoneDs=group_0_r,zoneError=Exception.写库group_0的表，不允许访问");
		 sqlMap=(SqlMapClient)ZdalShardrwSuite.context.getBean("zdalShardrwMysql4");
         int i=0;	
			Map<String, Object> params = new HashMap<String, Object>();	
			try {
				params.put("user_id", 10);
				params.put("age", 10);
				params.put("name", "testOnly");
				sqlMap.insert("insertShardrwMysql", params);
			}		
             catch (SQLException e) {
			 i=1;
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");			
		}
		Assert.areEqual(1, i, "验证异常");
		}
	
	
	
}
