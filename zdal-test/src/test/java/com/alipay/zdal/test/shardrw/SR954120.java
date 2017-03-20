package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
@Feature("shard+rw表后缀")
public class SR954120 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dbpsd;
	private String dbuser;
    private String dburl1;
    private String dburl2;
    
	@Before
	public void beforeTestCase(){
		dbpsd=ConstantsTest.mysq112Psd;
		 dbuser=ConstantsTest.mysq112User;
		 dburl1=ConstantsTest.mysql12UrlDocument0;
		 dburl2=ConstantsTest.mysql12UrlDocument1;
		 
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
		.getBean("zdalShardrwTableSuffix");
	}
	
	
	@Subject("shard+rw:表后缀throughalldb,插入到group_0的写库里面的00表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954121(){
		Step("shard+rw:表后缀throughalldb,插入到group_0的写库里面的00表");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("the_key", "1234");
		params.put("the_namespace", "5678");
		params.put("the_value", 2);
		
		try {
			sqlMap.insert("insertShardrwTableSuffix", params);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chackData(dburl1);
		deleteData(dburl1);
	}
	
	@Subject("shard+rw:表后缀throughalldb,插入到group_1的写库里面03表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954122(){
		Step("shard+rw:表后缀throughalldb,插入到group_1的写库里面03表");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("the_key", "1234");
		params.put("the_namespace", "5678");
		params.put("the_value", 3);
		
		try {
			sqlMap.insert("insertShardrwTableSuffix", params);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}	
		chackData(dburl2);
		deleteData(dburl2);
	}
	

	

	/**
	 * 数据检查
	 * @param url
	 */
	private void chackData(String url){
		ResultSet rs;
		String sql1="select count(*) from document_00";
		String sql2="select count(*) from document_03";
		if(url.equalsIgnoreCase(dburl1)){
			rs=ZdalTestCommon.dataCheckFromJDBC(sql1, dburl1, dbpsd, dbuser);	
		}else{
			rs=ZdalTestCommon.dataCheckFromJDBC(sql2, dburl2, dbpsd, dbuser);	
		}					 		
		try {
			rs.next();
			Assert.areEqual(1, rs.getInt(1), "数据检查");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * 删除数据
	 * @param url
	 */
	private void deleteData(String url){
		String sql1="delete from document_00";
		String sql2="delete from document_03";
		if(url.equalsIgnoreCase(dburl1)){
			ZdalTestCommon.dataUpdateJDBC(sql1, dburl1, dbpsd, dbuser);
		}else{
			ZdalTestCommon.dataUpdateJDBC(sql2, dburl2, dbpsd, dbuser);	
		}					 		
	}		


}
