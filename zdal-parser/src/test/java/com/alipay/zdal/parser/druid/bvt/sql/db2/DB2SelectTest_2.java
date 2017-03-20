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
package com.alipay.zdal.parser.druid.bvt.sql.db2;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Column;
import com.alipay.zdal.parser.visitor.ZdalDB2SchemaStatVisitor;

public class DB2SelectTest_2 {
    @Test
    public void test_0() throws Exception {
        String sql = "SELECT CTRYNUM, FMS_INSTANCE_CD FROM DBEFMSDR.FMSA_O_WW_CTRY_AG WHERE ACCT_YR=? WITH UR";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalDB2SchemaStatVisitor visitor = new ZdalDB2SchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("bindConditions :" + visitor.getBindVarConditions());
        System.out.println("rownum :" + visitor.getRownums());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(3, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getConditions().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("DBEFMSDR.FMSA_O_WW_CTRY_AG")));

        Assert.assertTrue(visitor.getColumns().contains(
            new Column("DBEFMSDR.FMSA_O_WW_CTRY_AG", "CTRYNUM")));
        // Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "first_name")));
        // Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "full_name")));

        //        String output = SQLUtils.toSQLString(stmt, JdbcConstants.DB2);
        //        Assert.assertEquals("SELECT CTRYNUM, FMS_INSTANCE_CD" //
        //                            + "\nFROM DBEFMSDR.FMSA_O_WW_CTRY_AG"//
        //                            + "\nWHERE ACCT_YR = ?"//
        //                            + "\nWITH UR", //
        //            output);
    }
}
