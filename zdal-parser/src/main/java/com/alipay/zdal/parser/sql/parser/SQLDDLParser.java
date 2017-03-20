/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.statement.SQLTableConstaint;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLDDLParser.java, v 0.1 2012-11-17 ÏÂÎç3:54:08 Exp $
 */
public class SQLDDLParser extends SQLStatementParser {

    public SQLDDLParser(String sql) {
        super(sql);
    }

    public SQLDDLParser(SQLExprParser exprParser) {
        super(exprParser);
    }

    protected SQLTableConstaint parseConstraint() {
        SQLName name = null;
        if (lexer.token() == Token.CONSTRAINT) {
            lexer.nextToken();
        }

        if (lexer.token() == Token.IDENTIFIER) {
            name = this.exprParser.name();
        }

        if (lexer.token() == Token.PRIMARY) {
            lexer.nextToken();
            accept(Token.KEY);

            throw new ParserException("TODO");
        }

        if (name != null) {

        }

        throw new ParserException("TODO");
    }
}
