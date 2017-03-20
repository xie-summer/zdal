/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.SQLOrderBy;
import com.alipay.zdal.parser.sql.ast.expr.SQLAggregateExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLMethodInvokeExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLObjectCreateExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLPropertyExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDeleteStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLTruncateStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleOrderBy;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.CycleClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfFlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfSnapshotClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.VersionsFlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.GroupingSetExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.CellAssignment;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.CellAssignmentItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelColumn;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelColumnClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelRulesClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.QueryPartitionClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleErrorLoggingClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleParameter;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OraclePartitionByRangeClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleRangeValuesClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SampleClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SearchClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SubqueryFactoringClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.SubqueryFactoringClause.Entry;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleAggregateExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleDateExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleDbLinkExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleExtractExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.expr.OracleTimestampExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterProcedureStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableAddConstaint;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableRenameTo;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleBlockStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleCommitStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleConstraintState;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleCreateProcedureStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleCreateSequenceStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExprStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleFetchStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleGrantStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleIfStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleIfStatement.ElseIf;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleLoopStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMergeStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMergeStatement.MergeInsertClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMergeStatement.MergeUpdateClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClauseItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.InsertIntoClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleOrderByItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OraclePLSQLCommitStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSavePointStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectForUpdate;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectHierachicalQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.CheckOption;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.ReadOnly;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleTableExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleTruncateStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateSetListClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateSetListMultiColumnItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateSetListSingleColumnItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateSetValueClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.sql.stat.TableStat.Mode;
import com.alipay.zdal.parser.sql.stat.TableStat.Relationship;
import com.alipay.zdal.parser.sql.util.JdbcUtils;
import com.alipay.zdal.parser.sql.visitor.SchemaStatVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSchemaStatVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:53:08 Exp $
 */
public class OracleSchemaStatVisitor extends SchemaStatVisitor implements OracleASTVisitor {

    public OracleSchemaStatVisitor() {
        this(new ArrayList<Object>());
    }

    public OracleSchemaStatVisitor(List<Object> parameters) {
        super(parameters);
        this.variants.put("DUAL", null);
        this.variants.put("NOTFOUND", null);
        this.variants.put("TRUE", null);
        this.variants.put("FALSE", null);
    }

    public String getDbType() {
        return JdbcUtils.ORACLE;
    }

    protected Column getColumn(SQLExpr expr) {
        if (expr instanceof OracleOuterExpr) {
            expr = ((OracleOuterExpr) expr).getExpr();
        }

        return super.getColumn(expr);
    }

    public boolean visit(OracleSelectTableReference x) {
        SQLExpr expr = x.getExpr();

        if (expr instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr methodInvoke = (SQLMethodInvokeExpr) expr;
            if ("TABLE".equalsIgnoreCase(methodInvoke.getMethodName())
                && methodInvoke.getParameters().size() == 1) {
                expr = methodInvoke.getParameters().get(0);
            }
        }

        Map<String, String> aliasMap = getAliasMap();

        if (expr instanceof SQLName) {
            String ident;
            if (expr instanceof SQLPropertyExpr) {
                String owner = ((SQLPropertyExpr) expr).getOwner().toString();
                String name = ((SQLPropertyExpr) expr).getName();

                if (aliasMap.containsKey(owner)) {
                    owner = aliasMap.get(owner);
                }
                ident = owner + "." + name;
            } else {
                ident = expr.toString();
            }

            if (subQueryMap.containsKey(ident)) {
                return false;
            }

            if ("DUAL".equalsIgnoreCase(ident)) {
                return false;
            }

            x.putAttribute(ATTR_TABLE, ident);

            TableStat stat = getTableStat(ident);

            Mode mode = getMode();
            switch (mode) {
                case Delete:
                    stat.incrementDeleteCount();
                    break;
                case Insert:
                    stat.incrementInsertCount();
                    break;
                case Update:
                    stat.incrementUpdateCount();
                    break;
                case Select:
                    stat.incrementSelectCount();
                    break;
                case Merge:
                    stat.incrementMergeCount();
                    break;
                default:
                    break;
            }

            if (aliasMap != null) {
                if (x.getAlias() != null) {
                    aliasMap.put(x.getAlias(), ident);
                }
                aliasMap.put(ident, ident);
            }
            return false;
        }

        accept(x.getExpr());

        return false;
    }

