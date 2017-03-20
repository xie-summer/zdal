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
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
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
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleASTVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:52:37 Exp $
 */
public interface OracleASTVisitor extends SQLASTVisitor {

    void endVisit(OracleAggregateExpr astNode);

    void endVisit(OracleConstraintState astNode);

    void endVisit(OraclePLSQLCommitStatement astNode);

    void endVisit(OracleAnalytic x);

    void endVisit(OracleAnalyticWindowing x);

    void endVisit(OracleDateExpr x);

    void endVisit(OracleDbLinkExpr x);

    void endVisit(OracleDeleteStatement x);

    void endVisit(OracleExtractExpr x);

    void endVisit(OracleIntervalExpr x);

    void endVisit(SQLObjectCreateExpr x);

    void endVisit(OracleOrderBy x);

    void endVisit(OracleOuterExpr x);

    void endVisit(OracleSelectForUpdate x);

    void endVisit(OracleSelectHierachicalQueryClause x);

    void endVisit(OracleSelectJoin x);

    void endVisit(OracleOrderByItem x);

    void endVisit(OracleSelectPivot x);

    void endVisit(OracleSelectPivot.Item x);

    void endVisit(OracleSelectRestriction.CheckOption x);

    void endVisit(OracleSelectRestriction.ReadOnly x);

    void endVisit(OracleSelectSubqueryTableSource x);

    void endVisit(OracleSelectUnPivot x);

    void endVisit(OracleTableExpr x);

    void endVisit(OracleTimestampExpr x);

    void endVisit(OracleUpdateSetListClause x);

    void endVisit(OracleUpdateSetListMultiColumnItem x);

    void endVisit(OracleUpdateSetListSingleColumnItem x);

    void endVisit(OracleUpdateSetValueClause x);

    void endVisit(OracleUpdateStatement x);

    boolean visit(OracleAggregateExpr astNode);

    boolean visit(OracleConstraintState astNode);

    boolean visit(OraclePLSQLCommitStatement astNode);

    boolean visit(OracleAnalytic x);

    boolean visit(OracleAnalyticWindowing x);

    boolean visit(OracleDateExpr x);

    boolean visit(OracleDbLinkExpr x);

    boolean visit(OracleDeleteStatement x);

    boolean visit(OracleExtractExpr x);

    boolean visit(OracleIntervalExpr x);

    boolean visit(SQLObjectCreateExpr x);

    boolean visit(OracleOrderBy x);

    boolean visit(OracleOuterExpr x);

    boolean visit(OracleSelectForUpdate x);

    boolean visit(OracleSelectHierachicalQueryClause x);

    boolean visit(OracleSelectJoin x);

    boolean visit(OracleOrderByItem x);

    boolean visit(OracleSelectPivot x);

    boolean visit(OracleSelectPivot.Item x);

    boolean visit(OracleSelectRestriction.CheckOption x);

    boolean visit(OracleSelectRestriction.ReadOnly x);

    boolean visit(OracleSelectSubqueryTableSource x);

    boolean visit(OracleSelectUnPivot x);

    boolean visit(OracleTableExpr x);

    boolean visit(OracleTimestampExpr x);

    boolean visit(OracleUpdateSetListClause x);

    boolean visit(OracleUpdateSetListMultiColumnItem x);

    boolean visit(OracleUpdateSetListSingleColumnItem x);

    boolean visit(OracleUpdateSetValueClause x);

    boolean visit(OracleUpdateStatement x);

    boolean visit(SampleClause x);

    void endVisit(SampleClause x);

    boolean visit(OracleSelectTableReference x);

    void endVisit(OracleSelectTableReference x);

    boolean visit(PartitionExtensionClause x);

    void endVisit(PartitionExtensionClause x);

    boolean visit(VersionsFlashbackQueryClause x);

    void endVisit(VersionsFlashbackQueryClause x);

    boolean visit(AsOfFlashbackQueryClause x);

    void endVisit(AsOfFlashbackQueryClause x);

    boolean visit(GroupingSetExpr x);

    void endVisit(GroupingSetExpr x);

    boolean visit(SubqueryFactoringClause x);

    void endVisit(SubqueryFactoringClause x);

    boolean visit(SubqueryFactoringClause.Entry x);

    void endVisit(SubqueryFactoringClause.Entry x);

