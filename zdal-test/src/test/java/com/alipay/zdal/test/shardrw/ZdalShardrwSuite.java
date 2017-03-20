package com.alipay.zdal.test.shardrw;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@RunWith(Suite.class)
@Suite.SuiteClasses({ 
//shard+rw
	
	SR954010.class,SR954020.class,SR954030.class,SR954040.class,
	SR954050.class,SR954060.class,
	
	SR954070.class,SR954080.class,
	SR954090.class,SR954100.class,
	SR954110.class
	,SR954120.class,
	SR954130.class,SR954140.class,SR954150.class

}
)
public class ZdalShardrwSuite {
  
	public static ApplicationContext context;

	@BeforeClass
	public static void beforeTestClass() {
		String[] springXmlPath = {  "./shardrw/spring-shardrw-ds.xml" };
		context = new ClassPathXmlApplicationContext(springXmlPath);
		
	}
}
