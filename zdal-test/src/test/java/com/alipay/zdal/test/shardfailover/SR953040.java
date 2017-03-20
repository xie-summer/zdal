package com.alipay.zdal.test.shardfailover;

import java.sql.Date;
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
@Feature("shard+Failover ,表后缀")
public class SR953040 {

	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;
	private String dbpsd;
	private String dbuser;
    private String dburl1;
    private String dburl2;
    private String dburlsample;

	@Before
	public void beforeTestCase(){
		dbpsd=ConstantsTest.mysq112Psd;
		 dbuser=ConstantsTest.mysq112User;
		 dburl1=ConstantsTest.mysql12UrlItemNumberId;
		 dburl2=ConstantsTest.mysql12UrlUserCharId;
		 dburlsample=ConstantsTest.mysql12UrlSample1;
		 	
	}
	
	@Subject("shard+failover，表后缀，dbIndexForEachDB。预计插入到0库的0表")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953041() {
		Step("shard+failover，表后缀，dbIndexForEachDB。预计插入到0库的0表");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
		.getBean("zdalShardfailoverTableSuffix1");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_char_id", Integer.valueOf("11"));
		params.put("AUCTION_ID", Integer.valueOf("10"));
		params.put("item_number_id", Integer.valueOf("11"));
		params.put("item_char_id",Integer.valueOf("11"));
		params.put("name", "test_address");
		
		try {
			sqlMap.insert("insertShardfailoverTableSuffix1", params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chackData(dburl1);
		deleteData(dburl1);
	}
	
	
		@Subject("shard+failover，表后缀，dbIndexForEachDB。预计插入到1库的1表")
		@Priority(PriorityLevel.HIGHEST)
		@Test
		public void TC953042() {
			Step("shard+failover，表后缀，dbIndexForEachDB。预计插入到1库的1表");
			sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
			.getBean("zdalShardfailoverTableSuffix1");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("user_char_id", Integer.valueOf("11"));
			params.put("AUCTION_ID", Integer.valueOf("11"));
			params.put("item_number_id", Integer.valueOf("11"));
			params.put("item_char_id",Integer.valueOf("11"));
			params.put("name", "test_address");
			
			try {
				sqlMap.insert("insertShardfailoverTableSuffix1", params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			chackData(dburl2);
			deleteData(dburl2);
	}
		
		@Subject("shard+failover，表后缀，twoColumnForEachDB。写1库的01_09表")
		@Priority(PriorityLevel.HIGHEST)
		@Test
		public void TC953043(){
			Step("shard+failover，表后缀，twoColumnForEachDB。写1库的01_09表");
			sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
			.getBean("zdalShardfailoverTableSuffix2");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("card_no", "2013101711");
			params.put("gmtTime", Date.valueOf("2013-10-17"));
			params.put("next_sync_time", Date.valueOf("2013-10-17"));
			
			try {
				sqlMap.insert("insertShardfailoverTableSuffix2",params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		
		/**
		 * 数据检查
		 * @param url
		 */
		private void chackData(String url){
			ResultSet rs;
			String sql1="select count(*) from auction_auctions_0";
			String sql2="select count(*) from auction_auctions_1";
			String sql3="select count(*) from card_no_month_01_09";
			if(url.equalsIgnoreCase(dburl1)){
				rs=ZdalTestCommon.dataCheckFromJDBC(sql1, dburl1, dbpsd, dbuser);	
			}else if(url.equalsIgnoreCase(dburl2)){
				rs=ZdalTestCommon.dataCheckFromJDBC(sql2, dburl2, dbpsd, dbuser);	
			}	else{
				rs=ZdalTestCommon.dataCheckFromJDBC(sql3, dburlsample, dbpsd, dbuser);
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
			String sql1="delete from auction_auctions_0";
			String sql2="delete from auction_auctions_1";
			String sql3="delete from card_no_month_01_09";
			if(url.equalsIgnoreCase(dburl1)){
				ZdalTestCommon.dataUpdateJDBC(sql1, dburl1, dbpsd, dbuser);
			}else if(url.equalsIgnoreCase(dburl2)){
				ZdalTestCommon.dataUpdateJDBC(sql2, dburl2, dbpsd, dbuser);	
			}	else{
				ZdalTestCommon.dataUpdateJDBC(sql3, dburlsample, dbpsd, dbuser);	
			}
		}		

}
