/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.statement.SQLTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.FlashbackQueryClause;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectTableSource.java, v 0.1 2012-11-17 ÏÂÎç3:50:21 Exp $
 */
public interface OracleSelectTableSource extends SQLTableSource {

    OracleSelectPivotBase getPivot();

    void setPivot(OracleSelectPivotBase pivot);

    FlashbackQueryClause getFlashback();

    void setFlashback(FlashbackQueryClause flashback);
}
