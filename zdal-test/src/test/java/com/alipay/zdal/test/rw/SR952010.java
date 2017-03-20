package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Feature("rw mysql 读写分离")
public class SR952010 {
    public TestAssertion Assert = new TestAssertion();
    private SqlMapClient sqlMap;
    private String       url1;
    private String       url2;
    private String       psd;
    private String       user;

    @Before
    public void beforeTestCase() {
        url1 = ConstantsTest.mysql12UrlZds1;
        url2 = ConstantsTest.mysql12UrlZds2;
        psd = ConstantsTest.mysq112Psd;
        user = ConstantsTest.mysq112User;
    }

    @After
    public void afterTestCase() {

        ZdalTestCommon.dataDeleteForZds();
    }

    @Subject("rw 写库 ds0:r2w1,ds1:r1w2")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952011() {
        Step("rw 写库 ds0:r2w1,ds1:r1w2");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql1", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("断言数据取值个数");
        Assert.areEqual(true, 0 <= count1 && count1 <= 15, "the count1 value");
        Assert.areEqual(true, 10 <= count2 && count2 <= 30, "the count2 value");
    }

    @Subject("rw 读库 ds0:r2w1,ds1:r1w2")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952012() {
        Step("数据准备");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql1", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("断言数据取值个数");
        Assert.areEqual(true, 10 <= countA && countA <= 30, "the countA value:" + countA);
        Assert.areEqual(true, 0 <= countB && countB <= 15, "the countB value:" + countB);

    }

    @Subject("rw 写库 ds0:r1w0,ds1:r0w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952013() {
        Step("rw 写库 ds0:r1w0,ds1:r0w1");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql2", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("断言数据取值个数");
        Assert.areEqual(true, count1 == 0, "the count1 value");
        Assert.areEqual(true, count2 == 30, "the count2 value");

    }

    @Subject("rw 读库 ds0:r1w0,ds1:r0w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952014() {
        Step("rw 读库 ds0:r1w0,ds1:r0w1");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql2", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("断言取值个数");
        Assert.areEqual(true, countA == 30, "the countA value");
        Assert.areEqual(true, countB == 0, "the countB value");

    }

    @Subject("rw 写库:ds0:r1,ds1:w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952015() {
        Step("rw 写库:ds0:r1,ds1:w1");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql3", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("断言取值个数");
        Assert.areEqual(true, count1 == 0, "the count1 value");
        Assert.areEqual(true, count2 == 30, "the count2 value");
    }

    @Subject("rw 读库:ds0:r1,ds1:w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952016() {
        Step("rw 读库:ds0:r1,ds1:w1");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql3", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("断言取值个数");
        Assert.areEqual(true, countA == 30, "the countA value");
        Assert.areEqual(true, countB == 0, "the countB value");
    }

    @Subject("rw 写库:ds0:r1w1,ds1:r0w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952017() {
        Step("rw 写库:ds0:r1w1,ds1:r0w0");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql4", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("断言取值个数");
        Assert.areEqual(true, count1 == 30, "the count1 value");
        Assert.areEqual(true, count2 == 0, "the count2 value");
    }

    @Subject("rw 读库:ds0:r1w1,ds1:r0w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952018() {
        Step("rw 读库:ds0:r1w1,ds1:r0w0");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql4", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("断言获取数据个数");
        Assert.areEqual(true, countA == 30, "the countA value");
        Assert.areEqual(true, countB == 0, "the countB value");
    }

    @Subject("rw 写库:ds0:rw,ds1:r0w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952019() {
        Step("rw 写库:ds0:rw,ds1:r0w0");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql5", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("获取数据个数");
        Assert.areEqual(true, count1 == 30, "the count1 value");
        Assert.areEqual(true, count2 == 0, "the count2 value");

    }

    @Subject("rw 读库:ds0:rw,ds1:r0w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201a() {
        Step("rw 读库:ds0:rw,ds1:r0w0");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql5", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("获取数据个数");
        Assert.areEqual(true, countA == 30, "the countA value");
        Assert.areEqual(true, countB == 0, "the countB value");
    }

    @Subject("rw 写库:ds0:rw,ds1:r1w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201b() {
        Step("rw 写库:ds0:rw,ds1:r1w1");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql6", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("获取数据个数");
        Assert.areEqual(true, count1 > 20, "the count1 value:" + count1);
        Assert.areEqual(true, count2 < 10, "the count2 value:" + count2);

    }

    @Subject("rw 读库:ds0:rw,ds1:r1w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201c() {
        Step("rw 读库:ds0:rw,ds1:r1w1");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql6", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("获取数据个数");
        Assert.areEqual(true, countA > 20, "the countA value:" + countA);
        Assert.areEqual(true, countB < 10, "the countB value:" + countB);

    }

