/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.parser;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: LayoutCharacters.java, v 0.1 2012-11-17 ÏÂÎç3:53:34 Exp $
 */
public interface LayoutCharacters {

    /**
     * Tabulator column increment.
     */
    final static int  TABINC = 8;

    /**
     * Tabulator character.
     */
    final static byte TAB    = 0x8;

    /**
     * Line feed character.
     */
    final static byte LF     = 0xA;

    /**
     * Form feed character.
     */
    final static byte FF     = 0xC;

    /**
     * Carriage return character.
     */
    final static byte CR     = 0xD;

    /**
     * QS_TODO ä¸ºä»€ä¹ˆä¸æ˜?x0ï¼?br/>
     * End of input character. Used as a sentinel to denote the character one beyond the last defined character in a
     * source file.
     */
    final static byte EOI    = 0x1A;
}
