package com.alipay.zdal.parser.druid.sql.parser;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.parser.Lexer;
import com.alipay.zdal.parser.sql.parser.Token;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLLexerTest2.java, v 0.1 2012-5-17 ÉÏÎç10:25:31 xiaoqing.zhouxq Exp $
 */
public class SQLLexerTest2 extends TestCase {

    public void test_lexer() throws Exception {
        String sql = "SELECT * FROM T WHERE F1 = ? ORDER BY F2";
        Lexer lexer = new Lexer(sql);
        for (;;) {
            lexer.nextToken();
            Token tok = lexer.token();

            if (tok == Token.IDENTIFIER) {
                System.out.println(tok.name() + "\t\t" + lexer.stringVal());
            } else if (tok == Token.LITERAL_INT) {
                System.out.println(tok.name() + "\t\t" + lexer.numberString());
            } else {
                System.out.println(tok.name() + "\t\t\t" + tok.name);
            }

            if (tok == Token.WHERE) {
                System.out.println("where pos : " + lexer.pos());
            }

            if (tok == Token.EOF) {
                break;
            }
        }
    }

    public void test_lexer2() throws Exception {
        String sql = "SELECT substr('''a''bc',0,3) FROM dual";
        Lexer lexer = new Lexer(sql);
        for (;;) {
            lexer.nextToken();
            Token tok = lexer.token();

            if (tok == Token.IDENTIFIER) {
                System.out.println(tok.name() + "\t\t" + lexer.stringVal());
            } else if (tok == Token.LITERAL_INT) {
                System.out.println(tok.name() + "\t\t" + lexer.numberString());
            } else if (tok == Token.LITERAL_CHARS) {
                System.out.println(tok.name() + "\t\t" + lexer.stringVal());
            } else {
                System.out.println(tok.name() + "\t\t\t" + tok.name);
            }

            if (tok == Token.EOF) {
                break;
            }
        }
    }

}
