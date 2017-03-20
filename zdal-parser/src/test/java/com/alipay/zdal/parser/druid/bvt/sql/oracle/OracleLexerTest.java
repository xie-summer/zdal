package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleLexer;
import com.alipay.zdal.parser.sql.parser.Token;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleLexerTest.java, v 0.1 2012-5-17 ÉÏÎç10:18:40 xiaoqing.zhouxq Exp $
 */
public class OracleLexerTest extends TestCase {

    public void test_hint() throws Exception {
        String sql = "SELECT /*+FIRST_ROWS*/ * FROM T WHERE F1 = ? ORDER BY F2";
        OracleLexer lexer = new OracleLexer(sql);
        for (;;) {
            lexer.nextToken();
            Token tok = lexer.token();

            switch (tok) {
                case IDENTIFIER:
                    System.out.println(tok.name() + "\t\t" + lexer.stringVal());
                    break;
                case HINT:
                    System.out.println(tok.name() + "\t\t\t" + lexer.stringVal());
                    break;
                default:
                    System.out.println(tok.name() + "\t\t\t" + tok.name);
                    break;
            }

            if (tok == Token.EOF) {
                break;
            }
        }
    }
}
