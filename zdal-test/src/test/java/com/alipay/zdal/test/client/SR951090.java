package com.alipay.zdal.test.client;

import org.junit.Test;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;

@RunWith(ATSJUnitRunner.class)
@Feature("从zdc上拉配置文件")
public class SR951090 {
	
	@Subject("从zdc上获取配置文件")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951091(){
		Step("从zdc上获取配置文件");
	//	ZdalDataSource zd = (ZdalDataSource) ZdalTestSuite.context
	//	.getBean("zdalClientGetFileFromZdc");
		
	}

}
