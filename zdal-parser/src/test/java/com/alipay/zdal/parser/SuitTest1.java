package com.alipay.zdal.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
	SQLParserOfMysqlWithDeleteTest.class,
	SQLParserOfMysqlWithInsertTest.class,
	SQLParserOfMysqlWithSelectTest.class,
	SQLParserOfMysqlWithUpdateTest.class,
	SQLParserOfOracleWithDeleteTest.class,
	SQLParserOfOracleWithInsertTest.class,
	SQLParserOfOracleWithSelectTest.class,
	SQLParserOfOracleWithUpdateTest.class
}
)
public class SuitTest1 {

}
