package com.alipay.zdal.test.sqlparser;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw数据源:table.cloum,in(),betweeen and,<>,not in (?,?)")
public class SR956010 {
    String               url1   = ConstantsTest.mysql12UrlZds1;
    String               psd    = ConstantsTest.mysq112Psd;
    String               user   = ConstantsTest.mysq112User;

    public TestAssertion Assert = new TestAssertion();
    private SqlMapClient sqlMap;
    private List<Object> rs     = null;

    @Before
    public void beforeTestcase() {
        // 数据准备
        prepareData();

        sqlMap = (SqlMapClient) ZdalSqlParserSuite.context.getBean("zdalsqlParserMysql01");
    }

    @After
    public void afterTestcase() {
        // 数据清除
        deleteData();
    }

    @Subject("执行:select colu2 from test1 where test1.clum = #clum#")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956011() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql01Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Assert.areEqual(1, rs.size(), "select colu2 from table whre table.id 的断言");
    }

    @Subject("执行:select colu2 from test1 where test1.clum in (#clum1#,#clum2#)")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956012() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        params.put("clum2", 200);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql02Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Assert.areEqual(2, rs.size(),
            "select colu2 from test1 where test1.clum in (#clum1#,#clum2#) 的断言");
    }

    @Subject("执行:select colu2 from table whre table.id between ? and ?")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956013() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        params.put("clum2", 200);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql03Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Assert.areEqual(2, rs.size(), "select colu2 from table whre table.id between ? and ?的断言");
    }

    @Subject("执行:select colu2 from table whre table.id <> ?")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956014() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql04Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Assert.areEqual(1, rs.size(), "select colu2 from table whre table.id <> ?的断言");
    }

    @Subject("执行:select colu2 from test1 where test1.clum not in (#clum1#)")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956015() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql05Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Assert.areEqual(1, rs.size(),
            "select colu2 from test1 where test1.clum not in (#clum1#)的断言");
    }

    @Subject("执行:select count(*) from test1 where test1.clum = #clum1#")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956016() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql06Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("1", hs.get("count").toString(),
            "select count(*) from test1 where test1.clum = #clum1#的断言");

    }

    @Subject("执行:select count(1) from test1 where test1.clum = #clum1#")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956017() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql07Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("1", hs.get("count").toString(),
            "select count(1) from test1 where test1.clum = #clum1#的断言");

    }

    @Subject("执行:select count(1) as count from test1 where (test1.colu2 is not null) and test1.clum = #clum1#")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956018() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql08Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual("1", hs.get("count").toString(),
                "select count(1) as count from test1 where (test1.colu2 is not null) and test1.clum = #clum1#");

    }

    @Subject("执行:select count(1) as count from test1 where (test1.colu2 in('DB_A','DB_B')) and test1.clum = #clum1#")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC956019() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql09Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual(
                "1",
                hs.get("count").toString(),
                "select count(1) as count from test1 where (test1.colu2 in('DB_A','DB_B')) and test1.clum = #clum1#");
    }

    @Subject("执行:select count(1) as count from test1 where (test1.colu2 in('DB_A','DB_B')) and test1.clum = #clum1# order by test1.clum")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601a() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql10Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual(
                "1",
                hs.get("count").toString(),
                "select count(1) as count from test1 where (test1.colu2 in('DB_A','DB_B')) and test1.clum = #clum1# order by test1.clum");
    }

    @Subject("执行:select count(1) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.clum = #clum1# ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601b() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql11Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual(
                "1",
                hs.get("count").toString(),
                "select count(1) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.clum = #clum1# ");
    }

    @Subject("执行:select distinct(colu2) as colu2 from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.clum = #clum1# ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601c() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql12Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual(
                "DB_A",
                hs.get("colu2").toString(),
                "select distinct(colu2) as colu2 from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.clum = #clum1# ");
    }

    @Subject("执行:select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.colu2 like '%DB%' ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601d() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clum1", 100);
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql13Sql", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual(
                "2",
                hs.get("count").toString(),
                "select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and test1.colu2 like '%DB%' ");
    }

    @Subject("执行:select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) limit 0,1 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601e() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql14Sql");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual("2", hs.get("count").toString(),
                "select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) limit 0,1 ");
    }

    @Subject("执行:select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and 1=1 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601f() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql15Sql");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert
            .areEqual("2", hs.get("count").toString(),
                "select count(*) as count from test1 where (test1.colu2 not in('DB_D','DB_E')) and 1=1 ");
    }

    @Subject("执行:select count(*) as count from test1 where test1.clum > 101 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601g() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql16Sql");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("1", hs.get("count").toString(),
            "select count(*) as count from test1 where test1.clum > 101 ");
    }

    @Subject("执行:select count(*) as count from test1 where test1.clum <= 199 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601h() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql17Sql");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("1", hs.get("count").toString(),
            "select count(*) as count from test1 where test1.clum <= 199 ");
    }

    @Subject("执行:select sum(clum) as sumvalue from test1 where test1.clum <= 201 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601j() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql18Sql");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("300", hs.get("sumvalue").toString(),
            "select sum(clum) as sumvalue from test1 where test1.clum <= 201");
    }

    @Subject("执行:select min(clum) as minvalue from test1 where test1.clum <= 201 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601k() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql19Sql");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("100", hs.get("minvalue").toString(),
            "select min(clum) as minvalue from test1 where test1.clum <= 201");

    }

    @Subject("执行:select max(clum) as maxvalue from test1 where test1.clum <= 201 ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC95601l() {
        try {
            rs = (List<Object>) sqlMap.queryForList("zdalsqlParserMysql20Sql");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap hs = (HashMap) rs.get(0);
        Assert.areEqual("200", hs.get("maxvalue").toString(),
            "select max(clum) as maxvalue from test1 where test1.clum <= 201");

    }

    /**
     * 数据准备
     */
    private void prepareData() {
        String insertSql1 = "insert into test1(clum,colu2) values (100,'DB_A')";
        String insertSql2 = "insert into test1(clum,colu2) values (200,'DB_B')";
        ZdalTestCommon.dataUpdateJDBC(insertSql1, url1, psd, user);
        ZdalTestCommon.dataUpdateJDBC(insertSql2, url1, psd, user);
    }

    /**
     * 删除数据
     */
    private void deleteData() {
        String deleteSql = "delete from test1";
        ZdalTestCommon.dataUpdateJDBC(deleteSql, url1, psd, user);
    }

}
