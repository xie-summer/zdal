package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.SQLUtils;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleFormatTest.java, v 0.1 2012-5-17 ионГ10:17:01 xiaoqing.zhouxq Exp $
 */
public class OracleFormatTest extends TestCase {
    public void test_formatOracle() {
        String sql = SQLUtils.formatOracle("select substr('123''''a''''bc',0,3) FROM dual");
        System.out.println(sql);
    }
}
