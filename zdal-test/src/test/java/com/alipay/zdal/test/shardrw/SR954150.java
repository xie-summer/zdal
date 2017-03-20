package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
@Feature("shard+rw重试")
public class SR954150 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String url0;
	private String url0_bac;
	private String user;
	private String psd;

	@Before
	public void beforeTestCase() {
		url0=ConstantsTest.mysql12UrlTranation0;
		url0_bac = ConstantsTest.mysql12UrlTranation0_bac;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwWriteReadRetry");
	}
	
	@After
	public void afterTestCase(){
		Step("删除数据");
		String delStr = "delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, url0, psd, user);
		ZdalTestCommon.dataUpdateJDBC(delStr, url0_bac, psd, user);
	}

	@Subject("shard+rw。写库，分库到group_0中 ds0:r2w10,ds2:r10w5,其中ds0不可用,全部写到ds2中")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954151() {
		Step("shard+rw。写库，分库到group_0中 ds0:r2w10,ds2:r10w5,其中ds0不可用,全部写到ds2中");
		int countA = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < 20; i++) {
			try {
				params.put("user_id", 10 * i);
				params.put("age", Integer.valueOf("10"));
				params.put("name", "test_address");
				sqlMap.insert("insertShardrwMysql", params);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Step("检查数据");
			String sqlStr = "select count(*) from user_0";
			ResultSet rs0 = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url0_bac,
					psd, user);
			try {
				rs0.next();
				countA = rs0.getInt(1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Assert.areEqual(true, countA == 20, "检查记录条数,countA="+countA);
	
	}
	
	
	@SuppressWarnings("unchecked")
	@Subject("shard+rw。读库，分库到group_0中 ds0:r2w10,ds2:r10w5,其中ds0不可用,全部从ds2中读")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954152(){
		Step("shard+rw。读库，分库到group_0中 ds0:r2w10,ds2:r10w5,其中ds0不可用,全部从ds2中读");
		int countA=0;
		int countB=0;
		Step("准备数据");
		String insertSql1="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values (10,10,'DB_A',now(),now())";
	    String insertSql2="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values (10,10,'DB_B',now(),now())";
	    ZdalTestCommon.dataUpdateJDBC(insertSql1, url0, psd, user);
	    ZdalTestCommon.dataUpdateJDBC(insertSql2, url0_bac, psd, user);
	    Step("读数据");
	    for(int readnum=0;readnum<20;readnum++){
	    try {
	    	List<Object> sr=(List<Object>)sqlMap.queryForList("selectShardrwMysql");
	    	for(int i=0;i<sr.size();i++){
	    		HashMap<String, String> hs = (HashMap<String, String>) sr
				.get(i);
		if ("DB_A".equalsIgnoreCase((String) hs.get("name"))) {
			countA++;
		} else if ("DB_B"
				.equalsIgnoreCase((String) hs.get("name"))) {
			countB++;
		}
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    Assert.areEqual(true, countB==20, "shardrw的读写重试,countB="+countB);
	    
	}

}
