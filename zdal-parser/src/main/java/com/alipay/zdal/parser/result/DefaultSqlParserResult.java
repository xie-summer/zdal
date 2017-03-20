/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.common.sqljep.function.ComparativeAND;
import com.alipay.zdal.common.sqljep.function.ComparativeOR;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.sql.parser.ParserException;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.sql.stat.TableStat.Mode;
import com.alipay.zdal.parser.sql.stat.TableStat.SELECTMODE;
import com.alipay.zdal.parser.sqlobjecttree.ComparativeMapChoicer;
import com.alipay.zdal.parser.visitor.BindVarCondition;
import com.alipay.zdal.parser.visitor.OrderByEle;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: AbstractSqlParserResult.java, v 0.1 2012-5-21 下午03:11:27 xiaoqing.zhouxq Exp $
 */
public abstract class DefaultSqlParserResult implements SqlParserResult, ComparativeMapChoicer {

    protected ZdalSchemaStatVisitor visitor;

    /**
     * 如果没有skip和max会返回此值
     */
    public final static int         DEFAULT_SKIP_MAX = -1000;

    protected String                tableName        = null;

    public DefaultSqlParserResult(ZdalSchemaStatVisitor visitor) {
        if (visitor == null) {
            throw new SqlParserException("ERROR the visitor is null ");
        }
        this.visitor = visitor;
    }

    public List<OrderByEle> getGroupByEles() {
        Set<Column> columns = visitor.getGroupByColumns();
        List<OrderByEle> results = Collections.emptyList();
        for (Column column : columns) {
            OrderByEle orderByEle = new OrderByEle(column.getTable(), column.getName());
            orderByEle.setAttributes(column.getAttributes());
            results.add(orderByEle);
        }
        return results;
    }

    public GroupFunctionType getGroupFuncType() {
        if (SELECTMODE.COUNT == visitor.getSelectMode()) {
            return GroupFunctionType.COUNT;
        } else if (SELECTMODE.MAX == visitor.getSelectMode()) {
            return GroupFunctionType.MAX;
        } else if (SELECTMODE.MIN == visitor.getSelectMode()) {
            return GroupFunctionType.MIN;
        } else if (SELECTMODE.SUM == visitor.getSelectMode()) {
            return GroupFunctionType.SUM;
        }
        return GroupFunctionType.NORMAL;
    }

    public boolean isDML() {
        return (visitor.getSqlMode() == Mode.Delete) || (visitor.getSqlMode() == Mode.Insert)
               || (visitor.getSqlMode() == Mode.Select) || (visitor.getSqlMode() == Mode.Update);
    }

    public List<OrderByEle> getOrderByEles() {
        List<Column> columns = visitor.getOrderByColumns();
        List<OrderByEle> results = new ArrayList<OrderByEle>();
        for (Column column : columns) {
            OrderByEle orderByEle = new OrderByEle(column.getTable(), column.getName());
            orderByEle.setAttributes(column.getAttributes());
            results.add(orderByEle);
        }
        return results;
    }

    /**
     * 获取表名
     * @return
     * @see com.alipay.zdal.parser.result.SqlParserResult#getTableName()
     */
    public String getTableName() {
        if (visitor.getTables() == null || visitor.getTables().isEmpty()) {
            throw new SqlParserException("ERROR ## the tableName is null");
        }
        if (StringUtil.isBlank(tableName)) {
            for (Entry<TableStat.Name, TableStat> entry : visitor.getTables().entrySet()) {
                String temp = entry.getKey().getName();
                if (tableName == null) {
                    if (temp != null) {
                        tableName = temp.toLowerCase();
                    }
                } else {
                    if (temp != null && !tableName.equals(entry.getKey().getName().toLowerCase())) {
                        throw new IllegalArgumentException("sql语句中的表名不同，请保证所有sql语句的表名"
                                                           + "以及他们的schemaName相同，包括内嵌sql");
                    }
                }
            }
        }
        return tableName;
    }