    boolean visit(SearchClause x);

    void endVisit(SearchClause x);

    boolean visit(CycleClause x);

    void endVisit(CycleClause x);

    boolean visit(OracleBinaryFloatExpr x);

    void endVisit(OracleBinaryFloatExpr x);

    boolean visit(OracleBinaryDoubleExpr x);

    void endVisit(OracleBinaryDoubleExpr x);

    boolean visit(OracleSelect x);

    void endVisit(OracleSelect x);

    boolean visit(OracleCursorExpr x);

    void endVisit(OracleCursorExpr x);

    boolean visit(OracleIsSetExpr x);

    void endVisit(OracleIsSetExpr x);

    boolean visit(ModelClause.ReturnRowsClause x);

    void endVisit(ModelClause.ReturnRowsClause x);

    boolean visit(ModelClause.MainModelClause x);

    void endVisit(ModelClause.MainModelClause x);

    boolean visit(ModelClause.ModelColumnClause x);

    void endVisit(ModelClause.ModelColumnClause x);

    boolean visit(ModelClause.QueryPartitionClause x);

    void endVisit(ModelClause.QueryPartitionClause x);

    boolean visit(ModelClause.ModelColumn x);

    void endVisit(ModelClause.ModelColumn x);

    boolean visit(ModelClause.ModelRulesClause x);

    void endVisit(ModelClause.ModelRulesClause x);

    boolean visit(ModelClause.CellAssignmentItem x);

    void endVisit(ModelClause.CellAssignmentItem x);

    boolean visit(ModelClause.CellAssignment x);

    void endVisit(ModelClause.CellAssignment x);

    boolean visit(ModelClause x);

    void endVisit(ModelClause x);

    boolean visit(OracleMergeStatement x);

    void endVisit(OracleMergeStatement x);

    boolean visit(OracleMergeStatement.MergeUpdateClause x);

    void endVisit(OracleMergeStatement.MergeUpdateClause x);

    boolean visit(OracleMergeStatement.MergeInsertClause x);

    void endVisit(OracleMergeStatement.MergeInsertClause x);

    boolean visit(OracleErrorLoggingClause x);

    void endVisit(OracleErrorLoggingClause x);

    boolean visit(OracleReturningClause x);

    void endVisit(OracleReturningClause x);

    boolean visit(OracleInsertStatement x);

    void endVisit(OracleInsertStatement x);

    boolean visit(InsertIntoClause x);

    void endVisit(InsertIntoClause x);

    boolean visit(OracleMultiInsertStatement x);

    void endVisit(OracleMultiInsertStatement x);

    boolean visit(ConditionalInsertClause x);

    void endVisit(ConditionalInsertClause x);

    boolean visit(ConditionalInsertClauseItem x);

    void endVisit(ConditionalInsertClauseItem x);

    boolean visit(OracleSelectQueryBlock x);

    void endVisit(OracleSelectQueryBlock x);

    boolean visit(OracleBlockStatement x);

    void endVisit(OracleBlockStatement x);

    boolean visit(OracleLockTableStatement x);

    void endVisit(OracleLockTableStatement x);

    boolean visit(OracleAlterSessionStatement x);

    void endVisit(OracleAlterSessionStatement x);

    boolean visit(OracleExprStatement x);

    void endVisit(OracleExprStatement x);

    boolean visit(OracleDatetimeExpr x);

    void endVisit(OracleDatetimeExpr x);

    boolean visit(OracleSysdateExpr x);

    void endVisit(OracleSysdateExpr x);

    boolean visit(OracleExceptionStatement x);

    void endVisit(OracleExceptionStatement x);

    boolean visit(OracleExceptionStatement.Item x);

    void endVisit(OracleExceptionStatement.Item x);

    boolean visit(OracleArgumentExpr x);

    void endVisit(OracleArgumentExpr x);

    boolean visit(OracleSetTransactionStatement x);

    void endVisit(OracleSetTransactionStatement x);

    boolean visit(OracleGrantStatement x);

    void endVisit(OracleGrantStatement x);

    boolean visit(OracleExplainStatement x);

    void endVisit(OracleExplainStatement x);

    boolean visit(OracleAlterProcedureStatement x);

    void endVisit(OracleAlterProcedureStatement x);

