package com.alipay.zdal.test.rw;

import java.sql.Connection;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestBase;
import com.alipay.zdal.test.common.ZdalTestCommon;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal针对mysql数据源的事务操作")
public class SR952080 extends ZdalTestBase {
	private Connection connection = null;
	private PreparedStatement ps=null;
	private ResultSet st=null;
	public TestAssertion Assert = new TestAssertion();

	@Before
	public void beforeTestCase() {
		
		zdalDataSource = (ZdalDataSource) ZdalRwSuite.context
		.getBean("zdalRwDSMysql2");
		try {
			connection = zdalDataSource.getConnection();			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void afterTestCase() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection = null;
			e.printStackTrace();
		}
	}

	@Subject("当事务中多条sql语句，有失败情况")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952081() {
		Step("当事务中多条sql语句，有失败情况");
		String sqlStr = "insert into test1 value(100,'DB_G')";
		try {
			connection.setAutoCommit(false);
			ps=connection.prepareStatement(sqlStr);
			ps.execute();					
			Thread.sleep(50);
			Step(" 插入冲突的数据");
            ps.execute();
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block		
			e.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			try {	
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Step("数据进行了回滚，检查数据");
		String sql="select count(*) from test1 where clum=100";
		String dburl=ConstantsTest.mysql12UrlZds2;
		String dbpsd=ConstantsTest.mysq112Psd;
		String dbuser=ConstantsTest.mysq112User;
		st=ZdalTestCommon.dataCheckFromJDBC(sql, dburl, dbpsd, dbuser);
		try {
			st.next();
			Assert.areEqual(0, st.getInt(1), "验证无数据");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