    public boolean visit(SQLAggregateExpr x) {
        accept(x.getArguments());
        return false;
    }

    public boolean visit(OracleAggregateExpr x) {
        accept(x.getArguments());
        accept(x.getOver());
        return false;
    }

    public void endVisit(OracleSelect x) {
        endVisit((SQLSelect) x);
    }

    public boolean visit(OracleSelect x) {
        setCurrentTable(x);

        if (x.getOrderBy() != null) {
            x.getOrderBy().setParent(x);
        }

        accept(x.getFactoring());
        accept(x.getQuery());

        setCurrentTable(x, (String) x.getQuery().getAttribute("table"));

        accept(x.getOrderBy());

        return false;
    }

    public void endVisit(SQLSelect x) {
        if (x.getQuery() != null) {
            String table = (String) x.getQuery().getAttribute(ATTR_TABLE);
            if (table != null) {
                x.putAttribute(ATTR_TABLE, table);
            }
        }
        restoreCurrentTable(x);
    }

    public boolean visit(OracleUpdateStatement x) {
        setAliasMap();
        setMode(x, Mode.Update);

        SQLTableSource tableSource = x.getTableSource();
        SQLExpr tableExpr = null;

        if (tableSource instanceof SQLExprTableSource) {
            tableExpr = ((SQLExprTableSource) tableSource).getExpr();
        }

        if (tableExpr instanceof SQLName) {
            String ident = tableExpr.toString();
            setCurrentTable(ident);

            TableStat stat = getTableStat(ident);
            stat.incrementUpdateCount();

            Map<String, String> aliasMap = getAliasMap();
            aliasMap.put(ident, ident);
            aliasMap.put(tableSource.getAlias(), ident);
        } else {
            tableSource.accept(this);
        }

        accept(x.getItems());
        accept(x.getWhere());

        return false;
    }

    public void endVisit(OracleUpdateStatement x) {
        clearAliasMap();
    }

    public boolean visit(OracleDeleteStatement x) {
        return visit((SQLDeleteStatement) x);
    }

    public void endVisit(OracleDeleteStatement x) {
        clearAliasMap();
    }

    public boolean visit(OracleSelectQueryBlock x) {
        if (x.getWhere() != null) {
            x.getWhere().setParent(x);
        }

        if (x.getInto() instanceof SQLName) {
            String tableName = x.getInto().toString();
            TableStat stat = getTableStat(tableName);
            if (stat != null) {
                stat.incrementInsertCount();
            }
        }

        visit((SQLSelectQueryBlock) x);

        return true;
    }

    public void endVisit(OracleSelectQueryBlock x) {
        endVisit((SQLSelectQueryBlock) x);
    }

    public boolean visit(SQLPropertyExpr x) {
        if ("ROWNUM".equalsIgnoreCase(x.getName())) {
            return false;
        }

        return super.visit(x);
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        accept(x.getParameters());
        return false;
    }

    public boolean visit(SQLIdentifierExpr x) {
        if ("ROWNUM".equalsIgnoreCase(x.getName())) {
            return false;
        }

        if ("SYSDATE".equalsIgnoreCase(x.getName())) {
            return false;
        }

        if ("+".equalsIgnoreCase(x.getName())) {
            return false;
        }

        if ("LEVEL".equals(x.getName())) {
            return false;
        }

        return super.visit(x);
    }

    public void endVisit(OracleAggregateExpr astNode) {

    }

    public void endVisit(OracleConstraintState astNode) {

    }

    public void endVisit(OraclePLSQLCommitStatement astNode) {

    }

    public void endVisit(OracleAnalytic x) {

    }

    public void endVisit(OracleAnalyticWindowing x) {

    }

    public void endVisit(OracleDateExpr x) {

    }

    public void endVisit(OracleDbLinkExpr x) {

    }

    public void endVisit(OracleExtractExpr x) {

    }

    public void endVisit(OracleIntervalExpr x) {

    }

    public void endVisit(SQLObjectCreateExpr x) {

    }

    public void endVisit(OracleOrderBy x) {

    }

