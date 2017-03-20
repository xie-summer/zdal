package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Feature("shard+rw数据源的 分库分表")
public class SR954110 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dbpsd;
	private String dbuser;
	private String dburl1;
	private String dburl0bac;
	private String dburl1bac;

	@Before
	public void beforeTestCase() {
		dburl1 = ConstantsTest.mysql12UrlTranation1;
		dburl0bac=ConstantsTest.mysql12UrlTranation0_bac;
		dburl1bac=ConstantsTest.mysql12UrlTranation1_bac;
		dbuser = ConstantsTest.mysq112User;
		dbpsd = ConstantsTest.mysq112Psd;
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwShardDbShardTable");
	}

	@Subject("shard+rw，分库分表，写库。写group_1库的ds1的user_1表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954111() {
		Step("shard+rw，分库分表，写库。写group_1库的ds1的user_1表");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("age", Integer.valueOf("11"));
		params.put("name", "test_address");

		try {
			sqlMap.insert("insertShardrwMysql", params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		testCheckData(dburl1);
		testDeleData(dburl1);
	}
	
	@SuppressWarnings("unchecked")
	@Subject("shard+rw，分库分表，读库。读group_0库的ds2的user_1表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954112() {
		Step("shard+rw，分库分表，读库。读group_0库的ds2的user_1表");
		testPrepareData();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("2"));
		params.put("age", Integer.valueOf("1"));

		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlPriority",params);
		    Assert.areEqual(1, rs.size(), "检查查询结果记录");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(1, 2, "不该出现的异常");
		}
		Step("清除数据");
		testDeleData(dburl0bac);
	}
	
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的表,对结果进行merge")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954113(){
		testPrepareData1();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlMergeResult");
			Assert.areEqual(2, rs.size(), "检查查询结果记录");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		testDeleData1();
	}
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的表,对结果进行merge,并且对结果的sum求值")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954114(){
		testPrepareData1();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlMergeResultSum");
			Assert.areEqual(1, rs.size(), "检查查询结果记录");
			HashMap hm=(HashMap)rs.get(0);
			String sumvalue=hm.get("sumvalue").toString();
			Assert.areEqual("5", sumvalue, "检查SUM的值");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testDeleData1();	
	}
	
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的表,对结果进行merge,并且对结果的min求值")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954115(){
		testPrepareData1();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlMergeResultMin");
			Assert.areEqual(1, rs.size(), "检查查询结果记录");
			HashMap hm=(HashMap)rs.get(0);
			String minvalue=hm.get("minvalue").toString();
			Assert.areEqual("2", minvalue, "检查MIN的值");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		testDeleData1();
	}
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的表,对结果进行merge,并且对结果的max求值")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954116(){
		testPrepareData1();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlMergeResultMax");
			Assert.areEqual(1, rs.size(), "检查查询结果记录");
			HashMap hm=(HashMap)rs.get(0);
			String maxvalue=hm.get("maxvalue").toString();
			Assert.areEqual("3", maxvalue, "检查MAX的值");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testDeleData1();
	}
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的user_0和user_1两个表,对结果进行merge")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954117(){
		testPrepareData2();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlTwoTablesMergeResult");
			Assert.areEqual(4, rs.size(), "检查查询结果记录");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		testDeleData2();
	}
	
	@Subject("shard+rw，分库分表，读库。读group_0,group_1两库的user_0和user_1两个表,对结果进行merge,并求sum值")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954118(){
		testPrepareData2();
		try {
			List<Object> rs=(List<Object>)sqlMap.queryForList("selectShardrwMysqlTwoTablesMergeResultSum");
			Assert.areEqual(1, rs.size(), "检查查询结果记录");
			HashMap hm=(HashMap)rs.get(0);
			String sumvalue=hm.get("sumvalue").toString();
			Assert.areEqual("10", sumvalue, "检查sum值");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		testDeleData2();
	}
	
	

	/**
	 * 检查数据数量
	 * 
	 * @param dburl
	 */
	private void testCheckData(String dburl) {
		String sql = "select count(*) from user_1";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sql, dburl, dbpsd,
				dbuser);
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
	private void testDeleData(String dburl) {
		String delStr = "delete from user_1";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);
	}

	/**
	 * 准备数据
	 */
	private void testPrepareData() {
		String insertSql = "insert into user_1 (user_id,age,name,gmt_created,gmt_modified) values (2,1,'test',now(),now()) ";
		ZdalTestCommon.dataUpdateJDBC(insertSql, dburl0bac, dbpsd, dbuser);
	}
	
	private void testPrepareData1(){
		String insertSql = "insert into user_1 (user_id,age,name,gmt_created,gmt_modified) values (2,1,'testbac_0',now(),now()) ";
		String insertSql1="insert into user_1 (user_id,age,name,gmt_created,gmt_modified) values (3,1,'testbac_1',now(),now()) ";
		ZdalTestCommon.dataUpdateJDBC(insertSql, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(insertSql1, dburl1bac, dbpsd, dbuser);	
	}
	
	private void testDeleData1() {
		String delStr = "delete from user_1";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl1bac, dbpsd, dbuser);
	}
	
	private void testPrepareData2(){
		String insertSql_0="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values (2,2,'testbac_0',now(),now())";
		String insertSql = "insert into user_1 (user_id,age,name,gmt_created,gmt_modified) values (2,1,'testbac_0',now(),now()) ";
		String insertSql1_0="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values (3,2,'testbac_1',now(),now()) ";
		String insertSql1="insert into user_1 (user_id,age,name,gmt_created,gmt_modified) values (3,1,'testbac_1',now(),now()) ";
		ZdalTestCommon.dataUpdateJDBC(insertSql_0, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(insertSql, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(insertSql1_0, dburl1bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(insertSql1, dburl1bac, dbpsd, dbuser);	
	}
	
	private void testDeleData2() {
		String delStr_0 = "delete from user_0";
		String delStr = "delete from user_1";
		ZdalTestCommon.dataUpdateJDBC(delStr_0, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl0bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delStr_0, dburl1bac, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl1bac, dbpsd, dbuser);
	}

}
