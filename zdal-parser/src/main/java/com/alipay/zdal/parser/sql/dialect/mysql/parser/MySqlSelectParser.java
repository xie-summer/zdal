/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.parser;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLSetQuantifier;
import com.alipay.zdal.parser.sql.ast.expr.SQLLiteralExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectGroupByClause;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQuery;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLUnionQuery;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlIndexHintImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectGroupBy;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alipay.zdal.parser.sql.parser.ParserException;
import com.alipay.zdal.parser.sql.parser.SQLExprParser;
import com.alipay.zdal.parser.sql.parser.SQLSelectParser;
import com.alipay.zdal.parser.sql.parser.Token;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlSelectParser.java, v 0.1 2012-11-17 ÏÂÎç3:40:19 Exp $
 */
public class MySqlSelectParser extends SQLSelectParser {

    public MySqlSelectParser(SQLExprParser exprParser) {
        super(exprParser);
    }

    public MySqlSelectParser(String sql) throws ParserException {
        this(new MySqlExprParser(sql));
    }

    @Override
    public SQLSelectQuery query() throws ParserException {
        if (lexer.token() == (Token.LPAREN)) {
            lexer.nextToken();

            SQLSelectQuery select = query();
            accept(Token.RPAREN);

            return queryRest(select);
        }

        MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();

        if (lexer.token() == Token.SELECT) {
            lexer.nextToken();

            if (lexer.token() == Token.HINT) {
                this.exprParser.parseHints(queryBlock.getHints());
            }

            if (lexer.token() == (Token.DISTINCT)) {
                queryBlock.setDistionOption(SQLSetQuantifier.DISTINCT);
                lexer.nextToken();
            } else if (identifierEquals("DISTINCTROW")) {
                queryBlock.setDistionOption(SQLSetQuantifier.DISTINCTROW);
                lexer.nextToken();
            } else if (lexer.token() == (Token.ALL)) {
                queryBlock.setDistionOption(SQLSetQuantifier.ALL);
                lexer.nextToken();
            }

            if (identifierEquals("HIGH_PRIORITY")) {
                queryBlock.setHignPriority(true);
                lexer.nextToken();
            }

            if (identifierEquals("STRAIGHT_JOIN")) {
                queryBlock.setStraightJoin(true);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_SMALL_RESULT")) {
                queryBlock.setSmallResult(true);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_BIG_RESULT")) {
                queryBlock.setBigResult(true);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_BUFFER_RESULT")) {
                queryBlock.setBufferResult(true);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_CACHE")) {
                queryBlock.setCache(true);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_NO_CACHE")) {
                queryBlock.setCache(false);
                lexer.nextToken();
            }

            if (identifierEquals("SQL_CALC_FOUND_ROWS")) {
                queryBlock.setCalcFoundRows(true);
                lexer.nextToken();
            }

            parseSelectList(queryBlock);

            if (lexer.token() == (Token.INTO)) {
                lexer.nextToken();

                if (identifierEquals("OUTFILE")) {
                    lexer.nextToken();

                    MySqlOutFileExpr outFile = new MySqlOutFileExpr();
                    outFile.setFile(expr());

                    queryBlock.setInto(outFile);

                    if (identifierEquals("FIELDS") || identifierEquals("COLUMNS")) {
                        lexer.nextToken();

                        if (identifierEquals("TERMINATED")) {
                            lexer.nextToken();
                            accept(Token.BY);
                        }
                        outFile.setColumnsTerminatedBy((SQLLiteralExpr) expr());

                        if (identifierEquals("OPTIONALLY")) {
                            lexer.nextToken();
                            outFile.setColumnsEnclosedOptionally(true);
                        }

                        if (identifierEquals("ENCLOSED")) {
                            lexer.nextToken();
                            accept(Token.BY);
                            outFile.setColumnsEnclosedBy((SQLLiteralExpr) expr());
                        }

                        if (identifierEquals("ESCAPED")) {
                            lexer.nextToken();
                            accept(Token.BY);
                            outFile.setColumnsEscaped((SQLLiteralExpr) expr());
                        }
                    }

                    if (identifierEquals("LINES")) {
                        lexer.nextToken();

                        if (identifierEquals("STARTING")) {
                            lexer.nextToken();
                            accept(Token.BY);
                            outFile.setLinesStartingBy((SQLLiteralExpr) expr());
                        } else {
                            identifierEquals("TERMINATED");
                            lexer.nextToken();
                            accept(Token.BY);
                            outFile.setLinesTerminatedBy((SQLLiteralExpr) expr());
                        }
                    }
                } else {
                    queryBlock.setInto(this.exprParser.name());
                }
            }
        }

        parseFrom(queryBlock);

        parseWhere(queryBlock);

        parseGroupBy(queryBlock);

        queryBlock.setOrderBy(this.exprParser.parseOrderBy());

        if (lexer.token() == Token.LIMIT) {
            queryBlock.setLimit(parseLimit());
        }

        if (identifierEquals("PROCEDURE")) {
            lexer.nextToken();
            throw new ParserException("TODO");
        }

        if (lexer.token() == Token.INTO) {
            lexer.nextToken();
            SQLExpr expr = this.exprParser.name();
            queryBlock.setInto(expr);
        }

        if (lexer.token() == Token.FOR) {
            lexer.nextToken();
            accept(Token.UPDATE);

            queryBlock.setForUpdate(true);
        }

        if (lexer.token() == Token.LOCK) {
            lexer.nextToken();
            accept(Token.IN);
            acceptIdentifier("SHARE");
            acceptIdentifier("MODE");
            queryBlock.setLockInShareMode(true);
        }

        return queryRest(queryBlock);
    }

