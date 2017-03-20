/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObjectImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLAlterTableAddIndex.java, v 0.1 2012-11-17 ÏÂÎç3:20:02 xiaoqing.zhouxq Exp $
 */
public class SQLAlterTableAddIndex extends SQLObjectImpl implements SQLAlterTableItem {

    private static final long          serialVersionUID = 1L;

    private SQLName                    name;

    private List<SQLSelectOrderByItem> items            = new ArrayList<SQLSelectOrderByItem>();

    private String                     type;

    @Override
    protected void accept0(SQLASTVisitor visitor) {

    }

    public List<SQLSelectOrderByItem> getItems() {
        return items;
    }

    public void setItems(List<SQLSelectOrderByItem> items) {
        this.items = items;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
