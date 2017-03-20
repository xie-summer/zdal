/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLParser.java, v 0.1 2012-11-17 ÏÂÎç3:54:20 Exp $
 */
public class SQLParser {

    protected final Lexer lexer;

    public SQLParser(String sql) {
        this(new Lexer(sql));
        this.lexer.nextToken();
    }

    public SQLParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public final Lexer getLexer() {
        return lexer;
    }

    protected boolean identifierEquals(String text) {
        return lexer.token() == Token.IDENTIFIER && lexer.stringVal().equalsIgnoreCase(text);
    }

    protected void acceptIdentifier(String text) {
        if (identifierEquals(text)) {
            lexer.nextToken();
        } else {
            setErrorEndPos(lexer.pos());
            throw new SQLParserException("syntax error, expect " + text + ", actual "
                                         + lexer.token());
        }
    }

    protected String as() throws ParserException {
        String alias = null;

        if (lexer.token() == Token.AS) {
            lexer.nextToken();

            if (lexer.token() == Token.LITERAL_ALIAS) {
                alias = '"' + lexer.stringVal() + '"';
                lexer.nextToken();
                return alias;
            }

            if (lexer.token() == Token.IDENTIFIER) {
                alias = lexer.stringVal();
                lexer.nextToken();
                return alias;
            }

            if (lexer.token() == Token.LITERAL_CHARS) {
                alias = "'" + lexer.stringVal() + "'";
                lexer.nextToken();
                return alias;
            }

            if (lexer.token() == Token.KEY || lexer.token() == Token.CASE) {
                alias = lexer.token.name();
                lexer.nextToken();
                return alias;
            }

            switch (lexer.token()) {
                case KEY:
                    alias = lexer.token().name();
                    lexer.nextToken();
                    return alias;
                default:
                    break;
            }

            throw new ParserException("Error : " + lexer.token());
        }

        if (lexer.token() == Token.LITERAL_ALIAS) {
            alias = '"' + lexer.stringVal() + '"';
            lexer.nextToken();
        } else if (lexer.token() == Token.IDENTIFIER) {
            alias = lexer.stringVal();
            lexer.nextToken();
        } else if (lexer.token() == Token.LITERAL_CHARS) {
            alias = "'" + lexer.stringVal() + "'";
            lexer.nextToken();
        } else if (lexer.token() == Token.CASE) {
            alias = lexer.token.name();
            lexer.nextToken();
        }

        switch (lexer.token()) {
            case KEY:
                alias = lexer.token().name();
                lexer.nextToken();
                return alias;
            default:
                break;
        }

        return alias;
    }

    public void accept(Token token) {
        if (lexer.token() == token) {
            lexer.nextToken();
        } else {
            setErrorEndPos(lexer.pos());
            throw new SQLParserException("syntax error, expect " + token + ", actual "
                                         + lexer.token() + " " + lexer.stringVal());
        }
    }

    public void match(Token token) {
        if (lexer.token() != token) {
            throw new SQLParserException("syntax error, expect " + token + ", actual "
                                         + lexer.token() + " " + lexer.stringVal());
        }
    }

    private int errorEndPos = -1;

    protected void setErrorEndPos(int errPos) {
        if (errPos > errorEndPos) {
            errorEndPos = errPos;
        }
    }

}
