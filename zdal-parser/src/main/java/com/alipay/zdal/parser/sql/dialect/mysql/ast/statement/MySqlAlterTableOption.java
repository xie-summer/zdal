/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlAlterTableOption.java, v 0.1 2012-11-17 ÏÂÎç3:31:39 Exp $
 */
public class MySqlAlterTableOption extends MySqlObjectImpl implements SQLAlterTableItem {

    private static final long serialVersionUID = 1L;

    private String            name;
    private String            value;

    public MySqlAlterTableOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public MySqlAlterTableOption() {
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
