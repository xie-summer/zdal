package com.alipay.zdal.test.sqlparser;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	SR956010.class
    ,SR956030.class,
	 SR956020.class
	
})
public class ZdalSqlParserSuite {
	
	public static ApplicationContext context;

	@BeforeClass
	public static void beforeTestClass() {
		String[] springXmlPath = {  "./sqlparser/spring-sqlparser-ds.xml" };
		context = new ClassPathXmlApplicationContext(springXmlPath);
		
	}

}
