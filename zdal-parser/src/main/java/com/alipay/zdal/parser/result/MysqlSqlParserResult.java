/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.result;

import java.util.List;
import java.util.Set;

import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.visitor.BindVarCondition;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MysqlSqlParserResult.java, v 0.1 2012-5-21 下午03:12:20 xiaoqing.zhouxq Exp $
 */
public class MysqlSqlParserResult extends DefaultSqlParserResult {

    public MysqlSqlParserResult(ZdalSchemaStatVisitor visitor) {
        super(visitor);
    }

    /**
     * 判断max是否是绑定变量.
     * @return
     */
    public int isRowCountBind() {
        ZdalMySqlSchemaStatVisitor mysqlVisitor = (ZdalMySqlSchemaStatVisitor) visitor;
        Set<BindVarCondition> limits = mysqlVisitor.getLimits();
        if (limits == null || limits.isEmpty()) {
            return -1;
        }
        for (BindVarCondition limit : limits) {
            if (ZdalSchemaStatVisitor.ROWCOUNT.equals(limit.getColumnName())) {
                if (limit.getValue() == null) {
                    return limit.getIndex();
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    public int getMax(List<Object> arguments) {
        int skip = getSkip(arguments);
        if (skip != DEFAULT_SKIP_MAX && skip < 0) {
            throw new SqlParserException("ERROR ## the skip is less than 0");
        }

        int rowCount = getRowCount(arguments);
        if (rowCount != DEFAULT_SKIP_MAX && rowCount < 0) {
            throw new SqlParserException("ERROR ## the rowcount is less than 0");
        }
        if (skip == DEFAULT_SKIP_MAX) {
            if (rowCount == DEFAULT_SKIP_MAX) {
                //如果skip和rowcount都不存在就返回默认值.
                return DEFAULT_SKIP_MAX;
            } else {
                //如果skip不存在，就返回rowcount.
                return rowCount;
            }
        } else {
            if (rowCount == DEFAULT_SKIP_MAX) {
                return skip;
            } else {
                return skip + rowCount;
            }
        }

    }

    /**
     * 判断skip是否是绑定变量的.
     * @return
     */
    public int isSkipBind() {
        ZdalMySqlSchemaStatVisitor mysqlVisitor = (ZdalMySqlSchemaStatVisitor) visitor;
        Set<BindVarCondition> limits = mysqlVisitor.getLimits();
        if (limits == null || limits.isEmpty()) {
            return -1;
        }
        for (BindVarCondition limit : limits) {
            if (ZdalSchemaStatVisitor.OFFSET.equals(limit.getColumnName())) {
                if (limit.getValue() == null) {
                    return limit.getIndex();
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    public int getSkip(List<Object> arguments) {
        ZdalMySqlSchemaStatVisitor mysqlVisitor = (ZdalMySqlSchemaStatVisitor) visitor;
        Set<BindVarCondition> limits = mysqlVisitor.getLimits();
        if (limits == null || limits.isEmpty()) {
            return DEFAULT_SKIP_MAX;
        }
        int result = DEFAULT_SKIP_MAX;
        //如果一条sql语句中包含多个limit条件，可能会有问题.
        for (BindVarCondition limit : limits) {
            if (ZdalSchemaStatVisitor.OFFSET.equals(limit.getColumnName())) {
                //如果是绑定参数，就从参数列表中获取offset的值.
                if (limit.getValue() == null) {
                    Object obj = arguments.get(limit.getIndex());
                    if (obj instanceof Long) {
                        throw new SqlParserException("ERROR ## row selecter can't handle long data");
                    } else if (obj instanceof Integer) {
                        int tmp = ((Integer) obj).intValue();
                        if (tmp > result) {
                            result = tmp;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## bind offset var has an error , "
                                                     + obj + " is not a int value");
                    }
                } else {
                    //从sql语句中获取offset的值.
                    Comparable<?> tmp = limit.getValue();
                    if (tmp instanceof Number) {
                        int skip = ((Integer) tmp).intValue();
                        if (skip > result) {
                            result = skip;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## get offset's value has an error,"
                                                     + tmp + " is not a int value");
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取mysql中的limit m,n中的n绑定参数值.
     * @param args
     * @return
     */
    private int getRowCount(List<Object> args) {
        ZdalMySqlSchemaStatVisitor mysqlVisitor = (ZdalMySqlSchemaStatVisitor) visitor;
        Set<BindVarCondition> limits = mysqlVisitor.getLimits();
        if (limits == null || limits.isEmpty()) {
            return DEFAULT_SKIP_MAX;
        }
        int result = DEFAULT_SKIP_MAX;
        //如果一条sql语句中包含多个limit条件，可能会有问题.
        for (BindVarCondition limit : limits) {
            if (ZdalSchemaStatVisitor.ROWCOUNT.equals(limit.getColumnName())) {
                //如果是绑定参数，就从参数列表中获取rowcount的值.
                if (limit.getValue() == null) {
                    Object obj = args.get(limit.getIndex());
                    if (obj instanceof Long) {
                        throw new SqlParserException("ERROR ## row selecter can't handle long data");
                    } else if (obj instanceof Integer) {
                        int tmp = ((Integer) obj).intValue();
                        if (tmp > result) {
                            result = tmp;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## bind rowcount var has an error , "
                                                     + obj + " is not a int value");
                    }
                } else {//从sql语句中获取rowcount的值.
                    Comparable<?> tmp = limit.getValue();
                    if (tmp instanceof Number) {
                        int skip = ((Integer) tmp).intValue();
                        if (skip > result) {
                            result = skip;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## get rowcount's value has an error,"
                                                     + tmp + " is not a int value");
                    }
                }
            }
        }
        return result;
    }

}
