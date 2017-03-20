package com.alipay.zdal.test.rw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
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
@Feature("rw读写分享的优先级,读优先级为p,写优先级为q。测试写")
public class SR952040 {

	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private int countA = 0;
	private int countB = 0;

	private String url1;
	private String url2;
	private String user;
	private String psd;

	@Before
	public void beforeTestCase() {
		url1 = ConstantsTest.mysql12UrlZds1;
		url2 = ConstantsTest.mysql12UrlZds2;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
	}

	@After
	public void afterTestCase() {
		ZdalTestCommon.dataDeleteForZds();
	}

	@Subject("写:ds0:r1w1q0,ds1:r1w1q1，全部写入ds0中")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952041() {
		Step("写:ds0:r1w1q0,ds1:r1w1q1，全部写入ds0中");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority5");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==10&&countB==0, "验证优先级的写");
	}
	
	
	@Subject("写:ds0:r1w1q1,ds1:r1w1q1，优先级一样，按比例分配")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952042() {
		Step("写:ds0:r1w1q1,ds1:r1w1q1，优先级一样，按比例分配");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority6");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA<10&&countB<10, "验证优先级的写,countA:"+countA+",countB:"+countB);
	}
	
	
	@Subject("写:ds0:r1w0q0,ds1:r1w1q1，ds0不可用。全部写到ds1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952043() {
		Step("写:ds0:r1w0q0,ds1:r1w1q1，ds0不可用。全部写到ds1");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority7");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==0&&countB ==10, "验证优先级的写");
	}
	
	
	@Subject("写:ds0:r2w4p0q1,ds1:r1w1p1q0，全部写到ds1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952044() {
		Step("写:ds0:r2w4p0q1,ds1:r1w1p1q0，全部写到ds1");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority8");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==0&&countB ==10, "验证优先级的写");
	}
	
	/**
	 * 连续写库
	 * @param sqlMa
	 */
	private void testWriteDb(SqlMapClient sqlMa){
		//countA=0;
		//countB=0;
		// 连续写10次
		for (int i = 1; i <= 10; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", i);
			try {
				sqlMa.insert("insertRwSql", params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		String querySql = "select count(*) from test1 where colu2 = 'DB_G'";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, url1, psd,
				user);
		ResultSet rs2 = ZdalTestCommon.dataCheckFromJDBC(querySql, url2, psd,
				user);
		try {
			Assert.areEqual(true, rs.next() && rs2.next(), "the value");
			countA = rs.getInt(1);
			countB = rs2.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
