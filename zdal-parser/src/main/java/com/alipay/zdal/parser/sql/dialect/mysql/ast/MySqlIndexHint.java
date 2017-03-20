/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlIndexHint.java, v 0.1 2012-11-17 ÏÂÎç3:29:17 Exp $
 */
public interface MySqlIndexHint extends MySqlHint {
    public static enum Option {
        JOIN("JOIN"), ORDER_BY("ORDER BY"), GROUP_BY("GROUP BY");

        public final String name;

        Option(String name) {
            this.name = name;
        }
    }
}