    @Subject("rw 写库:ds0:r1w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201d() {
        Step("rw 写库:ds0:r1w1");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql7", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("获取数据个数");
        Assert.areEqual(true, count1 == 30, "the count1 value");
        Assert.areEqual(true, count2 == 0, "the count2 value");

    }

    @Subject("rw 读库:ds0:r1w1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201e() {
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql7", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Assert.areEqual(true, countA == 30, "the countA value");
        Assert.areEqual(true, countB == 0, "the countB value");

    }

    @Subject("rw 写库:ds0:r1w0,ds1:r1w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201f() {
        Step("rw 写库:ds0:r1w0,ds1:r1w0");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql8", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Step("获取数据个数");
        Assert.areEqual(true, count1 == 0, "the count1 value");
        Assert.areEqual(true, count2 == 0, "the count2 value");

    }

    @Subject("rw 读库:ds0:r1w0,ds1:r1w0")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201h() {
        Step("rw 读库:ds0:r1w0,ds1:r1w0");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql8", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Assert.areEqual(true, 10 <= countA && countA <= 30, "the countA value");
        Assert.areEqual(true, 10 <= countB && countB <= 30, "the countB value");

    }

    @Subject("rw 写库:ds0:r2w1,ds1:r1w2,当ds0连接不上时，全部写入ds1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201i() {
        Step("rw 写库:ds0:r2w1,ds1:r1w2,当ds0连接不上时，全部写入ds1");
        HashMap<String, Integer> mp = writeRwMysqlDB("zdalRwMysql9", "insertRwSql");
        int count1 = mp.get("count1");
        int count2 = mp.get("count2");
        Assert.areEqual(true, count1 == 0, "the count1 value");
        Assert.areEqual(true, count2 == 30, "the count2 value");
    }

    @Subject("rw 读库:ds0:r2w1,ds1:r1w2,当ds0连接不上时，全部读ds1")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC95201j() {
        Step("rw 读库:ds0:r2w1,ds1:r1w2,当ds0连接不上时，全部读ds1");
        ZdalTestCommon.dataPrepareForZds();
        HashMap<String, Integer> mp = readRwMysqlDB("zdalRwMysql9", "queryRwSql");
        int countA = mp.get("countA");
        int countB = mp.get("countB");
        Step("获取数据个数");
        Assert.areEqual(true, countA == 0, "the count1 value");
        Assert.areEqual(true, countB == 30, "the count2 value");

    }

    /**
     * 针对rw mysql库的写库的公共函数
     * 
     * @param beanName
     * @param sqlName
     * @return
     */

    private HashMap<String, Integer> writeRwMysqlDB(String beanName, String sqlName) {
        HashMap<String, Integer> mp = new HashMap<String, Integer>();
        sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean(beanName);
        String sqlStr1 = sqlName;
        Map<String, Object> params;
        int inserttime = 30;
        for (int i = 1; i <= inserttime; i++) {
            try {
                params = new HashMap<String, Object>();
                params.put("num", i);
                sqlMap.insert(sqlStr1, params);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // 进写入的数据count，进行验证
        String querySql = "select count(*) from test1 where colu2 = 'DB_G'";
        ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, url1, psd, user);
        ResultSet rs2 = ZdalTestCommon.dataCheckFromJDBC(querySql, url2, psd, user);
        try {
            Assert.areEqual(true, rs.next() && rs2.next(), "the value");
            int count1 = rs.getInt(1);
            int count2 = rs2.getInt(1);

            mp.put("count1", count1);
            mp.put("count2", count2);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mp;
    }

    /**
     * 针对rw mysql库的读库的公共函数（zds库）
     * 
     * @param beanName
     * @param sqlName
     * @return
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> readRwMysqlDB(String beanName, String sqlName) {
        HashMap<String, Integer> hp = new HashMap<String, Integer>();
        int countA = 0;
        int countB = 0;
        // 准备数据
        ZdalTestCommon.dataPrepareForZds();

        // 读取数据，并计算从每个库里面读数的次数
        sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean(beanName);
        String sqlStr1 = sqlName;
        for (int countnum = 0; countnum < 30; countnum++) {
            try {
                List<Object> a = (List<Object>) sqlMap.queryForList(sqlStr1);
                for (int i = 0; i < a.size(); i++) {
                    HashMap<String, String> hs = (HashMap<String, String>) a.get(i);
                    if ("DB_A".equalsIgnoreCase((String) hs.get("colu2"))) {
                        countA++;
                    } else if ("DB_B".equalsIgnoreCase((String) hs.get("colu2"))) {
                        countB++;
                    }
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        hp.put("countA", countA);
        hp.put("countB", countB);
        return hp;
    }

}
