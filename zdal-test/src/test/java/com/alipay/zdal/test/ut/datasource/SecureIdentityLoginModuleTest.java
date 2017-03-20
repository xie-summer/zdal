package com.alipay.zdal.test.ut.datasource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alipay.zdal.datasource.resource.security.SecureIdentityLoginModule;


public class SecureIdentityLoginModuleTest {
    private static final String PASSWORD     = "mysql";
    private static final String ENC_PASSWORD = "-76079f94c1e11c89";

    @Test
    public void testDecodedPassword() throws Exception {

        assertEquals(PASSWORD, new String(SecureIdentityLoginModule.decode(ENC_PASSWORD)));
    }
}
