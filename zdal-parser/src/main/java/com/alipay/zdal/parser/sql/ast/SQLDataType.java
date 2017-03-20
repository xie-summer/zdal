/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast;

import java.util.List;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLDataType.java, v 0.1 2012-11-17 обнГ3:11:38 xiaoqing.zhouxq Exp $
 */
public interface SQLDataType extends SQLObject {

    String getName();

    void setName(String name);

    List<SQLExpr> getArguments();
}
