/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.visitor;

import java.util.Map;

import com.alipay.zdal.parser.sql.ast.SQLCommentHint;
import com.alipay.zdal.parser.sql.ast.SQLDataType;
import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLSetQuantifier;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLMethodInvokeExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNullExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.ast.statement.SQLCharactorDataType;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnConstraint;
import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlBinaryExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlBooleanExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlExtractExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlIntervalExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlMatchAgainstExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddColumn;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddIndex;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddUnique;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableCharacter;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateIndexStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropUser;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropViewStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlReplicateStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlRollbackStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSQLColumnDefinition;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectGroupBy;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetTransactionIsolationLevelStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlStartTransactionStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alipay.zdal.parser.sql.visitor.SQLASTOutputVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlOutputVisitor.java, v 0.1 2012-11-17 ÏÂÎç3:40:59 Exp $
 */
public class MySqlOutputVisitor extends SQLASTOutputVisitor implements MySqlASTVisitor {

    public MySqlOutputVisitor(Appendable appender) {
        super(appender);
    }

    public boolean visit(MySqlBooleanExpr x) {
        print(x.getValue() ? "true" : "false");

        return false;
    }

    public void endVisit(MySqlBooleanExpr x) {
    }

    public boolean visit(SQLSelectQueryBlock select) {
        if (select instanceof MySqlSelectQueryBlock) {
            return visit((MySqlSelectQueryBlock) select);
        }

        return false;
    }

    public boolean visit(MySqlSelectQueryBlock x) {
        if (x.getOrderBy() != null) {
            x.getOrderBy().setParent(x);
        }

        print("SELECT ");

        for (SQLCommentHint hint : x.getHints()) {
            hint.accept(this);
            print(' ');
        }

        if (SQLSetQuantifier.ALL == x.getDistionOption())
            print("ALL ");
        else if (SQLSetQuantifier.DISTINCT == x.getDistionOption())
            print("DISTINCT ");
        else if (SQLSetQuantifier.DISTINCTROW == x.getDistionOption()) {
            print("DISTINCTROW ");
        }

        if (x.isHignPriority()) {
            print("HIGH_PRIORITY ");
        }

        if (x.isStraightJoin()) {
            print("STRAIGHT_JOIN ");
        }

        if (x.isSmallResult()) {
            print("SQL_SMALL_RESULT ");
        }

        if (x.isBigResult()) {
            print("SQL_BIG_RESULT ");
        }

        if (x.isBufferResult()) {
            print("SQL_BUFFER_RESULT ");
        }

        if (x.getCache() != null) {
            if (x.getCache().booleanValue()) {
                print("SQL_CACHE ");
            } else {
                print("SQL_NO_CACHE ");
            }
        }

        if (x.isCalcFoundRows()) {
            print("SQL_CALC_FOUND_ROWS ");
        }

        printSelectList(x.getSelectList());

        if (x.getInto() != null) {
            println();
            print("INTO ");
            x.getInto().accept(this);
        }

        if (x.getFrom() != null) {
            println();
            print("FROM ");
            x.getFrom().accept(this);
        }

        if (x.getWhere() != null) {
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getGroupBy() != null) {
            println();
            x.getGroupBy().accept(this);
        }

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        if (x.getLimit() != null) {
            println();
            x.getLimit().accept(this);
        }

        if (x.getProcedureName() != null) {
            print(" PROCEDURE ");
            x.getProcedureName().accept(this);
            if (x.getProcedureArgumentList().size() > 0) {
                print("(");
                printAndAccept(x.getProcedureArgumentList(), ", ");
                print(")");
            }
        }

        if (x.isForUpdate()) {
            println();
            print("FOR UPDATE");
        }

        if (x.isLockInShareMode()) {
            println();
            print("LOCK IN SHARE MODE");
        }

        return false;
    }

    public boolean visit(SQLColumnDefinition x) {
        MySqlSQLColumnDefinition mysqlColumn = null;

        if (x instanceof MySqlSQLColumnDefinition) {
            mysqlColumn = (MySqlSQLColumnDefinition) x;
        }

        x.getName().accept(this);
        print(' ');
        x.getDataType().accept(this);

        if (x.getDefaultExpr() != null) {
            if (x.getDefaultExpr() instanceof SQLNullExpr) {
                print(" NULL");
            } else {
                print(" DEFAULT");
                x.getDefaultExpr().accept(this);
            }
        }

        if (mysqlColumn != null && mysqlColumn.isAutoIncrement()) {
            print(" AUTO_INCREMENT");
        }

        for (SQLColumnConstraint item : x.getConstaints()) {
            print(' ');
            item.accept(this);
        }

        return false;
    }

    public boolean visit(MySqlSelectQueryBlock.Limit x) {
        print("LIMIT ");
        if (x.getOffset() != null) {
            x.getOffset().accept(this);
            print(", ");
        }
        x.getRowCount().accept(this);

        return false;
    }

    public boolean visit(SQLDataType x) {
        print(x.getName());
        if (x.getArguments().size() > 0) {
            print("(");
            printAndAccept(x.getArguments(), ", ");
            print(")");
        }

        if (x instanceof SQLCharactorDataType) {
            SQLCharactorDataType charType = (SQLCharactorDataType) x;
            if (charType.getCharSetName() != null) {
                print(" CHARACTER SET ");
                print(charType.getCharSetName());

                if (charType.getCollate() != null) {
                    print(" COLLATE ");
                    print(charType.getCollate());
                }
            }
        }
        return false;
    }

    public void endVisit(Limit x) {

    }

