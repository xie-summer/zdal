/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.parser;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLListExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLUpdateSetItem;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alipay.zdal.parser.sql.parser.Lexer;
import com.alipay.zdal.parser.sql.parser.ParserException;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;
import com.alipay.zdal.parser.sql.parser.Token;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleUpdateParser.java, v 0.1 2012-11-17 ÏÂÎç3:52:30 Exp $
 */
public class OracleUpdateParser extends SQLStatementParser {

    public OracleUpdateParser(String sql) throws ParserException {
        super(new OracleExprParser(sql));
    }

    public OracleUpdateParser(Lexer lexer) {
        super(new OracleExprParser(lexer));
    }

    @Override
    public OracleUpdateStatement parseUpdateStatement() throws ParserException {
        OracleUpdateStatement update = new OracleUpdateStatement();

        if (lexer.token() == Token.UPDATE) {
            lexer.nextToken();

            parseHints(update);

            if (identifierEquals("ONLY")) {
                update.setOnly(true);
            }

            SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
            update.setTableSource(tableSource);

            if ((update.getAlias() == null) || (update.getAlias().length() == 0)) {
                update.setAlias(as());
            }
        }

        parseSet(update);

        parseWhere(update);

        parseReturn(update);

        parseErrorLoging(update);

        return update;
    }

    private void parseErrorLoging(OracleUpdateStatement update) throws ParserException {
        if (identifierEquals("LOG"))
            throw new ParserException("TODO" + update);
    }

    private void parseReturn(OracleUpdateStatement update) throws ParserException {
        if (identifierEquals("RETURN") || lexer.token() == Token.RETURNING) {
            lexer.nextToken();

            for (;;) {
                SQLExpr item = this.exprParser.expr();
                update.getReturning().add(item);

                if (lexer.token() == Token.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                break;
            }

            accept(Token.INTO);

            for (;;) {
                SQLExpr item = this.exprParser.expr();
                update.getReturningInto().add(item);

                if (lexer.token() == Token.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                break;
            }
        }
    }

    private void parseHints(OracleUpdateStatement update) throws ParserException {
        if (lexer.token() == Token.HINT) {
            throw new ParserException("TODO" + update);
        }
    }

    private void parseWhere(OracleUpdateStatement update) throws ParserException {
        if (lexer.token() == (Token.WHERE)) {
            lexer.nextToken();
            update.setWhere(this.exprParser.expr());
        }
    }

    private void parseSet(OracleUpdateStatement update) throws ParserException {
        accept(Token.SET);

        for (;;) {
            SQLUpdateSetItem item = new SQLUpdateSetItem();

            if (lexer.token() == (Token.LPAREN)) {
                lexer.nextToken();
                SQLListExpr list = new SQLListExpr();
                this.exprParser.exprList(list.getItems());
                accept(Token.RPAREN);
                item.setColumn(list);
            } else {
                item.setColumn(this.exprParser.primary());
            }
            accept(Token.EQ);
            item.setValue(this.exprParser.expr());
            update.getItems().add(item);

            if (lexer.token() != Token.COMMA) {
                break;
            }

            lexer.nextToken();
        }
    }
}
