/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleSelectRestriction.java, v 0.1 2012-11-17 ÏÂÎç3:50:04 Exp $
 */
public abstract class OracleSelectRestriction extends OracleSQLObjectImpl {

    private static final long serialVersionUID = 1L;

    public OracleSelectRestriction() {

    }

    public static class CheckOption extends OracleSelectRestriction {

        private static final long serialVersionUID = 1L;

        private OracleConstraint  constraint;

        public CheckOption() {

        }

        public OracleConstraint getConstraint() {
            return this.constraint;
        }

        public void setConstraint(OracleConstraint constraint) {
            this.constraint = constraint;
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.constraint);
            }

            visitor.endVisit(this);
        }
    }

    public static class ReadOnly extends OracleSelectRestriction {

        private static final long serialVersionUID = 1L;

        public ReadOnly() {

        }

        public void accept0(OracleASTVisitor visitor) {
            visitor.visit(this);

            visitor.endVisit(this);
        }
    }
}
