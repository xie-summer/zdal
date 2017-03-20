/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.visitor;

import com.alipay.zdal.parser.sql.ast.expr.SQLObjectCreateExpr;
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
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitorAdapter;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleASTVisitorAdapter.java, v 0.1 2012-11-17 ÏÂÎç3:52:42 Exp $
 */
public class OracleASTVisitorAdapter extends SQLASTVisitorAdapter implements OracleASTVisitor {

    public boolean visit(OracleSelect x) {
        return true;
    }

    public void endVisit(OracleSelect x) {
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

    public void endVisit(OracleDeleteStatement x) {

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

    public void endVisit(OracleUpdateStatement x) {

    }

    public boolean visit(OracleAggregateExpr astNode) {

        return true;
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

    public boolean visit(OracleDeleteStatement x) {

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

        return true;
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

        return true;
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

        return true;
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

    public boolean visit(OracleUpdateStatement x) {

        return true;
    }

    public boolean visit(SampleClause x) {

        return true;
    }

    public void endVisit(SampleClause x) {

    }

    public boolean visit(OracleSelectTableReference x) {

        return true;
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

        return true;
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

    public boolean visit(ModelClause x) {
        return true;
    }

    public void endVisit(ModelClause x) {

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

    public boolean visit(OracleMergeStatement x) {
        return true;
    }

    public void endVisit(OracleMergeStatement x) {
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
        return true;
    }

    public void endVisit(OracleInsertStatement x) {

    }

    public boolean visit(InsertIntoClause x) {
        return true;
    }

    public void endVisit(InsertIntoClause x) {

    }

    public boolean visit(OracleMultiInsertStatement x) {
        return true;
    }

    public void endVisit(OracleMultiInsertStatement x) {

    }

    public boolean visit(ConditionalInsertClause x) {
        return true;
    }

    public void endVisit(ConditionalInsertClause x) {

    }

    public boolean visit(ConditionalInsertClauseItem x) {
        return true;
    }

    public void endVisit(ConditionalInsertClauseItem x) {

    }

    public boolean visit(OracleSelectQueryBlock x) {
        return true;
    }

    public void endVisit(OracleSelectQueryBlock x) {

    }

    public boolean visit(OracleBlockStatement x) {
        return true;
    }

    public void endVisit(OracleBlockStatement x) {

    }

    public boolean visit(OracleLockTableStatement x) {
        return true;
    }

    public void endVisit(OracleLockTableStatement x) {

    }

    public boolean visit(OracleAlterSessionStatement x) {
        return true;
    }

    public void endVisit(OracleAlterSessionStatement x) {

    }

    public boolean visit(OracleExprStatement x) {
        return true;
    }

    public void endVisit(OracleExprStatement x) {

    }

    public boolean visit(OracleDatetimeExpr x) {
        return true;
    }

    public void endVisit(OracleDatetimeExpr x) {

    }

    public boolean visit(OracleSysdateExpr x) {
        return true;
    }

    public void endVisit(OracleSysdateExpr x) {

    }

    public boolean visit(OracleExceptionStatement x) {
        return true;
    }

    public void endVisit(OracleExceptionStatement x) {

    }

    public boolean visit(OracleExceptionStatement.Item x) {
        return true;
    }

    public void endVisit(OracleExceptionStatement.Item x) {

    }

    public boolean visit(OracleArgumentExpr x) {
        return true;
    }

    public void endVisit(OracleArgumentExpr x) {

    }

    public boolean visit(OracleSetTransactionStatement x) {
        return true;
    }

    public void endVisit(OracleSetTransactionStatement x) {

    }

    public boolean visit(OracleGrantStatement x) {
        return true;
    }

    public void endVisit(OracleGrantStatement x) {

    }

    public boolean visit(OracleExplainStatement x) {
        return true;
    }

    public void endVisit(OracleExplainStatement x) {

    }

    public boolean visit(OracleAlterProcedureStatement x) {
        return true;
    }

    public void endVisit(OracleAlterProcedureStatement x) {

    }

    public boolean visit(OracleAlterTableDropPartition x) {
        return true;
    }

    public void endVisit(OracleAlterTableDropPartition x) {

    }

    public boolean visit(OracleAlterTableTruncatePartition x) {
        return true;
    }

    public void endVisit(OracleAlterTableTruncatePartition x) {

    }

    public boolean visit(OracleAlterTableStatement x) {
        return true;
    }

    public void endVisit(OracleAlterTableStatement x) {

    }

    public boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x) {
        return true;
    }

    public void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x) {

    }

    public boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        return true;
    }

    public void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {

    }

    public boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        return true;
    }

    public void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {

    }

    public boolean visit(OracleAlterTableSplitPartition x) {
        return true;
    }

    public void endVisit(OracleAlterTableSplitPartition x) {

    }

    public boolean visit(OracleAlterTableModify x) {
        return true;
    }

    public void endVisit(OracleAlterTableModify x) {

    }

    public boolean visit(OracleCreateIndexStatement x) {
        return true;
    }

    public void endVisit(OracleCreateIndexStatement x) {

    }

    public boolean visit(OracleAlterIndexStatement x) {
        return true;
    }

    public void endVisit(OracleAlterIndexStatement x) {

    }

    public boolean visit(OracleForStatement x) {
        return true;
    }

    public void endVisit(OracleForStatement x) {

    }

    public boolean visit(OracleAlterIndexStatement.Rebuild x) {
        return true;
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
        return true;
    }

    public void endVisit(OracleAlterTableAddConstaint x) {

    }

    public boolean visit(OraclePrimaryKey x) {
        return true;
    }

    public void endVisit(OraclePrimaryKey x) {

    }

    public boolean visit(OracleCreateTableStatement x) {
        return true;
    }

    public void endVisit(OracleCreateTableStatement x) {

    }

    public boolean visit(OracleAlterTableRenameTo x) {
        return true;
    }

    public void endVisit(OracleAlterTableRenameTo x) {

    }

    public boolean visit(OracleStorageClause x) {
        return true;
    }

    public void endVisit(OracleStorageClause x) {

    }

    public boolean visit(OracleGotoStatement x) {
        return true;
    }

    public void endVisit(OracleGotoStatement x) {

    }

    public boolean visit(OracleLabelStatement x) {
        return true;
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
        return true;
    }

    public void endVisit(OracleAlterTriggerStatement x) {

    }

    public boolean visit(OracleAlterSynonymStatement x) {
        return true;
    }

    public void endVisit(OracleAlterSynonymStatement x) {

    }

    public boolean visit(AsOfSnapshotClause x) {
        return true;
    }

    public void endVisit(AsOfSnapshotClause x) {

    }

    public boolean visit(OracleAlterViewStatement x) {
        return true;
    }

    public void endVisit(OracleAlterViewStatement x) {

    }

    public boolean visit(OracleAlterTableMoveTablespace x) {
        return true;
    }

    public void endVisit(OracleAlterTableMoveTablespace x) {

    }

    public boolean visit(OracleSizeExpr x) {
        return true;
    }

    public void endVisit(OracleSizeExpr x) {

    }

    public boolean visit(OracleFileSpecification x) {
        return true;
    }

    public void endVisit(OracleFileSpecification x) {

    }

    public boolean visit(OracleAlterTablespaceAddDataFile x) {
        return true;
    }

    public void endVisit(OracleAlterTablespaceAddDataFile x) {

    }

    public boolean visit(OracleAlterTablespaceStatement x) {
        return true;
    }

    public void endVisit(OracleAlterTablespaceStatement x) {

    }

    public boolean visit(OracleTruncateStatement x) {
        return true;
    }

    public void endVisit(OracleTruncateStatement x) {

    }

    public boolean visit(OracleCreateSequenceStatement x) {
        return true;
    }

    public void endVisit(OracleCreateSequenceStatement x) {

    }

    public boolean visit(OracleRangeValuesClause x) {
        return true;
    }

    public void endVisit(OracleRangeValuesClause x) {

    }

    public boolean visit(OraclePartitionByRangeClause x) {
        return true;
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
        return true;
    }

    public void endVisit(OracleSavePointStatement x) {

    }

    public boolean visit(OracleCreateProcedureStatement x) {
        return true;
    }

    public void endVisit(OracleCreateProcedureStatement x) {

    }

}
