package com.alipay.zdal.test.ut.client;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.common.lang.StringUtil;

public class Groovy {
	
	  public static List<String> cal() {
	        List<String> result = new ArrayList<String>();

	        for (int i = 0; i < 10; i++) {
	            result.add(StringUtil.alignRight(i + "", 2, '0'));
	        }
	        for (int i = 0; i < 100; i++) {
	            result.add(StringUtil.alignRight(i + "", 3, '0'));
	        }
	        return result;
	    }


}
