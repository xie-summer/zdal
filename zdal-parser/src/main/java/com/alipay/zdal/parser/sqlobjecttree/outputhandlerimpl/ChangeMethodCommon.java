package com.alipay.zdal.parser.sqlobjecttree.outputhandlerimpl;

import java.util.List;
import java.util.Map;

import com.alipay.zdal.parser.output.OutputHandler;



public abstract class ChangeMethodCommon implements OutputHandler{

    @Override
    public String handle(List<Object> param, Number skip, Number max,
                         Map<Integer, Object> changeParam) {
        return null;
    }
	
}
