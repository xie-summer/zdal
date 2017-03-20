/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.expr.SQLInListExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLParserUtils;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ParameterizedOutputVisitorUtils.java, v 0.1 2012-11-17 ÏÂÎç3:56:10 Exp $
 */
public class ParameterizedOutputVisitorUtils {

    public static final String ATTR_PARAMS_SKIP = "druid.parameterized.skip";

    public static String parameterize(String sql, String dbType) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        statemen.accept(visitor);

        return out.toString();
    }

    public static SQLASTOutputVisitor createParameterizedOutputVisitor(Appendable out, String dbType) {
        if (JdbcUtils.ORACLE.equals(dbType)) {
            return new OracleParameterizedOutputVisitor(out);
        }

        if (JdbcUtils.MYSQL.equals(dbType)) {
            return new MySqlParameterizedOutputVisitor(out);
        }
        return new ParameterizedOutputVisitor(out);
    }

    public static boolean visit(SQLASTOutputVisitor v, SQLInListExpr x) {
        x.getExpr().accept(v);

        if (x.isNot()) {
            v.print(" NOT IN (?)");
        } else {
            v.print(" IN (?)");
        }

        return false;
    }

    public static SQLBinaryOpExpr merge(SQLBinaryOpExpr x) {
        if (x.getLeft() instanceof SQLLiteralExpr && x.getRight() instanceof SQLLiteralExpr) {
            if (x.getOperator() == SQLBinaryOperator.Equality
                || x.getOperator() == SQLBinaryOperator.NotEqual) {
                x.getLeft().getAttributes().put(ATTR_PARAMS_SKIP, true);
                x.getRight().getAttributes().put(ATTR_PARAMS_SKIP, true);
            }
            return x;
        }

        if (x.getRight() instanceof SQLLiteralExpr) {
            x = new SQLBinaryOpExpr(x.getLeft(), x.getOperator(), new SQLVariantRefExpr("?"));
        }

        if (x.getLeft() instanceof SQLLiteralExpr) {
            x = new SQLBinaryOpExpr(new SQLVariantRefExpr("?"), x.getOperator(), x.getRight());
        }

        for (;;) {
            if (x.getRight() instanceof SQLBinaryOpExpr) {
                if (x.getLeft() instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr leftBinaryExpr = (SQLBinaryOpExpr) x.getLeft();
                    if (leftBinaryExpr.getRight().equals(x.getRight())) {
                        x = leftBinaryExpr;
                        continue;
                    }
                }
                x = new SQLBinaryOpExpr(x.getLeft(), x.getOperator(),
                    merge((SQLBinaryOpExpr) x.getRight()));
            }

            break;
        }

        if (x.getLeft() instanceof SQLBinaryOpExpr) {
            x = new SQLBinaryOpExpr(merge((SQLBinaryOpExpr) x.getLeft()), x.getOperator(),
                x.getRight());
        }

        // ID = ? OR ID = ? => ID = ?
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            if ((x.getLeft() instanceof SQLBinaryOpExpr)
                && (x.getRight() instanceof SQLBinaryOpExpr)) {
                SQLBinaryOpExpr left = (SQLBinaryOpExpr) x.getLeft();
                SQLBinaryOpExpr right = (SQLBinaryOpExpr) x.getRight();

                if (mergeEqual(left, right)) {
                    return left;
                }

                if (isLiteralExpr(left.getLeft())
                    && left.getOperator() == SQLBinaryOperator.BooleanOr) {
                    if (mergeEqual(left.getRight(), right)) {
                        return left;
                    }
                }
            }
        }

        return x;
    }

    private static boolean mergeEqual(SQLExpr a, SQLExpr b) {
        if (!(a instanceof SQLBinaryOpExpr)) {
            return false;
        }
        if (!(b instanceof SQLBinaryOpExpr)) {
            return false;
        }

        SQLBinaryOpExpr binaryA = (SQLBinaryOpExpr) a;
        SQLBinaryOpExpr binaryB = (SQLBinaryOpExpr) b;

        if (binaryA.getOperator() != SQLBinaryOperator.Equality) {
            return false;
        }

        if (binaryB.getOperator() != SQLBinaryOperator.Equality) {
            return false;
        }

        if (!(binaryA.getRight() instanceof SQLLiteralExpr || binaryA.getRight() instanceof SQLVariantRefExpr)) {
            return false;
        }

        if (!(binaryB.getRight() instanceof SQLLiteralExpr || binaryB.getRight() instanceof SQLVariantRefExpr)) {
            return false;
        }

        return binaryA.getLeft().toString().equals(binaryB.getLeft().toString());
    }

    private static boolean isLiteralExpr(SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        }

        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binary = (SQLBinaryOpExpr) expr;
            return isLiteralExpr(binary.getLeft()) && isLiteralExpr(binary.getRight());
        }

        return false;
    }
}
