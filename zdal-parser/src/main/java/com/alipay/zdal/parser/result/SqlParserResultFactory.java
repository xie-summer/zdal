/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.result;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.visitor.ZdalDB2SchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * 创建sqlparserresult的工程类.
 * @author xiaoqing.zhouxq
 * @version $Id: SqlParserResultFactory.java, v 0.1 2012-5-21 下午03:18:34 xiaoqing.zhouxq Exp $
 */
public class SqlParserResultFactory {

    public static SqlParserResult createSqlParserResult(ZdalSchemaStatVisitor visitor, DBType dbType) {
        if (dbType.isMysql()) {
            if (!(visitor instanceof ZdalMySqlSchemaStatVisitor)) {
                throw new SqlParserException(
                    "ERROR ## the visitor is not ZdalMySqlSchemaStatVisitor");
            }
            return new MysqlSqlParserResult(visitor);
        } else if (dbType.isOracle()) {
            if (!(visitor instanceof ZdalOracleSchemaStatVisitor)) {
                throw new SqlParserException(
                    "ERROR ## the visitor is not ZdalOracleSchemaStatVisitor");
            }
            return new OracleSqlParserResult(visitor);
        } else if (dbType.isDB2()) {
            if (!(visitor instanceof ZdalDB2SchemaStatVisitor)) {
                throw new SqlParserException("ERROR ## the visitor is not ZdalDB2SchemaStatVisitor");
            }
            return new DB2SqlParserResult(visitor);
        } else {
            throw new IllegalArgumentException("ERROR ## dbType = " + dbType + " is not support");
        }
    }

}