    public void endVisit(OracleOuterExpr x) {

    }

    public void endVisit(OracleSelectForUpdate x) {

    }

    public void endVisit(OracleSelectHierachicalQueryClause x) {

    }

    public void endVisit(OracleSelectJoin x) {

    }

    public void endVisit(OracleOrderByItem x) {

    }

    public void endVisit(OracleSelectPivot x) {

    }

    public void endVisit(Item x) {

    }

    public void endVisit(CheckOption x) {

    }

    public void endVisit(ReadOnly x) {

    }

    public void endVisit(OracleSelectSubqueryTableSource x) {

    }

    public void endVisit(OracleSelectUnPivot x) {

    }

    public void endVisit(OracleTableExpr x) {

    }

    public void endVisit(OracleTimestampExpr x) {

    }

    public void endVisit(OracleUpdateSetListClause x) {

    }

    public void endVisit(OracleUpdateSetListMultiColumnItem x) {

    }

    public void endVisit(OracleUpdateSetListSingleColumnItem x) {

    }

    public void endVisit(OracleUpdateSetValueClause x) {

    }

    public boolean visit(OracleConstraintState astNode) {

        return true;
    }

    public boolean visit(OraclePLSQLCommitStatement astNode) {

        return true;
    }

    public boolean visit(OracleAnalytic x) {

        return true;
    }

    public boolean visit(OracleAnalyticWindowing x) {

        return true;
    }

    public boolean visit(OracleDateExpr x) {

        return true;
    }

    public boolean visit(OracleDbLinkExpr x) {

        return true;
    }

    public boolean visit(OracleExtractExpr x) {

        return true;
    }

    public boolean visit(OracleIntervalExpr x) {

        return true;
    }

    public boolean visit(SQLObjectCreateExpr x) {

        return true;
    }

    public boolean visit(OracleOrderBy x) {
        return this.visit((SQLOrderBy) x);
    }

    public boolean visit(OracleOuterExpr x) {

        return true;
    }

    public boolean visit(OracleSelectForUpdate x) {

        return true;
    }

    public boolean visit(OracleSelectHierachicalQueryClause x) {

        return true;
    }

    public boolean visit(OracleSelectJoin x) {
        x.getLeft().accept(this);
        x.getRight().accept(this);

        {
            String leftTable = (String) x.getLeft().getAttribute(ATTR_TABLE);
            String rightTable = (String) x.getRight().getAttribute(ATTR_TABLE);
            if (leftTable != null && leftTable.equals(rightTable)) {
                x.putAttribute(ATTR_TABLE, leftTable);
            }
        }
        if (x.getCondition() != null) {
            x.getCondition().accept(this);
        }

        for (SQLExpr item : x.getUsing()) {
            if (item instanceof SQLIdentifierExpr) {
                String columnName = ((SQLIdentifierExpr) item).getName();
                String leftTable = (String) x.getLeft().getAttribute(ATTR_TABLE);
                String rightTable = (String) x.getRight().getAttribute(ATTR_TABLE);
                if (leftTable != null && rightTable != null) {
                    Relationship relationship = new Relationship();
                    relationship.setLeft(new Column(leftTable, columnName));
                    relationship.setRight(new Column(rightTable, columnName));
                    relationship.setOperator("USING");
                    relationships.add(relationship);
                }

                if (leftTable != null) {
                    addColumn(leftTable, columnName);
                }

                if (rightTable != null) {
                    addColumn(rightTable, columnName);
                }
            }
        }

        return false;
    }

    public boolean visit(OracleOrderByItem x) {

        return true;
    }

    public boolean visit(OracleSelectPivot x) {

        return true;
    }

    public boolean visit(Item x) {

        return true;
    }

    public boolean visit(CheckOption x) {

        return true;
    }

    public boolean visit(ReadOnly x) {

        return true;
    }

    public boolean visit(OracleSelectSubqueryTableSource x) {
        accept(x.getSelect());
        accept(x.getPivot());
        accept(x.getFlashback());

        String table = (String) x.getSelect().getAttribute(ATTR_TABLE);
        if (x.getAlias() != null) {
            if (table != null) {
                this.aliasMap.put(x.getAlias(), table);
            }
            this.subQueryMap.put(x.getAlias(), x.getSelect());
            this.setCurrentTable(x.getAlias());
        }

        if (table != null) {
            x.putAttribute(ATTR_TABLE, table);
        }
        return false;
    }

