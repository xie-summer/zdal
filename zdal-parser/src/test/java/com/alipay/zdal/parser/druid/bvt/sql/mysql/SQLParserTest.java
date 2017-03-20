/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.MysqlTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserTest.java, v 0.1 2012-5-17 ÉÏÎç10:08:07 xiaoqing.zhouxq Exp $
 */
public class SQLParserTest extends MysqlTest {

    public void test_select() throws Exception {
        String sql = "   SELECT COUNT(*) FROM close_plan " + "WHERE 1=1          "
                     + "AND close_type = ?             " + "AND target_type = ?             "
                     + "AND target_id = ?         " + "AND(    mi_name=?   )               "
                     + "AND end_time >= ?         ";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(6, visitor.getColumns().size());
        Assert.assertEquals(5, visitor.getConditions().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("close_plan")));

        Assert.assertTrue(visitor.getColumns().contains(new Column("close_plan", "*")));
        Assert.assertTrue(visitor.getColumns().contains(
            new Column("close_plan", "close_type")));
        Assert.assertTrue(visitor.getColumns().contains(
            new Column("close_plan", "target_type")));
        Assert.assertTrue(visitor.getColumns().contains(
            new Column("close_plan", "target_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new Column("close_plan", "mi_name")));
        Assert.assertTrue(visitor.getColumns().contains(
            new Column("close_plan", "end_time")));
    }

}
