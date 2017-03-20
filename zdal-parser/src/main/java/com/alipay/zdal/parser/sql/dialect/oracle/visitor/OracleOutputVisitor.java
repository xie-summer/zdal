/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.visitor;

import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLDataType;
import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.SQLSetQuantifier;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLAllColumnExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLMethodInvokeExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLObjectCreateExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLQueryExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.alipay.zdal.parser.sql.ast.statement.SQLRollbackStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleOrderBy;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.CycleClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfFlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfSnapshotClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause.VersionsFlashbackQueryClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.GroupingSetExpr;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.CellAssignment;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.CellAssignmentItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.CellReferenceOption;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelColumn;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelColumnClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelRuleOption;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ModelRulesClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.QueryPartitionClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.ModelClause.ReferenceModelClause;
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
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement.Rebuild;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterProcedureStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableAddConstaint;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableRenameTo;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.NestedTablePartitionSpec;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.TableSpaceItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.UpdateIndexesClause;
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
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
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
import com.alipay.zdal.parser.sql.visitor.SQLASTOutputVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleOutputVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:52:58 Exp $
 */
public class OracleOutputVisitor extends SQLASTOutputVisitor implements OracleASTVisitor {

    private final boolean printPostSemi;

    public OracleOutputVisitor(Appendable appender) {
        this(appender, true);
    }

    public OracleOutputVisitor(Appendable appender, boolean printPostSemi) {
        super(appender);
        this.printPostSemi = printPostSemi;
    }

    public boolean isPrintPostSemi() {
        return printPostSemi;
    }

    public void postVisit(SQLObject x) {
        if (!printPostSemi) {
            return;
        }

        if (x instanceof SQLStatement) {
            if (x instanceof OraclePLSQLCommitStatement) {
                return;
            }
            if (x.getParent() instanceof OracleCreateProcedureStatement) {
                return;
            }

            if (x.getParent() != null) {
                print(";");
            } else {
                println(";");
            }
        }
    }

    private void printHints(List<SQLHint> hints) {
        if (hints.size() > 0) {
            print("/*+ ");
            printAndAccept(hints, ", ");
            print(" */");
        }
    }

    public boolean visit(OracleAggregateExpr expr) {
        print(expr.getMethodName());
        print("(");

        if (expr.getOption() != null) {
            print(expr.getOption().toString());
            print(' ');
        }

        printAndAccept(expr.getArguments(), ", ");
        print(")");

        if (expr.getOver() != null) {
            print(" OVER (");
            expr.getOver().accept(this);
            print(")");
        }
        return false;
    }

    public boolean visit(SQLAllColumnExpr x) {
        print("*");
        return false;
    }

    public boolean visit(OracleAnalytic x) {
        boolean space = false;
        if (x.getPartitionBy().size() > 0) {
            print("PARTITION BY ");
            printAndAccept(x.getPartitionBy(), ", ");

            space = true;
        }

        if (x.getOrderBy() != null) {
            if (space) {
                print(" ");
            }
            x.getOrderBy().accept(this);
            space = true;
        }

        if (x.getWindowing() != null) {
            if (space) {
                print(" ");
            }
            x.getWindowing().accept(this);
        }

        return false;
    }

    public boolean visit(OracleAnalyticWindowing x) {
        print(x.getType().name().toUpperCase());
        print(" ");
        x.getExpr().accept(this);
        return false;
    }

    public boolean visit(OracleDateExpr x) {
        print("DATE '");
        print(x.getLiteral());
        print('\'');
        return false;
    }

    public boolean visit(OracleDbLinkExpr x) {
        x.getExpr().accept(this);
        print("@");
        print(x.getDbLink());
        return false;
    }

    public boolean visit(OracleDeleteStatement x) {
        if (x.getTableName() != null) {
            print("DELETE ");
            printHints(x.getHints());

            print("FROM ");
            if (x.isOnly()) {
                print("ONLY (");
                x.getTableName().accept(this);
                print(")");
            } else {
                x.getTableName().accept(this);
            }

            printAlias(x.getAlias());
        }

        if (x.getWhere() != null) {
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getReturning() != null) {
            println();
            x.getReturning().accept(this);
        }

        return false;
    }

    public boolean visit(OracleExtractExpr x) {
        print("EXTRACT(");
        print(x.getUnit().name());
        print(" FROM ");
        x.getFrom().accept(this);
        print(")");
        return false;
    }

    public boolean visit(OracleIntervalExpr x) {
        if (x.getValue() instanceof SQLLiteralExpr) {
            print("INTERVAL ");
            x.getValue().accept(this);
            print(" ");
        } else {
            print('(');
            x.getValue().accept(this);
            print(") ");
        }

        print(x.getType().name());

        if (x.getPrecision() != null) {
            print("(");
            print(x.getPrecision().intValue());
            if (x.getFactionalSecondsPrecision() != null) {
                print(", ");
                print(x.getFactionalSecondsPrecision().intValue());
            }
            print(")");
        }

        if (x.getToType() != null) {
            print(" TO ");
            print(x.getToType().name());
            if (x.getToFactionalSecondsPrecision() != null) {
                print("(");
                print(x.getToFactionalSecondsPrecision().intValue());
                print(")");
            }
        }

        return false;
    }

    public boolean visit(OracleOrderBy x) {
        if (x.getItems().size() > 0) {
            print("ORDER ");
            if (x.isSibings()) {
                print("SIBLINGS ");
            }
            print("BY ");

            printAndAccept(x.getItems(), ", ");
        }
        return false;
    }

    public boolean visit(OracleOuterExpr x) {
        x.getExpr().accept(this);
        print("(+)");
        return false;
    }

    public boolean visit(OraclePLSQLCommitStatement astNode) {
        print("/");
        println();
        return false;
    }

    public boolean visit(SQLSelect x) {
        if (x instanceof OracleSelect) {
            return visit((OracleSelect) x);
        }

        return super.visit(x);
    }

    public boolean visit(OracleSelect x) {
        if (x.getFactoring() != null) {
            x.getFactoring().accept(this);
            println();
        }

        x.getQuery().accept(this);

        if (x.getRestriction() != null) {
            print(" ");
            x.getRestriction().accept(this);
        }

        if (x.getForUpdate() != null) {
            println();
            x.getForUpdate().accept(this);
        }

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        return false;
    }

    public boolean visit(OracleSelectForUpdate x) {
        print("FOR UPDATE");
        if (x.getOf().size() > 0) {
            print("(");
            printAndAccept(x.getOf(), ", ");
            print(")");
        }

        if (x.isNotWait()) {
            print(" NOWAIT");
        } else if (x.isSkipLocked()) {
            print(" SKIP LOCKED");
        } else if (x.getWait() != null) {
            print(" WAIT ");
            x.getWait().accept(this);
        }
        return false;
    }

