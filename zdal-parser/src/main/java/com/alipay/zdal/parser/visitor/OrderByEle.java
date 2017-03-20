/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.parser.sql.ast.SQLOrderingSpecification;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OrderByEle.java, v 0.1 2012-5-25 下午02:59:43 xiaoqing.zhouxq Exp $
 */
public class OrderByEle {
    private String              table;
    private String              name;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public OrderByEle() {

    }

    public OrderByEle(String table, String name) {
        this.table = table;
        this.name = name;
    }

    /**
     * 返回某列是升序还是倒序，如果不存在orderBy.type的值，就说明是ASC的排序方式.
     * @return
     */
    public boolean isASC() {
        if (attributes.get("orderBy.type") == null) {
            return true;
        } else {
            SQLOrderingSpecification orderType = (SQLOrderingSpecification) (attributes
                .get("orderBy.type"));
            if (orderType == SQLOrderingSpecification.ASC) {
                return true;
            }
            return false;
        }
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
        OrderByEle column = (OrderByEle) obj;

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

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void appendSQL(StringBuilder sb) {
        if (table != null) {
            sb.append(table).append(".");
        }
        sb.append(name);
        if (this.isASC()) {
            sb.append(" ASC ");
        } else {
            sb.append(" DESC ");
        }
    }

    public StringBuilder regTableModifiable(String oraTabName, List<Object> list, StringBuilder sb) {
        if (table != null) {
            if (table.equalsIgnoreCase(oraTabName)) {
                if (sb != null) {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                    sb.append(".");
                } else {
                    throw new IllegalStateException("wrong state");
                }
            } else {
                sb.append(table).append(".");
            }
        }
        sb.append(name);
        if (this.isASC()) {
            sb.append(" ASC ");
        } else {
            sb.append(" DESC ");
        }
        return sb;
    }
}
