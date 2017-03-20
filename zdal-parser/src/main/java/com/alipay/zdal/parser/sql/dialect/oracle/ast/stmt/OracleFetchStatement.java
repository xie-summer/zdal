/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleFetchStatement.java, v 0.1 2012-11-17 ÏÂÎç3:47:30 Exp $
 */
public class OracleFetchStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLName           cursorName;

    private List<SQLExpr>     into             = new ArrayList<SQLExpr>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, cursorName);
            acceptChild(visitor, into);
        }
        visitor.endVisit(this);
    }

    public SQLName getCursorName() {
        return cursorName;
    }

    public void setCursorName(SQLName cursorName) {
        this.cursorName = cursorName;
    }

    public List<SQLExpr> getInto() {
        return into;
    }

    public void setInto(List<SQLExpr> into) {
        this.into = into;
    }

}
