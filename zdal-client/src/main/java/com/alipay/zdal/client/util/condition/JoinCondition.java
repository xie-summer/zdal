/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.alipay.zdal.client.util.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 增加RouteCondition方式下，对join的简单支持，这里只是添加了被join的虚拟表名，在对sql进行表名替换的时候用到
 *
 * @version $Id: JoinCondition.java, v 0.1 2010-1-29 下午02:02:01 jiangping Exp $
 */
public class JoinCondition extends AdvanceCondition {
    List<String> virtualJoinTableNames = new ArrayList<String>();

    /**
     * @return Returns the virtualJoinTableNames.
     */
    public List<String> getVirtualJoinTableNames() {
        return virtualJoinTableNames;
    }

    /**
     * @param virtualJoinTableNames The virtualJoinTableNames to set.
     */
    public void setVirtualJoinTableNames(List<String> virtualJoinTableNames) {
        this.virtualJoinTableNames = virtualJoinTableNames;
    }

    /**
     * 添加被join的虚拟表名
     * @param virtualTableName
     */
    public void addVirtualJoinTableName(String virtualTableName) {
        if (virtualTableName == null) {
            throw new IllegalArgumentException("请输入逻辑表名");
        }
        this.virtualJoinTableNames.add(virtualTableName.toLowerCase());
    }
}