    public void endVisit(MySqlTableIndex x) {

    }

    public boolean visit(MySqlTableIndex x) {
        print("INDEX");
        if (x.getName() != null) {
            print(" ");
            x.getName().accept(this);
        }

        if (x.getIndexType() != null) {
            print(" USING ");
            print(x.getIndexType());
        }

        print("(");
        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        print(")");
        return false;
    }

    public boolean visit(MySqlCreateTableStatement x) {

        print("CREATE ");

        for (SQLCommentHint hint : x.getHints()) {
            hint.accept(this);
            print(' ');
        }

        if (SQLCreateTableStatement.Type.GLOBAL_TEMPORARY.equals(x.getType())) {
            print("TEMPORARY TABLE ");
        } else {
            print("TABLE ");
        }

        if (x.isIfNotExiists()) {
            print("IF NOT EXISTS ");
        }

        x.getName().accept(this);
        print(" (");
        incrementIndent();
        println();
        for (int i = 0, size = x.getTableElementList().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
                println();
            }
            x.getTableElementList().get(i).accept(this);
        }
        decrementIndent();
        println();
        print(")");

        for (Map.Entry<String, String> option : x.getTableOptions().entrySet()) {
            print(" ");
            print(option.getKey());
            print(" = ");
            print(option.getValue());
        }

        if (x.getQuery() != null) {
            print(" ");
            incrementIndent();
            println();
            x.getQuery().accept(this);
            decrementIndent();
        }

