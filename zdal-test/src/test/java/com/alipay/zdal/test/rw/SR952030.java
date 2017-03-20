package com.alipay.zdal.test.rw;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.List;
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
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("读写分享的优先级,读优先级为p,写优先级为q。测试读")
public class SR952030 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private int countA = 0;
	private int countB = 0;

	@Before
	public void beforeTestCase() {
		// 数据准备
		ZdalTestCommon.dataPrepareForZds();
	}

	@After
	public void afterTestCase() {
		ZdalTestCommon.dataDeleteForZds();
	}

	
	@Subject("读:ds0:r1w1p0,ds1:r1w0p1，若ds0库正常，则操作都落到高优先级的库")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952031() {
        Step("读:ds0:r1w1p0,ds1:r1w0p1，若ds0库正常，则操作都落到高优先级的库");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority1");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA == 10 && countB == 0, "countA值");
	}

	@Subject("读:ds0:r1w1p1,ds1:r1w0p1，优先级相等，则按比例分配")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952032() {
		Step("读:ds0:r1w1p1,ds1:r1w0p1，优先级相等，则按比例分配");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority2");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA < 10 && countB < 10, "countA值");
	}

	@Subject("读:ds0:r1w1p0,ds1:r1w0p0，优先级相等，则按比例分配")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952033() {
		Step("读:ds0:r1w1p0,ds1:r1w0p0，优先级相等，则按比例分配");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority3");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA < 10 && countB < 10, "countA值");
	}

	@Subject("读:ds0:r0w1p0,ds1:r1w0p1，ds0不可用，直接从ds1中读取")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952034() {
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority4");
		testReadDb(sqlMap);

		Assert.areEqual(true, countA == 0 && countB == 10, "countA值");

	}
	
	
	@Subject("读:ds0:r2w4p0q1,ds1:r1w1p1q0，全部从ds0中读取")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952035() {
		Step("读:ds0:r2w4p0q1,ds1:r1w1p1q0，全部从ds0中读取");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority8");
		testReadDb(sqlMap);

		Assert.areEqual(true, countA == 10 && countB == 0, "countA值");

	}
	
	

	

	/**
	 * 连续读取db
	 * 
	 * @param sqlMa
	 */
	@SuppressWarnings("unchecked")
	public void testReadDb(SqlMapClient sqlMa) {
		countA = 0;
		countB = 0;
		// 连续读10次
		for (int i = 0; i < 10; i++) {
			try {
				List<Object> a = (List<Object>) sqlMa
						.queryForList("queryRwSql");
				for (int j = 0; j < a.size(); j++) {
					HashMap<String, String> hs = (HashMap<String, String>) a
							.get(j);
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
	}

}
