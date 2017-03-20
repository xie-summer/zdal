package com.alipay.zdal.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DbConfig.class})
public class DataSourceTest {

	public static void main(String[] args) {
		
		SpringApplication.run(DataSourceTest.class, args);
	}

}
