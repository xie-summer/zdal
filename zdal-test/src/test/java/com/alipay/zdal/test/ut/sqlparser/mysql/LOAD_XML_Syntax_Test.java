package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class LOAD_XML_Syntax_Test {
	@Test
	 public void test_0() throws Exception {
	        //        String sql = "LOAD XML LOCAL INFILE 'person.xml' INTO TABLE person ROWS IDENTIFIED BY '<person>';";
	        String sql = "select "
	                     + "t1.owner_card_no,opposite_card_no,opposite_name,opposite_nick_name,consume_title,"
	                     + "consume_fee,consume_type,t1.biz_type,biz_sub_type,biz_orig,t1.biz_in_no,biz_out_no,"
	                     + "biz_extra_no,biz_state,consume_status,consume_refund_status,in_out,consume_site,"
	                     + "trade_from,flag_locked,flag_refund,gmt_biz_create,delete_type,delete_time,"
	                     + "delete_over_time,gmt_modify from "
	                     + "(select owner_card_no,biz_in_no,biz_type from ps_consume_delete15 where owner_card_no = ?  and delete_type IN ( ?,?) ORDER BY gmt_biz_create DESC LIMIT ?, ?) "
	                     + " as t1,ps_consume_delete15 t2    "
	                     + " where t1.owner_card_no =t2.owner_card_no  and t1.biz_in_no=t2.biz_in_no and  t1.biz_type=t2.biz_type order by biz_type";
	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);
	        System.out.println(text);
	        //        Assert.assertEquals(
	        //            "LOAD XML LOCAL INFILE 'person.xml' INTO TABLE person ROWS IDENTIFIED BY '<person>';",
	        //            text);
	    }

	    private String output(List<SQLStatement> stmtList) {
	        StringBuilder out = new StringBuilder();

	        for (SQLStatement stmt : stmtList) {
	            stmt.accept(new MySqlOutputVisitor(out));
	            out.append(";");
	        }

	        return out.toString();
	    }

}
