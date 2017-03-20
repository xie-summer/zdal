/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlIndexHintImpl.java, v 0.1 2012-11-17 ÏÂÎç3:29:21 Exp $
 */
public abstract class MySqlIndexHintImpl extends MySqlObjectImpl implements MySqlIndexHint {

    private static final long     serialVersionUID = 1L;

    private MySqlIndexHint.Option option;

    private List<SQLName>         indexList        = new ArrayList<SQLName>();

    @Override
    public abstract void accept0(MySqlASTVisitor visitor);

    public MySqlIndexHint.Option getOption() {
        return option;
    }

    public void setOption(MySqlIndexHint.Option option) {
        this.option = option;
    }

    public List<SQLName> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<SQLName> indexList) {
        this.indexList = indexList;
    }

}
