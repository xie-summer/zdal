package com.alipay.zdal.test.ut.datasource;

import static com.alipay.ats.internal.domain.ATS.Step;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
/**
 * 创建zdatasource同时传入valve参数
 * @author yin.meng
 *
 */
@RunWith(ATSJUnitRunner.class)
@Feature("创建zdatasource同时传入valve参数")
public class ZDS951050 extends ZDSTest{
	ZDataSource zDataSource = null;
	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
	
	@Before
	public void zdsSetUp() throws Exception{
		localTxDSDo.setConnectionURL("jdbc:oracle:thin:@10.253.94.6:1521:perfdb6");
		localTxDSDo.setDriverClass("oracle.jdbc.OracleDriver");
		try {
			localTxDSDo.setEncPassword("-7cda29b2eef25d0e");
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		localTxDSDo.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
		localTxDSDo.setMaxPoolSize(12);
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");
	}
	
	@After
	public void zdsTearDown() {		
	    try {
			zDataSource.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /*@Subject("不配置valve参数")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951051() {		
    	Step("创建数据源");
    	try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("获取valve参数");
		Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   noLimit    = new ThresholdAndPeriod(-1, -1);
	    HashMap<String, ThresholdAndPeriod> emptyTable = new HashMap<String, ThresholdAndPeriod>();
	    
	    Step("验证valve参数");
	    Assert.isTrue(sqlLimit.equals(noLimit),"校验sql限流");
	    Assert.isTrue(txLimit.equals(txLimit),"校验tx限流");
	    Assert.isTrue(tableLimit.equals(emptyTable),"校验table限流");	    
	}*/

    @Subject("配置合法valve参数")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951052() {
		Step("准备valve参数");
		
		Step("创建数据源");
		try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("获取valve参数");
		/*Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   Limit1    = new ThresholdAndPeriod(1, 1);
	    ThresholdAndPeriod   Limit2    = new ThresholdAndPeriod(1, 60);
	    HashMap<String, ThresholdAndPeriod> Limit3 = new HashMap<String, ThresholdAndPeriod>();
	    Limit3.put("t1",Limit1);
	    Limit3.put("t2", Limit2);
	    
		Step("验证valve参数");
        Assert.isTrue(sqlLimit.equals(Limit1),"校验sql限流");
	    Assert.isTrue(txLimit.equals(Limit2),"校验tx限流");
	    Assert.isTrue(tableLimit.equals(Limit3),"校验table限流");*/
	}
	
    @Subject("配置不合法valve参数")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951053() {		
		Step("准备valve参数");
		
		Step("创建数据源");
		try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("获取valve参数");
		/*Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   noLimit    = new ThresholdAndPeriod(-1, -1);
	    HashMap<String, ThresholdAndPeriod> emptyTable = new HashMap<String, ThresholdAndPeriod>();
	    
		Step("验证valve参数");
	    Assert.isTrue(sqlLimit.equals(noLimit),"校验sql限流");
	    Assert.isTrue(txLimit.equals(txLimit),"校验tx限流");
	    Assert.isTrue(tableLimit.equals(emptyTable),"校验table限流");	    */
	}
}