    public boolean visit(OracleSelectUnPivot x) {

        return true;
    }

    public boolean visit(OracleTableExpr x) {

        return true;
    }

    public boolean visit(OracleTimestampExpr x) {

        return true;
    }

    public boolean visit(OracleUpdateSetListClause x) {

        return true;
    }

    public boolean visit(OracleUpdateSetListMultiColumnItem x) {

        return true;
    }

    public boolean visit(OracleUpdateSetListSingleColumnItem x) {

        return true;
    }

    public boolean visit(OracleUpdateSetValueClause x) {

        return true;
    }

    public boolean visit(SampleClause x) {

        return true;
    }

    public void endVisit(SampleClause x) {

    }

    public void endVisit(OracleSelectTableReference x) {

    }

    public boolean visit(PartitionExtensionClause x) {

        return true;
    }

    public void endVisit(PartitionExtensionClause x) {

    }

    public boolean visit(VersionsFlashbackQueryClause x) {

        return true;
    }

    public void endVisit(VersionsFlashbackQueryClause x) {

    }

    public boolean visit(AsOfFlashbackQueryClause x) {

        return true;
    }

    public void endVisit(AsOfFlashbackQueryClause x) {

    }

    public boolean visit(GroupingSetExpr x) {

        return true;
    }

    public void endVisit(GroupingSetExpr x) {

    }

    public boolean visit(SubqueryFactoringClause x) {

        return true;
    }

    public void endVisit(SubqueryFactoringClause x) {

    }

    public boolean visit(Entry x) {
        Map<String, String> aliasMap = getAliasMap();
        if (aliasMap != null) {
            String alias = null;
            if (x.getName() != null) {
                alias = x.getName().toString();
            }

            if (alias != null) {
                aliasMap.put(alias, null);
                subQueryMap.put(alias, x.getSubQuery());
            }
        }
        x.getSubQuery().accept(this);
        return false;
    }

    public void endVisit(Entry x) {

    }

    public boolean visit(SearchClause x) {

        return true;
    }

    public void endVisit(SearchClause x) {

    }

    public boolean visit(CycleClause x) {

        return true;
    }

    public void endVisit(CycleClause x) {

    }

    public boolean visit(OracleBinaryFloatExpr x) {

        return true;
    }

    public void endVisit(OracleBinaryFloatExpr x) {

    }

    public boolean visit(OracleBinaryDoubleExpr x) {

        return true;
    }

    public void endVisit(OracleBinaryDoubleExpr x) {

    }

    public boolean visit(OracleCursorExpr x) {

        return true;
    }

    public void endVisit(OracleCursorExpr x) {

    }

    public boolean visit(OracleIsSetExpr x) {

        return true;
    }

    public void endVisit(OracleIsSetExpr x) {

    }

    public boolean visit(ReturnRowsClause x) {

        return true;
    }

    public void endVisit(ReturnRowsClause x) {

    }

    public boolean visit(MainModelClause x) {

        return true;
    }

    public void endVisit(MainModelClause x) {

    }

    public boolean visit(ModelColumnClause x) {

        return true;
    }

    public void endVisit(ModelColumnClause x) {

    }

    public boolean visit(QueryPartitionClause x) {

        return true;
    }

    public void endVisit(QueryPartitionClause x) {

    }

    public boolean visit(ModelColumn x) {

        return true;
    }

    public void endVisit(ModelColumn x) {

    }

    public boolean visit(ModelRulesClause x) {

        return true;
    }

    public void endVisit(ModelRulesClause x) {

    }

    public boolean visit(CellAssignmentItem x) {

        return true;
    }

    public void endVisit(CellAssignmentItem x) {

    }

    public boolean visit(CellAssignment x) {

        return true;
    }

    public void endVisit(CellAssignment x) {

    }

    public boolean visit(ModelClause x) {
        return true;
    }

    public void endVisit(ModelClause x) {

    }

