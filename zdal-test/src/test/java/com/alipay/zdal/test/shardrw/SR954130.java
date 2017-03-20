package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+rw事务的访问")
public class SR954130 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;
	private String dburl ;
	private String dburl2;
	private String dbpsd ;
	private String dbuser ;

	@Before
	public void beforeTestCase() {
		 dburl = ConstantsTest.mysql12UrlTranation0;
		 dburl2=ConstantsTest.mysql12UrlTranation1;
		 dbpsd = ConstantsTest.mysq112Psd;
		 dbuser = ConstantsTest.mysq112User;
		 
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwShardDbShardTable");
		tt = (TransactionTemplate) ZdalShardrwSuite.context
				.getBean("shardrwtransactionTemplate1");
	}

	@Subject("shard+rw:zdal事务进行输入，预期写入group_0的ds0库0表的测试，实际在读取时读ds0的0表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954131() {

		testTransactionInsertSelect();

		Step("数据验证，清除数据");
		testCheckData();
	}

	@Subject("shard+rw:zdal事务进行输入，预期写入group_0的ds0库0表的测试，实际在读取时读ds0的1表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954132() {

		testTransactionInsertSelectOther();

		Step("数据验证，清除数据");
		testCheckData();
	}

	@Subject("shard+rw:zdal事务进行输入，预期写入group_0的ds0库0表的测试，当读时读其它group_1，则此时读该group_1的写库")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954133() {

		testTransactionInsertSelectOther2();

		Step("数据验证，清除数据");
		testCheckData();
	}

	@Subject("shard+rw:zdal事务进行输入，两次写入的group不同")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954134() {

		Step("shard+rw:zdal事务进行输入，两次写入的group不同");
		testTransactionInsertInsert();
		
		String delSql="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl2, dbpsd, dbuser);
	}

	/**
	 * 先insert后select的事务。在事务中的读，当读操作分到同一个group中时，读的是当前写库
	 */
	private void testTransactionInsertSelect() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysql";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);
						Assert.areEqual(1, res_1.size(), "验证select结果非空");

					} catch (SQLException e) {
						status.setRollbackOnly();
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 写库到group的user_0表，读取同一个group的user_1表
	 */
	private void testTransactionInsertSelectOther() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysqlOther";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);
						Step("此时读取的是ds0的user_1表");
						Assert.areEqual(0, res_1.size(), "验证select结果非空");

					} catch (SQLException e) {
						status.setRollbackOnly();
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 写库到group的user_0表，读取不同的group的user_0表
	 */
	private void testTransactionInsertSelectOther2() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysqlOther2";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);

						Assert.areEqual(0, res_1.size(), "验证select结果非空,"+res_1.size());

					} catch (SQLException e) {
						status.setRollbackOnly();
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 前后两次插入的sql，按分库规则，归属在不同的group上
	 */
	private void testTransactionInsertInsert() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					Map<String, Object> params2 = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "testA");
					params2.put("user_id", 11);
					params2.put("age", 10);
					params.put("name", "testB");
					String insertSql = "insertShardrwMysql";
					try {
						sqlMap.insert(insertSql, params);
						sqlMap.insert(insertSql, params2);

					} catch (Exception e) {
						status.setRollbackOnly();
						e.printStackTrace();
						Assert.areEqual(NestedSQLException.class, e.getClass(),
								"异常");
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 检查数据数量,之后删除数据
	 * 
	 * @param dburl
	 */
	private void testCheckData() {
		
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
		String delStr = "delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);
	}

}
