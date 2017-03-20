package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("rw ReadWriteRule非法")
public class SR952130 extends ZdalTestBase {
    public TestAssertion Assert = new TestAssertion();

    @Subject("readWriteRule为''")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952131() {
        Step("readWriteRule为''");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs1");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        Step("初始化断言");
        try {
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "验证异常类");
        }
    }

    @Subject("readWriteRule为非法字符")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952132() {
        Step("readWriteRule为非法字符");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs2");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        try {
            Step("初始化异常");
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "验证异常类");
        }
    }

    @Subject("readWriteRule指定的ds不存在")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952133() {
        Step("readWriteRule指定的ds不存在");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs3");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        Step("初始化异常");
        try {
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "验证异常类");
        }
    }

}
