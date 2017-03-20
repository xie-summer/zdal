/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.parser;

import static com.alipay.zdal.parser.sql.parser.CharTypes.isFirstIdentifierChar;
import static com.alipay.zdal.parser.sql.parser.CharTypes.isIdentifierChar;
import static com.alipay.zdal.parser.sql.parser.LayoutCharacters.EOI;
import static com.alipay.zdal.parser.sql.parser.Token.LITERAL_CHARS;

import java.util.HashMap;
import java.util.Map;

import com.alipay.zdal.parser.sql.parser.Keywords;
import com.alipay.zdal.parser.sql.parser.Lexer;
import com.alipay.zdal.parser.sql.parser.NotAllowCommentException;
import com.alipay.zdal.parser.sql.parser.SQLParserException;
import com.alipay.zdal.parser.sql.parser.Token;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlLexer.java, v 0.1 2012-11-17 ÏÂÎç3:40:13 Exp $
 */
public class MySqlLexer extends Lexer {

    public final static Keywords DEFAULT_MYSQL_KEYWORDS;

    static {
        Map<String, Token> map = new HashMap<String, Token>();

        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());

        map.put("DUAL", Token.DUAL);
        map.put("FALSE", Token.FALSE);
        map.put("IDENTIFIED", Token.IDENTIFIED);
        map.put("IF", Token.IF);
        map.put("KILL", Token.KILL);

        map.put("LIMIT", Token.LIMIT);
        map.put("TRUE", Token.TRUE);

