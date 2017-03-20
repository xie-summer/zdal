/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.visitor;

import com.alipay.zdal.parser.sql.ast.SQLObject;

/**
 * 在sql中绑定了多少参数，按照顺序保存.
 * @author xiaoqing.zhouxq
 * @version $Id: BindVarCondition.java, v 0.1 2012-5-25 上午09:47:50 xiaoqing.zhouxq Exp $
 */
public class BindVarCondition {

    private String        tableName;

    private String        columnName;

    private String        operator;

    /** 这个参数在sql语句中出现的位置 */
    private int           index = -1;

    /** 0:one ;1:and; 2:or */
    private int           op    = 0;

    private SQLObject     parent;

    private Comparable<?> value;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Comparable<?> getValue() {
        return value;
    }

    public void setValue(Comparable<?> value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.tableName + "." + this.columnName + " " + this.operator + " " + this.value
               + " " + this.index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
        result = prime * result + index;
        result = prime * result + op;
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BindVarCondition other = (BindVarCondition) obj;
        if (columnName == null) {
            if (other.columnName != null)
                return false;
        } else if (!columnName.equals(other.columnName))
            return false;
        if (index != other.index)
            return false;
        if (op != other.op)
            return false;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (tableName == null) {
            if (other.tableName != null)
                return false;
        } else if (!tableName.equals(other.tableName))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public SQLObject getParent() {
        return parent;
    }

    public void setParent(SQLObject parent) {
        this.parent = parent;
    }

}
