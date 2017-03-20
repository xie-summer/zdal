/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import com.alipay.zdal.parser.sql.ast.SQLCommentHint;
import com.alipay.zdal.parser.sql.ast.SQLDataType;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.ast.expr.SQLAggregateExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLAllColumnExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLAllExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLAnyExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBetweenExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBitStringLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCaseExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCastExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLDateLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLDefaultExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLExistsExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLHexExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLHexStringLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLInListExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLInSubQueryExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIntegerExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIntervalLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLListExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLMethodInvokeExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNotExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNullExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNumberExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLPropertyExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLQueryExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLSomeExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLUnaryExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.ast.statement.NotNullConstraint;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableAddColumn;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLAssignItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLCallStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.ast.statement.SQLCommentStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateViewStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDeleteStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDropIndexStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDropTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDropViewStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLJoinTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLReleaseSavePointStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLRollbackStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSavePointStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectGroupByClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectOrderByItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSetStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSubqueryTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableElement;
import com.alipay.zdal.parser.sql.ast.statement.SQLTruncateStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLUnionQuery;
import com.alipay.zdal.parser.sql.ast.statement.SQLUniqueConstraint;
import com.alipay.zdal.parser.sql.ast.statement.SQLUpdateSetItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLUpdateStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLUseStatement;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLASTVisitorAdapter.java, v 0.1 2012-11-17 ÏÂÎç3:56:36 Exp $
 */
public class SQLASTVisitorAdapter implements SQLASTVisitor {

    public void endVisit(SQLAllColumnExpr x) {
    }

    public void endVisit(SQLBetweenExpr x) {
    }

    public void endVisit(SQLBinaryOpExpr x) {
    }

    public void endVisit(SQLCaseExpr x) {
    }

    public void endVisit(SQLCaseExpr.Item x) {
    }

    public void endVisit(SQLCharExpr x) {
    }

    public void endVisit(SQLIdentifierExpr x) {
    }

    public void endVisit(SQLInListExpr x) {
    }

    public void endVisit(SQLIntegerExpr x) {
    }

    public void endVisit(SQLExistsExpr x) {
    }

    public void endVisit(SQLNCharExpr x) {
    }

    public void endVisit(SQLNotExpr x) {
    }

    public void endVisit(SQLNullExpr x) {
    }

    public void endVisit(SQLNumberExpr x) {
    }

    public void endVisit(SQLPropertyExpr x) {
    }

    public void endVisit(SQLSelectGroupByClause x) {
    }

    public void endVisit(SQLSelectItem x) {
    }

    public void endVisit(SQLSelectStatement selectStatement) {
    }

    public void postVisit(SQLObject astNode) {
    }

    public void preVisit(SQLObject astNode) {
    }

    public boolean visit(SQLAllColumnExpr x) {
        return true;
    }

    public boolean visit(SQLBetweenExpr x) {
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return true;
    }

    public boolean visit(SQLCaseExpr x) {
        return true;
    }

    public boolean visit(SQLCaseExpr.Item x) {
        return true;
    }

    public boolean visit(SQLCastExpr x) {
        return true;
    }

    public boolean visit(SQLCharExpr x) {
        return true;
    }

    public boolean visit(SQLExistsExpr x) {
        return true;
    }

    public boolean visit(SQLIdentifierExpr x) {
        return true;
    }

    public boolean visit(SQLInListExpr x) {
        return true;
    }

    public boolean visit(SQLIntegerExpr x) {
        return true;
    }

    public boolean visit(SQLNCharExpr x) {
        return true;
    }

    public boolean visit(SQLNotExpr x) {
        return true;
    }

    public boolean visit(SQLNullExpr x) {
        return true;
    }

    public boolean visit(SQLNumberExpr x) {
        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        return true;
    }

    public boolean visit(SQLSelectGroupByClause x) {
        return true;
    }

    public boolean visit(SQLSelectItem x) {
        return true;
    }

    public void endVisit(SQLCastExpr x) {
    }

    public boolean visit(SQLSelectStatement astNode) {
        return true;
    }

    public void endVisit(SQLAggregateExpr x) {
    }

    public boolean visit(SQLAggregateExpr x) {
        return true;
    }

    public boolean visit(SQLVariantRefExpr x) {
        return true;
    }

    public void endVisit(SQLVariantRefExpr x) {
    }

    public boolean visit(SQLQueryExpr x) {
        return true;
    }

    public void endVisit(SQLQueryExpr x) {
    }

    public boolean visit(SQLBitStringLiteralExpr x) {
        return true;
    }

    public void endVisit(SQLBitStringLiteralExpr x) {
    }

    public boolean visit(SQLHexStringLiteralExpr x) {
        return true;
    }

    public void endVisit(SQLHexStringLiteralExpr x) {
    }

    public boolean visit(SQLDateLiteralExpr x) {
        return true;
    }

    public void endVisit(SQLDateLiteralExpr x) {
    }

    public boolean visit(SQLSelect x) {
        return true;
    }

    public void endVisit(SQLSelect select) {
    }

    public boolean visit(SQLSelectQueryBlock x) {
        return true;
    }

    public void endVisit(SQLSelectQueryBlock x) {
    }

    public boolean visit(SQLExprTableSource x) {
        return true;
    }

    public void endVisit(SQLExprTableSource x) {
    }

    public boolean visit(SQLIntervalLiteralExpr x) {
        return true;
    }

    public void endVisit(SQLIntervalLiteralExpr x) {
    }

    public boolean visit(SQLOrderBy x) {
        return true;
    }

    public void endVisit(SQLOrderBy x) {
    }

