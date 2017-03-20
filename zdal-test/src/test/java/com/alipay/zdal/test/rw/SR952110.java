package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw数据源ds0:r10w10：使用事务模板：单数据源事务")
public class SR952110 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;
	String url;
	String user;
	String psd;

	@Before
	public void beforeTestCase() {
		url = ConstantsTest.mysq112UrlFail0;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalTransactionManager");
		tt = (TransactionTemplate) ZdalRwSuite.context
				.getBean("transactionTemplate1");
	}

	@Subject("正常事务:先select后delete")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952111() {
		Step(" 数据准备");
		ZdalTestCommon.dataPrepareForFail0();
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					String querySql = "rw-Select";
					String deleteSql = "rw-Delete";
					try {
						List<Object> res_1 = sqlMap.queryForList(querySql);
						Assert.areEqual(1, res_1.size(), "验证select结果非空");

						Step("事务内删除数据");
						int res_2 = sqlMap.delete(deleteSql);
						Assert.areEqual(1, res_2, "验证delete成功");
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	      Step("验证目前无数据");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "数据成功");
			Assert.areEqual(0, rs.getInt(1), "验证无数据");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Subject("正常事务:先insert后select")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952112() {
		try {
			Step("正常事务:先insert后select");
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";
					String selectSql = "rw-Select";
					try {
						 sqlMap.insert(insertSql);
						 List<Object> res_1 =sqlMap.queryForList(selectSql);
						 Assert.areEqual(1, res_1.size(), "验证select结果非空");						
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("验证目前有数据");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "数据成功");
			Assert.areEqual(1, rs.getInt(1), "验证无数据");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delStr="delete from master_0 where user_id=20";
		ZdalTestCommon.dataUpdateJDBC(delStr, url, psd, user);
		
	}


	@Subject("异常事务;插入两条主键冲突的sql，回滚")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952113() {
	Step("异常事务;插入两条主键冲突的sql，回滚");
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";					
					try {						
						sqlMap.insert(insertSql);
						sqlMap.insert(insertSql);						
										        
					} catch (SQLException e) {
						Step("需要回滚，去掉下面一句，会提交一条数据到db");
						status.setRollbackOnly();
						e.printStackTrace();
					}					
					return 0;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("验证目前无数据");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "数据成功");
			Assert.areEqual(0, rs.getInt(1), "验证无数据");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	
	
	@Subject("正常事务:先insert后delete")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952114() {
		Step("正常事务:先insert后delete");
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";
					String deleteSql = "rw-Delete";
					try {
						 sqlMap.insert(insertSql);
						 sqlMap.delete(deleteSql);					
					} catch (SQLException e) {
						status.setRollbackOnly();
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	
	
}
