package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;


public class OracleBlockTest1   {
@Test
    public void test_0() throws Exception {
        String sql = "DECLARE   n        NUMBER;   str_stmt VARCHAR2(4000);   sql_text ora_name_list_t;   l_trace  NUMBER;   l_alert  NUMBER; BEGIN   n := ora_sql_txt(sql_text);   FOR i IN 1 .. n LOOP     str_stmt := SUBSTR(str_stmt || sql_text(i), 1, 300);   END LOOP;    SELECT COUNT(*)     INTO l_trace     FROM DUAL    WHERE (sys_context('userenv', 'ip_address') IS NOT NULL)      and lower(str_stmt) like 'alter% compile%';    IF l_trace > 0 THEN     RAISE_APPLICATION_ERROR(-20001,'Please try later,DBA is publishing DDL for project');   END IF; END;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);


        Assert.assertEquals(0, visitor.getTables().size());

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("departments")));
        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        //        Assert.assertEquals(0, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("UNKNOWN", "location_id")));
    }
}
