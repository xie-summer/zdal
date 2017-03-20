/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLName;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLForeignKeyConstraint.java, v 0.1 2012-11-17 обнГ3:22:21 xiaoqing.zhouxq Exp $
 */
public interface SQLForeignKeyConstraint extends SQLConstaint {

    List<SQLName> getReferencingColumns();

    SQLName getReferencedTableName();

    void setReferencedTableName(SQLName value);

    List<SQLName> getReferencedColumns();
}
