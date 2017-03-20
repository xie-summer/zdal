package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.test.common.ZdalTestCommon;

@RunWith(ATSJUnitRunner.class)
@Feature("事务内的连续操作")
public class SR952090 {
	public TestAssertion Assert = new TestAssertion();
	private Connection cn = null;
	private Statement st = null;
	private ZdalDataSource zdalDataSource = null;

	@Before
	public void beforeTestCase() {
		zdalDataSource = (ZdalDataSource) ZdalRwSuite.context
				.getBean("zdalRwDSMysql2");
		
		try {
			cn = zdalDataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void afterTestCase() {
		try {
			ZdalTestCommon.dataDeleteForZds();

			st.close();
			cn.close();
			//zdalDataSource.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st = null;
			cn = null;
			zdalDataSource = null;
		}
	}

	
	@Subject("事务内先写后读,从写库里面读[目前只支持一个写库]，因为它是事务")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952091() {
		try {
			Step("事务内先写后读,从写库里面读[目前只支持一个写库]，因为它是事务");
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("insert into test1 value(111,'DB_G')");
			ResultSet rs = st
					.executeQuery("select count(*) from test1 where clum=111");
			rs.next();
			int count = rs.getInt(1);
			Step("从写库中读数据");
			Assert.areEqual(1, count, "从写库中读数据");

			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "非预期异常" + e);
		}
	}
	

	@Subject("事务内先读后写,从写库里面读[目前只支持一个写库]，因为它是事务")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952092() {
		Step("数据准备");
		ZdalTestCommon.dataPrepareForZds();
		Step("开启事务");
		try {
		
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("select count(*) from test1 where clum=100");
			st.execute("insert into test1 value(112,'DB_G')");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "非预期异常" + e);
		}
	}
	
	
	@Subject("事务内连续读,从写库里面读[目前只支持一个写库]，因为它是事务")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952093(){
		Step("数据准备");
		ZdalTestCommon.dataPrepareForZds();
		Step("开启事务");
		try {
		
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("select count(*) from test1 where clum=100");
			st.execute("select count(*) from test1 where clum=112");
			st.execute("select count(*) from test1 where clum=113");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "非预期异常" + e);
		}
	}

	@Subject("事务内连续写")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952094(){
		Step(" 开启事务");
		try {
			
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("insert into test1 value(112,'DB_G')");
			st.execute("insert into test1 value(113,'DB_G')");
			st.execute("insert into test1 value(114,'DB_G')");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "非预期异常" + e);
		}
	}
	
	@Subject("多事务连接同时关闭")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952095(){
		long cCount=1;
		try {
			cn.setAutoCommit(false);
			st =cn.createStatement();
			Step("开启1个事务且提交");
			st.execute("select * from test1 where clum = 12");
			st.execute("delete from test1 where clum = 12");
			cn.commit();
			cn.setAutoCommit(true);
			
			Step("开启另一个事务");
			Step("开启2个事务且提交");
			cn.setAutoCommit(false);
			java.sql.Statement st1 = cn.createStatement();
			st1.execute("select * from test1 where clum = 12");
			st1.execute("delete from test1 where clum = 12");
			cn.commit();
			cn.setAutoCommit(true);
			
			cn.close();
			cCount=zdalDataSource.getDataSourcesMap().get("ds1").getLocalTxDataSource().getPoolCondition().getInUseConnectionCount();		    
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.areEqual(Long.parseLong("0"), cCount, "所有连接已关闭");
	}
	
	
	

}
