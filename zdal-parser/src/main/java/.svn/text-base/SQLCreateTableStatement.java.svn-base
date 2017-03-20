/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLStatementImpl;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLCreateTableStatement.java, v 0.1 2012-11-17 ÏÂÎç3:21:25 xiaoqing.zhouxq Exp $
 */
@SuppressWarnings("serial")
public class SQLCreateTableStatement extends SQLStatementImpl implements SQLDDLStatement {

    protected Type                  type;
    protected SQLExprTableSource    tableSource;

    protected List<SQLTableElement> tableElementList = new ArrayList<SQLTableElement>();

    public SQLCreateTableStatement() {

    }

    public SQLName getName() {
        if (tableSource == null) {
            return null;
        }

        return (SQLName) tableSource.getExpr();
    }

    public void setName(SQLName name) {
        this.setTableSource(new SQLExprTableSource(name));
    }

    public SQLExprTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLExprTableSource tableSource) {
        this.tableSource = tableSource;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        GLOBAL_TEMPORARY, LOCAL_TEMPORARY
    }

    public List<SQLTableElement> getTableElementList() {
        return tableElementList;
    }

    @Override
    public void output(StringBuffer buf) {
        buf.append("CREATE TABLE ");
        if (Type.GLOBAL_TEMPORARY.equals(this.type)) {
            buf.append("GLOBAL TEMPORARY ");
        } else if (Type.LOCAL_TEMPORARY.equals(this.type)) {
            buf.append("LOCAL TEMPORARY ");
        }

        this.tableSource.output(buf);
        buf.append(" ");

        buf.append("(");
        for (int i = 0, size = tableElementList.size(); i < size; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            tableElementList.get(i).output(buf);
        }
        buf.append(")");
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSource);
            this.acceptChild(visitor, tableElementList);
        }
        visitor.endVisit(this);
    }
}
