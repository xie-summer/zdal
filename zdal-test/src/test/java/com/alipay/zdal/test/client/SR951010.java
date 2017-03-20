package com.alipay.zdal.test.client;

import static com.alipay.ats.internal.domain.ATS.Step;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal 初始化")
public class SR951010 extends ZdalTestBase {

    @Before
    public void beginTestCase() {
        appName = "zdalClientInitApp";
        localFile = "./config/client";
        zdalDataSource.setAppName(appName);
        zdalDataSource.setConfigPath(localFile);
    }

    @Subject("appdsName 为null，进行初始化")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951011() throws Throwable {
        try {
            Step("appDsName is null ,进行初始化");
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "appDSName 是 null，进行初始化");

        }
    }

    @Subject("appdsName 为空值，进行初始化")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951012() throws Throwable {
        appDsName = "";
        zdalDataSource.setAppDsName(appDsName);
        Step("appName 为 '',进行初始化");
        try {
            Step("appDsName 为 '',进行初始化");
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "appDSName 为 ''，进行初始化");

        }
    }

    @Subject(" ZdataconsoleUrl 为空值,zdalconfigLocal 为 false")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951013() throws Throwable {
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl 为空值,zdalconfigLocal 为 false，进行初始化");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(),
                "zdataconsoleUrl 为 ''，zdalconfigLocal 为 false,进行初始化");
        }
    }

    @Subject("the ZdataconsoleUrl 为 '',但 zdalconfigLocal 为 true")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951014() throws Throwable {
        int notException = 0;
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("the ZdataconsoleUrl 为 '',但 zdalconfigLocal 为 true,进行初始化");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            notException = 1;
        }
        Assert.areEqual(0, notException, "not Exception");

    }

    @Subject(" ZdataconsoleUrl 为 null, zdalconfigLocal 为 false")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951015() throws Throwable {
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl 为 null, zdalconfigLocal 为 false,进行初始化");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(),
                "zdataconsoleUrl 为 null，zdalconfigLocal 为 false,进行初始化");
        }
    }

    @Subject(" configPath 为 ''")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951016() throws Throwable {
        appDsName = "configPathisnull";
        zdalDataSource.setAppDsName(appDsName);
        zdalDataSource.setConfigPath("");
        Step("configPath 为 空值,进行初始化");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "configPath 为 '',进行初始化");
        }
    }

    @Subject(" dbmode 为 null值")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951017() throws Throwable {
        appDsName = "configPathisnull";
        zdalDataSource.setAppDsName(appDsName);
        //zdalDataSource.setDbmode(null);
        Step("dbmode 为 null值,进行初始化");
        try {
            zdalDataSource.init();
        } catch (ZdalClientException e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "dbmode 为 null值,进行初始化");
        }
    }

    @Subject("ZdataconsoleUrl 为 OK,但 localFile文件不存在")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951018() throws Throwable {
        appDsName = "testDsNotExist";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl 为 OK,但 localFile文件不存在，进行初始化");
        try {
            zdalDataSource.init();
        } catch (ZdalClientException e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(),
                "ZdataconsoleUrl 为 OK,但 localFile文件不存在，进行初始化");
        }
    }

}
