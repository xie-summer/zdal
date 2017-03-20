/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResultFactory;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTOutputVisitor;
import com.alipay.zdal.parser.visitor.ZdalDB2SchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * SQL 解析器的实现类，主要是将SQL解析后存放到cache中，
 * 如果cache中有该条SQL,则直接从cache中取，否则进行parse
 * 
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserImp.java, v 0.1 2012-5-22 上午10:01:18 xiaoqing.zhouxq Exp $
 */
public class DefaultSQLParser implements SQLParser {
    private static final Logger      LOG         = Logger.getLogger(DefaultSQLParser.class);

    private static final ParserCache GLOBALCACHE = ParserCache.instance();

    public SqlParserResult parse(String sql, DBType dbType) {
        this.parseSQL(sql, dbType);
        ZdalSchemaStatVisitor visitor = getStatement(sql);
        try {
            if (visitor == null) {
                // 如果没取到，尝试分析sql并初始化
                this.parseSQL(sql, dbType);
                visitor = getStatement(sql);

            }
        } catch (Exception e) {
            throw new SqlParserException("the sql = " + sql + " is not support yet "
                                         + e.getMessage());
        }
        return SqlParserResultFactory.createSqlParserResult(visitor, dbType);
    }

    /**
     * 尝试从cache中取该sql,如果未取到，则分析该sql并初始化。
     * 
     * 最坏情况是多次初始化，但因为key一致，同一条sql分析并初始化以后的结果是一致的
     * 
     * 但有可能因为乱序发生put在init之前的问题,因此整个加锁。
     * @param sql
     */
    public void parseSQL(String sql) {
        this.nestedParseSql(sql, DBType.MYSQL);
    }

    public void parseSQL(String sql, DBType dbType) {
        this.nestedParseSql(sql, dbType);
    }

    private void nestedParseSql(final String sql, final DBType dbType) {
        if (sql == null) {
            throw new SqlParserException("sql must not be null");
        }
        //为了防止多次重复初始化，所以使用了future task来确保初始化只进行一次
        FutureTask<ZdalSchemaStatVisitor> future = GLOBALCACHE.getFutureTask(sql);
        if (future == null) {
            Callable<ZdalSchemaStatVisitor> parserHandler = new Callable<ZdalSchemaStatVisitor>() {
                public ZdalSchemaStatVisitor call() throws Exception {
                    final List<SQLStatement> parserResults = getSqlStatements(sql, dbType);
                    if (parserResults == null || parserResults.isEmpty()) {
                        LOG.error("ERROR ## the sql parser result is null,the sql = " + sql);
                        return null;
                    }
                    if (parserResults.size() > 1) {
                        LOG
                            .warn("WARN ## after this sql parser,has more than one SQLStatement,the sql = "
                                  + sql);
                    }
                    SQLStatement statement = parserResults.get(0);
                    ZdalSchemaStatVisitor visitor = null;
                    if (dbType.isMysql()) {
                        visitor = new ZdalMySqlSchemaStatVisitor();
                        statement.accept(visitor);
                    } else if (dbType.isOracle()) {
                        visitor = new ZdalOracleSchemaStatVisitor();
                        statement.accept(visitor);
                    } else if (dbType.isDB2()) {
                        visitor = new ZdalDB2SchemaStatVisitor();
                        statement.accept(visitor);
                    } else {
                        throw new IllegalArgumentException("ERROR ## dbType = " + dbType
                                                           + " is not support");
                    }
                    return visitor;
                }
            };
            future = new FutureTask<ZdalSchemaStatVisitor>(parserHandler);
            future = GLOBALCACHE.setFutureTaskIfAbsent(sql, future);
            future.run();
        }
    }

    /**
     * 输出parse分析过后的sql语句.
     * @param parserResults
     * @return
     */
    public String outputParsedSql(List<SQLStatement> parserResults, boolean isMysql) {
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = null;
        if (isMysql) {
            visitor = new MySqlOutputVisitor(out);
        } else {
            visitor = new OracleOutputVisitor(out);
        }
        for (SQLStatement stmt : parserResults) {
            stmt.accept(visitor);
        }

        return out.toString();
    }

    /**
     * 通过parser模块分析sql语句,返回java对象表示的sql.
     * @param sql
     * @param isMysql
     * @return
     */
    private List<SQLStatement> getSqlStatements(final String sql, final DBType dbType) {
        if (dbType.isMysql()) {
            MySqlStatementParser parser = new MySqlStatementParser(sql);
            return parser.parseStatementList();
        } else if (dbType.isOracle()) {
            OracleStatementParser parser = new OracleStatementParser(sql);
            return parser.parseStatementList();
        } else if (dbType.isDB2()) {
            OracleStatementParser parser = new OracleStatementParser(sql);
            return parser.parseStatementList();
        } else {
            throw new IllegalArgumentException("ERROR ## dbType = " + dbType + " is not support");
        }
    }

    /**
     * 根据SQL获取对应的javaSQL对象
     * @param sql
     * @return java SQL 对象。 如果cache中没有则返回空
     */
    private ZdalSchemaStatVisitor getStatement(String sql) {
        try {
            FutureTask<ZdalSchemaStatVisitor> future = GLOBALCACHE.getFutureTask(sql);
            if (future == null) {
                return null;
            } else {
                return future.get();
            }
        } catch (Exception e) {
            throw new SqlParserException("ERROR ## get sqlparser result from cache has an error:",
                e);
        }
    }

}
