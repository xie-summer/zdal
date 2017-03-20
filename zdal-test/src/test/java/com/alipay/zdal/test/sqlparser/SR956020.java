package com.alipay.zdal.test.sqlparser;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import static com.alipay.ats.internal.domain.ATS.Step;
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
@Feature("shard数据源:复杂的select语句")
public class SR956020 {
	private String url0=ConstantsTest.mysq112UrlFail0;
	private String url1=ConstantsTest.mysq112UrlFail1;
	private String url2=ConstantsTest.mysql12UrlSample0;
	private String url3=ConstantsTest.mysql12UrlSample1;
	private String user=ConstantsTest.mysq112User;
	private String psd=ConstantsTest.mysq112Psd;
	
	
	
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private List<Object> rs = null;
	

	
	@Subject("插入到0库0表，执行:insert into student (id,age,name,content) values (#id#,#age#,'testname','testcontent')")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956021(){			
		sqlMap = (SqlMapClient) ZdalSqlParserSuite.context
		.getBean("zdalsqlParserMysql02");
		Step("插入0库的0表");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", 10);
		params.put("age", 10);
		try {
			 sqlMap.insert("zdalsqlParserMysql02InsertSql",
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("断言插入到0库0表");
		String selectSql="select count(*) from student_0 where id=10 and age=10";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBCOracle(selectSql,url0,psd,user);
		try {
			Assert.isTrue(rs.next(), "验证查询结果");
			Assert.areEqual(1, rs.getInt(1), "插入到0库0表，执行:insert into student (id,age,name,content) values (#id#,#age#,'testname','testcontent')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Step("清除数据");
		String deleteSql="delete from student_0";
		ZdalTestCommon.dataUpdateJDBC(deleteSql, url0, psd, user);
	}
	
	@Subject("插到1库1表，执行:insert into student (id,age,name,content) values (#id#,#age#,'testname','testcontent')")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956022(){			
		sqlMap = (SqlMapClient) ZdalSqlParserSuite.context
		.getBean("zdalsqlParserMysql02");
		Step("插入1库的1表");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", 11);
		params.put("age", 11);
		try {
			 sqlMap.insert("zdalsqlParserMysql02InsertSql",
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("断言插入到1库1表");
		String selectSql="select count(*) from student_1 where id=11 and age=11";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBCOracle(selectSql,url1,psd,user);
		try {
			Assert.isTrue(rs.next(), "验证查询结果");
			Assert.areEqual(1, rs.getInt(1), "插到1库1表，执行:insert into student (id,age,name,content) values (#id#,#age#,'testname','testcontent')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("清除数据");
		String deleteSql="delete from student_1";
		ZdalTestCommon.dataUpdateJDBC(deleteSql, url1, psd, user);
	}
	
	@Subject("执行sql语句，对1个库的2个表，进行merge")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956023(){
		Step("在1库的10表和09表中分别插入数据");
		String insertSql1="insert into card_no_month_01_10(card_no,gmtTime,next_sync_time) values('2013101711','2013-11-22','2013-11-22')";
	    String insertSql2="insert into card_no_month_01_09(card_no,gmtTime,next_sync_time) values('2013101711','2013-10-22','2013-10-22')";
	    ZdalTestCommon.dataUpdateJDBC(insertSql1, url3, psd, user);
	    ZdalTestCommon.dataUpdateJDBC(insertSql2, url3, psd, user);
	    Step("查询这两条记录结果，需要将两条结果进行merge");
	    sqlMap = (SqlMapClient) ZdalSqlParserSuite.context
		.getBean("zdalsqlParserMysql03");
	    HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("card_no", "2013101711");
		params.put("gmtTime1", Date.valueOf("2013-05-17"));
		params.put("gmtTime2", Date.valueOf("2013-12-22"));
	    try {
			rs=(List<Object>)sqlMap.queryForList("zdalsqlParserMysql03SelectSql",params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap hs=(HashMap)rs.get(0);		
		Assert.areEqual("2", hs.get("count").toString(), "对1个库的2个表进行select语句，查询结果需要merge");
		Step("清除数据");
		String deleteSql="delete from card_no_month_01_09";
		String deleteSql2="delete from card_no_month_01_10";
		ZdalTestCommon.dataUpdateJDBC(deleteSql, url3, psd, user);
		ZdalTestCommon.dataUpdateJDBC(deleteSql2, url3, psd, user);
	}

}
