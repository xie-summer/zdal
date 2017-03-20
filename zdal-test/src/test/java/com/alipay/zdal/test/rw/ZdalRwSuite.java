package com.alipay.zdal.test.rw;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(Suite.class)
@Suite.SuiteClasses( {

                      //rw
                      SR952140.class, SR952050.class, SR952150.class, SR952160.class,
                      SR952170.class, SR952010.class, SR952020.class, SR952060.class,
                      SR952070.class, SR952030.class, SR952040.class, SR952130.class,
                      SR952090.class, SR952080.class, SR952100.class, SR952110.class,
                      SR952120.class, SR952190.class, SR952200.class

}

)
public class ZdalRwSuite {

    public static ApplicationContext context;

    @BeforeClass
    public static void beforeTestClass() {
        String[] springXmlPath = { "./rw/spring-rw-ds.xml" };
        context = new ClassPathXmlApplicationContext(springXmlPath);
    }

}
