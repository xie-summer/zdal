/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持2列同时分库分表
 * 比如: 根据userid 后2位 % 100 ,gmtcreate取月份 % 12
 * 生成后的表名: xxxx_01_01 ~~ xxxx_99_11
 * @author liang.chenl
 *
 */
public class SimpleTableTwoColumnsMapProvider extends SimpleTableMapProvider {
    int    from2  = 0;
    int    to2    = 11;
    int    step2  = 1;
    String padding2;
    String tableFactor2;
    int    width2 = 2;

    @Override
    protected List<String> getSuffixList(int from, int to, int width, int step, String tableFactor,
                                         String padding) {

        if (padding2 == null) {
            padding2 = padding;
        }
        if (tableFactor2 == null) {
            tableFactor2 = tableFactor;
        }

        if (from == DEFAULT_INT || to == DEFAULT_INT) {
            throw new IllegalArgumentException("from,to must be spec!");
        }
        int length = (to - from + 1) * (to2 - from2 + 1);
        List<String> tableList = new ArrayList<String>(length);
        StringBuilder sb = new StringBuilder();
        sb.append(tableFactor);
        sb.append(padding);

        for (int i = from; i <= to; i = i + step) {
            String suffix = getSuffixInit(width, i);
            for (int j = from2; j <= to2; j = j + step2) {
                StringBuilder singleTableBuilder = new StringBuilder(sb.toString());
                String suffix2 = getSuffixInit(width2, j);
                singleTableBuilder.append(suffix).append(padding2).append(suffix2);
                tableList.add(singleTableBuilder.toString());
            }
        }
        return tableList;
    }

    public int getFrom2() {
        return from2;
    }

    public void setFrom2(int from2) {
        this.from2 = from2;
    }

    public int getTo2() {
        return to2;
    }

    public void setTo2(int to2) {
        this.to2 = to2;
    }

    public int getStep2() {
        return step2;
    }

    public void setStep2(int step2) {
        this.step2 = step2;
    }

    public String getPadding2() {
        return padding2;
    }

    public void setPadding2(String padding2) {
        this.padding2 = padding2;
    }

    public String getTableFactor2() {
        return tableFactor2;
    }

    public void setTableFactor2(String tableFactor2) {
        this.tableFactor2 = tableFactor2;
    }

    public int getWidth2() {
        return width2;
    }

    public void setWidth2(int width2) {
        this.width2 = width2;
    }

}
