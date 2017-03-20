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
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.alipay.zdal.test.common.AllTestSuit;
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw 动态指定库")
public class SR952050 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private String url1;
	private String url2;
	private String psd;
	private String user;
	

	@Before
	public void beforeTestCase() {
		
		url1 = ConstantsTest.mysql12UrlZds1;
		url2 = ConstantsTest.mysql12UrlZds2;
		psd = ConstantsTest.mysq112Psd;
		user = ConstantsTest.mysq112User;
	}

	@After
	public void afterTestCase() {

		ZdalTestCommon.dataDeleteForZds();
		ThreadLocalMap.reset();
	}

	@Subject("动态指定库，rw 写库 ds0:r2w1p0,ds1:r1w2p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952051() {
		Step("动态指定库，rw 写库 ds0:r2w1p0,ds1:r1w2p1");
		HashMap<String, Integer> mp = writeRwMysqlDBByIndex(
				"zdalrwmysqlDBIndex", "insertRwSql", 1);
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Step("获取个数");
		Assert.areEqual(true, count1 == 0, "the count1 value:"+count1);
		Assert.areEqual(true, count2 == 30, "the count2 value:"+count2);

	}

	
	@Subject("动态指定库，rw 读库 ds0:r2w1p0,ds1:r1w2p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952052() {
		Step("动态指定库，rw 读库 ds0:r2w1p0,ds1:r1w2p1");
		HashMap<String, Integer> mp = readRwMysqlDBByIndex(
				"zdalrwmysqlDBIndex", "queryRwSql", 1);

		int countA = mp.get("countA");
		int countB = mp.get("countB");
		Step("获取个数");

		Assert.areEqual(true, countA == 0, "the countA value");
		Assert.areEqual(true, countB == 30, "the countA value");

	}

	@Subject("动态指定库,越界，rw 写库 ds0:r2w1p0,ds1:r1w2p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952053() {
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalrwmysqlDBIndex");
		Map<String, Object> params;

		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 3);
		try {
			params = new HashMap<String, Object>();
			params.put("num", 101);
			sqlMap.insert("insertRwSql", params);
		} catch (Exception e) { //
			// TODO Auto-generated catch block
			Assert.areEqual(NestedSQLException.class, e.getClass(), "验证是否抛出异常");
		}
	}
	

	/**
	@Subject("动态指定库,越界，rw 读库 ds0:r2w1p0,ds1:r1w2p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void testcase04() {

		sqlMap = (SqlMapClient) ZdalTestSuite.context
				.getBean("zdalrwmysqlDBIndex");
		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 3);
		try {
			 sqlMap.queryForList("queryRwSql");
		} catch (Exception e) { // TODO Auto-generated catch block
			Assert.areEqual(NestedSQLException.class, e.getClass(), "验证是否抛出异常");
		}
	}
	*/

	
	@Subject("动态指定库,库权重为0，rw 读库 ds0:r0w1p0,ds1:r1w2p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952055() {
		Step("动态指定库,库权重为0，rw 读库 ds0:r0w1p0,ds1:r1w2p1");
		HashMap<String, Integer> mp = readRwMysqlDBByIndex(
				"zdalrwmysqlDBIndex2", "queryRwSql", 0);

		int countA = mp.get("countA");
		int countB = mp.get("countB");
		Step("获取个数");
		Assert.areEqual(true, countA == 30, "the countA value");
		Assert.areEqual(true, countB == 0, "the countA value");

	}

	@Subject("动态指定库，rw 写库 ds0:r1w1p0,ds1:r1w0p1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952056() {
		Step("动态指定库，rw 写库 ds0:r1w1p0,ds1:r1w0p1");
		HashMap<String, Integer> mp = writeRwMysqlDBByIndex(
				"zdalrwmysqlDBIndex3", "insertRwSql", 1);
		int count1 = mp.get("count1");
		int count2 = mp.get("count2");
		Step("获取个数");
		Assert.areEqual(true, count1 == 0, "the count1 value");
		Assert.areEqual(true, count2 == 30, "the count2 value");

	}


	/**
	 * 动态指定库 写库
	 * 
	 * @param beanName
	 * @param sqlName
	 * @return
	 */

	private HashMap<String, Integer> writeRwMysqlDBByIndex(String beanName,
			String sqlName, int dbIndex) {
		HashMap<String, Integer> mp = new HashMap<String, Integer>();
		sqlMap = (SqlMapClient)ZdalRwSuite.context.getBean(beanName);
		String sqlStr1 = sqlName;
		Map<String, Object> params;

		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, dbIndex);

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
		// 进写入的数据count，进行验证
		String querySql = "select count(*) from test1 where colu2 = 'DB_G'";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, url1, psd,
				user);
		ResultSet rs2 = ZdalTestCommon.dataCheckFromJDBC(querySql, url2, psd,
				user);
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
	 * 动态指写库读
	 * 
	 * @param beanName
	 * @param sqlName
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> readRwMysqlDBByIndex(String beanName,
			String sqlName, int dbIndex) {
		HashMap<String, Integer> hp = new HashMap<String, Integer>();
		int countA = 0;
		int countB = 0;
		// 准备数据
		ZdalTestCommon.dataPrepareForZds();

		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, dbIndex);

		// 读取数据，并计算从每个库里面读数的次数
		sqlMap = (SqlMapClient)ZdalRwSuite.context.getBean(beanName);
		String sqlStr1 = sqlName;
		for (int countnum = 0; countnum < 30; countnum++) {
			try {
				List<Object> a = (List<Object>) sqlMap.queryForList(sqlStr1);
				for (int i = 0; i < a.size(); i++) {
					HashMap<String, String> hs = (HashMap<String, String>) a
							.get(i);
					if ("DB_A".equalsIgnoreCase((String) hs.get("colu2"))) {
						countA++;
					} else if ("DB_B"
							.equalsIgnoreCase((String) hs.get("colu2"))) {
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
