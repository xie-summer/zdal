package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.client.util.condition.SimpleCondition;
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+rw ,直接绕过分库分表规则的路由")
public class SR954100 {

	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private TransactionTemplate tt;
	private String dburl0;
	private String dbpsd;
	private String dbuser;

	@Before
	public void beforeTestCase() {
		dburl0 = ConstantsTest.mysql12UrlTranation0;
		dbpsd = ConstantsTest.mysq112Psd;
		dbuser = ConstantsTest.mysq112User;
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwShardDbShardTable");
		tt = (TransactionTemplate) ZdalShardrwSuite.context
				.getBean("shardrwtransactionTemplate1");
	}

	@After
	public void afterTestCase() {
		ThreadLocalMap.reset();
	}

	
	@Subject("shard+rw，绕过分库分表规则的路由")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954101() {
		insertData();
		Step("数据检查");
		testCheckData(dburl0);
		Step("清除数据");
		testDeleData(dburl0);

	}

	@Subject("shard+rw事务，绕过分库分表规则的路由")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954102() {
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					insertData();
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("检查数据");
		testCheckData(dburl0);
		Step("清除数据");
		testDeleData(dburl0);
	}

	@Subject("shard+rw，绕过分库分表规则的路由,少了一个分库的条件，报异常")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954103() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("age", Integer.valueOf("11"));
		params.put("name", "test_ROUTE_CONDITION");

		Step("改变路由，少一个分库的条件");
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setVirtualTableName("user");
		simpleCondition.put("age", 10);
		ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, simpleCondition);
		try {
			sqlMap.insert("insertShardrwMysql", params);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(NestedSQLException.class, ex.getClass(), "异常验证");
		}
	}

	/**
	 * 插入数据
	 */
	private void insertData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("age", Integer.valueOf("11"));
		params.put("name", "test_ROUTE_CONDITION");

		Step("改变路由");
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setVirtualTableName("user");
		simpleCondition.put("user_id", 10);
		simpleCondition.put("age", 10);
		ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, simpleCondition);

		try {
			sqlMap.insert("insertShardrwMysql", params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 检查数据数量
	 * 
	 * @param dburl
	 */
	private void testCheckData(String dburl) {
		String sql = "select count(*) from user_0";
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
		String delStr = "delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);

	}

}