        return false;
    }

    public void endVisit(MySqlKey x) {

    }

    public void endVisit(MySqlPrimaryKey x) {

    }

    public boolean visit(MySqlKey x) {
        if (x.getName() != null) {
            print("CONSTRAINT ");
            x.accept(this);
            print(' ');
        }

        print("KEY");

        if (x.getIndexType() != null) {
            print(" USING ");
            print(x.getIndexType());
        }

        print(" (");

        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        print(")");

        return false;
    }

    public boolean visit(MySqlPrimaryKey x) {
        if (x.getName() != null) {
            print("CONSTRAINT ");
            x.accept(this);
            print(' ');
        }

        print("PRIAMRY KEY");

        if (x.getIndexType() != null) {
            print(" USING ");
            print(x.getIndexType());
        }

        print(" (");

        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                print(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        print(")");

        return false;
    }

    public boolean visit(SQLCharExpr x) {
        print(x.toString());
        return false;
    }

    public boolean visit(SQLVariantRefExpr x) {
        if (x.isGlobal()) {
            print("@@global.");
        } else {
            String varName = x.getName();
            if ((!varName.startsWith("@")) && (!varName.equals("?")) && (!varName.startsWith("#"))
                && (!varName.startsWith("$"))) {
                print("@@");
            }
        }

        for (int i = 0; i < x.getName().length(); ++i) {
            char ch = x.getName().charAt(i);
            if (ch == '\'') {
                if (x.getName().startsWith("@@") && i == 2) {
                    print(ch);
                } else if (x.getName().startsWith("@") && i == 1) {
                    print(ch);
                } else if (i != 0 && i != x.getName().length() - 1) {
                    print("\\'");
                } else {
                    print(ch);
                }
            } else {
                print(ch);
            }
        }

        String collate = (String) x.getAttribute("COLLATE");
        if (collate != null) {
            print(" COLLATE ");
            print(collate);
        }

        return false;
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        if ("SUBSTRING".equalsIgnoreCase(x.getMethodName())) {
            if (x.getOwner() != null) {
                x.getOwner().accept(this);
                print(".");
            }
            print(x.getMethodName());
            print("(");
            printAndAccept(x.getParameters(), ", ");
            SQLExpr from = (SQLExpr) x.getAttribute("FROM");
            if (from != null) {
                print(" FROM ");
                from.accept(this);
            }

            SQLExpr _for = (SQLExpr) x.getAttribute("FOR");
            if (_for != null) {
                print(" FOR ");
                _for.accept(this);
            }
            print(")");

            return false;
        }

        if ("TRIM".equalsIgnoreCase(x.getMethodName())) {
            if (x.getOwner() != null) {
                x.getOwner().accept(this);
                print(".");
            }
            print(x.getMethodName());
            print("(");

            String trimType = (String) x.getAttribute("TRIM_TYPE");
            if (trimType != null) {
                print(trimType);
                print(' ');
            }

            printAndAccept(x.getParameters(), ", ");

            SQLExpr from = (SQLExpr) x.getAttribute("FROM");
            if (from != null) {
                print(" FROM ");
                from.accept(this);
            }

            print(")");

            return false;
        }

        if ("CONVERT".equalsIgnoreCase(x.getMethodName())) {
            if (x.getOwner() != null) {
                x.getOwner().accept(this);
                print(".");
            }
            print(x.getMethodName());
            print("(");
            printAndAccept(x.getParameters(), ", ");

            String charset = (String) x.getAttribute("USING");
            if (charset != null) {
                print(" USING ");
                print(charset);
            }
            print(")");
            return false;
        }

        return super.visit(x);
    }

    public void endVisit(MySqlIntervalExpr x) {

    }

    public boolean visit(MySqlIntervalExpr x) {
        print("INTERVAL ");
        x.getValue().accept(this);
        print(' ');
        print(x.getUnit().name());
        return false;
    }

    public boolean visit(MySqlExtractExpr x) {
        print("EXTRACT(");
        print(x.getUnit().name());
        print(" FROM ");
        x.getValue().accept(this);
        print(')');
        return false;
    }

    public void endVisit(MySqlExtractExpr x) {

    }

    public void endVisit(MySqlMatchAgainstExpr x) {

    }

    public boolean visit(MySqlMatchAgainstExpr x) {
        print("MATCH (");
        printAndAccept(x.getColumns(), ", ");
        print(")");

        print(" AGAINST (");
        x.getAgainst().accept(this);
        if (x.getSearchModifier() != null) {
            print(' ');
            print(x.getSearchModifier().name);
        }
        print(')');

        return false;
    }

    public void endVisit(MySqlBinaryExpr x) {

    }

    public boolean visit(MySqlBinaryExpr x) {
        print("b'");
        print(x.getValue());
        print('\'');

        return false;
    }

    public void endVisit(MySqlPrepareStatement x) {
    }

    public boolean visit(MySqlPrepareStatement x) {
        print("PREPARE ");
        x.getName().accept(this);
        print(" FROM ");
        x.getFrom().accept(this);
        return false;
    }

    public void endVisit(MySqlExecuteStatement x) {

    }

    public boolean visit(MySqlExecuteStatement x) {
        print("EXECUTE ");
        x.getStatementName().accept(this);
        if (x.getParameters().size() > 0) {
            print(" USING ");
            printAndAccept(x.getParameters(), ", ");
        }
        return false;
    }

    public void endVisit(MySqlDeleteStatement x) {

    }

    public boolean visit(MySqlDeleteStatement x) {
        print("DELETE ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isQuick()) {
            print("QUICK ");
        }

        if (x.isIgnore()) {
            print("IGNORE ");
        }

        if (x.getFrom() == null) {
            print("FROM ");
            x.getTableSource().accept(this);
        } else {
            x.getTableSource().accept(this);
            println();
            print("FROM ");
            x.getFrom().accept(this);
        }

        if (x.getUsing() != null) {
            println();
            print("USING ");
            x.getUsing().accept(this);
        }

        if (x.getWhere() != null) {
            println();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        if (x.getLimit() != null) {
            println();
            x.getLimit().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlInsertStatement x) {

    }

    public boolean visit(MySqlInsertStatement x) {
        print("INSERT ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isDelayed()) {
            print("DELAYED ");
        }

        if (x.isHighPriority()) {
            print("HIGH_PRIORITY ");
        }

        if (x.isIgnore()) {
            print("IGNORE ");
        }

        print("INTO ");

        x.getTableName().accept(this);

        if (x.getColumns().size() > 0) {
            incrementIndent();
            print(" (");
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

        if (x.getValuesList().size() != 0) {
            println();
            print("VALUES ");
            for (int i = 0, size = x.getValuesList().size(); i < size; ++i) {
                if (i != 0) {
                    print(", ");
                }
                x.getValuesList().get(i).accept(this);
            }

        }

        if (x.getQuery() != null) {
            println();
            x.getQuery().accept(this);
        }

        if (x.getDuplicateKeyUpdate().size() != 0) {
            print(" ON DUPLICATE KEY UPDATE ");
            printAndAccept(x.getDuplicateKeyUpdate(), ", ");
        }

        return false;
    }

    public void endVisit(MySqlLoadDataInFileStatement x) {

    }

    public boolean visit(MySqlLoadDataInFileStatement x) {
        print("LOAD DATA ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isConcurrent()) {
            print("CONCURRENT ");
        }

        if (x.isLocal()) {
            print("LOCAL ");
        }

        print("INFILE ");

        x.getFileName().accept(this);

        if (x.isReplicate()) {
            print(" REPLACE ");
        }

        if (x.isIgnore()) {
            print(" IGNORE ");
        }

        print(" INTO TABLE ");
        x.getTableName().accept(this);

        if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null
            || x.getColumnsEscaped() != null) {
            print(" COLUMNS");
            if (x.getColumnsTerminatedBy() != null) {
                print(" TERMINATED BY ");
                x.getColumnsTerminatedBy().accept(this);
            }

            if (x.getColumnsEnclosedBy() != null) {
                if (x.isColumnsEnclosedOptionally()) {
                    print(" OPTIONALLY");
                }
                print(" ENCLOSED BY ");
                x.getColumnsEnclosedBy().accept(this);
            }

            if (x.getColumnsEscaped() != null) {
                print(" ESCAPED BY ");
                x.getColumnsEscaped().accept(this);
            }
        }

        if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
            print(" LINES");
            if (x.getLinesStartingBy() != null) {
                print(" STARTING BY ");
                x.getLinesStartingBy().accept(this);
            }

            if (x.getLinesTerminatedBy() != null) {
                print(" TERMINATED BY ");
                x.getLinesTerminatedBy().accept(this);
            }
        }

        if (x.getSetList().size() != 0) {
            print(" SET ");
            printAndAccept(x.getSetList(), ", ");
        }

        return false;
    }

    public void endVisit(MySqlReplicateStatement x) {

    }

    public boolean visit(MySqlReplicateStatement x) {
        print("REPLACE ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isDelayed()) {
            print("DELAYED ");
        }

        print("INTO ");

        x.getTableName().accept(this);

        if (x.getColumns().size() > 0) {
            print(" (");
            for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
                if (i != 0) {
                    print(", ");
                }
                x.getColumns().get(i).accept(this);
            }
            print(")");
        }

        if (x.getValuesList().size() != 0) {
            println();
            print("VALUES ");
            int size = x.getValuesList().size();
            if (size == 0) {
                print("()");
            } else {
                for (int i = 0; i < size; ++i) {
                    if (i != 0) {
                        print(", ");
                    }
                    x.getValuesList().get(i).accept(this);
                }
            }
        }

        if (x.getQuery() != null) {
            x.getQuery().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlSelectGroupBy x) {

    }

    public boolean visit(MySqlSelectGroupBy x) {
        super.visit(x);

        if (x.isRollUp()) {
            print(" WITH ROLLUP");
        }

        return false;
    }

    public void endVisit(MySqlStartTransactionStatement x) {

    }

    public boolean visit(MySqlStartTransactionStatement x) {
        print("START TRANSACTION");
        if (x.isConsistentSnapshot()) {
            print(" WITH CONSISTENT SNAPSHOT");
        }

        if (x.isBegin()) {
            print(" BEGIN");
        }

        if (x.isWork()) {
            print(" WORK");
        }

        return false;
    }

    public void endVisit(MySqlCommitStatement x) {

    }

    public boolean visit(MySqlCommitStatement x) {
        print("COMMIT");

        if (x.isWork()) {
            print(" WORK");
        }

        if (x.getChain() != null) {
            if (x.getChain().booleanValue()) {
                print(" AND CHAIN");
            } else {
                print(" AND NO CHAIN");
            }
        }

        if (x.getRelease() != null) {
            if (x.getRelease().booleanValue()) {
                print(" AND RELEASE");
            } else {
                print(" AND NO RELEASE");
            }
        }

        return false;
    }

    public void endVisit(MySqlRollbackStatement x) {

    }

    public boolean visit(MySqlRollbackStatement x) {
        print("ROLLBACK");

        if (x.getChain() != null) {
            if (x.getChain().booleanValue()) {
                print(" AND CHAIN");
            } else {
                print(" AND NO CHAIN");
            }
        }

        if (x.getRelease() != null) {
            if (x.getRelease().booleanValue()) {
                print(" AND RELEASE");
            } else {
                print(" AND NO RELEASE");
            }
        }

        if (x.getTo() != null) {
            print(" TO ");
            x.getTo().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowColumnsStatement x) {

    }

    public boolean visit(MySqlShowColumnsStatement x) {
        if (x.isFull()) {
            print("SHOW FULL COLUMNS");
        } else {
            print("SHOW COLUMNS");
        }

        if (x.getTable() != null) {
            print(" FROM ");
            if (x.getDatabase() != null) {
                x.getDatabase().accept(this);
                print('.');
            }
            x.getTable().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowTablesStatement x) {

    }

    public boolean visit(MySqlShowTablesStatement x) {
        if (x.isFull()) {
            print("SHOW FULL TABLES");
        } else {
            print("SHOW TABLES");
        }

        if (x.getDatabase() != null) {
            print(" FROM ");
            x.getDatabase().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowDatabasesStatement x) {

    }

    public boolean visit(MySqlShowDatabasesStatement x) {
        print("SHOW DATABASES");

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowWarningsStatement x) {

    }

    public boolean visit(MySqlShowWarningsStatement x) {
        if (x.isCount()) {
            print("SHOW COUNT(*) WARNINGS");
        } else {
            print("SHOW WARNINGS");
            if (x.getLimit() != null) {
                print(' ');
                x.getLimit().accept(this);
            }
        }

        return false;
    }

    public void endVisit(MySqlShowStatusStatement x) {

    }

    public boolean visit(MySqlShowStatusStatement x) {
        print("SHOW ");

        if (x.isGlobal()) {
            print("GLOBAL ");
        }

        if (x.isSession()) {
            print("SESSION ");
        }

        print("STATUS");

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlLoadXmlStatement x) {

    }

    public boolean visit(MySqlLoadXmlStatement x) {
        print("LOAD XML ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isConcurrent()) {
            print("CONCURRENT ");
        }

        if (x.isLocal()) {
            print("LOCAL ");
        }

        print("INFILE ");

        x.getFileName().accept(this);

        if (x.isReplicate()) {
            print(" REPLACE ");
        }

        if (x.isIgnore()) {
            print(" IGNORE ");
        }

        print(" INTO TABLE ");
        x.getTableName().accept(this);

        if (x.getCharset() != null) {
            print(" CHARSET ");
            print(x.getCharset());
        }

        if (x.getRowsIdentifiedBy() != null) {
            print(" ROWS IDENTIFIED BY ");
            x.getRowsIdentifiedBy().accept(this);
        }

        if (x.getSetList().size() != 0) {
            print(" SET ");
            printAndAccept(x.getSetList(), ", ");
        }

        return false;
    }

    public void endVisit(CobarShowStatus x) {

    }

    public boolean visit(CobarShowStatus x) {
        print("SHOW COBAR_STATUS");
        return false;
    }

    public void endVisit(MySqlKillStatement x) {

    }

    public boolean visit(MySqlKillStatement x) {
        if (MySqlKillStatement.Type.CONNECTION.equals(x.getType())) {
            print("KILL CONNECTION ");
        } else if (MySqlKillStatement.Type.QUERY.equals(x.getType())) {
            print("KILL QUERY ");
        }
        x.getThreadId().accept(this);
        return false;
    }

    public void endVisit(MySqlBinlogStatement x) {

    }

    public boolean visit(MySqlBinlogStatement x) {
        print("BINLOG ");
        x.getExpr().accept(this);
        return false;
    }

    public void endVisit(MySqlResetStatement x) {

    }

    public boolean visit(MySqlResetStatement x) {
        print("RESET ");
        for (int i = 0; i < x.getOptions().size(); ++i) {
            if (i != 0) {
                print(", ");
            }
            print(x.getOptions().get(i));
        }
        return false;
    }

    public void endVisit(MySqlCreateUserStatement x) {

    }

    public boolean visit(MySqlCreateUserStatement x) {
        print("CREATE USER ");
        printAndAccept(x.getUsers(), ", ");
        return false;
    }

    public void endVisit(UserSpecification x) {

    }

    public boolean visit(UserSpecification x) {
        x.getUser().accept(this);

        if (x.getPassword() != null) {
            print(" IDENTIFIED BY ");
            x.getPassword().accept(this);
        }

        if (x.getAuthPlugin() != null) {
            print(" IDENTIFIED WITH ");
            x.getAuthPlugin().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlDropUser x) {

    }

    public boolean visit(MySqlDropUser x) {
        print("DROP USER ");
        printAndAccept(x.getUsers(), ", ");
        return false;
    }

    public void endVisit(MySqlDropTableStatement x) {

    }

    public boolean visit(MySqlDropTableStatement x) {
        if (x.isTemporary()) {
            print("DROP TEMPORARY TABLE ");
        } else {
            print("DROP TABLE ");
        }
        if (x.isIfExists()) {
            print("IF EXISTS ");
        }

        printAndAccept(x.getTableSources(), ", ");

        if (x.getOption() != null) {
            print(' ');
            print(x.getOption());
        }
        return false;
    }

    public void endVisit(MySqlPartitionByKey x) {

    }

    public boolean visit(MySqlPartitionByKey x) {
        print("PARTITION BY KEY (");
        printAndAccept(x.getColumns(), ", ");
        print(")");

        if (x.getPartitionCount() != null) {
            print(" PARTITIONS ");
            x.getPartitionCount().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlSelectQueryBlock x) {

    }

    public boolean visit(MySqlOutFileExpr x) {
        print("OUTFILE ");
        x.getFile().accept(this);

        if (x.getCharset() != null) {
            print(" CHARACTER SET ");
            print(x.getCharset());
        }

        if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null
            || x.getColumnsEscaped() != null) {
            print(" COLUMNS");
            if (x.getColumnsTerminatedBy() != null) {
                print(" TERMINATED BY ");
                x.getColumnsTerminatedBy().accept(this);
            }

            if (x.getColumnsEnclosedBy() != null) {
                if (x.isColumnsEnclosedOptionally()) {
                    print(" OPTIONALLY");
                }
                print(" ENCLOSED BY ");
                x.getColumnsEnclosedBy().accept(this);
            }

            if (x.getColumnsEscaped() != null) {
                print(" ESCAPED BY ");
                x.getColumnsEscaped().accept(this);
            }
        }

        if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
            print(" LINES");
            if (x.getLinesStartingBy() != null) {
                print(" STARTING BY ");
                x.getLinesStartingBy().accept(this);
            }

            if (x.getLinesTerminatedBy() != null) {
                print(" TERMINATED BY ");
                x.getLinesTerminatedBy().accept(this);
            }
        }

        return false;
    }

    public void endVisit(MySqlOutFileExpr x) {

    }

    public boolean visit(MySqlDescribeStatement x) {
        print("DESC ");
        x.getObject().accept(this);
        return false;
    }

    public void endVisit(MySqlDescribeStatement x) {

    }

    public boolean visit(MySqlUpdateStatement x) {
        print("UPDATE ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isIgnore()) {
            print("IGNORE ");
        }

        x.getTableSource().accept(this);

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

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        if (x.getLimit() != null) {
            println();
            x.getLimit().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlUpdateStatement x) {

    }

    public boolean visit(MySqlSetTransactionIsolationLevelStatement x) {
        return false;
    }

    public void endVisit(MySqlSetTransactionIsolationLevelStatement x) {
        if (x.getGlobal() == null) {
            print("SET TRANSACTION ISOLATION LEVEL ");
        } else if (x.getGlobal().booleanValue()) {
            print("SET GLOBAL TRANSACTION ISOLATION LEVEL ");
        } else {
            print("SET SESSION TRANSACTION ISOLATION LEVEL ");
        }
        print(x.getLevel());
    }

    public boolean visit(MySqlSetNamesStatement x) {
        print("SET NAMES ");
        if (x.isDefault()) {
            print("DEFAULT");
        } else {
            print(x.getCharSet());
            if (x.getCollate() != null) {
                print(" COLLATE ");
                print(x.getCollate());
            }
        }
        return false;
    }

    public void endVisit(MySqlSetNamesStatement x) {

    }

    public boolean visit(MySqlSetCharSetStatement x) {
        print("SET CHARACTER SET ");
        if (x.isDefault()) {
            print("DEFAULT");
        } else {
            print(x.getCharSet());
            if (x.getCollate() != null) {
                print(" COLLATE ");
                print(x.getCollate());
            }
        }
        return false;
    }

    public void endVisit(MySqlSetCharSetStatement x) {

    }

    public void endVisit(MySqlShowAuthorsStatement x) {

    }

    public boolean visit(MySqlShowAuthorsStatement x) {
        print("SHOW AUTHORS");
        return false;
    }

    public void endVisit(MySqlShowBinaryLogsStatement x) {

    }

    public boolean visit(MySqlShowBinaryLogsStatement x) {
        print("SHOW BINARY LOGS");
        return false;
    }

    public boolean visit(MySqlShowMasterLogsStatement x) {
        print("SHOW MASTER LOGS");
        return false;
    }

    public void endVisit(MySqlShowMasterLogsStatement x) {

    }

    public boolean visit(MySqlShowCollationStatement x) {
        print("SHOW COLLATION");
        if (x.getPattern() != null) {
            print(" LIKE ");
            x.getPattern().accept(this);
        }
        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowCollationStatement x) {

    }

    public boolean visit(MySqlShowBinLogEventsStatement x) {
        print("SHOW BINLOG EVENTS");
        if (x.getIn() != null) {
            print(" IN ");
            x.getIn().accept(this);
        }
        if (x.getFrom() != null) {
            print(" FROM ");
            x.getFrom().accept(this);
        }
        if (x.getLimit() != null) {
            print(" ");
            x.getLimit().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowBinLogEventsStatement x) {

    }

    public boolean visit(MySqlShowCharacterSetStatement x) {
        print("SHOW CHARACTER SET");
        if (x.getPattern() != null) {
            print(" LIKE ");
            x.getPattern().accept(this);
        }
        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowCharacterSetStatement x) {

    }

    public boolean visit(MySqlShowContributorsStatement x) {
        print("SHOW CONTRIBUTORS");
        return false;
    }

    public void endVisit(MySqlShowContributorsStatement x) {

    }

    public boolean visit(MySqlShowCreateDatabaseStatement x) {
        print("SHOW CREATE DATABASE ");
        x.getDatabase().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateDatabaseStatement x) {

    }

    public boolean visit(MySqlShowCreateEventStatement x) {
        print("SHOW CREATE EVENT ");
        x.getEventName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateEventStatement x) {

    }

    public boolean visit(MySqlShowCreateFunctionStatement x) {
        print("SHOW CREATE FUNCTION ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateFunctionStatement x) {

    }

    public boolean visit(MySqlShowCreateProcedureStatement x) {
        print("SHOW CREATE PROCEDURE ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateProcedureStatement x) {

    }

    public boolean visit(MySqlShowCreateTableStatement x) {
        print("SHOW CREATE TABLE ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateTableStatement x) {

    }

    public boolean visit(MySqlShowCreateTriggerStatement x) {
        print("SHOW CREATE TRIGGER ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateTriggerStatement x) {

    }

    public boolean visit(MySqlShowCreateViewStatement x) {
        print("SHOW CREATE VIEW ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowCreateViewStatement x) {

    }

    public boolean visit(MySqlShowEngineStatement x) {
        print("SHOW ENGINE ");
        x.getName().accept(this);
        print(' ');
        print(x.getOption().name());
        return false;
    }

    public void endVisit(MySqlShowEngineStatement x) {

    }

    public boolean visit(MySqlShowEventsStatement x) {
        print("SHOW EVENTS");
        if (x.getSchema() != null) {
            print(" FROM ");
            x.getSchema().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowEventsStatement x) {

    }

    public boolean visit(MySqlShowFunctionCodeStatement x) {
        print("SHOW FUNCTION CODE ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowFunctionCodeStatement x) {

    }

    public boolean visit(MySqlShowFunctionStatusStatement x) {
        print("SHOW FUNCTION STATUS");
        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowFunctionStatusStatement x) {

    }

    public boolean visit(MySqlShowEnginesStatement x) {
        if (x.isStorage()) {
            print("SHOW STORAGE ENGINES");
        } else {
            print("SHOW ENGINES");
        }
        return false;
    }

    public void endVisit(MySqlShowEnginesStatement x) {

    }

    public boolean visit(MySqlShowErrorsStatement x) {
        if (x.isCount()) {
            print("SHOW COUNT(*) ERRORS");
        } else {
            print("SHOW ERRORS");
            if (x.getLimit() != null) {
                print(' ');
                x.getLimit().accept(this);
            }
        }
        return false;
    }

    public void endVisit(MySqlShowErrorsStatement x) {

    }

    public boolean visit(MySqlShowGrantsStatement x) {
        print("SHOW GRANTS");
        if (x.getUser() != null) {
            print(" FOR ");
            x.getUser().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowGrantsStatement x) {

    }

    public boolean visit(MySqlUserName x) {
        print(x.getUserName());
        if (x.getHost() != null) {
            print('@');
            print(x.getHost());
        }
        return false;
    }

    public void endVisit(MySqlUserName x) {

    }

    public boolean visit(MySqlShowIndexesStatement x) {
        print("SHOW INDEX");

        if (x.getTable() != null) {
            print(" FROM ");
            if (x.getDatabase() != null) {
                x.getDatabase().accept(this);
                print('.');
            }
            x.getTable().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowIndexesStatement x) {

    }

    public boolean visit(MySqlShowKeysStatement x) {
        print("SHOW KEYS");

        if (x.getTable() != null) {
            print(" FROM ");
            if (x.getDatabase() != null) {
                x.getDatabase().accept(this);
                print('.');
            }
            x.getTable().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowKeysStatement x) {

    }

    public boolean visit(MySqlShowMasterStatusStatement x) {
        print("SHOW MASTER STATUS");
        return false;
    }

    public void endVisit(MySqlShowMasterStatusStatement x) {

    }

    public boolean visit(MySqlShowOpenTablesStatement x) {
        print("SHOW OPEN TABLES");

        if (x.getDatabase() != null) {
            print(" FROM ");
            x.getDatabase().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowOpenTablesStatement x) {

    }

    public boolean visit(MySqlShowPluginsStatement x) {
        print("SHOW PLUGINS");
        return false;
    }

    public void endVisit(MySqlShowPluginsStatement x) {

    }

    public boolean visit(MySqlShowPrivilegesStatement x) {
        print("SHOW PRIVILEGES");
        return false;
    }

    public void endVisit(MySqlShowPrivilegesStatement x) {

    }

    public boolean visit(MySqlShowProcedureCodeStatement x) {
        print("SHOW PROCEDURE CODE ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(MySqlShowProcedureCodeStatement x) {

    }

    public boolean visit(MySqlShowProcedureStatusStatement x) {
        print("SHOW PROCEDURE STATUS");
        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowProcedureStatusStatement x) {

    }

    public boolean visit(MySqlShowProcessListStatement x) {
        if (x.isFull()) {
            print("SHOW FULL PROCESSLIST");
        } else {
            print("SHOW PROCESSLIST");
        }
        return false;
    }

    public void endVisit(MySqlShowProcessListStatement x) {

    }

    public boolean visit(MySqlShowProfileStatement x) {
        print("SHOW PROFILE");
        for (int i = 0; i < x.getTypes().size(); ++i) {
            if (i == 0) {
                print(' ');
            } else {
                print(", ");
            }
            print(x.getTypes().get(i).name);
        }

        if (x.getForQuery() != null) {
            print(" FOR QUERY ");
            x.getForQuery().accept(this);
        }

        if (x.getLimit() != null) {
            print(' ');
            x.getLimit().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlShowProfileStatement x) {

    }

    public boolean visit(MySqlShowProfilesStatement x) {
        print("SHOW PROFILES");
        return false;
    }

    public void endVisit(MySqlShowProfilesStatement x) {

    }

    public boolean visit(MySqlShowRelayLogEventsStatement x) {
        print("SHOW RELAYLOG EVENTS");

        if (x.getLogName() != null) {
            print(" IN ");
            x.getLogName().accept(this);
        }

        if (x.getFrom() != null) {
            print(" FROM ");
            x.getFrom().accept(this);
        }

        if (x.getLimit() != null) {
            print(' ');
            x.getLimit().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowRelayLogEventsStatement x) {

    }

    public boolean visit(MySqlShowSlaveHostsStatement x) {
        print("SHOW SLAVE HOSTS");
        return false;
    }

    public void endVisit(MySqlShowSlaveHostsStatement x) {

    }

    public boolean visit(MySqlShowSlaveStatusStatement x) {
        print("SHOW SLAVE STATUS");
        return false;
    }

    public void endVisit(MySqlShowSlaveStatusStatement x) {

    }

    public boolean visit(MySqlShowTableStatusStatement x) {
        print("SHOW TABLE STATUS");
        if (x.getDatabase() != null) {
            print(" FROM ");
            x.getDatabase().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowTableStatusStatement x) {

    }

    public boolean visit(MySqlShowTriggersStatement x) {
        print("SHOW TRIGGERS");

        if (x.getDatabase() != null) {
            print(" FROM ");
            x.getDatabase().accept(this);
        }

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowTriggersStatement x) {

    }

    public boolean visit(MySqlShowVariantsStatement x) {
        print("SHOW ");

        if (x.isGlobal()) {
            print("GLOBAL ");
        }

        if (x.isSession()) {
            print("SESSION ");
        }

        print("VARIABLES");

        if (x.getLike() != null) {
            print(" LIKE ");
            x.getLike().accept(this);
        }

        if (x.getWhere() != null) {
            print(" WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlShowVariantsStatement x) {

    }

    public boolean visit(MySqlAlterTableStatement x) {
        if (x.isIgnore()) {
            print("ALTER IGNORE TABLE ");
        } else {
            print("ALTER TABLE ");
        }
        x.getName().accept(this);
        incrementIndent();
        for (int i = 0; i < x.getItems().size(); ++i) {
            SQLAlterTableItem item = x.getItems().get(i);
            if (i != 0) {
                print(',');
            }
            println();
            item.accept(this);
        }
        decrementIndent();
        return false;
    }

    public void endVisit(MySqlAlterTableStatement x) {

    }

    public boolean visit(MySqlAlterTableAddColumn x) {
        print("ADD COLUMN ");
        printAndAccept(x.getColumns(), ", ");
        if (x.getAfter() != null) {
            print(" AFTER ");
            x.getAfter().accept(this);
        }
        return false;
    }

    public void endVisit(MySqlAlterTableAddColumn x) {

    }

    public boolean visit(MySqlCreateIndexStatement x) {
        print("CREATE ");
        if (x.getType() != null) {
            print(x.getType());
            print(" ");
        }

        print("INDEX ");

        x.getName().accept(this);
        print(" ON ");
        x.getTable().accept(this);
        print(" (");
        printAndAccept(x.getItems(), ", ");
        print(")");

        if (x.getUsing() != null) {
            print(" USING ");
            print(x.getUsing());
        }
        return false;
    }

    public void endVisit(MySqlCreateIndexStatement x) {

    }

    public boolean visit(MySqlRenameTableStatement.Item x) {
        x.getName().accept(this);
        print(" TO ");
        x.getTo().accept(this);
        return false;
    }

    public void endVisit(MySqlRenameTableStatement.Item x) {

    }

    public boolean visit(MySqlRenameTableStatement x) {
        print("RENAME TABLE ");
        printAndAccept(x.getItems(), ", ");
        return false;
    }

    public void endVisit(MySqlRenameTableStatement x) {

    }

    public boolean visit(MySqlDropViewStatement x) {
        print("DROP VIEW ");
        if (x.isIfExists()) {
            print("IF EXISTS ");
        }

        printAndAccept(x.getTableSources(), ", ");

        if (x.getOption() != null) {
            print(' ');
            print(x.getOption());
        }
        return false;
    }

    public void endVisit(MySqlDropViewStatement x) {

    }

    public boolean visit(MySqlUnionQuery x) {
        {
            boolean needParen = false;
            if (x.getLeft() instanceof MySqlSelectQueryBlock) {
                MySqlSelectQueryBlock right = (MySqlSelectQueryBlock) x.getLeft();
                if (right.getOrderBy() != null || right.getLimit() != null) {
                    needParen = true;
                }
            }
            if (needParen) {
                print('(');
                x.getLeft().accept(this);
                print(')');
            } else {
                x.getLeft().accept(this);
            }
        }
        println();
        print(x.getOperator().name);
        println();

        boolean needParen = false;

        if (x.getOrderBy() != null || x.getLimit() != null) {
            needParen = true;
        } else if (x.getRight() instanceof MySqlSelectQueryBlock) {
            MySqlSelectQueryBlock right = (MySqlSelectQueryBlock) x.getRight();
            if (right.getOrderBy() != null || right.getLimit() != null) {
                needParen = true;
            }
        }

        if (needParen) {
            print('(');
            x.getRight().accept(this);
            print(')');
        } else {
            x.getRight().accept(this);
        }

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        if (x.getLimit() != null) {
            println();
            x.getLimit().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlUnionQuery x) {

    }

    public boolean visit(MySqlUseIndexHint x) {
        print("USE INDEX ");
        if (x.getOption() != null) {
            print("FOR ");
            print(x.getOption().name);
            print(' ');
        }
        print('(');
        printAndAccept(x.getIndexList(), ", ");
        print(')');
        return false;
    }

    public void endVisit(MySqlUseIndexHint x) {

    }

    public boolean visit(MySqlIgnoreIndexHint x) {
        print("IGNORE INDEX ");
        if (x.getOption() != null) {
            print("FOR ");
            print(x.getOption().name);
            print(' ');
        }
        print('(');
        printAndAccept(x.getIndexList(), ", ");
        print(')');
        return false;
    }

    public void endVisit(MySqlIgnoreIndexHint x) {

    }

    public boolean visit(SQLExprTableSource x) {
        x.getExpr().accept(this);

        if (x.getAlias() != null) {
            print(' ');
            print(x.getAlias());
        }

        for (int i = 0; i < x.getHints().size(); ++i) {
            print(' ');
            x.getHints().get(i).accept(this);
        }

        return false;
    }

    public boolean visit(MySqlLockTableStatement x) {
        print("LOCK TABLES ");
        x.getTableSource().accept(this);
        if (x.getLockType() != null) {
            print(' ');
            print(x.getLockType().name);
        }
        return false;
    }

    public void endVisit(MySqlLockTableStatement x) {

    }

    public boolean visit(MySqlUnlockTablesStatement x) {
        print("UNLOCK TABLES");
        return false;
    }

    public void endVisit(MySqlUnlockTablesStatement x) {

    }

    public boolean visit(MySqlForceIndexHint x) {
        print("FORCE INDEX ");
        if (x.getOption() != null) {
            print("FOR ");
            print(x.getOption().name);
            print(' ');
        }
        print('(');
        printAndAccept(x.getIndexList(), ", ");
        print(')');
        return false;
    }

    public void endVisit(MySqlForceIndexHint x) {

    }

    public boolean visit(MySqlAlterTableChangeColumn x) {
        print("CHANGE COLUMN ");
        x.getColumnName().accept(this);
        print(' ');
        x.getNewColumnDefinition().accept(this);
        if (x.getFirst() != null) {
            if (x.getFirst().booleanValue()) {
                print(" FIRST");
            } else {
                print(" AFTER");
            }
        }

        return false;
    }

    public void endVisit(MySqlAlterTableChangeColumn x) {

    }

    public boolean visit(MySqlAlterTableCharacter x) {
        print("CHARACTER SET = ");
        x.getCharacterSet().accept(this);

        if (x.getCollate() != null) {
            print(", COLLATE = ");
            x.getCollate().accept(this);
        }

        return false;
    }

    public void endVisit(MySqlAlterTableCharacter x) {

    }

    public boolean visit(MySqlAlterTableAddIndex x) {
        print("ADD ");
        if (x.getType() != null) {
            print(x.getType());
            print(" ");
        }

        print("INDEX ");

        if (x.getName() != null) {
            x.getName().accept(this);
            print(' ');
        }
        print("(");
        printAndAccept(x.getItems(), ", ");
        print(")");

        if (x.getUsing() != null) {
            print(" USING ");
            print(x.getUsing());
        }
        return false;
    }

    public void endVisit(MySqlAlterTableAddIndex x) {

    }

    public boolean visit(MySqlAlterTableAddUnique x) {
        print("ADD ");
        if (x.getType() != null) {
            print(x.getType());
            print(" ");
        }

        print("UNIQUE ");

        if (x.getName() != null) {
            x.getName().accept(this);
            print(' ');
        }
        print("(");
        printAndAccept(x.getItems(), ", ");
        print(")");

        if (x.getUsing() != null) {
            print(" USING ");
            print(x.getUsing());
        }
        return false;
    }

    public void endVisit(MySqlAlterTableAddUnique x) {

    }

    public boolean visit(MySqlAlterTableOption x) {
        print(x.getName());
        print(" = ");
        print(x.getValue());
        return false;
    }

    public void endVisit(MySqlAlterTableOption x) {

    }

    public void endVisit(MySqlCreateTableStatement x) {

    }

    public boolean visit(MySqlHelpStatement x) {
        print("HELP ");
        x.getContent().accept(this);
        return false;
    }

    public void endVisit(MySqlHelpStatement x) {

    }

    public boolean visit(MySqlCharExpr x) {
        print(x.toString());
        return false;
    }

    public void endVisit(MySqlCharExpr x) {

    }
}
