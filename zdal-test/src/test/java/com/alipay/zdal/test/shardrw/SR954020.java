package com.alipay.zdal.test.shardrw;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static com.alipay.ats.internal.domain.ATS.Step;
import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

@RunWith(ATSJUnitRunner.class)
@Feature("全活策略: getAvailableDBAndTableByWeights")
public class SR954020 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td;

	@Before
	public void beforeTestCase() {
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
				.getBean("getAvailableDBAndTableByWeights");
	}

	@Subject("获取指定group中的逻辑表名对应的可用物理表")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954021() {
		Step("获取指定group中的逻辑表名对应的可用物理表");
		String[] re = td.getAvailableDBAndTableByWeights("users", 0, true);
		Assert.areEqual(true,
				Integer.parseInt(re[0]) == 0 || Integer.parseInt(re[0]) == 1,
				"获取可用表名");
		Assert.areEqual(
				true,
				re[1].equalsIgnoreCase("users_0")
						|| re[1].equalsIgnoreCase("users_1")
						|| re[1].equalsIgnoreCase("users_2")
						|| re[1].equalsIgnoreCase("users_3")
						|| re[1].equalsIgnoreCase("users_4"), "获取可用表名");
	}

	@Subject("指定的groupNum越界")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954022() {
		try{
			Step("指定的groupNum越界");
		 td.getAvailableDBAndTableByWeights("users", 99, true);
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.areEqual(IllegalArgumentException.class, ex.getClass(), "出现异常");
		}
	}
		
	@Subject("指定的逻辑表名不存在")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954023() {
		try{
			Step("指定的逻辑表名不存在");
		 td.getAvailableDBAndTableByWeights("userstest", 0, true);
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.areEqual(IllegalArgumentException.class, ex.getClass(), "出现异常");
		}
	}
	

}
