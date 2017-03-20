package com.alipay.zdal.test.ut.client;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.List;

import org.junit.Test;

public class GroovyTest {
	@Test
	public void test001(){
		
		 String groovy = "return com.alipay.zdal.test.ut.client.Groovy.cal();";
	        Binding binding = new Binding();
	        GroovyShell shell = new GroovyShell(binding);
	        Object result = shell.evaluate(groovy);
	        if (result instanceof List) {
	            List<String> rr = (List<String>) result;
	            for (String integer : rr) {
	                System.out.println(integer);
	            }
	        }
		
	}

}
