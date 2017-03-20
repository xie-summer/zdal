/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import java.util.List;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLEvalVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:56:41 Exp $
 */
public interface SQLEvalVisitor extends SQLASTVisitor {

    public static final String EVAL_VALUE = "eval.value";

    List<Object> getParameters();

    void setParameters(List<Object> parameters);

    int incrementAndGetVariantIndex();

    boolean isMarkVariantIndex();

    void setMarkVariantIndex(boolean markVariantIndex);
}