    public boolean visit(OracleMergeStatement x) {
        setAliasMap();

        String originalTable = getCurrentTable();

        setMode(x.getUsing(), Mode.Select);
        x.getUsing().accept(this);

        setMode(x, Mode.Merge);

        String ident = x.getInto().toString();
        setCurrentTable(x, ident);
        x.putAttribute("_old_local_", originalTable);

        TableStat stat = getTableStat(ident);
        stat.incrementMergeCount();

        Map<String, String> aliasMap = getAliasMap();
        if (aliasMap != null) {
            if (x.getAlias() != null) {
                aliasMap.put(x.getAlias(), ident);
            }
            aliasMap.put(ident, ident);
        }

        x.getOn().accept(this);

        if (x.getUpdateClause() != null) {
            x.getUpdateClause().accept(this);
        }

        if (x.getInsertClause() != null) {
            x.getInsertClause().accept(this);
        }

        return false;
    }

    public void endVisit(OracleMergeStatement x) {
        clearAliasMap();
    }

    public boolean visit(MergeUpdateClause x) {
        return true;
    }

    public void endVisit(MergeUpdateClause x) {

    }

    public boolean visit(MergeInsertClause x) {
        return true;
    }

    public void endVisit(MergeInsertClause x) {

    }

    public boolean visit(OracleErrorLoggingClause x) {
        return true;
    }

    public void endVisit(OracleErrorLoggingClause x) {

    }

    public boolean visit(OracleReturningClause x) {
        return true;
    }

    public void endVisit(OracleReturningClause x) {

    }

    public boolean visit(OracleInsertStatement x) {
        return visit((SQLInsertStatement) x);
    }

    public void endVisit(OracleInsertStatement x) {
        endVisit((SQLInsertStatement) x);
    }

    public boolean visit(InsertIntoClause x) {

        if (x.getTableName() instanceof SQLName) {
            String ident = ((SQLName) x.getTableName()).toString();
            setCurrentTable(x, ident);

            TableStat stat = getTableStat(ident);
            stat.incrementInsertCount();

            Map<String, String> aliasMap = getAliasMap();
            if (aliasMap != null) {
                if (x.getAlias() != null) {
                    aliasMap.put(x.getAlias(), ident);
                }
                aliasMap.put(ident, ident);
            }
        }

        accept(x.getColumns());
        accept(x.getQuery());
        accept(x.getReturning());
        accept(x.getErrorLogging());

        return false;
    }

    public void endVisit(InsertIntoClause x) {

    }

    public boolean visit(OracleMultiInsertStatement x) {
        x.putAttribute("_original_use_mode", getMode());
        setMode(x, Mode.Insert);

        setAliasMap();

        accept(x.getSubQuery());

        for (OracleMultiInsertStatement.Entry entry : x.getEntries()) {
            entry.setParent(x);
        }

        accept(x.getEntries());

        return false;
    }

    public void endVisit(OracleMultiInsertStatement x) {

    }

    public boolean visit(ConditionalInsertClause x) {
        for (ConditionalInsertClauseItem item : x.getItems()) {
            item.setParent(x);
        }
        if (x.getElseItem() != null) {
            x.getElseItem().setParent(x);
        }
        return true;
    }

    public void endVisit(ConditionalInsertClause x) {

    }

    public boolean visit(ConditionalInsertClauseItem x) {
        SQLObject parent = x.getParent();
        if (parent instanceof ConditionalInsertClause) {
            parent = parent.getParent();
        }
        if (parent instanceof OracleMultiInsertStatement) {
            SQLSelect subQuery = ((OracleMultiInsertStatement) parent).getSubQuery();
            if (subQuery != null) {
                String table = (String) subQuery.getAttribute("_table_");
                setCurrentTable(x, table);
            }
        }
        x.getWhen().accept(this);
        x.getThen().accept(this);
        restoreCurrentTable(x);
        return false;
    }

    public void endVisit(ConditionalInsertClauseItem x) {

    }

    public boolean visit(OracleBlockStatement x) {
        for (OracleParameter param : x.getParameters()) {
            param.setParent(x);

            SQLExpr name = param.getName();
            this.variants.put(name.toString(), name);
        }
        return true;
    }

    public void endVisit(OracleBlockStatement x) {

    }

    public boolean visit(OracleAlterSessionStatement x) {
        return false;
    }

    public void endVisit(OracleAlterSessionStatement x) {

    }

    public boolean visit(OracleExprStatement x) {
        return false;
    }

    public void endVisit(OracleExprStatement x) {

    }

