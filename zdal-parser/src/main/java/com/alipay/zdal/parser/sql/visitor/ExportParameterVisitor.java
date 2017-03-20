/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import java.util.List;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ExportParameterVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:55:54 Exp $
 */
public interface ExportParameterVisitor extends SQLASTVisitor {
    List<Object> getParameters();
}
