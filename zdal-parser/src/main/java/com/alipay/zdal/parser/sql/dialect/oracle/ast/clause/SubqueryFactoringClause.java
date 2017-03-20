/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQuery;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SubqueryFactoringClause.java, v 0.1 2012-11-17 ÏÂÎç3:42:38 Exp $
 */
public class SubqueryFactoringClause extends OracleSQLObjectImpl {

    private static final long serialVersionUID = 1L;

    private final List<Entry> entries          = new ArrayList<Entry>();

    public List<Entry> getEntries() {
        return entries;
    }

    public static class Entry extends OracleSQLObjectImpl {

        private static final long   serialVersionUID = 1L;

        private SQLIdentifierExpr   name;
        private final List<SQLName> columns          = new ArrayList<SQLName>();
        private SQLSelectQuery      subQuery;
        private SearchClause        searchClause;
        private CycleClause         cycleClause;

        public CycleClause getCycleClause() {
            return cycleClause;
        }

        public void setCycleClause(CycleClause cycleClause) {
            this.cycleClause = cycleClause;
        }

        public SearchClause getSearchClause() {
            return searchClause;
        }

        public void setSearchClause(SearchClause searchClause) {
            this.searchClause = searchClause;
        }

        public SQLIdentifierExpr getName() {
            return name;
        }

        public void setName(SQLIdentifierExpr name) {
            this.name = name;
        }

        public SQLSelectQuery getSubQuery() {
            return subQuery;
        }

        public void setSubQuery(SQLSelectQuery subQuery) {
            this.subQuery = subQuery;
        }

        public List<SQLName> getColumns() {
            return columns;
        }

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, name);
                acceptChild(visitor, columns);
                acceptChild(visitor, subQuery);
            }
            visitor.endVisit(this);
        }

    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, entries);
        }
        visitor.endVisit(this);
    }
}
