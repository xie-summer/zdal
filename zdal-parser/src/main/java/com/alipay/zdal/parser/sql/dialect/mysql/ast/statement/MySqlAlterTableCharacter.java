/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLAlterTableItem;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: MySqlAlterTableCharacter.java, v 0.1 2012-11-17 ÏÂÎç3:31:35 Exp $
 */
public class MySqlAlterTableCharacter extends MySqlObjectImpl implements SQLAlterTableItem {

    private static final long serialVersionUID = 1L;

    private SQLExpr           characterSet;
    private SQLExpr           collate;

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, characterSet);
            acceptChild(visitor, collate);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(SQLExpr characterSet) {
        this.characterSet = characterSet;
    }

    public SQLExpr getCollate() {
        return collate;
    }

    public void setCollate(SQLExpr collate) {
        this.collate = collate;
    }

}
