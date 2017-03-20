/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObject;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLConstaint.java, v 0.1 2012-11-17 обнГ3:21:03 xiaoqing.zhouxq Exp $
 */
public interface SQLConstaint extends SQLObject {

    SQLName getName();

    void setName(SQLName value);
}
