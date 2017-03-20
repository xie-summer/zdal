/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlResetStatement.java, v 0.1 2012-11-17 ÏÂÎç3:34:07 Exp $
 */
public class MySqlResetStatement extends MySqlStatementImpl {
    private static final long serialVersionUID = 1L;

    private List<String>      options          = new ArrayList<String>();

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
    }
}
