/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: OracleAlterTablespaceAddDataFile.java, v 0.1 2012-11-17 ÏÂÎç3:45:37 Exp $
 */
public class OracleAlterTablespaceAddDataFile extends OracleSQLObjectImpl implements
                                                                         OracleAlterTablespaceItem {

    private static final long             serialVersionUID = 1L;

    private List<OracleFileSpecification> files            = new ArrayList<OracleFileSpecification>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, files);
        }
        visitor.endVisit(this);
    }

    public List<OracleFileSpecification> getFiles() {
        return files;
    }

    public void setFiles(List<OracleFileSpecification> files) {
        this.files = files;
    }

}