    /**
     * 获取ComparativeMap.
     * map的key 是列名 value是绑定变量后的{@link Comparative}
     * 如果是个不可赋值的变量，则不会返回。
     * 不可赋值指的是，虽然可以解析，但解析以后的结果不能进行计算。
     * 如where col = concat(str,str);
     * 这种SQL虽然可以解析，但因为对应的处理函数没有完成，所以是不能赋值的。这种情况下col
     * 是不会被放到返回的map中的。
     * @param arguments 参数值列表.
     * @param partnationSet 拆分字段列表.
     * @return
     */
    public Map<String, Comparative> getColumnsMap(List<Object> arguments, Set<String> partnationSet) {
        Map<String, Comparative> copiedMap = new LinkedHashMap<String, Comparative>();
        for (String partnation : partnationSet) {
            Comparative comparative = getComparativeOf(partnation, arguments);
            if (comparative != null) {
                copiedMap.put(partnation, comparative);
            }
        }
        return copiedMap;
    }

    /**
     * 根据拆分字段从sql的字段中获取对应的列名和列值.
     * @param partinationKey
     * @param arguments
     * @return
     */
    private Comparative getComparativeOf(String partinationKey, List<Object> arguments) {
        List<BindVarCondition> bindColumns = visitor.getBindVarConditions();

        List<BindVarCondition> conditions = new ArrayList<BindVarCondition>();
        for (BindVarCondition tmp : bindColumns) {
            if (tmp.getColumnName().equalsIgnoreCase(partinationKey)) {
                conditions.add(tmp);
            }
        }
        if (!conditions.isEmpty()) { //先从绑定参数列表中查找.
            Comparative comparative = null;
            int index = 1;
            for (BindVarCondition bindVarCondition : conditions) {
                String op = bindVarCondition.getOperator();
                int function = Comparative.getComparisonByIdent(op);
                if (function == Comparative.NotSupport || op.trim().equalsIgnoreCase("in")) {//支持拆分字段是in的模式.
                    Object arg = arguments.get(bindVarCondition.getIndex());
                    Comparable<?> value = null;
                    if (arg instanceof Comparable<?>) {
                        value = (Comparable<?>) arg;
                    } else {
                        throw new ParserException("ERROR ## can not use this type of partination");
                    }

                    if (comparative == null) {
                        comparative = new Comparative(Comparative.Equivalent, value);
                        if (index == conditions.size()) {
                            return comparative;
                        }
                    } else {
                        Comparative next = new Comparative(Comparative.Equivalent, value);
                        ComparativeOR comparativeOR = new ComparativeOR();
                        comparativeOR.addComparative(comparative);
                        comparativeOR.addComparative(next);
                        comparative = comparativeOR;
                        if (index == conditions.size()) {
                            return comparativeOR;
                        }
                    }
                    index++;
                }
            }

            index = -1;
            for (BindVarCondition condition : conditions) {
                Object arg = arguments.get(condition.getIndex());
                Comparable<?> value = null;
                if (arg instanceof Comparable<?>) {
                    value = (Comparable<?>) arg;
                } else {
                    throw new ParserException("ERROR ## can not use this type of partination");
                }
                int function = Comparative.getComparisonByIdent(condition.getOperator());

                if (comparative == null) {
                    comparative = new Comparative(function, value);
                    index = condition.getIndex();
                } else {
                    Comparative next = new Comparative(function, value);
                    if (index == condition.getIndex()) {//在子查询中，存在拆分字段的index相同的情况，如果相同就不需要and/or 进行匹配了.
                        return comparative;
                    }
                    if (condition.getOp() == 1) {
                        ComparativeAND comparativeAND = new ComparativeAND();
                        comparativeAND.addComparative(comparative);
                        comparativeAND.addComparative(next);
                        return comparativeAND;
                    } else if (condition.getOp() == -1) {
                        ComparativeOR comparativeOR = new ComparativeOR();
                        comparativeOR.addComparative(comparative);
                        comparativeOR.addComparative(next);
                        return comparativeOR;
                    }
                }
            }
            return comparative;
        } else {
            List<BindVarCondition> noBindConditions = visitor.getNoBindVarConditions();

            if (noBindConditions.isEmpty()) {
                return null;
            }
            List<BindVarCondition> noBinditions = new ArrayList<BindVarCondition>();
            for (BindVarCondition tmp : noBindConditions) {
                if (tmp.getColumnName().equalsIgnoreCase(partinationKey)) {
                    int function = Comparative.getComparisonByIdent(tmp.getOperator());
                    if (function == Comparative.NotSupport) {
                        if (tmp.getOperator().trim().equalsIgnoreCase("in")) {
                            noBinditions.add(tmp);
                        } else {
                            continue;
                        }
                    } else {
                        noBinditions.add(tmp);
                    }
                }
            }
            Comparative comparative = null;
            for (BindVarCondition condition : noBinditions) {
                Comparable<?> value = condition.getValue();
                if (value == null) {
                    throw new SqlParserException(
                        "ERROR ## parse from no-bind-column of this partination is error,the partination name = "
                                + partinationKey);
                }
                if (!(value instanceof Comparable<?>)) {
                    throw new ParserException("ERROR ## can not use this type of partination");
                }
                if (condition.getOperator().trim().equalsIgnoreCase("in")) {
                    if (comparative == null) {
                        comparative = new Comparative(Comparative.Equivalent, value);
                    } else {
                        Comparative next = new Comparative(Comparative.Equivalent, value);
                        ComparativeOR comparativeOR = new ComparativeOR();
                        comparativeOR.addComparative(comparative);
                        comparativeOR.addComparative(next);
                        comparative = comparativeOR;
                    }
                } else {
                    int function = Comparative.getComparisonByIdent(condition.getOperator());

                    if (comparative == null) {
                        comparative = new Comparative(function, value);
                    } else {
                        Comparative next = new Comparative(function, value);
                        if (condition.getOp() == 1) {
                            ComparativeAND comparativeAND = new ComparativeAND();
                            comparativeAND.addComparative(comparative);
                            comparativeAND.addComparative(next);
                            return comparativeAND;
                        } else if (condition.getOp() == -1) {
                            ComparativeOR comparativeOR = new ComparativeOR();
                            comparativeOR.addComparative(comparative);
                            comparativeOR.addComparative(next);
                            return comparativeOR;
                        }
                    }
                }
            }
            return comparative;
        }
    }

