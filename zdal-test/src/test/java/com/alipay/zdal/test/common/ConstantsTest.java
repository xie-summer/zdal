package com.alipay.zdal.test.common;

public interface ConstantsTest {
	
	public static final String dbmode="dev";
	
	public static final String zone="gzone";
	
	public static final String zdataConsoleUrl="http://zdataconsole.p9.alipay.net:8080";
	
	public static final String mysql112Url="jdbc:mysql://mysql-1-2.bjl.alipay.net:3306";
	public static final String mysql122Url="jdbc:mysql://mysql-2-2.bjl.alipay.net:3306";
	public static final String mysq112User="mysql";
	public static final String mysq112Psd="mysql";
	public static final String mysq122User="mysql";
	public static final String mysq122Psd="mysqlbak";
	
	public static final String mysq112UrlTddl0=mysql112Url+"/tddl_0";
	public static final String mysq112UrlTddl1=mysql112Url+"/tddl_1";
	public static final String mysq112UrlTddl2=mysql112Url+"/tddl_2";
	public static final String mysq112UrlTddl3=mysql112Url+"/tddl_3";
	public static final String mysql12UrlZds1=mysql112Url+"/zds1";
	public static final String mysql12UrlZds2=mysql112Url+"/zds2";
	public static final String mysq112UrlFail0=mysql112Url+"/tddl_fail_0";
	public static final String mysq112UrlFail1=mysql112Url+"/tddl_fail_1";
	public static final String mysql12UrlTranation0=mysql112Url+"/tddl_transation_0";
	public static final String mysql12UrlTranation0_bac=mysql112Url+"/tddl_transation_0_bac";
	public static final String mysql12UrlTranation1=mysql112Url+"/tddl_transation_1";
	public static final String mysql12UrlTranation1_bac=mysql112Url+"/tddl_transation_1_bac";
	public static final String mysql12UrlItemNumberId=mysql112Url+"/item_number_id";
	public static final String mysql12UrlUserCharId=mysql112Url+"/user_char_id";
	public static final String mysql12UrlDocument0=mysql112Url+"/document_0";
	public static final String mysql12UrlDocument1=mysql112Url+"/document_1";
	public static final String mysql12UrlSample0=mysql112Url+"/tddl_sample_0";
	public static final String mysql12UrlSample1=mysql112Url+"/tddl_sample_1";
	public static final String mysql12UrlSequence0=mysql112Url+"/tddl_sequence_0";
	public static final String mysql12UrlSequence1=mysql112Url+"/tddl_sequence_1";
	public static final String mysql22UrlSequence2=mysql122Url+"/tddl_sequence_2";
	public static final String mysql22UrlSequence3=mysql122Url+"/tddl_sequence_3";
	
	public static final String oralcePrefUrl="jdbc:oracle:thin:@perf6.lab.alipay.net:1521:perfdb6";
	public static final String oraclePreUser1="ACM";
	public static final String oraclePreUser2="ACM1";
	public static final String oraclePrePsd="ali88";
	
}
