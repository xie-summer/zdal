package com.alipay.zdal.parser.output;

import java.util.List;
import java.util.Map;

public interface OutputHandler {
	public String handle(List<Object> param, Number skip,
                         Number max, Map<Integer, Object> changeParam);
}
