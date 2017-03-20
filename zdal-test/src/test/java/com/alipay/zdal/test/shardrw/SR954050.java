package com.alipay.zdal.test.shardrw;

import java.util.HashMap;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

@RunWith(ATSJUnitRunner.class)
@Feature("全活策略: GetAvailableGroupDBAndTable")
public class SR954050 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td;
    
	@Subject("获取user_id的值确定groupnum，然后逻辑表名对应的可用物理表")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954052() {
		Step("获取user_id的值确定groupnum，然后逻辑表名对应的可用物理表");
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
		.getBean("getAvailableGroupDBAndTable");
		Map<String,String> map = new HashMap<String, String>();
		map.put("user_id", "5");
		String[] re = td.getAvailableGroupDBAndTable("users", map, true);
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
}