    protected void parseGroupBy(SQLSelectQueryBlock queryBlock) throws ParserException {
        if (lexer.token() == (Token.GROUP)) {
            lexer.nextToken();
            accept(Token.BY);

            SQLSelectGroupByClause groupBy = new SQLSelectGroupByClause();
            while (true) {
                groupBy.getItems().add(this.exprParser.expr());
                if (!(lexer.token() == (Token.COMMA)))
                    break;
                lexer.nextToken();
            }

            if (identifierEquals("WITH")) {
                lexer.nextToken();
                acceptIdentifier("ROLLUP");

                MySqlSelectGroupBy mySqlGroupBy = new MySqlSelectGroupBy();
                mySqlGroupBy.getItems().addAll(groupBy.getItems());
                mySqlGroupBy.setRollUp(true);

                groupBy = mySqlGroupBy;
            }

            if (lexer.token() == Token.HAVING) {
                lexer.nextToken();

                groupBy.setHaving(this.exprParser.expr());
            }

            queryBlock.setGroupBy(groupBy);
        }
    }

    protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource)
                                                                             throws ParserException {
        if (identifierEquals("USING")) {
            return tableSource;
        }

        if (identifierEquals("USE")) {
            lexer.nextToken();
            MySqlUseIndexHint hint = new MySqlUseIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
        }

        if (identifierEquals("IGNORE")) {
            lexer.nextToken();
            MySqlIgnoreIndexHint hint = new MySqlIgnoreIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
        }

        if (identifierEquals("FORCE")) {
            lexer.nextToken();
            MySqlForceIndexHint hint = new MySqlForceIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
        }

        return super.parseTableSourceRest(tableSource);
    }

    private void parseIndexHint(MySqlIndexHintImpl hint) {
        if (lexer.token() == Token.INDEX) {
            lexer.nextToken();
        } else {
            accept(Token.KEY);
        }

        if (lexer.token() == Token.FOR) {
            lexer.nextToken();

            if (lexer.token() == Token.JOIN) {
                lexer.nextToken();
                hint.setOption(MySqlIndexHint.Option.JOIN);
            } else if (lexer.token() == Token.ORDER) {
                lexer.nextToken();
                accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.ORDER_BY);
            } else {
                accept(Token.GROUP);
                accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.GROUP_BY);
            }
        }

        accept(Token.LPAREN);
        this.exprParser.names(hint.getIndexList());
        accept(Token.RPAREN);
    }

    protected MySqlUnionQuery createSQLUnionQuery() {
        return new MySqlUnionQuery();
    }

    public SQLUnionQuery unionRest(SQLUnionQuery union) {
        if (lexer.token() == Token.LIMIT) {
            MySqlUnionQuery mysqlUnionQuery = (MySqlUnionQuery) union;
            mysqlUnionQuery.setLimit(parseLimit());
        }
        return super.unionRest(union);
    }

    public Limit parseLimit() {
        return ((MySqlExprParser) this.exprParser).parseLimit();
    }
}
