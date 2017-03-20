/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.result;

import java.util.List;
import java.util.Set;

import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.visitor.BindVarCondition;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSqlParserResult.java, v 0.1 2012-5-21 下午03:12:36 xiaoqing.zhouxq Exp $
 */
public class OracleSqlParserResult extends DefaultSqlParserResult {

    public OracleSqlParserResult(ZdalSchemaStatVisitor visitor) {
        super(visitor);
    }

    /**
     * 判断max是否是绑定变量.
     * @return
     */
    public int isRowCountBind() {
        ZdalOracleSchemaStatVisitor mysqlVisitor = (ZdalOracleSchemaStatVisitor) visitor;
        Set<BindVarCondition> rowNums = mysqlVisitor.getRownums();
        if (rowNums == null || rowNums.isEmpty()) {
            return -1;
        }
        for (BindVarCondition rowNum : rowNums) {
            if (ZdalSchemaStatVisitor.ROWCOUNT.equalsIgnoreCase(rowNum.getColumnName())) {
                if (rowNum.getValue() == null) {
                    return rowNum.getIndex();
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    public int getMax(List<Object> arguments) {
        ZdalOracleSchemaStatVisitor oracleVisitor = (ZdalOracleSchemaStatVisitor) visitor;
        Set<BindVarCondition> rowNums = oracleVisitor.getRownums();
        if (rowNums == null || rowNums.isEmpty()) {
            return DEFAULT_SKIP_MAX;
        }
        int result = DEFAULT_SKIP_MAX;
        //如果一条sql语句中包含多组rownum条件，可能会有问题.
        for (BindVarCondition rowNum : rowNums) {
            if (ZdalSchemaStatVisitor.ROWCOUNT.equalsIgnoreCase(rowNum.getColumnName())) {
                //如果是绑定参数，就从参数列表中获取rowcount的值.
                if (rowNum.getValue() == null) {
                    Object obj = arguments.get(rowNum.getIndex());
                    if (obj instanceof Long) {
                        throw new SqlParserException("ERROR ## row selecter can't handle long data");
                    } else if (obj instanceof Integer) {
                        int tmp = ((Integer) obj).intValue();
                        if (rowNum.getOperator().equals(SQLBinaryOperator.LessThan.name)) {//如果是小于，就需要减一
                            tmp = tmp - 1;
                        }
                        if (tmp > result) {
                            result = tmp;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## bind rowcount var has an error , "
                                                     + obj + " is not a int value");
                    }
                } else {//从sql语句中获取rowcount的值.
                    Comparable<?> tmp = rowNum.getValue();
                    if (tmp instanceof Number) {
                        int rowcount = ((Integer) tmp).intValue();
                        if (rowNum.getOperator().equals(SQLBinaryOperator.LessThan.name)) {//如果是小于，就需要减一
                            rowcount = rowcount - 1;
                        }
                        if (rowcount > result) {
                            result = rowcount;
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

    public int isSkipBind() {
        ZdalOracleSchemaStatVisitor oracleVisitor = (ZdalOracleSchemaStatVisitor) visitor;
        Set<BindVarCondition> rowNums = oracleVisitor.getRownums();
        if (rowNums == null || rowNums.isEmpty()) {
            return -1;
        }
        for (BindVarCondition rowNum : rowNums) {
            if (ZdalSchemaStatVisitor.OFFSET.equalsIgnoreCase(rowNum.getColumnName())) {
                if (rowNum.getValue() == null) {
                    return rowNum.getIndex();
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    public int getSkip(List<Object> arguments) {
        ZdalOracleSchemaStatVisitor oracleVisitor = (ZdalOracleSchemaStatVisitor) visitor;
        Set<BindVarCondition> rowNums = oracleVisitor.getRownums();
        if (rowNums == null || rowNums.isEmpty()) {
            return DEFAULT_SKIP_MAX;
        }
        int result = DEFAULT_SKIP_MAX;
        //如果一条sql语句中包含多组offset条件，可能会有问题.
        for (BindVarCondition rowNum : rowNums) {
            if (ZdalSchemaStatVisitor.OFFSET.equals(rowNum.getColumnName())) {
                //如果是绑定参数，就从参数列表中获取offset的值.
                if (rowNum.getValue() == null) {
                    Object obj = arguments.get(rowNum.getIndex());
                    if (obj instanceof Long) {
                        throw new SqlParserException("ERROR ## row selecter can't handle long data");
                    } else if (obj instanceof Integer) {
                        int tmp = ((Integer) obj).intValue();
                        if (rowNum.getOperator().equals(SQLBinaryOperator.GreaterThanOrEqual.name)) {//如果是大于等于，就需要减一
                            tmp = tmp - 1;
                        }
                        if (tmp > result) {
                            result = tmp;
                        }
                    } else {
                        throw new SqlParserException("ERROR ## bind offset var has an error , "
                                                     + obj + " is not a int value");
                    }
                } else {//从sql语句中获取rowcount的值.
                    Comparable<?> tmp = rowNum.getValue();
                    if (tmp instanceof Number) {
                        int offset = ((Integer) tmp).intValue();
                        if (rowNum.getOperator().equals(SQLBinaryOperator.GreaterThanOrEqual.name)) {//如果是大于等于，就需要减一
                            offset = offset - 1;
                        }
                        if (offset > result) {
                            result = offset;
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
}
