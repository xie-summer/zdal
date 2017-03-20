/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import java.util.Map;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLObject.java, v 0.1 2012-11-17 обнГ3:13:13 xiaoqing.zhouxq Exp $
 */
public interface SQLObject {

    void accept(SQLASTVisitor visitor);

    SQLObject getParent();

    void setParent(SQLObject parent);

    Map<String, Object> getAttributes();

    Object getAttribute(String name);

    void putAttribute(String name, Object value);

    Map<String, Object> getAttributesDirect();

    void output(StringBuffer buf);
}
