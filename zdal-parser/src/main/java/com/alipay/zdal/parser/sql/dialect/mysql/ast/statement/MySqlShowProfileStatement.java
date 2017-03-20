/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlShowProfileStatement.java, v 0.1 2012-11-17 ÏÂÎç3:37:54 Exp $
 */
public class MySqlShowProfileStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private List<Type>        types            = new ArrayList<Type>();

    private SQLExpr           forQuery;

    private Limit             limit;

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public SQLExpr getForQuery() {
        return forQuery;
    }

    public void setForQuery(SQLExpr forQuery) {
        this.forQuery = forQuery;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public static enum Type {
        ALL("ALL"), BLOCK_IO("BLOCK IO"), CONTEXT_SWITCHES("CONTEXT SWITCHES"), CPU("CPU"), IPC(
                                                                                                "IPC"), MEMORY(
                                                                                                               "MEMORY"), PAGE_FAULTS(
                                                                                                                                      "PAGE FAULTS"), SOURCE(
                                                                                                                                                             "SOURCE"), SWAPS(
                                                                                                                                                                              "SWAPS");

        public final String name;

        Type(String name) {
            this.name = name;
        }
    }

}