    public boolean visit(SQLSelectOrderByItem x) {
        return true;
    }

    public void endVisit(SQLSelectOrderByItem x) {
    }

    public boolean visit(SQLDropTableStatement x) {
        return true;
    }

    public void endVisit(SQLDropTableStatement x) {
    }

    public boolean visit(SQLCreateTableStatement x) {
        return true;
    }

    public void endVisit(SQLCreateTableStatement x) {
    }

    public boolean visit(SQLTableElement x) {
        return true;
    }

    public void endVisit(SQLTableElement x) {
    }

    public boolean visit(SQLColumnDefinition x) {
        return true;
    }

    public void endVisit(SQLColumnDefinition x) {
    }

    public boolean visit(SQLDataType x) {
        return true;
    }

    public void endVisit(SQLDataType x) {
    }

    public boolean visit(SQLDeleteStatement x) {
        return true;
    }

    public void endVisit(SQLDeleteStatement x) {
    }

    public boolean visit(SQLCurrentOfCursorExpr x) {
        return true;
    }

    public void endVisit(SQLCurrentOfCursorExpr x) {
    }

    public boolean visit(SQLInsertStatement x) {
        return true;
    }

    public void endVisit(SQLInsertStatement x) {
    }

    public boolean visit(SQLUpdateSetItem x) {
        return true;
    }

    public void endVisit(SQLUpdateSetItem x) {
    }

    public boolean visit(SQLUpdateStatement x) {
        return true;
    }

    public void endVisit(SQLUpdateStatement x) {
    }

    public boolean visit(SQLCreateViewStatement x) {
        return true;
    }

    public void endVisit(SQLCreateViewStatement x) {
    }

    public boolean visit(SQLUniqueConstraint x) {
        return true;
    }

    public void endVisit(SQLUniqueConstraint x) {
    }

    public boolean visit(NotNullConstraint x) {
        return true;
    }

    public void endVisit(NotNullConstraint x) {
    }

    public void endVisit(SQLMethodInvokeExpr x) {

    }

    public boolean visit(SQLMethodInvokeExpr x) {
        return true;
    }

    public void endVisit(SQLUnionQuery x) {

    }

    public boolean visit(SQLUnionQuery x) {
        return true;
    }

    public boolean visit(SQLUnaryExpr x) {
        return true;
    }

    public void endVisit(SQLUnaryExpr x) {

    }

    public boolean visit(SQLHexExpr x) {
        return false;
    }

    public void endVisit(SQLHexExpr x) {

    }

    public void endVisit(SQLSetStatement x) {

    }

    public boolean visit(SQLSetStatement x) {
        return true;
    }

    public void endVisit(SQLAssignItem x) {

    }

    public boolean visit(SQLAssignItem x) {
        return true;
    }

    public void endVisit(SQLCallStatement x) {

    }

    public boolean visit(SQLCallStatement x) {
        return true;
    }

    public void endVisit(SQLJoinTableSource x) {

    }

    public boolean visit(SQLJoinTableSource x) {
        return true;
    }

    public boolean visit(ValuesClause x) {
        return true;
    }

    public void endVisit(ValuesClause x) {

    }

    public void endVisit(SQLSomeExpr x) {

    }

    public boolean visit(SQLSomeExpr x) {
        return true;
    }

    public void endVisit(SQLAnyExpr x) {

    }

    public boolean visit(SQLAnyExpr x) {
        return true;
    }

    public void endVisit(SQLAllExpr x) {

    }

    public boolean visit(SQLAllExpr x) {
        return true;
    }

    public void endVisit(SQLInSubQueryExpr x) {

    }

    public boolean visit(SQLInSubQueryExpr x) {
        return true;
    }

    public void endVisit(SQLListExpr x) {

    }

    public boolean visit(SQLListExpr x) {
        return true;
    }

    public void endVisit(SQLSubqueryTableSource x) {

    }

    public boolean visit(SQLSubqueryTableSource x) {
        return true;
    }

    public void endVisit(SQLTruncateStatement x) {

    }

    public boolean visit(SQLTruncateStatement x) {
        return true;
    }

    public void endVisit(SQLDefaultExpr x) {

    }

    public boolean visit(SQLDefaultExpr x) {
        return true;
    }

    public void endVisit(SQLCommentStatement x) {

    }

    public boolean visit(SQLCommentStatement x) {
        return true;
    }

    public void endVisit(SQLUseStatement x) {

    }

    public boolean visit(SQLUseStatement x) {
        return true;
    }

    public boolean visit(SQLAlterTableAddColumn x) {
        return true;
    }

    public void endVisit(SQLAlterTableAddColumn x) {

    }

    public boolean visit(SQLAlterTableDropColumnItem x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropColumnItem x) {

    }

    public boolean visit(SQLDropIndexStatement x) {
        return true;
    }

    public void endVisit(SQLDropIndexStatement x) {

    }

    public boolean visit(SQLDropViewStatement x) {
        return true;
    }

    public void endVisit(SQLDropViewStatement x) {

    }

    public boolean visit(SQLSavePointStatement x) {
        return true;
    }

    public void endVisit(SQLSavePointStatement x) {

    }

    public boolean visit(SQLRollbackStatement x) {
        return true;
    }

    public void endVisit(SQLRollbackStatement x) {

    }

    public boolean visit(SQLReleaseSavePointStatement x) {
        return true;
    }

    public void endVisit(SQLReleaseSavePointStatement x) {
    }

    public boolean visit(SQLCommentHint x) {
        return true;
    }

    public void endVisit(SQLCommentHint x) {

    }

    public void endVisit(SQLCreateDatabaseStatement x) {

    }

    public boolean visit(SQLCreateDatabaseStatement x) {
        return true;
    }
}
