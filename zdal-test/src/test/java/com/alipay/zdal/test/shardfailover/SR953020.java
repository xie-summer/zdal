package com.alipay.zdal.test.shardfailover;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
@Feature("shard+Failover ,直接绕过分库分表规则的路由")
public class SR953020 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private TransactionTemplate tt;
	private String dburl1;
	private String dbpsd;
	private String dbuser;

	@Before
	public void beforeTestCase() {
		dburl1 = ConstantsTest.mysq112UrlTddl0;
		dbpsd = ConstantsTest.mysq112Psd;
		dbuser = ConstantsTest.mysq112User;
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalShardfailoverShardDbShardTable");
		tt = (TransactionTemplate) ZdalShardfailoverSuite.context
		.getBean("shardfailovertransactionTemplate1");
	}

	@After
	public void afterTestCase() {
		ThreadLocalMap.reset();
	}

	@Subject("shard+failover，绕过分库分表规则的路由")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953021() {
		insertData();
		Step("数据验证");
		testCheckData(dburl1);
		Step("消除数所");
		testDeleData(dburl1);
	}

	@Subject("shard+failover，绕过分库分表规则的路由,逻辑表名出错，抛异常")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953022() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_ROUTE_CONDITION");
		params.put("address", "test_ROUTE_CONDITION");
		Step("改变路由");
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setVirtualTableName("users001");
		simpleCondition.put("user_id", 10);
		ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, simpleCondition);

		try {
			sqlMap.insert("insertShardfailoverMysql", params);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(NestedSQLException.class, ex.getClass(), "异常信息");
		}
	}

	@Subject("shard+failover事务，绕过分库分表规则的路由")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953023(){
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
		Step("数据验证");
		testCheckData(dburl1);
		Step("消除数据");
		testDeleData(dburl1);
		
	}
	/**
	 * 插入数据
	 */
	private void insertData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("11"));
		params.put("name", "test_ROUTE_CONDITION");
		params.put("address", "test_ROUTE_CONDITION");
		// 改变路由
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setVirtualTableName("users");
		simpleCondition.put("user_id", 10);
		ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, simpleCondition);

		try {
			sqlMap.insert("insertShardfailoverMysql", params);
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
		String sql = "select count(*) from users_0";
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
		String delStr = "delete from users_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);

	}

}
