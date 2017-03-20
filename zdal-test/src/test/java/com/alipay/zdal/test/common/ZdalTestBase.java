package com.alipay.zdal.test.common;

import org.junit.After;
import org.junit.Before;

import com.alipay.ats.assertion.TestAssertion;
import com.alipay.zdal.client.jdbc.ZdalDataSource;

public class ZdalTestBase {
    protected ZdalDataSource zdalDataSource;
    protected String         appName;
    protected String         appDsName;
    protected String         localFile;
    protected String         dbmode;
    protected String         zone;
    protected String         dataSourceLocal;

    public TestAssertion     Assert = new TestAssertion();

    @Before
    public void beforTest() {
        zdalDataSource = new ZdalDataSource();
        zdalDataSource.setDbmode(ConstantsTest.dbmode);
    }

    @After
    public void endTestCase() {
        try {
            zdalDataSource.close();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            zdalDataSource = null;
        }
    }

}