    public boolean visit(OracleSelectHierachicalQueryClause x) {
        if (x.getStartWith() != null) {
            print("START WITH ");
            x.getStartWith().accept(this);
            println();
        }

        print("CONNECT BY ");

        if (x.isPrior()) {
            print("PRIOR ");
        }

        if (x.isNoCycle()) {
            print("NOCYCLE ");
        }

        x.getConnectBy().accept(this);

        return false;
    }

    public boolean visit(OracleSelectJoin x) {
        x.getLeft().accept(this);

        if (x.getJoinType() == JoinType.COMMA) {
            print(", ");
            x.getRight().accept(this);
        } else {
            boolean isRoot = x.getParent() instanceof SQLSelectQueryBlock;
            if (isRoot) {
                incrementIndent();
            }

            println();
            print(JoinType.toString(x.getJoinType()));
            print(" ");

            x.getRight().accept(this);

            if (isRoot) {
                decrementIndent();
            }

            if (x.getCondition() != null) {
                print(" ON ");
                x.getCondition().accept(this);
                print(" ");
            }

            if (x.getUsing().size() > 0) {
                print(" USING (");
                printAndAccept(x.getUsing(), ", ");
                print(")");
            }

            if (x.getFlashback() != null) {
                println();
                x.getFlashback().accept(this);
            }
        }

        return false;
    }

    public boolean visit(OracleOrderByItem x) {
        x.getExpr().accept(this);
        if (x.getType() != null) {
            print(" ");
            print(x.getType().name().toUpperCase());
        }

        if (x.getNullsOrderType() != null) {
            print(" ");
            print(x.getNullsOrderType().toFormalString());
        }

        return false;
    }

    public boolean visit(OracleSelectPivot x) {
        print("PIVOT");
        if (x.isXml()) {
            print(" XML");
        }
        print(" (");
        printAndAccept(x.getItems(), ", ");

        if (x.getPivotFor().size() > 0) {
            print(" FOR ");
            if (x.getPivotFor().size() == 1) {
                ((SQLExpr) x.getPivotFor().get(0)).accept(this);
            } else {
                print("(");
                printAndAccept(x.getPivotFor(), ", ");
                print(")");
            }
        }

        if (x.getPivotIn().size() > 0) {
            print(" IN (");
            printAndAccept(x.getPivotIn(), ", ");
            print(")");
        }

        print(")");

        return false;
    }

    public boolean visit(OracleSelectPivot.Item x) {
        x.getExpr().accept(this);
        if ((x.getAlias() != null) && (x.getAlias().length() > 0)) {
            print(" AS ");
            print(x.getAlias());
        }
        return false;
    }

    public boolean visit(SQLSelectQueryBlock select) {
        if (select instanceof OracleSelectQueryBlock) {
            return visit((OracleSelectQueryBlock) select);
        }

        return super.visit(select);
    }

    public boolean visit(OracleSelectQueryBlock x) {
        print("SELECT ");

        if (SQLSetQuantifier.ALL == x.getDistionOption()) {
            print("ALL ");
        } else if (SQLSetQuantifier.DISTINCT == x.getDistionOption()) {
            print("DISTINCT ");
        } else if (SQLSetQuantifier.UNIQUE == x.getDistionOption()) {
            print("UNIQUE ");
        }

        if (x.getHints().size() > 0) {
            print("/*+");
            printAndAccept(x.getHints(), ", ");
            print("*/ ");
        }

        printSelectList(x.getSelectList());

        if (x.getInto() != null) {
            println();
            print("INTO ");
            x.getInto().accept(this);
        }

        println();
        print("FROM ");
        if (x.getFrom() == null) {
            print("DUAL");
        } else {
            x.getFrom().setParent(x);
            x.getFrom().accept(this);
        }

        if (x.getWhere() != null) {
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getHierachicalQueryClause() != null) {
            println();
            x.getHierachicalQueryClause().accept(this);
        }

        if (x.getGroupBy() != null) {
            println();
            x.getGroupBy().accept(this);
        }

        if (x.getModelClause() != null) {
            println();
            x.getModelClause().accept(this);
        }

        return false;
    }

    public boolean visit(OracleSelectRestriction.CheckOption x) {
        print("CHECK OPTION");
        if (x.getConstraint() != null) {
            print(" ");
            x.getConstraint().accept(this);
        }
        return false;
    }

    public boolean visit(OracleSelectRestriction.ReadOnly x) {
        print("READ ONLY");
        return false;
    }

    public boolean visit(OracleSelectSubqueryTableSource x) {
        print("(");
        incrementIndent();
        println();
        x.getSelect().accept(this);
        decrementIndent();
        println();
        print(")");

        if (x.getPivot() != null) {
            println();
            x.getPivot().accept(this);
        }

        if (x.getFlashback() != null) {
            println();
            x.getFlashback().accept(this);
        }

        if ((x.getAlias() != null) && (x.getAlias().length() != 0)) {
            print(" ");
            print(x.getAlias());
        }

        return false;
    }

    public boolean visit(OracleSelectTableReference x) {
        if (x.isOnly()) {
            print("ONLY (");
            x.getExpr().accept(this);

            if (x.getPartition() != null) {
                print(" ");
                x.getPartition().accept(this);
            }

            print(")");
        } else {
            x.getExpr().accept(this);

            if (x.getPartition() != null) {
                print(" ");
                x.getPartition().accept(this);
            }
        }

        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }

        if (x.getSampleClause() != null) {
            print(" ");
            x.getSampleClause().accept(this);
        }

        if (x.getPivot() != null) {
            println();
            x.getPivot().accept(this);
        }

        if (x.getFlashback() != null) {
            println();
            x.getFlashback().accept(this);
        }

        printAlias(x.getAlias());

