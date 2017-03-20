package com.alipay.zdal.test.rw;

import java.math.BigDecimal;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
@Feature("select from dual-rw数据源oracle：ds0:r10w10,ds1:r10w0")
public class SR952150 {
	private SqlMapClient sqlMap;
	public TestAssertion Assert = new TestAssertion();

	@SuppressWarnings("unchecked")
	@Subject("select nextval from dual")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952151() {
		String val = null;
		long currva = 0;
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalrwOracleNextval");

		try {
			List<Object> a = sqlMap.queryForList("selectNextval");
			for(int i=0;i<a.size();i++){
			HashMap<String, BigDecimal> hm = (HashMap<String, BigDecimal>) a.get(i);
			val=hm.get("VAL").toString();		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("通过jdbc查询目前的val值");
		String sql0="select ID.nextval from dual";
		String sql1="select ID.CURRVAL  from dual";
		String dburl=ConstantsTest.oralcePrefUrl;
		String dbpsd=ConstantsTest.oraclePrePsd;
		String dbuser=ConstantsTest.oraclePreUser1;
		Step("jdbc不能直接单查currentval，需要之前查询nextval");
		ResultSet rs=ZdalTestCommon.dualCheckJDBC(dburl, sql0, sql1, dbpsd, dbuser);
		try {
			Assert.isTrue(rs.next(),"currval 值查询");
			
			currva=rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.areEqual(currva, Long.parseLong(val)+1, "验证序列");
	}

}
