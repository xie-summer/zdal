/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 伯牙
 * @version $Id: TableStat.java, v 0.1 2012-11-17 下午3:55:16 Exp $
 */
public class TableStat {

    int selectCount      = 0;
    int updateCount      = 0;
    int deleteCount      = 0;
    int insertCount      = 0;
    int dropCount        = 0;
    int mergeCount       = 0;
    int createCount      = 0;
    int alterCount       = 0;
    int createIndexCount = 0;

    public int getCreateIndexCount() {
        return createIndexCount;
    }

    public void incrementCreateIndexCount() {
        createIndexCount++;
    }

    public int getAlterCount() {
        return alterCount;
    }

    public void incrementAlterCount() {
        this.alterCount++;
    }

    public int getCreateCount() {
        return createCount;
    }

    public void incrementCreateCount() {
        this.createCount++;
    }

    public int getMergeCount() {
        return mergeCount;
    }

    public void incrementMergeCount() {
        this.mergeCount++;
    }

    public int getDropCount() {
        return dropCount;
    }

    public void incrementDropCount() {
        dropCount++;
    }

    public void setDropCount(int dropCount) {
        this.dropCount = dropCount;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void incrementSelectCount() {
        selectCount++;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void incrementUpdateCount() {
        updateCount++;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void incrementDeleteCount() {
        this.deleteCount++;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public void incrementInsertCount() {
        this.insertCount++;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(int insertCount) {
        this.insertCount = insertCount;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(4);
        if (mergeCount > 0) {
            buf.append("Merge");
        }
        if (insertCount > 0) {
            buf.append("Insert");
        }
        if (updateCount > 0) {
            buf.append("Update");
        }
        if (selectCount > 0) {
            buf.append("Select");
        }
        if (deleteCount > 0) {
            buf.append("Delete");
        }
        if (dropCount > 0) {
            buf.append("Drop");
        }
        if (createCount > 0) {
            buf.append("Create");
        }
        if (alterCount > 0) {
            buf.append("Alter");
        }
        if (createIndexCount > 0) {
            buf.append("CreateIndex");
        }

        return buf.toString();
    }

    public static class Name {

        private String name;

        public Name(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int hashCode() {
            return this.name.toLowerCase().hashCode();
        }

        public boolean equals(Object o) {
            if (!(o instanceof Name)) {
                return false;
            }

            Name other = (Name) o;

            return this.name.equalsIgnoreCase(other.name);
        }

        public String toString() {
            return this.name;
        }
    }

    public static class Relationship {

        private Column left;
        private Column right;
        private String operator;

        public Column getLeft() {
            return left;
        }

        public void setLeft(Column left) {
            this.left = left;
        }

        public Column getRight() {
            return right;
        }

        public void setRight(Column right) {
            this.right = right;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((left == null) ? 0 : left.hashCode());
            result = prime * result + ((operator == null) ? 0 : operator.hashCode());
            result = prime * result + ((right == null) ? 0 : right.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Relationship other = (Relationship) obj;
            if (left == null) {
                if (other.left != null) {
                    return false;
                }
            } else if (!left.equals(other.left)) {
                return false;
            }
            if (operator == null) {
                if (other.operator != null) {
                    return false;
                }
            } else if (!operator.equals(other.operator)) {
                return false;
            }
            if (right == null) {
                if (other.right != null) {
                    return false;
                }
            } else if (!right.equals(other.right)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return left + " " + operator + " " + right;
        }

    }

    public static class Condition {

        private Column       column;
        private String       operator;

        private List<Object> values = new ArrayList<Object>();

        public Column getColumn() {
            return column;
        }

        public void setColumn(Column column) {
            this.column = column;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public List<Object> getValues() {
            return values;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((column == null) ? 0 : column.hashCode());
            result = prime * result + ((operator == null) ? 0 : operator.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Condition other = (Condition) obj;
            if (column == null) {
                if (other.column != null) {
                    return false;
                }
            } else if (!column.equals(other.column)) {
                return false;
            }
            if (operator == null) {
                if (other.operator != null) {
                    return false;
                }
            } else if (!operator.equals(other.operator)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.column.toString() + " " + this.operator;
        }
    }

    public static class Column {

        private String              table;
        private String              name;

        private Map<String, Object> attributes = new HashMap<String, Object>();

        public Column() {

        }

        public Column(String table, String name) {
            this.table = table;
            this.name = name;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public int hashCode() {
            int tableHashCode = table != null ? table.toLowerCase().hashCode() : 0;
            int nameHashCode = name != null ? name.toLowerCase().hashCode() : 0;

            return tableHashCode + nameHashCode;
        }

        public String toString() {
            if (table != null) {
                return table + "." + name;
            }

            return name;
        }

        public boolean equals(Object obj) {
            Column column = (Column) obj;

            if (table == null) {
                if (column.getTable() != null) {
                    return false;
                }
            } else {
                if (!table.equalsIgnoreCase(column.getTable())) {
                    return false;
                }
            }

            if (name == null) {
                if (column.getName() != null) {
                    return false;
                }
            } else {
                if (!name.equalsIgnoreCase(column.getName())) {
                    return false;
                }
            }

            return true;
        }
    }

    public static enum Mode {
        Insert(1), Update(2), Delete(4), Select(8), Merge(16), Truncate(32);

        public final int mark;

        private Mode(int mark) {
            this.mark = mark;
        }
    }

    public static enum SELECTMODE {
        COUNT, MIN, MAX, SUM, ROWNUMBER;
    }
}
