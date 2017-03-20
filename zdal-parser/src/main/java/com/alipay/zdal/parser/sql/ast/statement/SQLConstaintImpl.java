/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLConstaintImpl.java, v 0.1 2012-11-17 ÏÂÎç3:21:08 xiaoqing.zhouxq Exp $
 */
@SuppressWarnings("serial")
public abstract class SQLConstaintImpl extends SQLObjectImpl implements SQLConstaint {

    private SQLName name;

    public SQLConstaintImpl() {

    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

}
