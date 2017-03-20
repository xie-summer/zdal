package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleInsertTest8   {
@Test
    public void test_0() throws Exception {
        String sql = "insert into AV_INFO_NEW (ID, GMT_CREATE, GMT_MODIFIED, COMPANY_ID, COMPANY_NAME_CN, COMPANY_NAME_EN, COMPANY_COUNTRY, "
                     + " COMPANY_PROVINCE, COMPANY_CITY, COMPANY_ADDR_CN, COMPANY_ADDR_EN, MEMBER_SEX, MEMBER_CN_NAME, MEMBER_FIRST_NAME, MEMBER_LAST_NAME,"
                     + "  MEMBER_PHONE_COUNTRY, MEMBER_PHONE_AREA, MEMBER_PHONE_NUMBER, MEMBER_JOB_TITLE_CN, MEMBER_JOB_TITLE_EN, MEMBER_DEPT_EN, "
                     + " MEMBER_DEPT_CN, LINK_EMAIL, STATUS, AV_PROVIDER, AV_ORIGIN)"
                     + " values (1000236058, sysdate, sysdate, 1300904670, 'dfsdf''TW'"
                     + ", 'Yunnan', 'sadf', '4r7V', 'fdgtg', 'M', 'dfsdnfo_name4', 'Fnameinfo_name4'"
                     + ", '33', '4444', '6666', 'dfdsfgsgsdfg', 'fggtgth', 'dsfsdfdd', null, 'zeus'"
                     + ")";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(26, visitor.getColumns().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("AV_INFO_NEW")));
        // Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        //
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "employee_id")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "commission_pct")));
    }

}
