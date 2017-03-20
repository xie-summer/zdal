package com.alipay.zdal.test.rw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Feature("rw oracle读写分离")
public class SR952020 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private String url;
	private String psd;
	private String user1;
	private String user2;
	

	@Before
	public void beforeTestCase() {
		
		url = ConstantsTest.oralcePrefUrl;
		user1 = ConstantsTest.oraclePreUser1;
		user2 = ConstantsTest.oraclePreUser2;
		psd = ConstantsTest.oraclePrePsd;
	}
	
	@After
	public void afterTestCase(){
		String delSql = "delete from acm_target_record ";
		ZdalTestCommon.dataUpdateJDBCOracle(delSql, url, psd, user1);
		ZdalTestCommon.dataUpdateJDBCOracle(delSql, url, psd, user2);

	}

	@Subject("rw oracle写库:ds0:r2w1,ds1:r1w2")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952021() {
		Step("rw oracle写库:ds0:r2w1,ds1:r1w2");
		HashMap<String, Integer> mp = writeRwOracleDB("zdalRwOracle1",
				"insertRwSqlOracle");
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Step("断言数据个数");
		Assert.areEqual(true, 0 <= count1 && count1 <= 15, "the count1 value");
		Assert.areEqual(true, 10 <= count2 && count2 <= 30, "the count2 value");
	}

	@Subject("rw oracle读库:ds0:r2w1,ds1:r1w2")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952022() {
		Step("rw oracle读库:ds0:r2w1,ds1:r1w2");
		HashMap<String, Integer> mp = readRwOracleDB("zdalRwOracle1",
				"queryRwSqlOracle");
		int count1 = mp.get("countA");
		int count2 = mp.get("countB");
		Step("断言数据个数");
		Assert.areEqual(true, 10 <= count1 && count1 <= 30, "the countA value:"+count1);
		Assert.areEqual(true, 0 <= count2 && count2 <= 15, "the countB value:"+count2);
	}
	
	@Subject("rw oracle写库:ds0:r1w1,ds1:r0w0")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952023() {
		Step("rw oracle写库:ds0:r1w1,ds1:r0w0");
		HashMap<String, Integer> mp = writeRwOracleDB("zdalRwOracle2",
				"insertRwSqlOracle");
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Step("数据个数");
		Assert.areEqual(true,  count1 == 30, "the count1 value");
		Assert.areEqual(true,  count2 == 0, "the count2 value");
	}
	
	@Subject("rw oracle读库:ds0:r1w1,ds1:r0w0")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952024() {
		Step("rw oracle读库:ds0:r1w1,ds1:r0w0");
		HashMap<String, Integer> mp = readRwOracleDB("zdalRwOracle2",
				"queryRwSqlOracle");
		int count1 = mp.get("countA");
		int count2 = mp.get("countB");
		Step("数据个数");
		Assert.areEqual(true,  count1 == 30, "the countA value");
		Assert.areEqual(true,  count2 == 0, "the countB value");
	}
	
	
	@Subject("rw oracle写库:ds0:r1,ds1:w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952025() {
		Step("rw oracle写库:ds0:r1,ds1:w1");
		HashMap<String, Integer> mp = writeRwOracleDB("zdalRwOracle3",
				"insertRwSqlOracle");
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Assert.areEqual(true,  count1 == 0, "the count1 value");
		Assert.areEqual(true,  count2 == 30, "the count2 value");
	}
	
	@Subject("rw oracle读库:ds0:r1,ds1:w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952026() {
		Step("rw oracle读库:ds0:r1,ds1:w1");
		HashMap<String, Integer> mp = readRwOracleDB("zdalRwOracle3",
				"queryRwSqlOracle");
		int count1 = mp.get("countA");
		int count2 = mp.get("countB");
		Assert.areEqual(true,  count1 == 30, "the countA value");
		Assert.areEqual(true,  count2 == 0, "the countB value");
	}
	
	
	@Subject("rw oracle写库:ds0:rw,ds1:r1w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952027() {
		Step("rw oracle写库:ds0:rw,ds1:r1w1");
		HashMap<String, Integer> mp = writeRwOracleDB("zdalRwOracle4",
				"insertRwSqlOracle");
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Assert.areEqual(true,  count1 > 20, "the count1 value");
		Assert.areEqual(true,  count2 < 10, "the count2 value");
	}
	
	@Subject("rw oracle读库:ds0:rw,ds1:r1w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952028() {
		Step("rw oracle读库:ds0:rw,ds1:r1w1");
		HashMap<String, Integer> mp = readRwOracleDB("zdalRwOracle4",
				"queryRwSqlOracle");
		int count1 = mp.get("countA");
		int count2 = mp.get("countB");
		Step("断言数据个数");
		Assert.areEqual(true,  count1 > 20, "the countA value");
		Assert.areEqual(true,  count2 < 10, "the countB value");
	}
	
	
	@Subject("rw oracle写库:ds0:r1w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952029() {
		Step("rw oracle写库:ds0:r1w1");
		HashMap<String, Integer> mp = writeRwOracleDB("zdalRwOracle5",
				"insertRwSqlOracle");
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Assert.areEqual(true,  count1 == 30, "the count1 value");
		Assert.areEqual(true,  count2 == 0, "the count2 value");
	}
	
	@Subject("rw oracle读库:ds0:r1w1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95202a() {
		Step("rw oracle读库:ds0:r1w1");
		HashMap<String, Integer> mp = readRwOracleDB("zdalRwOracle5",
				"queryRwSqlOracle");
		int count1 = mp.get("countA");
		int count2 = mp.get("countB");
		Assert.areEqual(true,  count1 ==30, "the countA value");
		Assert.areEqual(true,  count2 ==0, "the countB value");
	}
	
	
	

	/**
	 * 针对rw oracle库的写库的公共函数
	 * 
	 * @param beanName
	 * @param sqlName
	 * @return
	 */

	private HashMap<String, Integer> writeRwOracleDB(String beanName,
			String sqlName) {
		HashMap<String, Integer> mp = new HashMap<String, Integer>();
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean(beanName);
		String sqlStr1 = sqlName;
		Map<String, Object> params;
		int inserttime = 30;
		for (int i = 1; i <= inserttime; i++) {
			try {
				params = new HashMap<String, Object>();
				params.put("num", i);
				sqlMap.insert(sqlStr1, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 写入的数据count，进行验证
		String querySql = "select count(*) from acm_target_record where test_varchar = 'DB_G'";

		ResultSet rs = ZdalTestCommon.dataCheckFromJDBCOracle(querySql, url,
				psd, user1);
		ResultSet rs2 = ZdalTestCommon.dataCheckFromJDBCOracle(querySql, url,
				psd, user2);

		try {
			Assert.areEqual(true, rs.next() && rs2.next(), "the value");
			int count1 = rs.getInt(1);
			int count2 = rs2.getInt(1);

			mp.put("count1", count1);
			mp.put("count2", count2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}

	/**
	 * 针对rw oracle库的读库的公共函数
	 * 
	 * @param beanName
	 * @param sqlName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> readRwOracleDB(String beanName,
			String sqlName) {
		HashMap<String, Integer> hp = new HashMap<String, Integer>();
		int countA = 0;
		int countB = 0;
		// 先插入数据
		String insertSql1 = "insert into ACM_TARGET_RECORD (id,test_varchar,test_date,int_field_1,int_field_2,var_field_1,var_field_2) values (100,'DB_A',to_date('2013-10-08 20:46:34','YYYY-MM-DD-HH24:MI:SS'),1,1,'a','b')";
		String insertSql2 = "insert into ACM_TARGET_RECORD (id,test_varchar,test_date,int_field_1,int_field_2,var_field_1,var_field_2) values (100,'DB_B',to_date('2013-10-08 20:46:34','YYYY-MM-DD-HH24:MI:SS'),1,1,'a','b')";
		ZdalTestCommon.dataUpdateJDBCOracle(insertSql1, url, psd, user1);
		ZdalTestCommon.dataUpdateJDBCOracle(insertSql2, url, psd, user2);

		// 读取数据，并计算从每个库里面读数的次数
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean(beanName);
		for (int countnum = 0; countnum < 30; countnum++) {
			try {
				List<Object> a = (List<Object>) sqlMap.queryForList(sqlName);
				for (int i = 0; i < a.size(); i++) {
					HashMap<String, String> hs = (HashMap<String, String>) a
							.get(i);
					if ("DB_A".equalsIgnoreCase((String) hs.get("COLU2"))) {
						countA++;
					} else if ("DB_B"
							.equalsIgnoreCase((String) hs.get("COLU2"))) {
						countB++;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hp.put("countA", countA);
		hp.put("countB", countB);
		return hp;
	}

}
