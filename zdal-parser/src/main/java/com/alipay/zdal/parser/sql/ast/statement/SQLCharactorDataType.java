/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLDataTypeImpl;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCharactorDataType.java, v 0.1 2012-11-17 ÏÂÎç3:20:37 xiaoqing.zhouxq Exp $
 */
@SuppressWarnings("serial")
public class SQLCharactorDataType extends SQLDataTypeImpl {

    private String charSetName;
    private String collate;

    public SQLCharactorDataType(String name) {
        super(name);
    }

    public String getCharSetName() {
        return charSetName;
    }

    public void setCharSetName(String charSetName) {
        this.charSetName = charSetName;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

}
