/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLTableSourceImpl.java, v 0.1 2012-11-17 ÏÂÎç3:27:16 Exp $
 */
public abstract class SQLTableSourceImpl extends SQLObjectImpl implements SQLTableSource {

    private static final long serialVersionUID = 1L;

    protected String          alias;

    protected List<SQLHint>   hints            = new ArrayList<SQLHint>(2);

    public SQLTableSourceImpl() {

    }

    public SQLTableSourceImpl(String alias) {

        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<SQLHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLHint> hints) {
        this.hints = hints;
    }

}