    public boolean visit(OracleLockTableStatement x) {
        String tableName = x.getTable().toString();
        getTableStat(tableName);
        return false;
    }

    public void endVisit(OracleLockTableStatement x) {

    }

    public boolean visit(OracleDatetimeExpr x) {
        return true;
    }

    public void endVisit(OracleDatetimeExpr x) {

    }

    public boolean visit(OracleSysdateExpr x) {
        return false;
    }

    public void endVisit(OracleSysdateExpr x) {

    }

    public void endVisit(com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {

    }

    public boolean visit(com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {
        return true;
    }

    public boolean visit(OracleExceptionStatement x) {
        return true;
    }

    public void endVisit(OracleExceptionStatement x) {

    }

    public boolean visit(OracleArgumentExpr x) {
        return true;
    }

    public void endVisit(OracleArgumentExpr x) {

    }

    public boolean visit(OracleSetTransactionStatement x) {
        return false;
    }

    public void endVisit(OracleSetTransactionStatement x) {

    }

    public boolean visit(OracleGrantStatement x) {
        return false;
    }

    public void endVisit(OracleGrantStatement x) {

    }

    public boolean visit(OracleExplainStatement x) {
        return false;
    }

    public void endVisit(OracleExplainStatement x) {

    }

    public boolean visit(OracleAlterProcedureStatement x) {
        return false;
    }

    public void endVisit(OracleAlterProcedureStatement x) {

    }

    public boolean visit(OracleAlterTableDropPartition x) {
        return false;
    }

    public void endVisit(OracleAlterTableDropPartition x) {

    }

    public boolean visit(OracleAlterTableTruncatePartition x) {
        return false;
    }

    public void endVisit(OracleAlterTableTruncatePartition x) {

    }

    public boolean visit(OracleAlterTableStatement x) {
        String tableName = x.getName().toString();
        TableStat stat = getTableStat(tableName);
        stat.incrementAlterCount();

        setCurrentTable(x, tableName);

        for (SQLObject item : x.getItems()) {
            item.setParent(x);
            item.accept(this);
        }

        return false;
    }

    public void endVisit(OracleAlterTableStatement x) {
        restoreCurrentTable(x);
    }

    public boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x) {
        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x) {

    }

    public boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {

    }

    public boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {

    }

    public boolean visit(OracleAlterTableSplitPartition x) {
        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition x) {

    }

    public boolean visit(OracleAlterTableModify x) {
        OracleAlterTableStatement stmt = (OracleAlterTableStatement) x.getParent();
        String table = stmt.getName().toString();

        for (SQLColumnDefinition column : x.getColumns()) {
            String columnName = column.getName().toString();
            addColumn(table, columnName);

        }

        return false;
    }

    public void endVisit(OracleAlterTableModify x) {

    }

    public boolean visit(OracleCreateIndexStatement x) {
        this.setCurrentTable(x);
        if (x.getTable() != null) {
            String tableName = x.getTable().toString();

            TableStat stat = this.getTableStat(tableName);
            stat.incrementCreateIndexCount();
            this.setCurrentTable(tableName);

            accept(x.getItems());
        }
        return false;
    }

    public void endVisit(OracleCreateIndexStatement x) {
        restoreCurrentTable(x);
    }

    public boolean visit(OracleAlterIndexStatement x) {
        return false;
    }

    public void endVisit(OracleAlterIndexStatement x) {

    }

    public boolean visit(OracleForStatement x) {
        x.getRange().setParent(x);

        SQLName index = x.getIndex();
        this.getVariants().put(index.toString(), x);

        x.getRange().accept(this);
        accept(x.getStatements());

        return false;
    }

    public void endVisit(OracleForStatement x) {

    }

    public boolean visit(OracleAlterIndexStatement.Rebuild x) {
        return false;
    }

    public void endVisit(OracleAlterIndexStatement.Rebuild x) {

    }

    public boolean visit(Else x) {
        return true;
    }

    public void endVisit(Else x) {

    }

    public boolean visit(ElseIf x) {
        return true;
    }

    public void endVisit(ElseIf x) {

    }

    public boolean visit(OracleIfStatement x) {
        return true;
    }

    public void endVisit(OracleIfStatement x) {

    }

    public boolean visit(OracleRangeExpr x) {
        return true;
    }

    public void endVisit(OracleRangeExpr x) {

    }

    public boolean visit(OracleAlterTableAddConstaint x) {
        x.getConstraint().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableAddConstaint x) {

    }

    public boolean visit(OraclePrimaryKey x) {
        accept(x.getColumns());

        return false;
    }

    public void endVisit(OraclePrimaryKey x) {

    }

    public boolean visit(OracleCreateTableStatement x) {
        this.visit((SQLCreateTableStatement) x);

        if (x.getSelect() != null) {
            x.getSelect().accept(this);
        }

        return false;
    }

    public void endVisit(OracleCreateTableStatement x) {
        this.endVisit((SQLCreateTableStatement) x);
    }

    public boolean visit(OracleAlterTableRenameTo x) {
        return false;
    }

    public void endVisit(OracleAlterTableRenameTo x) {

    }

    public boolean visit(OracleStorageClause x) {
        return false;
    }

    public void endVisit(OracleStorageClause x) {

    }

    public boolean visit(OracleGotoStatement x) {
        return false;
    }

    public void endVisit(OracleGotoStatement x) {

    }

    public boolean visit(OracleLabelStatement x) {
        return false;
    }

    public void endVisit(OracleLabelStatement x) {

    }

    public boolean visit(OracleParameter x) {
        return true;
    }

    public void endVisit(OracleParameter x) {

    }

    public boolean visit(OracleCommitStatement x) {
        return true;
    }

    public void endVisit(OracleCommitStatement x) {

    }

    public boolean visit(OracleAlterTriggerStatement x) {
        return false;
    }

    public void endVisit(OracleAlterTriggerStatement x) {

    }

    public boolean visit(OracleAlterSynonymStatement x) {
        return false;
    }

    public void endVisit(OracleAlterSynonymStatement x) {

    }

    public boolean visit(AsOfSnapshotClause x) {
        return false;
    }

    public void endVisit(AsOfSnapshotClause x) {

    }

    public boolean visit(OracleAlterViewStatement x) {
        return false;
    }

    public void endVisit(OracleAlterViewStatement x) {

    }

    public boolean visit(OracleAlterTableMoveTablespace x) {
        return false;
    }

    public void endVisit(OracleAlterTableMoveTablespace x) {

    }

    public boolean visit(OracleSizeExpr x) {
        return false;
    }

    public void endVisit(OracleSizeExpr x) {

    }

    public boolean visit(OracleFileSpecification x) {
        return false;
    }

    public void endVisit(OracleFileSpecification x) {

    }

    public boolean visit(OracleAlterTablespaceAddDataFile x) {
        return false;
    }

    public void endVisit(OracleAlterTablespaceAddDataFile x) {

    }

    public boolean visit(OracleAlterTablespaceStatement x) {
        return false;
    }

    public void endVisit(OracleAlterTablespaceStatement x) {

    }

    public boolean visit(OracleTruncateStatement x) {
        return visit((SQLTruncateStatement) x);
    }

    public void endVisit(OracleTruncateStatement x) {

    }

    public boolean visit(OracleCreateSequenceStatement x) {
        return false;
    }

    public void endVisit(OracleCreateSequenceStatement x) {

    }

    public boolean visit(OracleRangeValuesClause x) {
        return false;
    }

    public void endVisit(OracleRangeValuesClause x) {

    }

    public boolean visit(OraclePartitionByRangeClause x) {
        return false;
    }

    public void endVisit(OraclePartitionByRangeClause x) {

    }

    public boolean visit(OracleLoopStatement x) {
        return true;
    }

    public void endVisit(OracleLoopStatement x) {

    }

    public boolean visit(OracleExitStatement x) {
        return true;
    }

    public void endVisit(OracleExitStatement x) {

    }

    public boolean visit(OracleFetchStatement x) {
        return true;
    }

    public void endVisit(OracleFetchStatement x) {

    }

    public boolean visit(OracleSavePointStatement x) {
        return false;
    }

    public void endVisit(OracleSavePointStatement x) {

    }

    public boolean visit(OracleCreateProcedureStatement x) {
        String name = x.getName().toString();
        this.variants.put(name, x);
        accept(x.getBlock());
        return false;
    }

    public void endVisit(OracleCreateProcedureStatement x) {

    }
}
