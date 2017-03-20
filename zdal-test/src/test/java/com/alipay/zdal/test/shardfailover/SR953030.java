package com.alipay.zdal.test.shardfailover;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+Failover ,分库分表规则")
public class SR953030 {
	public TestAssertion Assert  = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dbpsd;
	private String dbuser;
    private String dburl1;
    private String dburl3;
	
	@Before
	public void beforeTestCase(){
		 dbpsd=ConstantsTest.mysq112Psd;
		 dbuser=ConstantsTest.mysq112User;
		 dburl1=ConstantsTest.mysq112UrlTddl1;
		 dburl3=ConstantsTest.mysq112UrlTddl3;
		 sqlMap=(SqlMapClient)ZdalShardfailoverSuite.context.getBean("zdalShardfailoverShardDbShardTable");
	}
	
	
	@Subject("shard+failover，分库分表，写库。写master_1库users_1表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953031(){	
		Step("shard+failover，分库分表，写库。写master_1库users_1表");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_users");
		params.put("address", "test_address");
		
		try{
		sqlMap.insert("insertShardfailoverMysql", params);				
		}catch(Exception ex){
			ex.printStackTrace();	
		}
		Step("断言");
		testCheckData(dburl1);
		Step("清除数据");
		testDeleData(dburl1);
	}
	
	
	@SuppressWarnings("unchecked")
	@Subject("shard+failover，分库分表，读库。写master_3库users_3表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953032(){
		Step("shard+failover，分库分表，读库。写master_3库users_3表");
		testPrepareData();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("13"));
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectZoneDsZoneError",params);
		    Assert.areEqual(1, rs.size(), "检查查询结果记录");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(1, 2, "不该出现的异常");
		}
		Step("清除数据");
		testDeleData(dburl3);
	}
	

	/**
	 * 检查数据数量
	 * @param dburl
	 */
	private void  testCheckData(String dburl){
		String sql="select count(*) from users_1";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBC(sql, dburl, dbpsd, dbuser);		
		try {
			rs.next();
			Assert.areEqual(1, rs.getInt(1), "数据检查");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * 消除数据
	 */
	private void testDeleData(String dburl){
		String delStr="delete from users_1";
		String delStr3="delete from users_3";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delStr3, dburl3, dbpsd, dbuser);
	}
	
	/**
	 * 准备数据
	 */
	private void testPrepareData(){
		String insertSql="insert into users_3 (user_id,name,address) values (13,'test','test') ";
		ZdalTestCommon.dataUpdateJDBC(insertSql, dburl3, dbpsd, dbuser);
	}

}
