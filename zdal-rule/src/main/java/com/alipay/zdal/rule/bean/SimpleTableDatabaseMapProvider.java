/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 支持和Database下标一样的表命名方式,每个Database只有一张表。
 * 比如: database01 -> trade_base_01
 *      database02 -> trade_base_02
 * 		database03 -> trade_base_03
 * 
 * @author liang.chenl
 *
 */
public class SimpleTableDatabaseMapProvider extends SimpleTableMapProvider {

    @Override
    protected List<String> getSuffixList(int from, int to, int width, int step, String tableFactor,
                                         String padding) {

        List<String> tableList = new ArrayList<String>(1);
        StringBuilder sb = new StringBuilder();
        sb.append(tableFactor);
        sb.append(padding);

        int multiple = 0;
        try {
            multiple = Integer.valueOf(getParentID());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "使用SimpleTableDatabaseMapProvider，database的index值必须是个integer数字"
                        + "当前database的index是:" + getParentID());
        }
        String suffix = getSuffixInit(width, multiple);
        sb.append(suffix);
        tableList.add(sb.toString());
        return tableList;

    }
}