    boolean visit(OracleAlterTableDropPartition x);

    void endVisit(OracleAlterTableDropPartition x);

    boolean visit(OracleAlterTableTruncatePartition x);

    void endVisit(OracleAlterTableTruncatePartition x);

    boolean visit(OracleAlterTableStatement x);

    void endVisit(OracleAlterTableStatement x);

    boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x);

    void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x);

    boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x);

    void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x);

    boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x);

    void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x);

    boolean visit(OracleAlterTableSplitPartition x);

    void endVisit(OracleAlterTableSplitPartition x);

    boolean visit(OracleAlterTableModify x);

    void endVisit(OracleAlterTableModify x);

    boolean visit(OracleCreateIndexStatement x);

    void endVisit(OracleCreateIndexStatement x);

    boolean visit(OracleForStatement x);

    void endVisit(OracleForStatement x);

    boolean visit(Else x);

    void endVisit(Else x);

    boolean visit(ElseIf x);

    void endVisit(ElseIf x);

    boolean visit(OracleIfStatement x);

    void endVisit(OracleIfStatement x);

    boolean visit(OracleRangeExpr x);

    void endVisit(OracleRangeExpr x);

    boolean visit(OracleAlterIndexStatement x);

    void endVisit(OracleAlterIndexStatement x);

    boolean visit(OracleAlterTableAddConstaint x);

    void endVisit(OracleAlterTableAddConstaint x);

    boolean visit(OracleAlterTableRenameTo x);

    void endVisit(OracleAlterTableRenameTo x);

    boolean visit(OraclePrimaryKey x);

    void endVisit(OraclePrimaryKey x);

    boolean visit(OracleCreateTableStatement x);

    void endVisit(OracleCreateTableStatement x);

    boolean visit(OracleAlterIndexStatement.Rebuild x);

    void endVisit(OracleAlterIndexStatement.Rebuild x);

    boolean visit(OracleStorageClause x);

    void endVisit(OracleStorageClause x);

    boolean visit(OracleGotoStatement x);

    void endVisit(OracleGotoStatement x);

    boolean visit(OracleLabelStatement x);

    void endVisit(OracleLabelStatement x);

    boolean visit(OracleParameter x);

    void endVisit(OracleParameter x);

    boolean visit(OracleCommitStatement x);

    void endVisit(OracleCommitStatement x);

    boolean visit(OracleAlterTriggerStatement x);

    void endVisit(OracleAlterTriggerStatement x);

    boolean visit(OracleAlterSynonymStatement x);

    void endVisit(OracleAlterSynonymStatement x);

    boolean visit(OracleAlterViewStatement x);

    void endVisit(OracleAlterViewStatement x);

    boolean visit(AsOfSnapshotClause x);

    void endVisit(AsOfSnapshotClause x);

    boolean visit(OracleAlterTableMoveTablespace x);

    void endVisit(OracleAlterTableMoveTablespace x);

    boolean visit(OracleSizeExpr x);

    void endVisit(OracleSizeExpr x);

    boolean visit(OracleFileSpecification x);

    void endVisit(OracleFileSpecification x);

    boolean visit(OracleAlterTablespaceAddDataFile x);

    void endVisit(OracleAlterTablespaceAddDataFile x);

    boolean visit(OracleAlterTablespaceStatement x);

    void endVisit(OracleAlterTablespaceStatement x);

    boolean visit(OracleTruncateStatement x);

    void endVisit(OracleTruncateStatement x);

    boolean visit(OracleCreateSequenceStatement x);

    void endVisit(OracleCreateSequenceStatement x);

    boolean visit(OracleRangeValuesClause x);

    void endVisit(OracleRangeValuesClause x);

    boolean visit(OraclePartitionByRangeClause x);

    void endVisit(OraclePartitionByRangeClause x);

    boolean visit(OracleLoopStatement x);

    void endVisit(OracleLoopStatement x);

    boolean visit(OracleExitStatement x);

    void endVisit(OracleExitStatement x);

    boolean visit(OracleFetchStatement x);

    void endVisit(OracleFetchStatement x);

    boolean visit(OracleSavePointStatement x);

    void endVisit(OracleSavePointStatement x);

    boolean visit(OracleCreateProcedureStatement x);

    void endVisit(OracleCreateProcedureStatement x);
}
