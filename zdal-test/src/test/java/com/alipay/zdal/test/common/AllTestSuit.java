package com.alipay.zdal.test.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.alipay.zdal.test.client.ZdalClientSuite;
import com.alipay.zdal.test.rw.ZdalRwSuite;
import com.alipay.zdal.test.shardfailover.ZdalShardfailoverSuite;
import com.alipay.zdal.test.shardrw.ZdalShardrwSuite;
import com.alipay.zdal.test.sqlparser.ZdalSqlParserSuite;
import com.alipay.zdal.test.ut.sqlparser.mysql.UtZdalSqlParserMysqlSuite;
import com.alipay.zdal.test.ut.sqlparser.oracle.UtZdalSqlParserOracleSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ZdalClientSuite.class, ZdalRwSuite.class, ZdalShardfailoverSuite.class,
                      ZdalShardrwSuite.class, ZdalSqlParserSuite.class,

                      //ut
                      UtZdalSqlParserMysqlSuite.class, UtZdalSqlParserOracleSuite.class

})
public class AllTestSuit {

}