        return false;
    }

    public boolean visit(OracleSelectUnPivot x) {
        print("UNPIVOT");
        if (x.getNullsIncludeType() != null) {
            print(" ");
            print(OracleSelectUnPivot.NullsIncludeType.toString(x.getNullsIncludeType()));
        }

        print(" (");
        if (x.getItems().size() == 1) {
            ((SQLExpr) x.getItems().get(0)).accept(this);
        } else {
            print(" (");
            printAndAccept(x.getItems(), ", ");
            print(")");
        }

        if (x.getPivotFor().size() > 0) {
            print(" FOR ");
            if (x.getPivotFor().size() == 1) {
                ((SQLExpr) x.getPivotFor().get(0)).accept(this);
            } else {
                print("(");
                printAndAccept(x.getPivotFor(), ", ");
                print(")");
            }
        }

        if (x.getPivotIn().size() > 0) {
            print(" IN (");
            printAndAccept(x.getPivotIn(), ", ");
            print(")");
        }

        print(")");
        return false;
    }

    public boolean visit(OracleTableExpr x) {
        x.getTable().accept(this);

        if (x.getPartition() != null) {
            print(" PARTITION (");
            x.getPartition().accept(this);
            print(")");
        } else {
            if (x.getPartitionFor().size() > 0) {
                print(" PARTITION FOR (");
                for (int i = 0, size = x.getPartitionFor().size(); i < size; ++i) {
                    ((SQLName) x.getPartitionFor().get(i)).accept(this);
                }
                print(")");
            } else if (x.getSubPartition() != null) {
                print(" SUBPARTITION (");
                x.getSubPartition().accept(this);
                print(")");
            } else if (x.getSubPartitionFor().size() > 0) {
                print(" SUBPARTITION FOR (");
                for (int i = 0, size = x.getSubPartitionFor().size(); i < size; ++i) {
                    ((SQLName) x.getSubPartitionFor().get(i)).accept(this);
                }
                print(")");
            }
        }
        return false;
    }

    public boolean visit(OracleTimestampExpr x) {
        print("TIMESTAMP '");

        print(x.getLiteral());
        print('\'');

        if (x.getTimeZone() != null) {
            print(" AT TIME ZONE '");
            print(x.getTimeZone());
            print('\'');
        }

        return false;
    }

    public boolean visit(OracleUpdateSetListClause x) {
        print("SET ");
        printAndAccept(x.getItems(), ", ");
        return false;
    }

    public boolean visit(OracleUpdateSetListMultiColumnItem x) {
        print("(");
        printAndAccept(x.getColumns(), ", ");
        print(") = (");
        x.getSubQuery().accept(this);
        print(")");
        return false;
    }

    public boolean visit(OracleUpdateSetListSingleColumnItem x) {
        x.getColumn().accept(this);
        print(" = ");
        x.getValue().accept(this);
        return false;
    }

    public boolean visit(OracleUpdateSetValueClause x) {
        throw new UnsupportedOperationException();
    }

    public boolean visit(OracleUpdateStatement x) {
        print("UPDATE ");
        printHints(x.getHints());

        if (x.isOnly()) {
            print("ONLY (");
            x.getTableSource().accept(this);
            print(")");
        } else {
            x.getTableSource().accept(this);
        }

        printAlias(x.getAlias());

        println();

        print("SET ");
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
            }
            x.getItems().get(i).accept(this);
        }

        if (x.getWhere() != null) {
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getReturning().size() > 0) {
            println();
            print("RETURNING ");
            printAndAccept(x.getReturning(), ", ");
            print(" INTO ");
            printAndAccept(x.getReturningInto(), ", ");
        }

        return false;
    }

    // ///////////////////

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

    public void endVisit(SQLDataType x) {

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

    public void endVisit(SQLMethodInvokeExpr x) {

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

    public boolean visit(SampleClause x) {
        print("SAMPLE ");

        if (x.isBlock()) {
            print("BLOCK ");
        }

        print("(");
        printAndAccept(x.getPercent(), ", ");
        print(")");

        if (x.getSeedValue() != null) {
            print(" SEED (");
            x.getSeedValue().accept(this);
            print(")");
        }

        return false;
    }

    public void endVisit(SampleClause x) {

    }

    public void endVisit(OracleSelectTableReference x) {

    }

    public boolean visit(PartitionExtensionClause x) {
        if (x.isSubPartition()) {
            print("SUBPARTITION ");
        } else {
            print("PARTITION ");
        }

        if (x.getPartition() != null) {
            print("(");
            x.getPartition().accept(this);
            print(")");
        } else {
            print("FOR (");
            printAndAccept(x.getFor(), ",");
            print(")");
        }
        return false;
    }

    public void endVisit(PartitionExtensionClause x) {

    }

    public boolean visit(VersionsFlashbackQueryClause x) {
        print("VERSIONS BETWEEN ");
        print(x.getType().name());
        print(" ");
        x.getBegin().accept(this);
        print(" AND ");
        x.getEnd().accept(this);
        return false;
    }

    public void endVisit(VersionsFlashbackQueryClause x) {

    }

    public boolean visit(AsOfFlashbackQueryClause x) {
        print("AS OF ");
        print(x.getType().name());
        print(" (");
        x.getExpr().accept(this);
        print(")");
        return false;
    }

    public void endVisit(AsOfFlashbackQueryClause x) {

    }

    public boolean visit(GroupingSetExpr x) {
        print("GROUPING SETS");
        print(" (");
        printAndAccept(x.getParameters(), ", ");
        print(")");
        return false;
    }

    public void endVisit(GroupingSetExpr x) {

    }

    public boolean visit(SubqueryFactoringClause.Entry x) {
        x.getName().accept(this);

        if (x.getColumns().size() > 0) {
            print(" (");
            printAndAccept(x.getColumns(), ", ");
            print(")");
        }
        println();
        print("AS");
        println();
        print("(");
        incrementIndent();
        println();
        x.getSubQuery().accept(this);
        decrementIndent();
        println();
        print(")");

        if (x.getSearchClause() != null) {
            println();
            x.getSearchClause().accept(this);
        }

        if (x.getCycleClause() != null) {
            println();
            x.getCycleClause().accept(this);
        }
        return false;
    }

    public void endVisit(SubqueryFactoringClause.Entry x) {

    }

    public boolean visit(SubqueryFactoringClause x) {
        print("WITH");
        incrementIndent();
        println();
        printlnAndAccept(x.getEntries(), ", ");
        decrementIndent();
        return false;
    }

    public void endVisit(SubqueryFactoringClause x) {

    }

    public boolean visit(SearchClause x) {
        print("SEARCH ");
        print(x.getType().name());
        print(" FIRST BY ");
        printAndAccept(x.getItems(), ", ");
        print(" SET ");
        x.getOrderingColumn().accept(this);

        return false;
    }

    public void endVisit(SearchClause x) {

    }

    public boolean visit(CycleClause x) {
        print("CYCLE ");
        printAndAccept(x.getAliases(), ", ");
        print(" SET ");
        x.getMark().accept(this);
        print(" TO ");
        x.getValue().accept(this);
        print(" DEFAULT ");
        x.getDefaultValue().accept(this);

        return false;
    }

    public void endVisit(CycleClause x) {

    }

    public boolean visit(OracleBinaryFloatExpr x) {
        print(x.getValue().toString());
        print('F');
        return false;
    }

    public void endVisit(OracleBinaryFloatExpr x) {

    }

    public boolean visit(OracleBinaryDoubleExpr x) {
        print(x.getValue().toString());
        print('D');
        return false;
    }

    public void endVisit(OracleBinaryDoubleExpr x) {

    }

    public void endVisit(OracleSelect x) {

    }

    public boolean visit(OracleConstraintState x) {
        printlnAndAccept(x.getStates(), " ");
        return false;
    }

    public boolean visit(OracleCursorExpr x) {
        print("CURSOR(");
        incrementIndent();
        println();
        x.getQuery().accept(this);
        decrementIndent();
        println();
        print(")");
        return false;
    }

    public void endVisit(OracleCursorExpr x) {

    }

    public boolean visit(OracleIsSetExpr x) {
        x.getNestedTable().accept(this);
        print(" IS A SET");
        return false;
    }

    public void endVisit(OracleIsSetExpr x) {

    }

    public boolean visit(ReturnRowsClause x) {
        if (x.isAll()) {
            print("RETURN ALL ROWS");
        } else {
            print("RETURN UPDATED ROWS");
        }
        return false;
    }

    public void endVisit(ReturnRowsClause x) {

    }

    public boolean visit(ModelClause x) {
        print("MODEL");

        incrementIndent();
        for (CellReferenceOption opt : x.getCellReferenceOptions()) {
            print(' ');
            print(opt.name);
        }

        if (x.getReturnRowsClause() != null) {
            print(' ');
            x.getReturnRowsClause().accept(this);
        }

        for (ReferenceModelClause item : x.getReferenceModelClauses()) {
            print(' ');
            item.accept(this);
        }

        x.getMainModel().accept(this);
        decrementIndent();

        return false;
    }

    public void endVisit(ModelClause x) {

    }

    public boolean visit(MainModelClause x) {
        if (x.getMainModelName() != null) {
            print(" MAIN ");
            x.getMainModelName().accept(this);
        }

        println();
        x.getModelColumnClause().accept(this);

        for (CellReferenceOption opt : x.getCellReferenceOptions()) {
            println();
            print(opt.name);
        }

        println();
        x.getModelRulesClause().accept(this);

        return false;
    }

    public void endVisit(MainModelClause x) {

    }

    public boolean visit(ModelColumnClause x) {
        if (x.getQueryPartitionClause() != null) {
            x.getQueryPartitionClause().accept(this);
            println();
        }

        print("DIMENSION BY (");
        printAndAccept(x.getDimensionByColumns(), ", ");
        print(")");

        println();
        print("MEASURES (");
        printAndAccept(x.getMeasuresColumns(), ", ");
        print(")");
        return false;
    }

    public void endVisit(ModelColumnClause x) {

    }

    public boolean visit(QueryPartitionClause x) {
        print("PARTITION BY (");
        printAndAccept(x.getExprList(), ", ");
        print(")");
        return false;
    }

    public void endVisit(QueryPartitionClause x) {

    }

    public boolean visit(ModelColumn x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null) {
            print(" ");
            print(x.getAlias());
        }
        return false;
    }

    public void endVisit(ModelColumn x) {

    }

    public boolean visit(ModelRulesClause x) {
        if (x.getOptions().size() > 0) {
            print("RULES");
            for (ModelRuleOption opt : x.getOptions()) {
                print(" ");
                print(opt.name);
            }
        }

        if (x.getIterate() != null) {
            print(" ITERATE (");
            x.getIterate().accept(this);
            print(")");

            if (x.getUntil() != null) {
                print(" UNTIL (");
                x.getUntil().accept(this);
                print(")");
            }
        }

        print(" (");
        printAndAccept(x.getCellAssignmentItems(), ", ");
        print(")");
        return false;

    }

    public void endVisit(ModelRulesClause x) {

    }

    public boolean visit(CellAssignmentItem x) {
        if (x.getOption() != null) {
            print(x.getOption().name);
            print(" ");
        }

        x.getCellAssignment().accept(this);

        if (x.getOrderBy() != null) {
            print(" ");
            x.getOrderBy().accept(this);
        }

        print(" = ");
        x.getExpr().accept(this);

        return false;
    }

    public void endVisit(CellAssignmentItem x) {

    }

    public boolean visit(CellAssignment x) {
        x.getMeasureColumn().accept(this);
        print("[");
        printAndAccept(x.getConditions(), ", ");
        print("]");
        return false;
    }

    public void endVisit(CellAssignment x) {

    }

    public boolean visit(OracleMergeStatement x) {
        print("MERGE ");
        if (x.getHints().size() > 0) {
            printAndAccept(x.getHints(), ", ");
            print(" ");
        }

        print("INTO ");
        x.getInto().accept(this);

        if (x.getAlias() != null) {
            print(" ");
            print(x.getAlias());
        }

        println();
        print("USING ");
        x.getUsing().accept(this);

        print(" ON ");
        x.getOn().accept(this);

        if (x.getUpdateClause() != null) {
            println();
            x.getUpdateClause().accept(this);
        }

        if (x.getInsertClause() != null) {
            println();
            x.getInsertClause().accept(this);
        }

        if (x.getErrorLoggingClause() != null) {
            println();
            x.getErrorLoggingClause().accept(this);
        }

        return false;
    }

    public void endVisit(OracleMergeStatement x) {

    }

    public boolean visit(MergeUpdateClause x) {
        print("WHEN MATCHED THEN UPDATE SET ");
        printAndAccept(x.getItems(), ", ");
        if (x.getWhere() != null) {
            incrementIndent();
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            decrementIndent();
        }

        if (x.getDeleteWhere() != null) {
            incrementIndent();
            println();
            print("DELETE WHERE ");
            x.getDeleteWhere().setParent(x);
            x.getDeleteWhere().accept(this);
            decrementIndent();
        }

        return false;
    }

    public void endVisit(MergeUpdateClause x) {

    }

    public boolean visit(MergeInsertClause x) {
        print("WHEN NOT MATCHED THEN INSERT");
        if (x.getColumns().size() > 0) {
            print(" ");
            printAndAccept(x.getColumns(), ", ");
        }
        print(" VALUES (");
        printAndAccept(x.getValues(), ", ");
        print(")");
        if (x.getWhere() != null) {
            incrementIndent();
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            decrementIndent();
        }

        return false;
    }

    public void endVisit(MergeInsertClause x) {

    }

    public boolean visit(OracleErrorLoggingClause x) {
        print("LOG ERRORS ");
        if (x.getInto() != null) {
            print("INTO ");
            x.getInto().accept(this);
            print(" ");
        }

        if (x.getSimpleExpression() != null) {
            print("(");
            x.getSimpleExpression().accept(this);
            print(")");
        }

        if (x.getLimit() != null) {
            print(" REJECT LIMIT ");
            x.getLimit().accept(this);
        }

        return false;
    }

    public void endVisit(OracleErrorLoggingClause x) {

    }

    public boolean visit(OracleReturningClause x) {
        print("RETURNING ");
        printAndAccept(x.getItems(), ", ");
        print(" INTO ");
        printAndAccept(x.getValues(), ", ");

        return false;
    }

    public void endVisit(OracleReturningClause x) {

    }

    public boolean visit(OracleInsertStatement x) {
        visit((SQLInsertStatement) x);

        if (x.getReturning() != null) {
            println();
            x.getReturning().accept(this);
        }

        if (x.getErrorLogging() != null) {
            println();
            x.getErrorLogging().accept(this);
        }

        return false;
    }

    public void endVisit(OracleInsertStatement x) {
        endVisit((SQLInsertStatement) x);
    }

    public boolean visit(InsertIntoClause x) {
        print("INTO ");

        x.getTableName().accept(this);

        if (x.getColumns().size() > 0) {
            incrementIndent();
            println();
            print("(");
            for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        println();
                    }
                    print(", ");
                }
                x.getColumns().get(i).accept(this);
            }
            print(")");
            decrementIndent();
        }

        if (x.getValues() != null) {
            println();
            print("VALUES ");
            x.getValues().accept(this);
        } else {
            if (x.getQuery() != null) {
                println();
                x.getQuery().setParent(x);
                x.getQuery().accept(this);
            }
        }

        return false;
    }

    public void endVisit(InsertIntoClause x) {

    }

    public boolean visit(OracleMultiInsertStatement x) {
        print("INSERT ");

        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }

        if (x.getOption() != null) {
            print(x.getOption().name());
            print(" ");
        }

        for (int i = 0, size = x.getEntries().size(); i < size; ++i) {
            incrementIndent();
            println();
            x.getEntries().get(i).accept(this);
            decrementIndent();
        }

        println();
        x.getSubQuery().accept(this);

        return false;
    }

    public void endVisit(OracleMultiInsertStatement x) {

    }

    public boolean visit(ConditionalInsertClause x) {
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                println();
            }

            ConditionalInsertClauseItem item = x.getItems().get(i);

            item.accept(this);
        }

        if (x.getElseItem() != null) {
            println();
            print("ELSE");
            incrementIndent();
            println();
            x.getElseItem().accept(this);
            decrementIndent();
        }

        return false;
    }

    public void endVisit(ConditionalInsertClause x) {

    }

    public boolean visit(ConditionalInsertClauseItem x) {
        print("WHEN ");
        x.getWhen().accept(this);
        print(" THEN");
        incrementIndent();
        println();
        x.getThen().accept(this);
        decrementIndent();
        return false;
    }

    public void endVisit(ConditionalInsertClauseItem x) {

    }

    public void endVisit(OracleSelectQueryBlock x) {

    }

    public boolean visit(OracleBlockStatement x) {
        if (x.getParameters().size() != 0) {
            print("DECLARE");
            incrementIndent();
            println();

            for (int i = 0, size = x.getParameters().size(); i < size; ++i) {
                if (i != 0) {
                    println();
                }
                OracleParameter param = x.getParameters().get(i);
                param.accept(this);
                print(";");
            }

            decrementIndent();
            println();
        }
        print("BEGIN");
        incrementIndent();
        println();
        for (int i = 0, size = x.getStatementList().size(); i < size; ++i) {
            if (i != 0) {
                println();
            }
            SQLStatement stmt = x.getStatementList().get(i);
            stmt.setParent(x);
            stmt.accept(this);
        }
        decrementIndent();
        println();
        print("END");
        return false;
    }

    public void endVisit(OracleBlockStatement x) {

    }

    public boolean visit(OracleLockTableStatement x) {
        print("LOCK TABLE ");
        x.getTable().accept(this);
        print(" IN ");
        print(x.getLockMode().name());
        print(" MODE ");
        if (x.isNoWait()) {
            print("NOWAIT");
        } else if (x.getWait() != null) {
            print("WAIT ");
            x.getWait().accept(this);
        }
        return false;
    }

    public void endVisit(OracleLockTableStatement x) {

    }

    public boolean visit(OracleAlterSessionStatement x) {
        print("ALTER SESSION SET ");
        printAndAccept(x.getItems(), ", ");
        return false;
    }

    public void endVisit(OracleAlterSessionStatement x) {

    }

    public boolean visit(OracleExprStatement x) {
        x.getExpr().accept(this);
        return false;
    }

    public void endVisit(OracleExprStatement x) {

    }

    public boolean visit(OracleDatetimeExpr x) {
        x.getExpr().accept(this);
        SQLExpr timeZone = x.getTimeZone();

        if (timeZone instanceof SQLIdentifierExpr) {
            if (((SQLIdentifierExpr) timeZone).getName().equalsIgnoreCase("LOCAL")) {
                print(" AT LOCAL");
                return false;
            }
        }

        print(" AT TIME ZONE ");
        timeZone.accept(this);

        return false;
    }

    public void endVisit(OracleDatetimeExpr x) {

    }

    public boolean visit(OracleSysdateExpr x) {
        print("SYSDATE");
        if (x.getOption() != null) {
            print("@");
            print(x.getOption());
        }
        return false;
    }

    public void endVisit(OracleSysdateExpr x) {

    }

    public void endVisit(com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {

    }

    public boolean visit(com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {
        print("WHEN ");
        x.getWhen().accept(this);
        incrementIndent();

        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            println();
            SQLStatement stmt = x.getStatements().get(i);
            stmt.setParent(x);
            stmt.accept(this);
        }
        decrementIndent();
        return false;
    }

    public boolean visit(OracleExceptionStatement x) {
        print("EXCEPTION");
        incrementIndent();
        for (OracleExceptionStatement.Item item : x.getItems()) {
            println();
            item.accept(this);
        }
        decrementIndent();
        return false;
    }

    public void endVisit(OracleExceptionStatement x) {

    }

    public boolean visit(OracleArgumentExpr x) {
        print(x.getArgumentName());
        print(" => ");
        x.getValue().accept(this);
        return false;
    }

    public void endVisit(OracleArgumentExpr x) {

    }

    public boolean visit(OracleSetTransactionStatement x) {
        if (x.isReadOnly()) {
            print("SET TRANSACTION READ ONLY NAME ");
        } else {
            print("SET TRANSACTION NAME ");
        }
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleSetTransactionStatement x) {

    }

    public boolean visit(OracleGrantStatement x) {
        print("GRANT ");
        for (int i = 0, size = x.getPrivileges().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
            }
            print(x.getPrivileges().get(i));
        }

        if (x.getOn() != null) {
            print(" ON ");
            x.getOn().accept(this);
        }
        return false;
    }

    public void endVisit(OracleGrantStatement x) {

    }

    public boolean visit(OracleExplainStatement x) {
        print("EXPLAIN PLAN");
        incrementIndent();
        println();
        if (x.getStatementId() != null) {
            print("SET STATEMENT_ID = ");
            x.getStatementId().accept(this);
            println();
        }

        if (x.getInto() != null) {
            print("INTO ");
            x.getInto().accept(this);
            println();
        }

        print("FRO");
        println();
        x.getForStatement().accept(this);

        decrementIndent();
        return false;
    }

    public void endVisit(OracleExplainStatement x) {

    }

    public boolean visit(OracleAlterProcedureStatement x) {
        print("ALTER PROCEDURE ");
        x.getName().accept(this);
        if (x.isCompile()) {
            print(" COMPILE");
        }
        if (x.isReuseSettings()) {
            print(" REUSE SETTINGS");
        }
        return false;
    }

    public void endVisit(OracleAlterProcedureStatement x) {

    }

    public boolean visit(OracleAlterTableDropPartition x) {
        print("DROP PARTITION ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableDropPartition x) {

    }

    public boolean visit(OracleAlterTableStatement x) {
        print("ALTER TABLE ");
        x.getName().accept(this);
        incrementIndent();
        for (SQLAlterTableItem item : x.getItems()) {
            println();
            item.accept(this);
        }
        if (x.isUpdateGlobalIndexes()) {
            println();
            print("UPDATE GLOABL INDEXES");
        }
        decrementIndent();
        return false;
    }

    public void endVisit(OracleAlterTableStatement x) {

    }

    public boolean visit(OracleAlterTableTruncatePartition x) {
        print("TRUNCATE PARTITION ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableTruncatePartition x) {

    }

    public boolean visit(TableSpaceItem x) {
        print("TABLESPACE ");
        x.getTablespace().accept(this);
        return false;
    }

    public void endVisit(TableSpaceItem x) {

    }

    public boolean visit(UpdateIndexesClause x) {
        print("UPDATE INDEXES");
        if (x.getItems().size() > 0) {
            print("(");
            printAndAccept(x.getItems(), ", ");
            print(")");
        }
        return false;
    }

    public void endVisit(UpdateIndexesClause x) {

    }

    public boolean visit(OracleAlterTableSplitPartition x) {
        print("SPLIT PARTITION ");
        x.getName().accept(this);

        if (x.getAt().size() > 0) {
            incrementIndent();
            println();
            print("AT (");
            printAndAccept(x.getAt(), ", ");
            print(")");
            decrementIndent();
        }

        if (x.getInto().size() > 0) {
            println();
            incrementIndent();
            print("INTO (");
            printAndAccept(x.getInto(), ", ");
            print(")");
            decrementIndent();
        }

        if (x.getUpdateIndexes() != null) {
            println();
            incrementIndent();
            x.getUpdateIndexes().accept(this);
            decrementIndent();
        }
        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition x) {

    }

    public boolean visit(NestedTablePartitionSpec x) {
        print("PARTITION ");
        x.getPartition().accept(this);
        for (SQLObject item : x.getSegmentAttributeItems()) {
            print(" ");
            item.accept(this);
        }
        return false;
    }

    public void endVisit(NestedTablePartitionSpec x) {

    }

    public boolean visit(OracleAlterTableModify x) {
        print("MODIFY (");
        incrementIndent();
        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            println();
            SQLColumnDefinition column = x.getColumns().get(i);
            column.accept(this);
            if (i != size - 1) {
                print(", ");
            }
        }
        decrementIndent();
        println();
        print(")");

        return false;
    }

    public void endVisit(OracleAlterTableModify x) {

    }

    public boolean visit(OracleCreateIndexStatement x) {
        print("CREATE ");
        if (x.getType() != null) {
            print(x.getType());
            print(" ");
        }

        print("INDEX ");

        x.getName().accept(this);
        print(" ON ");
        x.getTable().accept(this);
        print("(");
        printAndAccept(x.getItems(), ", ");
        print(")");

        if (x.isIndexOnlyTopLevel()) {
            print(" INDEX ONLY TOPLEVEL");
        }

        if (x.getTablespace() != null) {
            print(" TABLESPACE ");
            x.getTablespace().accept(this);
        }

        if (x.isOnline()) {
            print(" ONLINE");
        }

        if (x.isNoParallel()) {
            print(" NOPARALLEL");
        } else if (x.getParallel() != null) {
            print(" PARALLEL ");
            x.getParallel().accept(this);
        }
        return false;
    }

    public void endVisit(OracleCreateIndexStatement x) {

    }

    public boolean visit(OracleAlterIndexStatement x) {
        print("ALTER INDEX ");
        x.getName().accept(this);

        if (x.getRenameTo() != null) {
            print(" RENAME TO ");
            x.getRenameTo().accept(this);
        }

        if (x.getMonitoringUsage() != null) {
            print(" MONITORING USAGE");
        }

        if (x.getRebuild() != null) {
            print(" ");
            x.getRebuild().accept(this);
        }

        if (x.getParallel() != null) {
            print(" PARALLEL");
            x.getParallel().accept(this);
        }

        return false;
    }

    public void endVisit(OracleAlterIndexStatement x) {

    }

    public boolean visit(Rebuild x) {
        print("REBUILD");

        if (x.getOption() != null) {
            print(" ");
            x.getOption().accept(this);
        }
        return false;
    }

    public void endVisit(Rebuild x) {

    }

    public boolean visit(OracleForStatement x) {
        print("FOR ");
        x.getIndex().accept(this);
        print(" IN ");
        x.getRange().accept(this);
        println();
        print("LOOP");
        incrementIndent();
        println();

        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                println();
            }
        }

        decrementIndent();
        println();
        print("END LOOP");
        return false;
    }

    public void endVisit(OracleForStatement x) {

    }

    public boolean visit(Else x) {
        print("ELSE");
        incrementIndent();
        println();

        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                println();
            }
            SQLStatement item = x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        decrementIndent();
        return false;
    }

    public boolean visit(ElseIf x) {
        print("ELSE IF ");
        x.getCondition().accept(this);
        print(" THEN");
        incrementIndent();
        println();

        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                println();
            }
            SQLStatement item = x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        decrementIndent();
        return false;
    }

    public void endVisit(ElseIf x) {

    }

    public void endVisit(Else x) {

    }

    public boolean visit(OracleIfStatement x) {
        print("IF ");
        x.getCondition().accept(this);
        print(" THEN");
        incrementIndent();
        println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                println();
            }
        }
        decrementIndent();

        for (ElseIf elseIf : x.getElseIfList()) {
            println();
            elseIf.accept(this);
        }

        if (x.getElseItem() != null) {
            println();
            x.getElseItem().accept(this);
        }
        println();
        print("END IF");
        return false;
    }

    public void endVisit(OracleIfStatement x) {

    }

    public boolean visit(OracleRangeExpr x) {
        x.getLowBound().accept(this);
        print("..");
        x.getUpBound().accept(this);
        return false;
    }

    public void endVisit(OracleRangeExpr x) {

    }

    protected void visitColumnDefault(SQLColumnDefinition x) {
        if (x.getParent() instanceof OracleBlockStatement) {
            print(" := ");
        } else {
            print(" DEFAULT ");
        }
        x.getDefaultExpr().accept(this);
    }

    public boolean visit(OracleAlterTableAddConstaint x) {
        print("ADD ");
        x.getConstraint().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableAddConstaint x) {

    }

    public boolean visit(OraclePrimaryKey x) {
        if (x.getName() != null) {
            print("CONSTRAINT ");
            x.getName().accept(this);
            print(" ");
        }
        print("PRIMARY KEY (");
        printAndAccept(x.getColumns(), ", ");
        print(")");
        if (x.getUsingIndex() != null) {
            print(" USING INDEX ");
            x.getUsingIndex().accept(this);
        }
        return false;
    }

    public void endVisit(OraclePrimaryKey x) {

    }

    public boolean visit(OracleCreateTableStatement x) {
        this.visit((SQLCreateTableStatement) x);

        incrementIndent();

        if (x.isOrganizationIndex()) {
            print(" ORGANIZATION INDEX");
        }

        if (x.getTablespace() != null) {
            print(" TABLESPACE ");
            x.getTablespace().accept(this);
        }

        if (x.isInMemoryMetadata()) {
            print(" IN_MEMORY_METADATA");
        }

        if (x.isCursorSpecificSegment()) {
            print(" CURSOR_SPECIFIC_SEGMENT");
        }

        if (x.getParallel() == Boolean.TRUE) {
            print(" PARALLEL");
        } else if (x.getParallel() == Boolean.FALSE) {
            print(" NOPARALLEL");
        }

        if (x.getCache() == Boolean.TRUE) {
            print(" CACHE");
        } else if (x.getCache() == Boolean.FALSE) {
            print(" NOCACHE");
        }

        if (x.getCompress() == Boolean.TRUE) {
            print(" COMPRESS");
        } else if (x.getCompress() == Boolean.FALSE) {
            print(" NOCOMPRESS");
        }

        if (x.getLogging() == Boolean.TRUE) {
            print(" LOGGING");
        } else if (x.getLogging() == Boolean.FALSE) {
            print(" NOLOGGING");
        }

        if (x.getStorage() != null) {
            print(" ");
            x.getStorage().accept(this);
        }

        if (x.isOnCommit()) {
            print(" ON COMMIT");
        }

        if (x.isPreserveRows()) {
            print(" PRESERVE ROWS");
        }

        if (x.getPartitioning() != null) {
            println();
            x.getPartitioning().accept(this);
        }

        if (x.getSelect() != null) {
            println();
            print("AS");
            println();
            x.getSelect().accept(this);
        }
        decrementIndent();
        return false;
    }

    public void endVisit(OracleCreateTableStatement x) {

    }

    public boolean visit(OracleAlterTableRenameTo x) {
        print("RENAME TO ");
        x.getTo().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableRenameTo x) {

    }

    public boolean visit(OracleStorageClause x) {
        print("STORAGE (");

        if (x.getInitial() != null) {
            print(" INITIAL ");
            x.getInitial().accept(this);
        }

        if (x.getFreeLists() != null) {
            print(" FREELISTS ");
            x.getFreeLists().accept(this);
        }

        if (x.getFreeListGroups() != null) {
            print(" FREELIST GROUPS ");
            x.getFreeListGroups().accept(this);
        }

        if (x.getBufferPool() != null) {
            print(" BUFFER_POOL ");
            x.getBufferPool().accept(this);
        }

        if (x.getObjno() != null) {
            print(" OBJNO ");
            x.getObjno().accept(this);
        }

        print(")");
        return false;
    }

    public void endVisit(OracleStorageClause x) {

    }

    public boolean visit(OracleGotoStatement x) {
        print("GOTO ");
        x.getLabel().accept(this);
        return false;
    }

    public void endVisit(OracleGotoStatement x) {

    }

    public boolean visit(OracleLabelStatement x) {
        print("<<");
        x.getLabel().accept(this);
        print(">>");
        return false;
    }

    public void endVisit(OracleLabelStatement x) {

    }

    public boolean visit(OracleParameter x) {
        if (x.getDataType().getName().equalsIgnoreCase("CURSOR")) {
            print("CURSOR ");
            x.getName().accept(this);
            print(" IS");
            incrementIndent();
            println();
            SQLSelect select = ((SQLQueryExpr) x.getDefaultValue()).getSubQuery();
            select.accept(this);
            decrementIndent();

        } else {
            x.getName().accept(this);
            print(" ");

            x.getDataType().accept(this);

            if (x.getDefaultValue() != null) {
                print(" := ");
                x.getDefaultValue().accept(this);
            }
        }

        return false;
    }

    public void endVisit(OracleParameter x) {

    }

    public boolean visit(OracleCommitStatement x) {
        print("COMMIT");

        if (x.isWrite()) {
            print(" WRITE");
            if (x.getWait() != null) {
                if (x.getWait().booleanValue()) {
                    print(" WAIT");
                } else {
                    print(" NOWAIT");
                }
            }

            if (x.getImmediate() != null) {
                if (x.getImmediate().booleanValue()) {
                    print(" IMMEDIATE");
                } else {
                    print(" BATCH");
                }
            }
        }

        return false;
    }

    public void endVisit(OracleCommitStatement x) {

    }

    public boolean visit(OracleAlterTriggerStatement x) {
        print("ALTER TRIGGER ");
        x.getName().accept(this);

        if (x.isCompile()) {
            print(" COMPILE");
        }

        if (x.getEnable() != null) {
            if (x.getEnable().booleanValue()) {
                print("ENABLE");
            } else {
                print("DISABLE");
            }
        }
        return false;
    }

    public void endVisit(OracleAlterTriggerStatement x) {

    }

    public boolean visit(OracleAlterSynonymStatement x) {
        print("ALTER SYNONYM ");
        x.getName().accept(this);

        if (x.isCompile()) {
            print(" COMPILE");
        }

        if (x.getEnable() != null) {
            if (x.getEnable().booleanValue()) {
                print("ENABLE");
            } else {
                print("DISABLE");
            }
        }
        return false;
    }

    public void endVisit(OracleAlterSynonymStatement x) {

    }

    public boolean visit(AsOfSnapshotClause x) {
        print("AS OF SNAPSHOT(");
        x.getExpr().accept(this);
        print(")");
        return false;
    }

    public void endVisit(AsOfSnapshotClause x) {

    }

    public boolean visit(OracleAlterViewStatement x) {
        print("ALTER VIEW ");
        x.getName().accept(this);

        if (x.isCompile()) {
            print(" COMPILE");
        }

        if (x.getEnable() != null) {
            if (x.getEnable().booleanValue()) {
                print("ENABLE");
            } else {
                print("DISABLE");
            }
        }
        return false;
    }

    public void endVisit(OracleAlterViewStatement x) {

    }

    public boolean visit(OracleAlterTableMoveTablespace x) {
        print(" MOVE TABLESPACE ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableMoveTablespace x) {

    }

    public boolean visit(OracleSizeExpr x) {
        x.getValue().accept(this);
        print(x.getUnit().name());
        return false;
    }

    public void endVisit(OracleSizeExpr x) {

    }

    public boolean visit(OracleFileSpecification x) {
        printAndAccept(x.getFileNames(), ", ");

        if (x.getSize() != null) {
            print(" SIZE ");
            x.getSize().accept(this);
        }

        if (x.isAutoExtendOff()) {
            print(" AUTOEXTEND OFF");
        } else if (x.getAutoExtendOn() != null) {
            print(" AUTOEXTEND ON ");
            x.getAutoExtendOn().accept(this);
        }
        return false;
    }

    public void endVisit(OracleFileSpecification x) {

    }

    public boolean visit(OracleAlterTablespaceAddDataFile x) {
        print("ADD DATAFILE");
        incrementIndent();
        for (OracleFileSpecification file : x.getFiles()) {
            println();
            file.accept(this);
        }
        decrementIndent();
        return false;
    }

    public void endVisit(OracleAlterTablespaceAddDataFile x) {

    }

    public boolean visit(OracleAlterTablespaceStatement x) {
        print("ALTER TABLESPACE ");
        x.getName().accept(this);
        println();
        x.getItem().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTablespaceStatement x) {

    }

    public boolean visit(OracleTruncateStatement x) {
        print("TRUNCATE TABLE ");
        printAndAccept(x.getTableSources(), ", ");

        if (x.isPurgeSnapshotLog()) {
            print(" PURGE SNAPSHOT LOG");
        }
        return false;
    }

    public void endVisit(OracleTruncateStatement x) {

    }

    public boolean visit(OracleCreateSequenceStatement x) {
        print("CREATE SEQUENCE ");
        x.getName().accept(this);

        if (x.getStartWith() != null) {
            print(" START WITH ");
            x.getStartWith().accept(this);
        }

        if (x.getIncrementBy() != null) {
            print(" INCREMENT BY ");
            x.getIncrementBy().accept(this);
        }

        if (x.getMaxValue() != null) {
            print(" MAXVALUE ");
            x.getMaxValue().accept(this);
        }

        if (x.getCycle() != null) {
            if (x.getCycle().booleanValue()) {
                print(" CYCLE");
            } else {
                print(" NOCYCLE");
            }
        }

        if (x.getCache() != null) {
            if (x.getCache().booleanValue()) {
                print(" CACHE");
            } else {
                print(" NOCACHE");
            }
        }

        return false;
    }

    public void endVisit(OracleCreateSequenceStatement x) {

    }

    public boolean visit(OracleRangeValuesClause x) {
        print("PARTITION ");
        x.getName().accept(this);
        print(" VALUES LESS THAN (");
        printAndAccept(x.getValues(), ", ");
        print(")");
        return false;
    }

    public void endVisit(OracleRangeValuesClause x) {

    }

    public boolean visit(OraclePartitionByRangeClause x) {
        print("PARTITION BY RANGE (");
        printAndAccept(x.getColumns(), ", ");
        print(")");

        if (x.getInterval() != null) {
            print(" INTERVAL ");
            x.getInterval().accept(this);
        }

        if (x.getStoreIn().size() > 0) {
            print(" STORE IN (");
            printAndAccept(x.getStoreIn(), ", ");
            print(")");
        }

        println();
        print("(");
        incrementIndent();
        for (int i = 0, size = x.getRanges().size(); i < size; ++i) {
            if (i != 0) {
                print(",");
            }
            println();
            x.getRanges().get(i).accept(this);
        }
        decrementIndent();
        println();
        print(")");
        return false;
    }

    public void endVisit(OraclePartitionByRangeClause x) {

    }

    public boolean visit(OracleLoopStatement x) {
        print("LOOP");
        incrementIndent();
        println();

        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                println();
            }
        }

        decrementIndent();
        println();
        print("END LOOP");
        return false;
    }

    public void endVisit(OracleLoopStatement x) {

    }

    public boolean visit(OracleExitStatement x) {
        print("EXIT");
        if (x.getWhen() != null) {
            print(" WHEN ");
            x.getWhen().accept(this);
        }
        return false;
    }

    public void endVisit(OracleExitStatement x) {

    }

    public boolean visit(OracleFetchStatement x) {
        print("FETCH ");
        x.getCursorName().accept(this);
        print(" INTO ");
        printAndAccept(x.getInto(), ", ");
        return false;
    }

    public void endVisit(OracleFetchStatement x) {

    }

    public void endVisit(SQLRollbackStatement x) {

    }

    public boolean visit(OracleSavePointStatement x) {
        print("ROLLBACK");
        if (x.getTo() != null) {
            print(" TO ");
            x.getTo().accept(this);
        }
        return false;
    }

    public void endVisit(OracleSavePointStatement x) {

    }

    public boolean visit(OracleCreateProcedureStatement x) {
        if (x.isOrReplace()) {
            print("CREATE OR REPLACE PROCEDURE ");
        } else {
            print("CREATE PROCEDURE ");
        }
        x.getName().accept(this);

        int paramSize = x.getParameters().size();

        if (paramSize > 0) {
            print(" (");
            incrementIndent();
            println();

            for (int i = 0; i < paramSize; ++i) {
                if (i != 0) {
                    print(", ");
                    println();
                }
                OracleParameter param = x.getParameters().get(i);
                param.accept(this);
            }

            decrementIndent();
            println();
            print(")");
        }

        println();
        x.getBlock().setParent(x);
        x.getBlock().accept(this);
        return false;
    }

    public void endVisit(OracleCreateProcedureStatement x) {

    }
}