    /**
     * @param tables
     * @param args
     * @param skip
     *            闭区间，从哪开始
     * @param max
     *            开区间，至哪
     * @return
     */
    public void getSqlReadyToRun(Set<String> tables, List<Object> args, Number skip, Number max,
                                 Map<Integer, Object> modifiedMap) {
        if (tables == null) {
            throw new IllegalArgumentException("待替换表名为空");
        }

        //如果是skip 和 max 都存在，并且是绑定变量的情况，则进行参数的替换
        if (this.isSkipBind() < 0 && this.isRowCountBind() < 0) {
            throw new IllegalArgumentException("The limit skip or rowCount set error!");
        }
        modifyParam(skip, isSkipBind(), modifiedMap);
        modifyParam(max, isRowCountBind(), modifiedMap);
    }

    protected void modifyParam(Number num, int index, Map<Integer, Object> changeParam) {

        Object obj = null;
        if (num instanceof Long) {
            obj = (Long) num;
        } else if (num instanceof Integer) {
            obj = (Integer) num;
        } else {
            throw new IllegalArgumentException("只支持int long的情况");
        }
        changeParam.put(index, obj);
    }

    protected String toColumns(Set<Column> columns) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (Column column : columns) {
            result.append(column);
            if (i != (columns.size() - 1)) {
                result.append(",");
            }
        }
        return result.toString();
    }

    public ComparativeMapChoicer getComparativeMapChoicer() {
        return this;
    }

}
