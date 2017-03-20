/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.parser;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleTokenType.java, v 0.1 2012-11-17 ÏÂÎç3:52:23 Exp $
 */
public final class OracleTokenType {

    public static final int LINECOMMENT      = 0;
    public static final int IDENTIFIER       = 1;
    public static final int VARIABLE         = 2;
    public static final int KEYWORD          = 3;
    public static final int OPERATOR         = 4;
    public static final int PUNCTUATION      = 5;
    public static final int CHAR             = 6;
    public static final int NCHAR            = 7;
    public static final int INT              = 8;
    public static final int FLOAT            = 9;
    public static final int DOUBLE           = 10;
    public static final int DECIMAL          = 11;
    public static final int EOF              = 12;
    public static final int UNKNOWN          = 13;
    public static final int MULTILINECOMMENT = 14;
    public static final int LONG             = 15;
    public static final int HINT             = 16;

    public static final String typename(int tokType) {
        switch (tokType) {
            case 0:
                return "Comment";
            case 1:
                return "Identifier";
            case 2:
                return "Variable";
            case 3:
                return "Keyword";
            case 4:
                return "Operator";
            case 5:
                return "Punctuation";
            case 6:
                return "Char";
            case 7:
                return "NChar";
            case 8:
                return "Int";
            case 9:
                return "Float";
            case 10:
                return "Double";
            case 11:
                return "Decimal";
            case 12:
                return "EOF";
            case 16:
                return "Hints";
            case 13:
            case 14:
            case 15:
        }
        return "Unknown";
    }
}