        DEFAULT_MYSQL_KEYWORDS = new Keywords(map);
    }

    public MySqlLexer(char[] input, int inputLength, boolean skipComment) {
        super(input, inputLength, skipComment);
        super.keywods = DEFAULT_MYSQL_KEYWORDS;
    }

    public MySqlLexer(String input) {
        super(input);
        super.keywods = DEFAULT_MYSQL_KEYWORDS;
    }

    public void scanVariable() {
        final char first = ch;

        if (ch != '@' && ch != ':' && ch != '#' && ch != '$') {
            throw new SQLParserException("illegal variable");
        }

        int hash = first;

        np = bp;
        sp = 1;

        if (buf[bp + 1] == '@') {
            ch = buf[++bp];
            hash = 31 * hash + ch;

            sp++;
        }

        if (buf[bp + 1] == '`') {
            ++bp;
            ++sp;
            char ch;
            for (;;) {
                ch = buf[++bp];

                if (ch == '`') {
                    sp++;
                    ch = buf[++bp];
                    break;
                } else if (ch == EOI) {
                    throw new SQLParserException("illegal identifier");
                }

                hash = 31 * hash + ch;

                sp++;
                continue;
            }

            this.ch = buf[bp];

            stringVal = symbolTable.addSymbol(buf, np, sp, hash);
            token = Token.VARIANT;
        } else if (buf[bp + 1] == '{') {
            ++bp;
            ++sp;
            char ch;
            for (;;) {
                ch = buf[++bp];

                if (ch == '}') {
                    sp++;
                    ch = buf[++bp];
                    break;
                } else if (ch == EOI) {
                    throw new SQLParserException("illegal identifier");
                }

                hash = 31 * hash + ch;

                sp++;
                continue;
            }

            this.ch = buf[bp];

            stringVal = symbolTable.addSymbol(buf, np, sp, hash);
            token = Token.VARIANT;
        } else {
            for (;;) {
                ch = buf[++bp];

                if (!isIdentifierChar(ch)) {
                    break;
                }

                hash = 31 * hash + ch;

                sp++;
                continue;
            }
        }

        this.ch = buf[bp];

        stringVal = symbolTable.addSymbol(buf, np, sp, hash);
        token = Token.VARIANT;
    }

    public void scanIdentifier() {
        final char first = ch;

        if (ch == '`') {
            int hash = first;

            np = bp;
            sp = 1;
            char ch;
            for (;;) {
                ch = buf[++bp];

                if (ch == '`') {
                    sp++;
                    ch = buf[++bp];
                    break;
                } else if (ch == EOI) {
                    throw new SQLParserException("illegal identifier");
                }

                hash = 31 * hash + ch;

                sp++;
                continue;
            }

            this.ch = buf[bp];

            stringVal = symbolTable.addSymbol(buf, np, sp, hash);
            Token tok = keywods.getKeyword(stringVal);
            if (tok != null) {
                token = tok;
            } else {
                token = Token.IDENTIFIER;
            }
        } else {

            final boolean firstFlag = isFirstIdentifierChar(first);
            if (!firstFlag) {
                throw new SQLParserException("illegal identifier");
            }

            int hash = first;

            np = bp;
            sp = 1;
            char ch;
            for (;;) {
                ch = buf[++bp];

                if (!isIdentifierChar(ch)) {
                    break;
                }

                hash = 31 * hash + ch;

                sp++;
                continue;
            }

            this.ch = buf[bp];

            stringVal = symbolTable.addSymbol(buf, np, sp, hash);
            Token tok = keywods.getKeyword(stringVal);
            if (tok != null) {
                token = tok;
            } else {
                token = Token.IDENTIFIER;
            }
        }
    }

    protected void scanString() {
        np = bp;
        boolean hasSpecial = false;

        for (;;) {
            if (bp >= buflen) {
                lexError(tokenPos, "unclosed.str.lit");
                return;
            }

            ch = buf[++bp];

            if (ch == '\\') {
                scanChar();
                if (!hasSpecial) {
                    System.arraycopy(buf, np + 1, sbuf, 0, sp);
                    hasSpecial = true;
                }

                switch (ch) {
                    case '\0':
                        putChar('\0');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '"':
                        putChar('"');
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case 'r':
                        putChar('\r');
                        break;
                    case 't':
                        putChar('\t');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'Z':
                        putChar((char) 0x1A); // ctrl + Z
                        break;
                    default:
                        putChar(ch);
                        break;
                }
                scanChar();
            }

            if (ch == '\'') {
                scanChar();
                if (ch != '\'') {
                    token = LITERAL_CHARS;
                    break;
                } else {
                    System.arraycopy(buf, np + 1, sbuf, 0, sp);
                    hasSpecial = true;
                    putChar('\'');
                    continue;
                }
            }

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(ch);
            } else {
                sbuf[sp++] = ch;
            }
        }

        if (!hasSpecial) {
            stringVal = new String(buf, np + 1, sp);
        } else {
            stringVal = new String(sbuf, 0, sp);
        }
    }

    public void scanComment() {
        if (ch != '/' && ch != '-') {
            throw new IllegalStateException();
        }

        np = bp;
        sp = 0;
        scanChar();

        // /*+ */
        if (ch == '*') {
            scanChar();
            sp++;

            while (ch == ' ') {
                scanChar();
                sp++;
            }

            boolean isHint = false;
            int startHintSp = sp + 1;
            if (ch == '!') {
                isHint = true;
                scanChar();
                sp++;
            }

            for (;;) {
                if (ch == '*' && buf[bp + 1] == '/') {
                    sp += 2;
                    scanChar();
                    scanChar();
                    break;
                }

                scanChar();
                sp++;
            }

            if (isHint) {
                stringVal = new String(buf, np + startHintSp, (sp - startHintSp) - 1);
                token = Token.HINT;
            } else {
                stringVal = new String(buf, np, sp);
                token = Token.MULTI_LINE_COMMENT;
            }

            if (token != Token.HINT && !isAllowComment()) {
                throw new NotAllowCommentException();
            }

            return;
        }

        if (!isAllowComment()) {
            throw new NotAllowCommentException();
        }

        if (ch == '/' || ch == '-') {
            scanChar();
            sp++;

            for (;;) {
                if (ch == '\r') {
                    if (buf[bp + 1] == '\n') {
                        sp += 2;
                        scanChar();
                        break;
                    }
                    sp++;
                    break;
                } else if (ch == EOI) {
                    break;
                }

                if (ch == '\n') {
                    scanChar();
                    sp++;
                    break;
                }

                scanChar();
                sp++;
            }

            stringVal = new String(buf, np + 1, sp);
            token = Token.LINE_COMMENT;
            return;
        }
    }
}
