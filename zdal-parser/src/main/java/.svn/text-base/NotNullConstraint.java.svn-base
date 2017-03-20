/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: NotNullConstraint.java, v 0.1 2012-11-17 обнГ3:19:50 xiaoqing.zhouxq Exp $
 */
@SuppressWarnings("serial")
public class NotNullConstraint extends SQLConstaintImpl implements SQLColumnConstraint {

    public NotNullConstraint() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

}
