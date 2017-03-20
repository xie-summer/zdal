package com.alipay.zdal.test.shardfailover;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



@RunWith(Suite.class)
@Suite.SuiteClasses({ 

//shard+failover
	SR953010.class,
	SR953020.class,
	SR953030.class,
	SR953040.class,
	SR953050.class,
	SR953060.class,
	SR953070.class
}
)

public class ZdalShardfailoverSuite {
	public static ApplicationContext context;
	
	@BeforeClass
	public static void beforeTestClass() {
		String[] springXmlPath = {
				"./shardfailover/spring-shardfailover-ds.xml" };
		context = new ClassPathXmlApplicationContext(springXmlPath);

	}
}
