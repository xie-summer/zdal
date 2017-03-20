/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.rule.ruleengine.entities.abstractentities.SharedElement;
import com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.TableMapProvider;
import com.alipay.zdal.rule.ruleengine.util.RuleUtils;

/**
 * 提供通过拼装的方式来生成SimpleTableMap的方式
 * 
 * 
 * 
 * 
 */
public class SimpleTableMapProvider implements TableMapProvider {

    public enum TYPE {
        NORMAL, CUSTOM
    }

    public static final String NORMAL_TAOBAO_TYPE             = "NORMAL";

    public static final String DEFAULT_PADDING                = "_";

    protected static final int DEFAULT_INT                    = -1;

    public static final int    DEFAULT_TABLES_NUM_FOR_EACH_DB = -1;

    private String             type                           = NORMAL_TAOBAO_TYPE;
    /**
     * table[padding]suffix
     * 默认的padding是_
     */
    private String             padding                        = DEFAULT_PADDING;
    /**
     * width 宽度
     */
    private int                width                          = DEFAULT_INT;
    /**
     * 分表标识因子。就是说最开始打头的是什么表，如果不指定则默认是逻辑表名
     */
    private String             tableFactor;
    /**
     * 逻辑表名
     */
    private String             logicTable;
    /**
     * 每次自增数
     */
    private int                step                           = 1;

    /**
     * 每个数据库的表的个数，如果指定了这项则每个表内的个数就为指定多个
     */
    private int                tablesNumberForEachDatabases   = DEFAULT_TABLES_NUM_FOR_EACH_DB;
    /**
     * database id
     */
    private String             parentID;
    /**
     * 每个数据库的表的个数有多少个
     * >= ?
     */
    private int                from                           = DEFAULT_INT;
    /**
     * <= ?
     */
    private int                to                             = DEFAULT_INT;

    protected boolean doesNotSetTablesNumberForEachDatabases() {
        return tablesNumberForEachDatabases == -1;
    }

    public int getFrom() {
        return from;
    }

    public String getPadding() {
        return padding;
    }

    public String getParentID() {
        return parentID;
    }

    public int getStep() {
        return step;
    }

    static public String getSuffixInit(int w, int i) {
        String suffix = null;
        if (w != DEFAULT_INT) {
            suffix = RuleUtils.placeHolder(w, i);
        } else {
            //如果不显式指定width，或指定为-1，则不补零，直接以数值为后缀				
            suffix = String.valueOf(i);
        }
        return suffix;
    }

    protected List<String> getSuffixList(int from, int to, int width, int step, String tableFactor,
                                         String padding) {
        if (from == DEFAULT_INT || to == DEFAULT_INT) {
            throw new IllegalArgumentException("from,to must be spec!");
        }
        int length = to - from + 1;
        List<String> tableList = new ArrayList<String>(length);
        StringBuilder sb = new StringBuilder();
        sb.append(tableFactor);
        sb.append(padding);

        for (int i = from; i <= to; i = i + step) {
            StringBuilder singleTableBuilder = new StringBuilder(sb.toString());

            String suffix = getSuffixInit(width, i);
            singleTableBuilder.append(suffix);
            tableList.add(singleTableBuilder.toString());

        }
        return tableList;
    }

    public String getTableFactor() {
        return tableFactor;
    }

    /** 
     * @see com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.TableMapProvider#getTablesMap()
     */
    public Map<String, SharedElement> getTablesMap() {
        TYPE typeEnum = TYPE.valueOf(type);
        //		switch (typeEnum) {
        //		case NORMAL:
        //			// 如果是正常的情况下，那么应该是表名因子+"_"+最长数字位数那样长的尾缀
        //			// 类似 tab_001~tab_100
        //			
        //			break;
        //		//custom的方式下，不需要添加默认的padding和width,直接退出case
        //		default:
        //			break;
        //		}
        makeRealTableNameTaobaoLike(typeEnum);
        List<String> tableNames;

        if (tableFactor == null && logicTable != null) {
            tableFactor = logicTable;
        }
        if (tableFactor == null) {
            throw new IllegalArgumentException("没有表名生成因子");
        }

        // 如果没有设置每个数据库表的个数，那么表示所有表都用统一的表名，类似(tab_0~tab_3)*16个数据库=64张表
        if (doesNotSetTablesNumberForEachDatabases()) {
            tableNames = getSuffixList(from, to, width, step, tableFactor, padding);
        } else {
            // 如果设置了每个数据库表的个数，那么表示所有表用不同的表名，类似(tab_0~tab63),分布在16个数据库上
            int multiple = 0;
            try {
                multiple = Integer.valueOf(parentID);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "使用simpleTableMapProvider并且指定了tablesNumberForEachDatabase参数，database的index值必须是个integer数字"
                            + "当前database的index是:" + parentID);
            }
            int start = tablesNumberForEachDatabases * multiple;
            // 因为尾缀的范围是到<=的数字，所以要-1.
            int end = start + tablesNumberForEachDatabases - 1;
            //设置当前database里面的表名
            tableNames = getSuffixList(start, end, width, step, tableFactor, padding);
        }
        List<Table> tables = null;
        tables = new ArrayList<Table>(tableNames.size());
        for (String tableName : tableNames) {
            Table tab = new Table();
            tab.setTableName(tableName);
            tables.add(tab);
        }
        Map<String, SharedElement> returnMap = RuleUtils
            .getSharedElemenetMapBySharedElementList(tables);
        return returnMap;
    }

    public int getTablesNumberForEachDatabases() {
        return tablesNumberForEachDatabases;
    }

    public int getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    /**
     * 如果是正常的情况下，那么应该是表名因子+"_"+最长数字位数那样长的尾缀，类似 tab_001~tab_100
     */
    protected void makeRealTableNameTaobaoLike(TYPE typeEnum) {
        if (typeEnum == TYPE.CUSTOM) {
            //do nothing
        } else {
            if (padding == null)
                padding = DEFAULT_PADDING;
            if (to != DEFAULT_INT && width == DEFAULT_INT) {
                width = String.valueOf(to).length();
            }
        }

    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setLogicTable(String logicTable) {
        this.logicTable = logicTable;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setTableFactor(String tableFactor) {
        this.tableFactor = tableFactor;
    }

    public void setTablesNumberForEachDatabases(int tablesNumberForEachDatabases) {
        this.tablesNumberForEachDatabases = tablesNumberForEachDatabases;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWidth(int width) {
        if (width > 8) {
            throw new IllegalArgumentException("占位符不能超过8位");
        }
        //表后缀占位符可以为0, 此时不补零
        if (width < 0) {
            throw new IllegalArgumentException("占位符不能为负值");
        }
        this.width = width;

    }

}
