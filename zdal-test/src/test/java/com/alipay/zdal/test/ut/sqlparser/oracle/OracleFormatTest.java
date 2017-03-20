package com.alipay.zdal.test.ut.sqlparser.oracle;

import org.junit.Test;


import com.alipay.zdal.parser.sql.SQLUtils;


public class OracleFormatTest   {
	@Test
    public void test_formatOracle() {
        String sql = SQLUtils.formatOracle("select substr('123''''a''''bc',0,3) FROM dual");
        System.out.println(sql);
    }
}
