/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCreateIndexStatement.java, v 0.1 2012-11-17 ÏÂÎç3:21:19 xiaoqing.zhouxq Exp $
 */
public class SQLCreateIndexStatement extends SQLStatementImpl implements SQLDDLStatement {

    /**
     * 
     */
    private static final long          serialVersionUID = 1L;

    private SQLName                    name;

    private SQLName                    table;

    private List<SQLSelectOrderByItem> items            = new ArrayList<SQLSelectOrderByItem>();

    private String                     type;

    public SQLName getTable() {
        return table;
    }

    public void setTable(SQLName table) {
        this.table = table;
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
