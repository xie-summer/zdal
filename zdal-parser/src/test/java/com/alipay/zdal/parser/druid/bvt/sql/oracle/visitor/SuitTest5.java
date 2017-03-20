package com.alipay.zdal.parser.druid.bvt.sql.oracle.visitor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	OracleOutputVisitorTest_Aggregate.class,
	OracleOutputVisitorTest_dblink.class,
	OracleOutputVisitorTest_delete.class,
	OracleOutputVisitorTest_forupdate.class,
	OracleOutputVisitorTest_orderBy.class,
	OracleOutputVisitorTest_Select_Limit.class,
	OracleOutputVisitorTest_Select_OrderBy.class,
	OracleOutputVisitorTest_selectJoin.class,
	OracleResourceTest.class,
	OracleSchemaStatVisitorTest_Delete.class,
	OracleSchemaStatVisitorTest_Insert.class,
	OracleSchemaStatVisitorTest_Subquery.class,
	OracleSchemaStatVisitorTest_Subquery2.class,
	OracleSchemaStatVisitorTest_Update.class,
	OracleSchemaStatVisitorTest1.class,
	OracleSchemaStatVisitorTest2.class,
	OracleSchemaStatVisitorTest3.class,
	OracleSchemaStatVisitorTest4.class
}
)
public class SuitTest5 {

}
