package com.alipay.zdal.parser.druid.bvt.sql.mysql.visitor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
	MySqlResourceTest.class,
	MySqlSchemaStatVisitorTest_Delete.class,
	MySqlSchemaStatVisitorTest_Insert.class,
	MySqlSchemaStatVisitorTest_Select_Limit.class,
	MySqlSchemaStatVisitorTest_Select_OrderBy.class,
	MySqlSchemaStatVisitorTest_Subquery.class,
	MySqlSchemaStatVisitorTest_Subquery2.class,
	MySqlSchemaStatVisitorTest_Update.class,
	MySqlSchemaStatVisitorTest1.class,
	MySqlSchemaStatVisitorTest2.class,
	MySqlSchemaStatVisitorTest3.class,
	MySqlSchemaStatVisitorTest5.class
}
)
public class SuitTest3 {

}
